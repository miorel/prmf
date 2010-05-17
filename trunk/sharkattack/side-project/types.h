typedef struct box* boxp;
typedef boxp* boxpp;
typedef struct color* colorp;
typedef struct pixel* pixelp;

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


