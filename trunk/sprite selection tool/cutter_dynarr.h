/**
* @file cutter_dynarr.h
* @brief This is a 'generic' dynamic array header file which is assembled at compile-time using the #define DYNARR_TYPE & #define DYNARR_IDENTIFIER
* @author Rodrigo Salazar
*/

/** \defgroup compile_dstruct Compile-time Data Structures */
/*@{*/

#ifndef CUTTER_DYNARR_H
#define CUTTER_DYNARR_H

#define XDYNARRNAME(x,y) x##_##y
#define DYNARRNAME(x,y) XDYNARRNAME(x,y)

#endif

#if defined (DYNARR_TYPE) && defined (DYNARR_IDENTIFIER)

/**
* @brief 'Generic' Dynamic array which is assembled using preprocessor directives
*/
struct DYNARRNAME(DYNARR_IDENTIFIER,dynarr)
{
	DYNARR_TYPE * data;
	int size;
	int num_elem;
};

/**
* @brief 'Generic' Method which returns the next slot availible in a given dynamic array.
*			Example: pixel_dynarr mypixdynarr; struct pixel* pixp = pixel_dynarr_slot(mypixdynarr);
* @param dyna This obscure datatype is seen in the main code as pixel_dynarr or box_dynarr
*/
inline DYNARR_TYPE * DYNARRNAME(DYNARR_IDENTIFIER,dynarr_slot) (struct DYNARRNAME(DYNARR_IDENTIFIER,dynarr) * dyna)
{
	/*
		I have this set up like a dynamic array except without any get and set methods..
		it's only function is to return the address of the next available structure.
	*/
	if( dyna->num_elem == dyna->size )
	{
			dyna->size = (dyna->data == NULL ? 16 :dyna->size*2);
			dyna->data = realloc(dyna->data, dyna->size*sizeof( DYNARR_TYPE ));
	}
	return &dyna->data[dyna->num_elem++];
}

/**
* @brief 'Generic' Method which free memory in a given dynamic array
*/
inline void DYNARRNAME(DYNARR_IDENTIFIER,dynarr_destroy) (struct DYNARRNAME(DYNARR_IDENTIFIER,dynarr) * dyna)
{
	/*
		Free memory
	*/
}

#else
#error DYNARR_TYPE or DYNARR_IDENTIFIER is undefined
#endif

#undef DYNARR_TYPE
#undef DYNARR_IDENTIFIER
/*@}*/

