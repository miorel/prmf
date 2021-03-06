/*! \mainpage Sprite Selection Tool
 * 
 * \section sum_sec Summary
 * Will allow for the automatic retrieval of individual sprite locations from a given sprite-sheet in PNG
 * image format. <br> This would alleviate the tedious work of manually entering coordinates of sprites
 * when writing programs which use large sprite-sheet images.
 *
 * \section srcs Useful sources: 
 * <ul>
 * <li>http://www.libpng.org/pub/png/libpng-1.4.0-manual.pdf </li>
 * <li>http://www.libpng.org/pub/png/book/toc.html </li>
 * <li>http://www.piko3d.com/?page_id=68 </li>
 * <li>http://tunginobi.spheredev.org/site/node/88 </li>
 * <li>http://fydo.net/gamedev/dynamic-arrays </li>
 * <li>http://www.flyingyogi.com/fun/spritelib.html </li>
 * <li>http://www.cs.toronto.edu/~jepson/csc2503/segmentation.pdf (pg.19 possible solution for our basic problem)  </li>
 * <li>Miorel-Lucian Palii </li>
 * <li>Preston Mueller </li>
 * </ul>
 *
 */

/**
 * @file cutter.c
 * @brief The entry point for the project and contains most important methods.
 *
 * @author Rodrigo Salazar
 */

#include <png.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <math.h>

#include "types.h"
#define QUEUE_TYPE struct pixel
#define QUEUE_IDENTIFIER pixel
#include "cutter_queue.h"

#define DYNARR_TYPE struct pixel
#define DYNARR_IDENTIFIER pixel
#include "cutter_dynarr.h"
#define DYNARR_TYPE struct box
#define DYNARR_IDENTIFIER box
#include "cutter_dynarr.h"

#define COLOR_TOLERANCE 50
png_bytepp read_png();
boxp solve();
struct color get_pixel();
char color_compare();
char get_next_seed();
char is_color_nearby();
void get_background_color();
void set_bg_processed();
void flood_seed();
void set_box_processed();
struct box_dynarr cutter();
void set_box_bounds();

/** \defgroup img_data Image Data */
/*@{*/
png_uint_32 img_width; ///< Image Width
png_uint_32 img_height; ///< Image Height
png_uint_32 bit_depth; ///< Number of bits used to represent color 
png_uint_32 channels; ///< Number of color channels, ie. RGBA = 4
png_uint_32 color_type;
unsigned long long stride; ///< Number of pixels per line
/*@}*/

/** \defgroup funcs Functions */
/*@{*/

/**
 * @brief The main entry point to the program, takes filename as an argument.
 * @param argc number of arguments
 * @param args arguments
 */
main(int argc,char *args[])
{
	if(argc == 2)
		cutter(args[1]);
	else
		cutter("test_images/easy_input.png");
}

/**
 * @brief This method makes the calls to all the functions necessary to complete the process.
 * @param filename The filename which contains the spritesheet
 */
struct box_dynarr cutter(const char* filename)
{
	png_bytepp png_rows = read_png(filename);
	assert(channels == 3 || channels == 4);
	//print_picture(png_rows,2);

	struct color bg_color;
	get_background_color(png_rows, &bg_color);
	printf("Background color found to be %d %d %d %d\n",bg_color.r,bg_color.g,bg_color.b,bg_color.a);

	char **processed_pixels = malloc(img_height * sizeof(char*));
	int i,j;
	for(i=0;i<img_height;++i)
		processed_pixels[i] = calloc(stride / channels, sizeof(char));

	set_bg_processed(png_rows,processed_pixels,&bg_color);
	
	struct pixel current_seed = {0,0};

	struct box_dynarr boxes= {NULL,0,0};
	while( get_next_seed(processed_pixels, &current_seed) )
	{
		flood_seed(processed_pixels,png_rows,&bg_color,&current_seed,box_dynarr_slot(&boxes));
	}

	printf("Debug: found %d sprites\n",boxes.num_elem);

	//Free image and char array from memory before exiting
	return boxes;
}

/**
 * @brief Scans the processed_pixels array given for the next pixel which has not been processed yet.
 * @param processed_pixels 2-dimensional array representing whether or not a pixel has been marked as processed
 * @param p pointer to a pixel struct used a guide as to where to start the search
 */
