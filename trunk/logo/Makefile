SHELL=/bin/bash

png: logo.png

class: Logo.class

clean:
	rm -f Logo.class logo.png

logo.png: Logo.class SaccoVanzettiWebBold.ttf
	java Logo

Logo.class: Logo.java
	javac Logo.java

.PHONY: png class clean
