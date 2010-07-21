SHELL=/bin/bash

COMPILER=javac
RUNTIME=java
PACKAGER=jar

all: jar

jar: source
	cd bin && ${PACKAGER} cf ../dist/jtrisapplet.jar *.class && cd ..

source:
	mkdir -p bin
	${COMPILER} -d bin src/*.java

clean:
	rm -rf bin dist/jtrisapplet.jar

commit:	clean
	svn commit .

.PHONY: all source jar clean commit
