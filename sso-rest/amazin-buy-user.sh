#!/bin/bash

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
REALM=amazin
TIMEOUT=30

#echo "Keycloak host : $KEYCLOAK"

RESPONSE=$(curl -s -v --connect-timeout $TIMEOUT \
    -d "@buy_postbody.txt" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

#echo ${RESPONSE} | jq .

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
endswith1 ${PART2_BASE64} | base64 -D | jq .
endswith2 ${PART2_BASE64} | base64 -D | jq .


./decodeToken.sh ${ACCESS_TOKEN}

curl -v -H "Authorization: Bearer ${ACCESS_TOKEN}
" http://localhost:8081/products/all