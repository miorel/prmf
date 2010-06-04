COMPILER=javac
RUNTIME=java
PACKAGER=jar
SIGNER=jarsigner

all:	jar

jar:	source
	${PACKAGER} cf ../dist/jtrisapplet.jar *.class

source:
	${COMPILER} *.java

clean:
	-rm *.class
	-rm ../dist/jtrisapplet.jar

sign:	jar
	${SIGNER} -storepass KeyStorePassword -keypass KeyPassPassword ../dist/jtrisapplet.jar mykey

commit:	clean
	svn commit ..

