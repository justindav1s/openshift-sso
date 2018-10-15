#!/usr/bin/env bash

PASSWORD=$1


ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "cn=*"