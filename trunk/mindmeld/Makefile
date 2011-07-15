SHELL=/bin/bash

all: mindmeld-dist.tbz2

mindmeld-dist.tbz2: index.pl mm.db Mindmeld.pm htaccess
	mkdir mindmeld-dist
	cp index.pl mm.db Mindmeld.pm htaccess mindmeld-dist
	cp show_question.pl show_category.pl login.pl register.pl mindmeld-dist
	chmod -R go-rwx mindmeld-dist
	find mindmeld-dist | perl -nle 'system("chmod", "0711", $$_) if /\.pl$$/'
	chmod go+r mindmeld-dist/htaccess
	tar -cf - mindmeld-dist | bzip2 --best > mindmeld-dist.tbz2
	rm -rf mindmeld-dist

mm.db: amino_acids.pl states.pl Mindmeld.pm clean
	perl amino_acids.pl
	perl states.pl

clean:
	rm -rf mm.db mindmeld-dist.tbz2 mindmeld-dist

.PHONY: clean all
