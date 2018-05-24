#!/bin/bash


#set -x

function endswith   {
B64=$(echo $1 |sed 's/\=//g')
echo ${B64}==
}

function endswith2   {
B64=$(echo $1 |sed 's/\=//g')
echo ${B64}==
}

function endswith1   {
B64=$(echo $1 |sed 's/\=//g')
echo ${B64}=
}

KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
KEYCLOAK_USER=amazin-app-buy
CLIENT_KEY=a4ed2f3b-9953-4b92-a966-5e0cf0cc9280
REALM=amazin
TIMEOUT=30

#echo "Keycloak host : $KEYCLOAK"

RESPONSE=$(curl -s --connect-timeout $TIMEOUT \
    --user ${KEYCLOAK_USER}:${CLIENT_KEY} \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token -d grant_type=client_credentials)

#echo ${RESPONSE} | jq .

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
endswith1 ${PART2_BASE64} | base64 -D | jq .
endswith2 ${PART2_BASE64} | base64 -D | jq .


curl -v -H "Authorization: Bearer ${ACCESS_TOKEN}
" http://localhost:8081/products/all