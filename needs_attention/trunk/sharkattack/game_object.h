#include <SDL/SDL.h>
#include <string>
#include "gamewindow.h"

#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
class Game_Object
{
	public:
		std::string object_name;
		virtual void receive_event(SDL_Event* event) = 0;
		virtual bool init_object(Game_Window* game) = 0;
		virtual blit_param* render_action(Game_Window* game) = 0;
		std::string get_object_name();
};
#endif
