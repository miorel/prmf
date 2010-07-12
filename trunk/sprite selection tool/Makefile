SHELL=/bin/bash

SRC=cutter.c
HEADER=cutter_dynarr.h cutter_queue.h types.h

a.out: $(SRC) $(HEADER)
	gcc -lpng $(SRC)

clean:
	rm -f a.out

.PHONY: clean
