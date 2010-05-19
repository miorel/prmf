SHELL=/bin/bash

SRC=algo.tex
RES=icpc.png
OTHER=Makefile README get_toc.pl
ALL=$(SRC) $(RES) $(OTHER)

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
