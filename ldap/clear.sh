#!/usr/bin/env bash

PASSWORD=$1

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=justindav1s,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=marc,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=rossi,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=superman,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "uid=wong,ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=marketing,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=accounting,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=superhero,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=people,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "ou=leadership,dc=datr,dc=eu"

ldapdelete -w ${PASSWORD} -D "cn=ldapadm,dc=datr,dc=eu" "dc=datr,dc=eu"