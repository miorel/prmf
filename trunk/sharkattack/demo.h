#include <SDL/SDL.h>
#include <list>
#include "gamewindow.h"
#include "game_object.h"

#ifndef DEMO_H
#define DEMO_H
class Demo : public Game_Window
	{
	private:
		SDL_Event event;
		std::list<Game_Object*> game_objects;

		void render();
	public:
		Demo();
		void play();
};
#endif

#ifndef CHARDIR_ENUM
#define CHARDIR_ENUM
enum CHARDIR
{
	LEFT,
	RIGHT,
	UP,
	DOWN
};
#endif