char get_next_seed(char **processed_pixels, pixelp p)
{
	/*
		Very obvious practicality flaw with this method, if a sprite is 1 pixel higher than the rest and is in the same row as the other,
		then it will be parsed first and added to the list first..making it very difficult for the user of the program to know the order
		of the sprites he is interested in, making the program effectively useless. The only solutions to this are to either sort the list
		before the end of the programs execution, sorting based on something I haven't thought of yet, or to edit this method so that the 
		order of seeds arrive in the correct order, the previous is probably the easier and safer task.
	*/

	//printf("Debug: pixel: %d %d\n",p->i,p->j);

	int row_size = stride / channels;
	int i, j;
	for(i = p->i; i < img_height; ++i)
	{
		for(j = (i == p->i ? p->j : 0); j < row_size; ++j)
		{
			if(processed_pixels[i][j] == 0)
			{
				p->i = i;
				p->j = j;
				return 1;
			}
		}
	}
	return 0;
}

/**
 * @brief Edits the processed_pixels array so that all pixels denoted in the range of the given box struct are changed to off position.
 * @param processed_pixels 2-dimensional array representing whether or not a pixel has been marked as processed
 * @param bp pointer to box structure representing the range which will be set to off in the processed_pixels array
 */

void set_box_processed(char **processed_pixels, boxp bp)
{
	printf("Debug: 'set_box_processed' box x:%d y:%d w:%d h:%d\n", bp->x,bp->y,bp->w,bp->h);
	int i, j;
	for(i = bp->y; i < (bp->y + bp->h); ++i)
	{
		for(j = bp->x; j < (bp->x + bp->w); ++j)
		{
			assert( i < img_height && j < (stride / channels) );
			processed_pixels[i][j] = 1;
		}
	}
}

/**
 * @brief Given a set of pixels, this method finds the smallest box that can contain the pixels.
 * @param dynp An array of pixels in the form of a dynamic array of type pixel_dynarr (type created at compile-time)
 * @param bp Pointer to a box structure which will hold the result of the calculation
 */
void set_box_bounds(struct pixel_dynarr* dynp, boxp bp)
{
	/*
		Must consider option of adding padding to box (optional).
	*/
	int minx=(stride/channels),miny=img_height,maxx=0,maxy=0;
	int i;
	for(i = 0; i < dynp->num_elem; ++i)
	{
		if(minx > dynp->data[i].j)
		{
			minx = dynp->data[i].j;
		}
		else if(maxx < dynp->data[i].j)
		{
			maxx = dynp->data[i].j;
		}
		
		if(miny > dynp->data[i].i)
		{
			miny = dynp->data[i].i;
		}		
		else if(maxy < dynp->data[i].i)
		{		
			maxy = dynp->data[i].i;
		}	
	}
	bp->w = maxx - minx;
	bp->h = maxy - miny;
	bp->y = miny;
	bp->x = minx;
}

/**
 * @brief Given a location on the image, this method returns if the color of interest is within a certain distance.
 *		 (Method not implemented yet) This method should reduce the possibly of connecting regions which should not be connected.
 * @param png_rows The actual image data as a color byte array
 * @param dynp ????
 * @param candidate Color we are searching for
 * @param i Y coordinate that we are searching around
 * @param j X coordinate that we are searching around
 * @param search_dist The radius of pixels around i,j that we are to examine
 */
char is_color_nearby(png_bytepp png_rows,struct pixel_dynarr * dynp, struct color * candidate, int i, int j,int search_dist)
{
	return 0;
}

/**
 * @brief Given a pixel on the image, this method will perform a flood fill on the area, starting from this pixel.
 * @param processed_pixels 2-dimensional array representing whether or not a pixel has been marked as processed 
 * @param png_rows The actual image data as a color byte array
 * @param bg_colorp The color determined to be the background color of the image
 * @param seed Pixel which will be the starting point for the flood fill
 * @param bp Pointer to box object which will ultimately hold a bounding box for the sprite which contained the seed pixel
 */
