#!/bin/bash

function padBase64  {
    STR=$1
    MOD=$((${#STR}%4))
    if [ $MOD -eq 1 ]; then
       STR="${STR}="
    elif [ $MOD -gt 1 ]; then
       STR="${STR}=="
    fi
    echo ${STR}
}

#KEYCLOAK=https://192.168.33.10:8443
KEYCLOAK=https://sso.datr.eu:8443
REALM="demo"
GRANT_TYPE="password"
CLIENT="tpp-registration-client"
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
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)

echo -n $ACCESS_TOKEN > ACCESS_TOKEN_BASE64.txt

PART1_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f1)
PART1_BASE64=$(padBase64 ${PART1_BASE64})
echo "HEADER"
HEADER=$(echo ${PART1_BASE64} | base64 -D)
echo -n $HEADER

PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo
echo "PAYLOAD"
echo $(echo ${PART2_BASE64} | base64 -D | jq .)

HEADERPAYLOAD_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f1-2)
echo -n $HEADERPAYLOAD_BASE64 > HEADERPAYLOAD_BASE64.txt

PART3_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f3)
PART3_BASE64=$(padBase64 ${PART3_BASE64})

echo -n $PART3_BASE64 \
| perl -ne 'tr|-_|+/|; print "$1\n" while length>76 and s/(.{0,76})//; print' \
| openssl enc -base64 -d > sig.dat

echo

openssl dgst -sha256 -verify publickey.pem -signature sig.dat HEADERPAYLOAD_BASE64.txt


