#ifndef CUTTER_DYNARR_H
#define CUTTER_DYNARR_H

#define XDYNARRNAME(x,y) x##_##y
#define DYNARRNAME(x,y) XDYNARRNAME(x,y)

#endif

#if defined (DYNARR_TYPE) && defined (DYNARR_IDENTIFIER)
struct DYNARRNAME(DYNARR_IDENTIFIER,dynarr)
{
	DYNARR_TYPE * data;
	int size;
	int num_elem;
};

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