void flood_seed(char **processed_pixels, png_bytepp png_rows,struct color * bg_colorp, pixelp seed, boxp bp)
{
	/*
		This is the very first approach to solving this problem, I don't see it failing with images that aren't too much trouble.
		Another approach involves building a minimum spanning tree around connected areas 
		(very different from this approach which looks at the image from the top down), including an algorithm would decide whether or not
		to create an edge between nearby connected regions (based on the average of the weighted edges in the min span tree)... 
		This 2nd approach is listed in the source list.
	*/

	//printf("Debug: current_seed: %d %d\n",seed->i,seed->j);

	struct pixel_dynarr dynp = {NULL, 0, 0};
	struct pixel_queue q = {NULL, 0, 0};
	//clockwise from top-left (being (x,y) = (-1,-1))
	char dx[] = {-1,0,1,1,1,0,-1,-1};
	char dy[] = {-1,-1,-1,0,1,1,1,0};

	pixel_queue_enqueue(&q,*seed);
	while( !pixel_queue_empty(&q) )
	{
		struct pixel curr_pixel = pixel_queue_pop(&q);
		int i;
		for(i = 0; i < 8; ++i) // NOTE: it's quite bad that the 8 is hard-coded
		{
			//printf("Debug: %d %d\n", curr_pixel.i+dx[i],curr_pixel.j+dy[i]);
			if(curr_pixel.i+dx[i] >= 0 && curr_pixel.i+dx[i] < img_height && curr_pixel.j+dy[i] >= 0 && curr_pixel.j+dy[i] < (stride / channels) )
			{
				//printf("\tMeets contraints\n");
				if(processed_pixels[curr_pixel.i+dx[i]][curr_pixel.j+dy[i]] == 0)
				{
					struct color comp = get_pixel(png_rows,curr_pixel.i+dx[i],curr_pixel.j+dy[i]);
					if( !color_compare(comp.r,comp.g,comp.b,comp.a,bg_colorp, COLOR_TOLERANCE ) ) //fails tolerance test
					{
						if( !is_color_nearby(png_rows,&dynp,&comp,curr_pixel.i+dx[i],curr_pixel.j+dy[i], 6 ) )
						{
							processed_pixels[ curr_pixel.i+dx[i]][curr_pixel.j+dy[i]] = 1;
							continue;
						}
					}
					struct pixel new_pixel = { curr_pixel.i+dx[i], curr_pixel.j+dy[i] };
					pixel_queue_enqueue(&q, new_pixel);
					processed_pixels[new_pixel.i][new_pixel.j] = 1;
					*pixel_dynarr_slot(&dynp) = new_pixel;
				}
			}
		}
	}
	set_box_bounds(&dynp, bp);
	set_box_processed(processed_pixels, bp);
	
	pixel_dynarr_destroy( &dynp );
	pixel_queue_destroy( &q );
}

/**
 * @brief This method will return 1 or 0 based on whether it determines 2 colors to be near enough to each other
 * 			There can be a set tolerance passed to this function which will act as factor to determine the result.
 *			If a tolerance is used then the result is based on the color distance between the given, which is 
 *			simply calculated as the distance between the points (since color can be interpreted as such)
 * @param r Color component being compared (Red)
 * @param g Color component being compared (Green)
 * @param b Color component being compared (Blue)
 * @param a Color component being compared  (Alpha transperancy)
 * @param color_comp Base color being used as reference 
 * @param tolerance Predetermined value used a tolerance distance
 */
char color_compare(int r,int g,int b, int a, colorp color_comp, int tolerance)
{
	/*
		distance between 2 colors is a comparison in similarity between colors , ||A-B||
		Added a value called color_tolerance , helps distinguish between background and images in troublesome images
		not sure how effective it is overall, it could possibly cause problems.
		
		Another possible solution I thought about was min and max box sizes..
		if a sprite reaches upwards of 10% of image area
		it can cause a restart and increase in color_tolerance .. but this needs testing.
	*/
	double color_dist = sqrt( pow((r-color_comp->r),2) +pow((g-color_comp->g),2)+pow((b-color_comp->b),2));
	if( color_dist <= COLOR_TOLERANCE )
		return 0;
	else
		return 1;
}

/**
 * @brief Sets processed_pixels array to 0 where ever png_rows is equal (based on color_compare) to bg_color.
 * @param png_rows The actual image data as a color byte array
 * @param processed_pixels 2-dimensional array representing whether or not a pixel has been marked as processed 
 * @param bg_colorp The color determined to be the background color of the image
 */
