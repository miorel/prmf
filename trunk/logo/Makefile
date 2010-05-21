SHELL=/bin/bash

logo.png: Logo.class SaccoVanzettiWebBold.ttf
	java Logo

Logo.class: Logo.java
	javac Logo.java

clean:
	rm -f Logo.class logo.png

.PHONY: clean
