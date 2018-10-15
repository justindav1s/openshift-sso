#!/usr/bin/env bash

PASSWORD=$1

ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*" | grep dn > dns.txt

tac dns.txt > snd.txt

while read dn;
do
    dn=$(echo $dn | sed 's/dn: //')
    echo Deleting : $dn;
    ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "$dn"
done < snd.txt

rm -rf dns.txt snd.txt

ldapsearch  -w $PASSWORD -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "*"