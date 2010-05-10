#include <SDL/SDL.h>
#include <SDL/SDL_image.h>
#include <iostream>
#include "gamewindow.h"

//refer to http://lazyfoo.net/SDL_tutorials/index.php
// this file is based on that tutorial, read it to get started

Game_Window::Game_Window() 
	: SCREEN_WIDTH(640),SCREEN_HEIGHT(480),SCREEN_BPP(32),FRAME_RATE(20)
{
	screen = NULL;
	initialized = false;
}

SDL_Surface* Game_Window::load_image( std::string filename )
{
	if(!initialized)
	{
		std::cerr << "ERROR: loading image without SDL initialization" << std::endl;
		return NULL;
	}

	SDL_Surface *loaded_image = NULL;
	SDL_Surface *optimized_image = NULL;
	
	loaded_image = IMG_Load ( filename.c_str() );
	if( loaded_image != NULL )
	{

	std::cerr << "image loaded: " << filename << std::endl;
		//changes format of loaded_image to format of 'screen'
		optimized_image = SDL_DisplayFormat( loaded_image );

	std::cerr << "\timage optimized" << std::endl;
/**
		if( optimized_image != NULL )
		{
			Uint32 colorkey = SDL_MapRGB(optimized_image->format, 0xFF ,0xFF ,0xFF );
			SDL_SetColorKey(optimized_image, SDL_SRCCOLORKEY, colorkey);
		}
		std::cerr << "\tcolor key applied" << std::endl;
**/

		//Free un-optimized
		SDL_FreeSurface( loaded_image );
	}
	else
		std::cerr << "ERROR LOADING " << filename << std::endl;

	return optimized_image;
}

int Game_Window::apply_surface(int x, int y, SDL_Surface* source, SDL_Surface* dest, SDL_Rect* clip )
{
	//A rect defines a coord, and can also define a rectangle you want to blit.
	SDL_Rect offset;
	offset.x = x;
	offset.y = y;
	return SDL_BlitSurface(source,clip,dest,&offset);
}

bool Game_Window::init()
{
	if( SDL_Init( SDL_INIT_EVERYTHING ) == -1 )
	{
		return false;
	}
	else
		initialized = true;

 	screen = SDL_SetVideoMode( SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_BPP, SDL_SWSURFACE ); 
	if( screen == NULL )
	{
		return false;
	}
	SDL_WM_SetCaption( "Sprite Demo" , "Demo" );
	return true;
}

int Game_Window::get_SCREEN_WIDTH()
{
	return SCREEN_WIDTH;
}

int Game_Window::get_SCREEN_HEIGHT()
{
	return SCREEN_HEIGHT;
}

SDL_Surface* Game_Window::get_screen()
{
	return screen;
}
