/*
Sprite Splicer

This is a work in progress which is likely to never be completed though.

It is going to be an implementation of libpng which is able to
select sprites from a spritesheet and return their locations.

Useful sources:
http://www.libpng.org/pub/png/libpng-1.4.0-manual.pdf
http://www.libpng.org/pub/png/book/toc.html
http://www.piko3d.com/?page_id=68
http://tunginobi.spheredev.org/site/node/88

The printf statements are only there for debugging purpose, I want the actual
end product to just take in a filename and return an array of box structs indicating
the locations and sizes of each sprite on the sheet.

From testing I have found that libpng will detect if a image contains an alpha layer
and return 4 bytes per pixel instead of 3 (RGBA instead of RGB info)
A fully transperant pixel is shown as x x x 0
*/

#include <png.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <time.h>

typedef struct box* boxp;
typedef struct color* colorp;
typedef struct pixel* pixelp;

png_bytepp read_png();
boxp solve();
struct color get_pixel();
void get_background_color();
void set_bg_processed();
char color_compare();
char get_next_seed();

png_uint_32 img_width;
png_uint_32 img_height;
png_uint_32 bit_depth;
png_uint_32 channels;
png_uint_32 color_type;
unsigned long long stride;
	
struct box
{
	int x,y,w,h;
};

struct color
{
	unsigned char r,g,b,a;
};

struct pixel
{
	int i,j;
};

main(int argc, char* args)
{

	png_bytepp png_rows = read_png("test_images/spritesheet.png");
	assert(channels == 3 || channels == 4);
	//print_picture(png_rows,1);

	struct color bg_color =  { 0, 0, 0, 0};
	get_background_color(png_rows, &bg_color);
	printf("Background color found to be %d %d %d %d\n",bg_color.r,bg_color.g,bg_color.b,bg_color.a);

	char **processed_pixels = malloc(img_height * sizeof(char*));
	int i,j;
	for(i=0;i<img_height;++i)
		processed_pixels[i] = calloc(stride, sizeof(char));

	set_bg_processed(png_rows,processed_pixels,&bg_color);
	
	struct pixel current_seed = { 0 , 0 };
	while( get_next_seed(processed_pixels, &current_seed) )
	{
		processed_pixels[current_seed.i][current_seed.j] = 1;
		//there is a bug here, though it correctly finds the first sprite and sets it as the first seed.. 
		//its not going to next row...after completing the first
		printf("(%d %d) ,",current_seed.i,current_seed.j);
	}
/**
	for(i=0;i<img_height;++i)
	{
		free(processed_pixels[i]);
		free(png_rows[i]);
	}

	free(processed_pixels);
//	free(png_rows);
**/

}

char get_next_seed(char **processed_pixels, pixelp p)
{
	int row_size = stride / channels;
	int i=p->i,j=p->j;
	for(;i<img_height;++i)
		for(;j<row_size;++j)
			if( !processed_pixels[i][j] )
			{
				p->i = i;
				p->j = j;
				return 1;
			}
	return 0;
}

char color_compare(int r,int g,int b, int a, colorp color_comp)
{
	if( color_comp->r == r && color_comp->g == g && color_comp->b == b && color_comp->a == a )
		return 0;
	else
		return 1;
}

void set_bg_processed(png_bytepp png_rows, char **processed_pixels, colorp bg_color)
{
	//this will also help avoid the event where e.g. BG is white and there is White inside of a sprite .. causing it to be seeded twice or other
	// strange errors.
	int i,j;
	for(i=0;i<img_height;++i)
	{
		for(j=0;j<stride;j+=channels)
		{

			if( !color_compare( png_rows[i][j], png_rows[i][j+1],png_rows[i][j+2], channels == 4 ? png_rows[i][j+3] : 255, bg_color )  )
			{
				int pixel_j = j / channels;
				//printf("debug: setting %d %d to processed ... \n ",i, pixel_j);
				//assert( pixel_j >= 0);
				processed_pixels[i][pixel_j] = 1;
				//printf("\tdone\n");
			}
		}
	}
}

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

struct color get_pixel(png_bytepp png_rows, int i, int j)
{
	struct color pixel;
	pixel.r = png_rows[i][j];
	pixel.g = png_rows[i][j+1];
	pixel.b = png_rows[i][j+2];
	if( channels == 4 )
		pixel.a = png_rows[i][j+3];
	else 
		pixel.a = 255;
	return pixel;
}

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

png_bytepp read_png(const char* file_name)
{
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
		printf("-File is not of PNG format\n\t");
		return;
	}
	else
		printf("-File is PNG format\n\t");
		
	free(header);

	png_structp png_ptr = png_create_read_struct(
							 PNG_LIBPNG_VER_STRING, NULL,NULL,NULL);
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
            if (bit_depth < 8)
            png_set_gray_1_2_4_to_8(png_ptr);
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