void set_bg_processed(png_bytepp png_rows, char **processed_pixels, colorp bg_color)
{
	/*
		From observation I have found that libpng will detect if a image contains an alpha layer
		and return 4 bytes per pixel instead of 3 (RGBA instead of RGB info)
		A fully transparent pixel is shown as x x x 0

		At this point..making a grid[][] with 1's for bg color and 0 otherwise would make using the real image useless
		this is the last method where actual image data will be used, after this only a boolean array will be used.

		This will also help avoid the case in which there is a background color pixel inside a sprite causing it to be
		seeded twice or other strange errors.
	*/
	int i,j;
	for(i=0;i<img_height;++i)
	{
		for(j=0;j<stride;j+=channels)
		{

			if( !color_compare( png_rows[i][j], png_rows[i][j+1],png_rows[i][j+2], channels == 4 ? png_rows[i][j+3] : 255, bg_color,0 )  )
			{
				int pixel_j = j / channels;
				processed_pixels[i][pixel_j] = 1;
			}
		}
	}
}

/**
 * @brief Convienience method which prints the byte array of the image loaded
 * @param png_rows The actual image data as a color byte array
 * @param nrows Number of rows in the image loaded
 */
void print_picture(png_bytepp png_rows, int nrows)
{
	//The following code prints out all the color info of the image!
	int i,j;
	for(i=0;i<nrows;++i)
	{
		for(j=0;j<stride;++j)
		{
			printf("%d ",png_rows[i][j]);
		}
		printf("\n");
	}
}

/**
 * @brief Given a pixel location, this method returns the color as a color structure
 * @param png_rows The actual image data as a color byte array
 * @param i Y coordinate on the image
 * @param j X coordinate on the image
 */
struct color get_pixel(png_bytepp png_rows, int i, int j)
{
	struct color pixel;
	pixel.r = png_rows[i][j];
	pixel.g = png_rows[i][j+1];
	pixel.b = png_rows[i][j+2];
	pixel.a = (channels == 4 ? png_rows[i][j+3] : 255);

	return pixel;
}

/**
 * @brief Crude method of finding/guessing the background color of the image
 * @param png_rows The actual image data as a color byte array
 * @param bg_color Pointer to a color structure which the result will be stored in
 */
void get_background_color(png_bytepp png_rows, colorp bg_color)
{
	/*
		still finding better solution to this.. for now I check most common color on the border of image. (only checking R and G)
		But this method should be fine for the purpose of the program, working with sheets of sprites.
	*/
	int max=0, max_i=0, max_j=0, i , j;
	int set[255][255];
	memset(set, 0 ,sizeof(set));
			
	for(i=0;i<img_height;i+=img_height-1)
	{
		for(j=0;j<stride;j+=channels)
		{

			if( ++set[ png_rows[i][j] ][ png_rows[i][j+1] ] > max )
			{
				max = set[ png_rows[i][j] ][ png_rows[i][j+1] ];
				max_i = i;
				max_j = j;
			}
		}
	}
	for(i=0;i<img_height;++i)
	{
		for(j=0;j<stride;j+=stride-1)
		{

			if( ++set[ png_rows[i][j] ][ png_rows[i][j+1] ] > max )
			{
				max = set[ png_rows[i][j] ][ png_rows[i][j+1] ];
				max_i = i;
				max_j = j;
			}
		}
	}
	bg_color->r = get_pixel(png_rows, max_i, max_j).r;
	bg_color->g = get_pixel(png_rows, max_i, max_j).g;
	bg_color->b = get_pixel(png_rows, max_i, max_j).b;
	bg_color->a = get_pixel(png_rows, max_i, max_j).a;
}

/**
 * @brief LibPNG code to load the file, set image conversions in place, and finally convert image-file to a useable color byte array
 * @param file_name Local file name of image to be loaded
 */
