#include <iostream>
#include "demo.h"

int main()
{
	Demo demo;
	
	if( demo.init() == false )
	{
		std::cout << "SDL Init Failed" << std::endl;
		return 1;
	}

	demo.play();
	return 0;
}

