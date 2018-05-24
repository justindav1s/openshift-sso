#!/bin/bash


#set -x

KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
KEYCLOAK_USER=amazin-app-buy
CLIENT_KEY=a4ed2f3b-9953-4b92-a966-5e0cf0cc9280
REALM=amazin
TIMEOUT=30

echo "Keycloak host : $KEYCLOAK"

RESPONSE=$(curl -v --connect-timeout $TIMEOUT --user ${KEYCLOAK_USER}:${CLIENT_KEY} ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token -d grant_type=client_credentials)
echo ${RESPONSE} | jq .

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
echo ACCESS_TOKEN
#echo ${ACCESS_TOKEN}
echo PART1
PART1_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f1)
#echo PART1_BASE64=${PART1_BASE64}
echo ${PART1_BASE64}== | base64 -D | jq .
echo PART2
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
#echo PART2_BASE64=${PART2_BASE64}
echo ${PART2_BASE64}= | base64 -D | jq .

echo ""
REFRESH_TOKEN=$(echo ${RESPONSE} | jq -r .refresh_token)
echo REFRESH_TOKEN
#echo ${REFRESH_TOKEN}
echo PART1
PART1_BASE64=$(echo ${REFRESH_TOKEN} | cut -d"." -f1)
#echo PART1_BASE64=${PART1_BASE64}
echo ${PART1_BASE64}== | base64 -D | jq .
echo PART2
PART2_BASE64=$(echo ${REFRESH_TOKEN} | cut -d"." -f2)
#echo PART2_BASE64=${PART2_BASE64}
echo ${PART2_BASE64}= | base64 -D | jq .
