#!/usr/bin/env bash

#!/bin/bash

#create our key pairs
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem

#sign and verify
echo 1
openssl dgst -sha1 -sign private.pem -out "$1".sha1 $1
echo 2
openssl dgst -sha1 -verify public.pem -signature "$1".sha1 $1
echo 3
#purposely not cleaning up after the program.