/**
* @file types.h
* @brief Header file which contains some commonly used data structures and some convenient typedefs
* @author Rodrigo Salazar
*/

typedef struct box* boxp;
typedef boxp* boxpp;
typedef struct color* colorp;
typedef struct pixel* pixelp;

/**
* @brief Structure to hold the size and position of a bounding box
*/
struct box
{
	int x,y,w,h;
};

/**
* @brief Structure to represent the properties of a color, RGB(A)
*/
struct color
{
	unsigned char r,g,b,a;
};

/**
* @brief Structure for coordinates within an image, using i as height
*/
struct pixel
{
	int i,j;
};


