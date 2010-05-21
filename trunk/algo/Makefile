# space-separated list of LaTeX source files
SRC=algo.tex

# non-LaTeX resources that are incorporated in the pdf
RES=icpc.png

# other files that belong in a source archive
OTHER=ChangeLog Makefile README toc.pl

ALL=$(SRC) $(RES) $(OTHER)
SHELL=/bin/bash
PDFLATEX_CMD=pdflatex -interaction=nonstopmode $(SRC)

algo.pdf: $(SRC) $(RES)
	if (which rubber >& /dev/null) then (rubber --pdf $(SRC)) else ($(PDFLATEX_CMD) && $(PDFLATEX_CMD) && $(PDFLATEX_CMD)) fi

algo.zip: $(ALL)
	zip -9 -r algo.zip $(ALL)

dist: algo.zip

clean:
	rm -f algo.{aux,log,pdf,toc,zip}

.PHONY: clean dist
