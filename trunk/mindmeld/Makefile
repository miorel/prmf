SHELL=/bin/bash

mindmeld-dist.tbz2: index.pl mm.db
	mkdir mindmeld-dist
	cp index.pl mm.db mindmeld-dist
	cp login.pl register.pl mindmeld-dist
	tar cvjf mindmeld-dist.tbz2 mindmeld-dist
	rm -rf mindmeld-dist

mm.db: amino_acids.pl states.pl Mindmeld.pm clean
	perl amino_acids.pl
	perl states.pl

clean:
	rm -f mm.db mindmeld-dist.tbz2

.PHONY: clean
