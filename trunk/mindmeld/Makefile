SHELL=/bin/bash

PAGES=index.pl options.pl stats.pl study.pl set_grade.pl show_question.pl show_category.pl logout.pl
IMAGES=rc_tl.png rc_tr.png rc_bl.png rc_br.png
Q_SCRIPTS=questions/amino_acids.pl questions/states.pl questions/symbols.pl

all: mindmeld-dist.tbz2

mindmeld-dist.tbz2: mm.db $(PAGES) $(IMAGES) MindMeld.pm htaccess mindmeld.png codegen
	mkdir mindmeld-dist
	mkdir mindmeld-dist/{MindMeld,img,css}
	cp mm.db MindMeld.pm htaccess mindmeld.png pixel.png mindmeld-dist
	cp $(PAGES) mindmeld-dist
	cp $(IMAGES) mindmeld-dist/img
	cp codegen/dist/MindMeld/* mindmeld-dist/MindMeld
	yui-compressor css/green.css > mindmeld-dist/css/green.css
	chmod -R go-rwx mindmeld-dist
	find mindmeld-dist | perl -nle 'system("chmod", "0711", $$_) if /\.pl$$/'
	chmod go+r mindmeld-dist/htaccess
	chmod go+r mindmeld-dist/mindmeld.png
	chmod go+r mindmeld-dist/pixel.png
	chmod go+r mindmeld-dist/css/green.css
	chmod go+r mindmeld-dist/img/*.png
	tar -cf - mindmeld-dist | bzip2 --best > mindmeld-dist.tbz2
	$(RM) -r mindmeld-dist

mm.db: $(Q_SCRIPTS) MindMeld.pm clean codegen
	for script in $(Q_SCRIPTS); do \
		PERL5LIB='codegen/dist:../PRMF-Auth/lib' perl $$script; \
	done

clean:
	$(RM) -r mm.db mindmeld-dist.tbz2 mindmeld-dist codegen/dist $(IMAGES)

codegen: codegen/exec.sh codegen/codegen.pl codegen/pipe.pl
	codegen/exec.sh

rc_tl.png: circle.svg
	inkscape -z -a=0:30:30:60 -e=rc_tl.png circle.svg

rc_tr.png: circle.svg
	inkscape -z -a=30:30:60:60 -e=rc_tr.png circle.svg

rc_bl.png: circle.svg
	inkscape -z -a=0:0:30:30 -e=rc_bl.png circle.svg

rc_br.png: circle.svg
	inkscape -z -a=30:0:60:30 -e=rc_br.png circle.svg

.PHONY: clean all codegen