png_bytepp read_png(const char* file_name)
{
	/*
		The printf statements are only there for debugging purpose, I want the actual
		end product to just take in a filename and return an array of box structs indicating
		the locations and sizes of each sprite on the sheet.
	*/
	FILE *fp = fopen(file_name, "rb");
	if(fp)
	{
		printf("-%s loaded\n\t",file_name);
	}
	else
	{
		printf("-Error loading file\n");
		return;
	}
	
	const int header_signature_size = 8;
	assert( header_signature_size <= 8 );

	void* header = malloc(header_signature_size);

	int bytes_read;
	if( (bytes_read = fread(header, 1, header_signature_size, fp)) && bytes_read == header_signature_size)
	{
		printf("-Header read (%d bytes read)\n\t", bytes_read);
	}
	else
	{
		printf("-Error reading Header data, %d bytes read\n",bytes_read);
		return;
	}
	
	int is_png = png_sig_cmp(header, 0, header_signature_size);
	if( is_png )
	{
		printf("-File is not in PNG format\n\t");
		return;
	}
	else
		printf("-File is in PNG format\n\t");
		
	free(header);

	png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	if(png_ptr == NULL)
	{
		printf("-Allocation of png_struct failed\n");
		return;
	}
	else
		printf("-Allocated png_struct\n\t");
		
	png_infop info_ptr = png_create_info_struct(png_ptr);
	if(info_ptr == NULL)
	{
		png_destroy_read_struct(&png_ptr, (png_infopp)NULL, (png_infopp)NULL);
		printf("-Allocation of info struct failed\n\t");
		return;
	}
	else
	{
		printf("-Allocated png_info struct\n\t");
	}
	
	png_infop end_ptr = png_create_info_struct(png_ptr);
	if(info_ptr == NULL)
	{
		png_destroy_read_struct(&png_ptr, &info_ptr, (png_infopp)NULL);
		printf("-Allocation of end_info struct failed\n");
		return;
	}
	else
	{
		printf("-Allocated end_info struct\n\t");
	}
	
	png_init_io(png_ptr, fp);
	printf("-Initialized PNG I/O\n\t");
	
	png_set_sig_bytes(png_ptr, header_signature_size);
	printf("-Set offset for header signature\n\t");
	
	png_read_info(png_ptr, info_ptr);
	printf("-Read image data from header into info_ptr\n\t");
	
	img_width = png_get_image_width(png_ptr,info_ptr);
	img_height = png_get_image_height(png_ptr,info_ptr);
	bit_depth = png_get_bit_depth(png_ptr,info_ptr);
	channels = png_get_channels(png_ptr,info_ptr);
	color_type = png_get_color_type(png_ptr, info_ptr);
	printf("-File info:\n\t\t-Image Width,Height: (%d,%d)\n\t\t-Bit Depth: %d\n\t\t-Channels: %d\n\t\t-Color Type: %d\n\t",
					(int)img_width,(int)img_height,(int)bit_depth,(int)channels,(int)color_type);
	
	switch (color_type) {
		case PNG_COLOR_TYPE_PALETTE:
			png_set_palette_to_rgb(png_ptr);
			channels = 3; 
			printf("-Color type set to be converted from PALETTE to RGB\n\t");
			break;
		case PNG_COLOR_TYPE_GRAY:
			if (bit_depth < 8) {
#if (PNG_LIBPNG_VER_MAJOR > 1 || (PNG_LIBPNG_VER_MAJOR == 1 && PNG_LIBPNG_VER_MINOR >= 4))
				png_set_expand_gray_1_2_4_to_8(png_ptr);
#else
				png_set_gray_1_2_4_to_8(png_ptr);
#endif
			}
			bit_depth = 8;
			printf("-Color type set to be expanded to low-bit GRAY to 8-bit\n\t");  
			break;
	}


	if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS)) 
	{
		png_set_tRNS_to_alpha(png_ptr);
		channels+=1;
		printf("-All tRNS Chunks set to be expanded to alpha channels\n\t");
	}
	if(bit_depth == 16)
	{
		png_set_strip_16(png_ptr);
		printf("-File set to be stripped from 16-bit depth to 8-bit depth\n\t");
	}
	
	png_bytepp rows = malloc(img_height * sizeof(png_bytep));
	char* data = malloc(img_height * img_width * channels * bit_depth / 8);
	printf("-Allocated memory for row bytes\n\t");
	
	stride = img_width * channels * bit_depth / 8 ;
	printf("-Determined row size in bytes to be %llu\n\t",stride);

	int i;
	for(i=0;i<img_height;++i)
	{
		rows[i] = &data[i*stride];
	}
	printf("-Assigned row addresses in memory\n\t");

	png_read_image(png_ptr, rows);
	printf("-Saved image in memory\n");

	fclose(fp);
	fp = NULL;
	png_destroy_read_struct(&png_ptr,&info_ptr,&end_ptr);

	return rows;
}

/*@}*/
