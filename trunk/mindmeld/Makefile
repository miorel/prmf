SHELL=/bin/bash

DIST=mindmeld-dist

PAGES=index.pl options.pl stats.pl study.pl set_grade.pl show_question.pl show_category.pl logout.pl edit_question.pl import.pl todo.pl
CSS=css/green.css

Q_SCRIPTS=questions/amino_acids.pl questions/states.pl questions/symbols.pl


IMAGES=rc_tl.png rc_tr.png rc_bl.png rc_br.png mindmeld.png

all: $(DIST).tbz2

$(DIST).tbz2: clean codegen mm.db $(PAGES) $(IMAGES) $(CSS) MindMeld.pm htaccess mm.ico
	mkdir "$(DIST)"
	mkdir "$(DIST)/MindMeld"
	cp mm.db MindMeld.pm htaccess pixel.png mm.ico mindmeld-dist
	cp codegen/dist/MindMeld/* mindmeld-dist/MindMeld
	chmod -R go-rwx "$(DIST)"
	chmod go+r "$(DIST)"/htaccess
	chmod go+r "$(DIST)"/pixel.png
	chmod go+r "$(DIST)"/mm.ico
	
	mkdir "$(DIST)/img"
	for file in $(IMAGES); do \
		outfile="$(DIST)/img/$$(basename $$file)"; \
		cp "$$file" "$$outfile"; \
		chmod 0644 "$$outfile"; \
	done
		
	mkdir "$(DIST)/css"
	for file in $(CSS); do \
		outfile="$(DIST)/css/$$(basename $$file)"; \
		yui-compressor "$$file" > "$$outfile"; \
		chmod 0644 "$$outfile"; \
	done
	
	for file in $(PAGES); do \
		outfile="$(DIST)/$$(basename $$file)"; \
		cat lib/header.pl "$$file" > "$$outfile"; \
		chmod 0711 "$$outfile"; \
	done
	
	tar -cf - "$(DIST)" | bzip2 --best > "$(DIST)".tbz2
	$(RM) -r "$(DIST)"

mm.db: clean codegen $(Q_SCRIPTS) MindMeld.pm
	for file in $(Q_SCRIPTS); do \
		PERL5LIB='codegen/dist:../PRMF-Auth/lib' perl $$file; \
	done
	#for file in questions/mnemosyne/*.xml; do \
	#	PERL5LIB='codegen/dist:../PRMF-Auth/lib' perl questions/mnemosyne.pl "$$file" "$$file.tmp"; \
	#	mv "$$file.tmp" "$$file"; \
	#done

clean:
	$(RM) -r mm.db "$(DIST).tbz2" "$(DIST)" codegen/dist $(IMAGES) logo.svg

codegen: codegen/exec.sh codegen/codegen.pl codegen/pipe.pl
	codegen/exec.sh

logo.svg: logo.pl
	perl logo.pl

mindmeld.png: logo.svg
	inkscape -z -h=50 -e=mindmeld.png logo.svg

rc_tl.png: circle.svg
	inkscape -z -a=0:30:30:60 -e=rc_tl.png circle.svg

rc_tr.png: circle.svg
	inkscape -z -a=30:30:60:60 -e=rc_tr.png circle.svg

rc_bl.png: circle.svg
	inkscape -z -a=0:0:30:30 -e=rc_bl.png circle.svg

rc_br.png: circle.svg
	inkscape -z -a=30:0:60:30 -e=rc_br.png circle.svg

.PHONY: clean all codegen
