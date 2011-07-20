SHELL=/bin/bash
PAGES=index.pl options.pl stats.pl study.pl set_grade.pl show_question.pl show_category.pl

all: mindmeld-dist.tbz2

mindmeld-dist.tbz2: $(PAGES) mm.db MindMeld.pm htaccess mindmeld.png codegen
	mkdir mindmeld-dist
	mkdir mindmeld-dist/{MindMeld,img,css}
	cp mm.db MindMeld.pm htaccess mindmeld.png mindmeld-dist
	cp $(PAGES) mindmeld-dist
	cp login.pl register.pl mindmeld-dist 
	cp codegen/dist/MindMeld/* mindmeld-dist/MindMeld
	chmod -R go-rwx mindmeld-dist
	find mindmeld-dist | perl -nle 'system("chmod", "0711", $$_) if /\.pl$$/'
	chmod go+r mindmeld-dist/htaccess
	chmod go+r mindmeld-dist/mindmeld.png
	tar -cf - mindmeld-dist | bzip2 --best > mindmeld-dist.tbz2
	rm -rf mindmeld-dist

mm.db: amino_acids.pl states.pl MindMeld.pm clean codegen
	PERL5LIB='codegen/dist' perl amino_acids.pl
	PERL5LIB='codegen/dist' perl states.pl

clean:
	rm -rf mm.db mindmeld-dist.tbz2 mindmeld-dist codegen/dist

codegen: codegen/exec.sh codegen/codegen.pl codegen/pipe.pl
	codegen/exec.sh

.PHONY: clean all codegen
