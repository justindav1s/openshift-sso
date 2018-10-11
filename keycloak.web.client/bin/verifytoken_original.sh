#!/usr/bin/env bash

assert() { if [[ $1 != $2 ]]; then echo "assert" $3; exit; fi }

KEYCLOAK=https://sso.datr.eu:8443
REALM="PSD2"
GRANT_TYPE="password"
CLIENT="kbci"
CLIENT_SECRET="c22faa6a-778d-4076-96bc-dce205292e63"
USER="test_user2"
USER_PASSWORD="123456"

echo "Keycloak host : $KEYCLOAK"
echo "Full URL : ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&username=${USER}&password=${USER_PASSWORD}"

echo "Keycloak host : $KEYCLOAK"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -sk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo "RESPONSE"=${RESPONSE}

resp=${RESPONSE%%?,?expires_in*}
jwt=${resp#*token?:?}

echo ""
echo JWT:
echo $jwt
echo ""
input=${jwt%.*}
echo ""
echo INPUT:
echo $input
encHdr=${input%.*}
encPayload=${input#*.}
encSig=${jwt##*.}
assert $jwt "$encHdr.$encPayload.$encSig" "failed to decompose jwt"

echo ""
echo Header:
echo $encHdr | openssl enc -base64 -d
echo
echo Payload:
echo -n $encPayload \
| perl -ne 'tr|-_|+/|; print "$1\n" while length>76 and s/(.{0,76})//; $_ .= ("", "", "==", "=")[length($_) % 4]; print' \
| openssl enc -base64 -d
echo

echo
echo encSig pre mung:
echo $encSig

echo
echo encSig post mung:
echo $encSig \
| perl -ne 'tr|-_|+/|; print "$1\n" while length>76 and s/(.{0,76})//; $_ .= ("", "", "==", "=")[length($_) % 4]; print'


# transform sig from JWT
# exchange -'s for +'s
# exchange _'s for /'s
# break sig in 76 char segments, seperate with carriage returns
# pad to mod 4 for base64 decoding
echo -n $encSig \
| perl -ne 'tr|-_|+/|; print "$1\n" while length>76 and s/(.{0,76})//; $_ .= ("", "", "==", "=")[length($_) % 4]; print' \
| openssl enc -base64 -d > demo.sig.dat


echo -n $input > demo.txt
echo
openssl dgst -sha256 -verify datr.eu_publickey.pem -signature demo.sig.dat demo.txt