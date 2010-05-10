#include <SDL/SDL.h>
#include <string>
#include "gamewindow.h"
#include "game_object.h"

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

#ifndef CHARACTER_H
#define CHARACTER_H
class Character : public Game_Object
{
	//This is currently dependent on the spritesheet...
	private:
		std::string sheet_file;
		SDL_Surface* sheet;
		SDL_Rect ani_up[8];
		SDL_Rect ani_down[8];
		SDL_Rect ani_left[7];
		SDL_Rect ani_right[7];
		int dir;
		int frame;
		int x,y;
		int velocity_x,velocity_y;
		int travel_speed;
		void cut_sheet();
	public:
		Character(std::string object_name, std::string filename);
		void receive_event(SDL_Event* event);
		int get_velocity_x();
		int get_velocity_y();
		int get_x();
		int get_y();
		void set_velocity(int x,int y);
		std::string get_filename();
		void init_character(SDL_Surface* surf, int midx, int midy);
		bool init_object(Game_Window* game);
		blit_param* render_action(Game_Window* game);
		SDL_Rect* get_sprite();
		SDL_Surface* get_sheet();
};
#endif

