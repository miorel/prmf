#ifndef CUTTER_QUEUE_H
#define CUTTER_QUEUE_H

#define XQUEUENAME(x,y) x##_##y
#define QUEUENAME(x,y) XQUEUENAME(x,y)

#endif

#if defined (QUEUE_TYPE) && defined (QUEUE_IDENTIFIER)
struct QUEUENAME(QUEUE_IDENTIFIER,queue)
{
	QUEUE_TYPE * data;
	int size;
	int num_elem;
};

inline void QUEUENAME(QUEUE_IDENTIFIER,queue_enqueue) ( struct QUEUENAME(QUEUE_IDENTIFIER,queue) * q, QUEUE_TYPE p)
{
	/*
		I have this queue putting new items at the end of the array, and popping from the front.
	*/
	if(q->num_elem == q->size)
	{
		q->size = ( q->data == NULL ? 32 : q->size*2 );
		q->data = realloc( q->data, q->size*sizeof(QUEUE_TYPE));
	}
	q->data[q->num_elem++] = p;
}

inline QUEUE_TYPE QUEUENAME(QUEUE_IDENTIFIER,queue_pop) (struct QUEUENAME(QUEUE_IDENTIFIER,queue) * q)
{
	assert( q->data != NULL && q->num_elem > 0);
	QUEUE_TYPE r = q->data[0];
	int i;
	for(i=0;i<q->num_elem-1;++i)
	{
		q->data[i] = q->data[i+1];
	}
	q->num_elem -= 1;
	return r;
}

inline char QUEUENAME(QUEUE_IDENTIFIER,queue_empty) (struct QUEUENAME(QUEUE_IDENTIFIER,queue) * q )
{
	return (q->num_elem == 0 ? 1 : 0);
}

inline void QUEUENAME(QUEUE_IDENTIFIER,queue_destroy) ( struct QUEUENAME(QUEUE_IDENTIFIER,queue) * q)
{
	/*
		Free memory
	*/
}
#else
#error QUEUE_TYPE or QUEUE_IDENTIFIER is undefined
#endif

#undef QUEUE_TYPE
#undef QUEUE_IDENTIFIER




