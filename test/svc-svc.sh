#!/bin/bash

function padBase64  {
    STR=$1
    MOD=$((${#STR}%4))
    if [[ $MOD -eq 1 ]]; then
       STR="${STR}="
    elif [[ $MOD -eq 2 ]]; then
       STR="${STR}=="
    elif [[ $MOD -eq 3 ]]; then
       STR="${STR}=="
    fi
    echo ${STR}
}


KEYCLOAK=http://127.0.0.1:8080
REALM="test"
GRANT_TYPE="client_credentials"
CLIENT="supplier1"
CLIENT_SECRET="be4fde57-c5a0-44f6-aa74-367217f88d83"
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}"

echo "Keycloak host : $KEYCLOAK"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -vk  \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

#REFRESH_TOKEN=$(echo ${RESPONSE} | jq -r .refresh_token)
#PART2_BASE64=$(echo ${REFRESH_TOKEN} | cut -d"." -f2)
#PART2_BASE64=$(padBase64 ${PART2_BASE64})
#echo ${PART2_BASE64} | base64 -D | jq .