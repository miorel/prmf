SHELL=/bin/bash

COMPILER=javac
RUNTIME=java
PACKAGER=jar
SIGNER=jarsigner

all: jar

jar: source
	cd bin && ${PACKAGER} cf ../dist/jtrisapplet.jar *.class && cd ..

source:
	mkdir -p bin
	${COMPILER} -d bin src/*.java

clean:
	rm -rf bin dist/jtrisapplet.jar

sign: jar
	${SIGNER} -storepass KeyStorePassword -keypass KeyPassPassword dist/jtrisapplet.jar mykey

commit:	clean
	svn commit .

.PHONY: all source jar clean sign commit
