#include <SDL/SDL.h>
#include "boost/range.hpp"
#include <string>
#include <iostream>
#include "gamewindow.h"
#include "character.h"

Character::Character(std::string object_name,std::string filename)
{
	this->object_name = object_name;
	sheet_file = filename;
	cut_sheet();
	dir = DOWN;
	frame = 0;
	velocity_x = 0;
	velocity_y = 0;
	travel_speed = 8;
}

void Character::cut_sheet()
{
	//Still very dependent on the spritesheet....
	for(int i=0;i<boost::size(ani_up);++i)
	{
		ani_up[i].x = 30 + i*65;
		ani_up[i].y = 530;
		ani_up[i].w = 41;
		ani_up[i].h = 49;
	}
	for(int i=0;i<boost::size(ani_down);++i)
	{
		ani_down[i].x = 30 + i*65;
		ani_down[i].y = 595;
		ani_down[i].w = 41;
		ani_down[i].h = 49;
	}
	for(int i=0;i<boost::size(ani_left);++i)
	{
		ani_left[i].x = 30 + i*65;
		ani_left[i].y = 665;
		ani_left[i].w = 41;
		ani_left[i].h = 49;
	}
	for(int i=0;i<boost::size(ani_right);++i)
	{
		ani_right[i].x = 30 + i*65;
		ani_right[i].y = 733;
		ani_right[i].w = 41;
		ani_right[i].h = 49;
	}
}

SDL_Rect* Character::get_sprite()
{
	SDL_Rect* ret = NULL;
	bool standing = (velocity_x == 0 && velocity_y == 0);
		
	switch(dir)
	{
		case DOWN:
			standing ? ret = &ani_down[0] : ret = &ani_down[ (frame % (boost::size(ani_down)-1) ) + 1];
			break; 
		case UP:
			standing ? ret = &ani_up[0] : ret = &ani_up[ (frame % (boost::size(ani_up)-1) ) + 1];
			break;
		case LEFT:
			standing ? ret = &ani_left[0] : ret = &ani_left[ (frame % (boost::size(ani_left)-1) ) + 1];
			break;
		case RIGHT:
			standing ? ret = &ani_right[0] : ret = &ani_right[ (frame % (boost::size(ani_right)-1) ) + 1];
			break;
		default:
			break;
	}

	++frame;
	if(ret == NULL)
		std::cerr << "ERROR CHOOSING SPRITE" << std::endl;

	//std::cout << "Sprite chosen x:" << ret->x << " y:" << ret->y << std::endl;
	return ret;
}
SDL_Surface* Character::get_sheet()
{
	return sheet;
}
void Character::receive_event(SDL_Event* event)
{
	//std::cout << "Entering receive_event for " << object_name << std::endl;
	
	std::cerr << "The key pressed is #" << event->key.keysym.sym << std::endl;
	switch(event->type)
	{
		case SDL_KEYUP:
			if(event->key.keysym.sym == SDLK_UP && get_velocity_y()==-1)
			{
				set_velocity(get_velocity_x(),0);
			}
			else if(event->key.keysym.sym == SDLK_DOWN && get_velocity_y()==1)
			{
				set_velocity(get_velocity_x(),0);
			}
			else if(event->key.keysym.sym == SDLK_LEFT && get_velocity_x()==-1)
			{
				set_velocity(0,get_velocity_y());
			}
			else if(event->key.keysym.sym == SDLK_RIGHT && get_velocity_x()==1)
			{
				set_velocity(0,get_velocity_y());
			}
			break;
		case SDL_KEYDOWN:
			if(event->key.keysym.sym == SDLK_UP)
			{
				set_velocity(0, -1);
				dir = UP;
				std::cout << "UP" << std::endl;
			}
			else if(event->key.keysym.sym == SDLK_DOWN)
			{
				set_velocity(0, 1);
				dir = DOWN;
				std::cout << "DOWN" << std::endl;
			}
			else if(event->key.keysym.sym == SDLK_LEFT)
			{
				set_velocity(-1, 0);
				dir = LEFT;
				std::cout << "LEFT" << std::endl;
			}
			else if(event->key.keysym.sym == SDLK_RIGHT)
			{
				set_velocity(1, 0);
				dir = RIGHT;
				std::cout << "RIGHT" << std::endl;
			}
			break;
	}
	//std::cout << "Exiting receive_event for " << object_name << std::endl;
}

bool Character::init_object(Game_Window* game)
{
	sheet = game->load_image( sheet_file );
	if( sheet == NULL)
	{
		std::cerr << "Character initialization failed for " << object_name << std::endl;
		return false;
	}
	x = game->get_SCREEN_WIDTH() / 2;
	y = game->get_SCREEN_HEIGHT() / 2;
	std::cerr << "Character initialized" << std::endl;
	return true;
}

blit_param* Character::render_action(Game_Window* game)
{
	x+=velocity_x*travel_speed;
	y+=velocity_y*travel_speed;
	//std::cout << "velo-x: " << velocity_x <<", velo-y: " << velocity_y << std::endl;
	blit_param* parameters = new blit_param;

	parameters->x = get_x();
	parameters->y = get_y();
	parameters->source = get_sheet();
	parameters->dest = game->get_screen();
	parameters->clip = get_sprite();

	return parameters;
}

std::string Character::get_filename()
{
	return sheet_file;
}
int Character::get_velocity_x()
{
	return velocity_x;
}
int Character::get_velocity_y()
{
	return velocity_y;
}
void Character::set_velocity(int setx,int sety)
{
	velocity_x = setx;
	velocity_y = sety;
}
int Character::get_x()
{
	return x;
}
int Character::get_y()
{
	return y;
}
