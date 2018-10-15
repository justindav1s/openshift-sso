#!/usr/bin/env bash


ldapadd -x -w 12jnd34 -D "cn=ldapadm,dc=datr,dc=eu" -f base.ldif

echo setting passwords

ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=Justin Davis,ou=people,dc=datr,dc=eu"
ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=Super Man,ou=people,dc=datr,dc=eu"
ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=June Rossi,ou=people,dc=datr,dc=eu"
ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=Marc Chambers,ou=people,dc=datr,dc=eu"
ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=Robert Wong,ou=people,dc=datr,dc=eu"
ldappasswd -H ldap://127.0.0.1:389 -D "cn=ldapadm,dc=datr,dc=eu" -w 12jnd34 -s password "cn=Cat Woman,ou=people,dc=datr,dc=eu"

