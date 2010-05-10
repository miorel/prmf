#include <SDL/SDL.h>
#include <SDL/SDL_image.h>
#include <string>

#ifndef GAMEWINDOW_H
#define GAMEWINDOW_H

//refer to http://lazyfoo.net/SDL_tutorials/index.php
// this file is based on that tutorial, read it to get started

class Game_Window
	{
	public:
		Game_Window();
		bool init();
		bool initialized;
		int apply_surface(int x, int y, SDL_Surface* source, SDL_Surface* dest, SDL_Rect* clip = NULL );
		SDL_Surface *load_image( std::string filename );
		int get_SCREEN_WIDTH();
		int get_SCREEN_HEIGHT();
		SDL_Surface* get_screen();
	protected:
		const int SCREEN_WIDTH;
		const int SCREEN_HEIGHT;
		const int SCREEN_BPP;
		const int FRAME_RATE;
		Uint32 last_frame;
		SDL_Surface *screen;	
		SDL_Event event_handle;
};
#endif

#ifndef blit_instruction
#define blit_instruction
struct blit_param
{
	int x,y;
	SDL_Surface* source;
	SDL_Surface* dest;
	SDL_Rect* clip;
};
#endif
