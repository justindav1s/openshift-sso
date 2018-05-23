#!/bin/bash


function endswith   {
B64=$(echo $1 |sed 's/\=//g')
echo ${B64}==
}

KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
REALM=amazin
TIMEOUT=30

echo "Keycloak host : $KEYCLOAK"

RESPONSE=$(curl -v --connect-timeout $TIMEOUT \
    -d "@buy_postbody.txt" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo ${RESPONSE} | jq .

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
echo ACCESS_TOKEN
#echo ${ACCESS_TOKEN}
echo PART1
PART1_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f1)
#echo PART1_BASE64=${PART1_BASE64}
endswith ${PART1_BASE64} | base64 -D | jq .
echo PART2
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
#echo PART2_BASE64=${PART2_BASE64}
endswith ${PART2_BASE64} | base64 -D | jq .

echo ""
REFRESH_TOKEN=$(echo ${RESPONSE} | jq -r .refresh_token)
echo REFRESH_TOKEN
#echo ${REFRESH_TOKEN}
echo PART1
PART1_BASE64=$(echo ${REFRESH_TOKEN} | cut -d"." -f1)
#echo PART1_BASE64=${PART1_BASE64}
endswith ${PART1_BASE64} | base64 -D | jq .
echo PART2
PART2_BASE64=$(echo ${REFRESH_TOKEN} | cut -d"." -f2)
echo PART2_BASE64=$(endswith ${PART2_BASE64})
echo ${PART2_BASE64}= | base64 -D | jq .
