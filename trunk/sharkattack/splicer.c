/*
Sprite Splicer

This is a work in progress which is likely to never be completed though.

It is going to be an implementation of libpng which is able to
select sprites from a spritesheet and return their locations.

Useful sources:
http://www.libpng.org/pub/png/libpng-1.4.0-manual.pdf
http://www.piko3d.com/?page_id=68

The printf statements are only there for debugging purpose, I want the actual
end product to just take in a filename and return an array of box structs indicating
the locations and sizes of each sprite on the sheet.
*/

#include <png.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

struct box
{
	int x,y,w,h;
};

void splicer(const char* file_name) 
{
	FILE *fp = fopen(file_name, "rb");
	if(fp)
	{
		printf("-%s loaded\n\t",file_name);
	}
	else
	{
		printf("-Error loading file\n\t");
	}
	
	const int header_signature_size = 8;
	assert( header_signature_size <= 8 );

	void* header = malloc(header_signature_size);
	if( header_signature_size == fread(header, 1, sizeof(header), fp) )
	{
		printf("-Header read\n\t");
	}
	else
	{
		printf("-Error reading Header data\n\t");
	}
	
	int is_png = png_sig_cmp(header, 0, header_signature_size);
	if( is_png )
	{
		printf("-File is not of PNG format\n\t");
		return;
	}
	else
		printf("-File is PNG format\n\t");
		
	png_structp png_ptr = png_create_read_struct(
							 PNG_LIBPNG_VER_STRING, NULL,NULL,NULL);
	if(png_ptr == NULL)
		printf("-Allocation of png_struct failed\n\t");
	else
		printf("-Allocated png_struct\n\t");
		
	png_infop info_ptr = png_create_info_struct(png_ptr);
	if(info_ptr == NULL)
	{
		png_destroy_read_struct(&png_ptr, (png_infopp)NULL, (png_infopp)NULL);
		printf("-Allocation of info struct failed\n\t");
	}
	else
	{
		printf("-Allocated png_info struct\n\t");
	}
	
	png_infop end_ptr = png_create_info_struct(png_ptr);
	if(info_ptr == NULL)
	{
		png_destroy_read_struct(&png_ptr, &info_ptr, (png_infopp)NULL);
		printf("-Allocation of end_info struct failed\n\t");
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
	
	png_uint_32 img_width = png_get_image_width(png_ptr,info_ptr);
	png_uint_32 img_height = png_get_image_height(png_ptr,info_ptr);
	png_uint_32 bit_depth = png_get_bit_depth(png_ptr,info_ptr);
	png_uint_32 channels = png_get_channels(png_ptr,info_ptr);
	png_uint_32 color_type = png_get_color_type(png_ptr, info_ptr);
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
	
	png_bytep rows = malloc(img_height * sizeof(png_bytep));
	char* data = malloc(img_height * img_width * channels * bit_depth / 8);
	printf("-Allocated memory for row bytes\n\t");
	
	const unsigned int stride = img_width * channels * bit_depth / 8 ;
	printf("-Determined row size in bytes to be %u\n\t",stride);
}

main()
{
	splicer("spritesheet.png");
	splicer("16-bit.png");
}
