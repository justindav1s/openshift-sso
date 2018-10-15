#!/usr/bin/env bash

PASSWORD=$1


ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*" | grep dn > dns.txt

tac dns.txt > snd.txt

while read dn;
do
    echo $dn;
    ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" $dn
done < snd.txt


ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*"