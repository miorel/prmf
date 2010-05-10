#include <SDL/SDL.h>
#include <SDL/SDL_timer.h>
#include "boost/foreach.hpp"
#include <iostream> 
#include <list>
#include "demo.h"
#include "character.h"

Demo::Demo()
{
	game_objects.push_back(new Character("Link","spritesheet.png"));
}

void Demo::render()
{
	//std::cerr << "entering render" << std::endl;
	if( SDL_GetTicks() - last_frame < 1000 / FRAME_RATE )
	{
		SDL_Delay( (1000 / FRAME_RATE) - (SDL_GetTicks()-last_frame) );
	}
	last_frame = SDL_GetTicks();
	
	std::list<blit_param> instructions;
	blit_param* temp;

	SDL_FillRect(screen,NULL,SDL_MapRGB(screen->format,0x3D,0x9B,0x4));

	BOOST_FOREACH(Game_Object* gw, game_objects)
	{
	   temp = gw->render_action(this);
		if( temp != NULL)
			instructions.push_back( *(temp) );
	}
	//possibly insert a priority sort of list right here so that items are blitted in correct order.
	BOOST_FOREACH(blit_param bp, instructions)
	{
		apply_surface(bp.x,bp.y,bp.source,bp.dest,bp.clip);
	}
	SDL_Flip( screen );
	//std::cerr << "screen flipped" << std::endl;
}

void Demo::play()
{
	BOOST_FOREACH(Game_Object* gw, game_objects)
	{
		gw->init_object(this);
	}

	bool quit = false;	
	render();

	while(!quit)
	{
		while( SDL_PollEvent( &event) )
		{
			if( event.type == SDL_QUIT )
			{
				quit = true;
				break;
			}
			
			//std::cerr << "entering boost foreach receive event" << std::endl;

			BOOST_FOREACH(Game_Object* gw, game_objects)
			{
				gw->receive_event(&event);
			}

		}
		render();
	}

}
