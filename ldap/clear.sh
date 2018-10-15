#!/usr/bin/env bash

PASSWORD=$1

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=justindav1s,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "cn=Marc Chambers,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "cn=June Rossi,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=superman,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=justindav1s,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "cn=Robert Wong,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=people, dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=groups,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "dc=datr,dc=eu"