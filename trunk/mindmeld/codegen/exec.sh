#!/bin/bash

cd "$(dirname $0)"
rm -rf lib
perl codegen.pl | perl pipe.pl
