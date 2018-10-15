#!/usr/bin/env bash

PASSWORD=$1


ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*" | grep dn > dns.txt

while read dn;
do
    echo $dn;
    ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" $dn
done < dns.txt


ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*"