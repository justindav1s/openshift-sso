#!/bin/bash

# This script requires jq, a command line to to parse and format JSon.
# https://stedolan.github.io/jq/

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

echo "********************************************************************************"
# Get Access Token with Client Credentials Request
KEYCLOAK=http://127.0.0.1:8080
REALM="demo"
GRANT_TYPE="client_credentials"
CLIENT="clientapi"
CLIENT_SECRET="ec449db5-8dc5-476d-83f0-21680d6d9b6a"

echo "Keycloak host : $KEYCLOAK"
echo "Full URL : ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -sk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .


#Get All Clients
RESPONSE=$(curl -vk \
    -X GET \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/clients?clientId=client1)

echo ${RESPONSE} | jq .

