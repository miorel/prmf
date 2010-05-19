# space-separated list of LaTeX source files
SRC=algo.tex

# non-LaTeX resources that are incorporated in the pdf
RES=icpc.png

# other files that belong in a source archive
OTHER=Makefile README get_toc.pl

ALL=$(SRC) $(RES) $(OTHER)
SHELL=/bin/bash
PDFLATEX_CMD=pdflatex $(SRC)

pdf: algo.pdf

zip: algo.zip

clean:
	rm -f algo.{aux,log,pdf,toc,zip}

algo.pdf: $(SRC) $(RES)
	rubber --pdf $(SRC) || ($(PDFLATEX_CMD); $(PDFLATEX_CMD); $(PDFLATEX_CMD))

algo.zip: $(ALL)
	zip -r algo.zip $(ALL)

.PHONY: clean
