# space-separated list of LaTeX source files
SRC=algo.tex

# non-LaTeX resources that are incorporated in the pdf
RES=icpc.png pp_vol.pdf

# other files that belong in a source archive
OTHER=ChangeLog Makefile README toc.pl

ALL=$(SRC) $(RES) $(OTHER)

SHELL=/bin/bash

ZIP=zip
ZIP_FLAGS=-9 -r

PDFLATEX=pdflatex
PDFLATEX_FLAGS=-interaction=nonstopmode
PDFLATEX_CMD=$(PDFLATEX) $(PDFLATEX_FLAGS) $(SRC)

RUBBER=rubber
RUBBER_FLAGS=--pdf
RUBBER_CMD=$(RUBBER) $(RUBBER_FLAGS) $(SRC)

algo.pdf: $(SRC) $(RES) date.txt
	if (type -p $(RUBBER) >& /dev/null); then \
		$(RUBBER_CMD); \
	else \
		{ $(PDFLATEX_CMD); } && { $(PDFLATEX_CMD); } && { $(PDFLATEX_CMD); }; \
	fi

date.txt: $(SRC) $(RES)
	date +"%B %-e, %Y %-l:%M%P" -r `ls -t $(SRC) $(RES) | head -n1` > date.txt

algo.zip: $(ALL)
	$(ZIP) $(ZIP_FLAGS) algo.zip $(ALL)

dist: algo.zip

clean:
	$(RM) algo.{aux,log,pdf,toc,zip} date.txt

.PHONY: dist clean
