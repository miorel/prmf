#!/bin/bash

rm -rf dist
perl codegen.pl | perl pipe.pl
