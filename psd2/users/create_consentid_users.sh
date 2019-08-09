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

KEYCLOAK=http://127.0.0.1:8080
echo "Keycloak host : $KEYCLOAK"

echo "GET ACCESS TONKEN FOR REGISTRATION CLIENT*****************************************"
# Get Access Token with Client Credentials Request
REALM="master"
GRANT_TYPE="client_credentials"
CLIENT="psd2-registration"
CLIENT_SECRET="29483e03-1182-4ff0-86a6-5e7da17bcf80"

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

USERNAME=$1

USERJSON=`cat user_template.json`
echo USERJSON = ${USERJSON}

#update user's json representation with new consent id
USERJSON=$(echo ${USERJSON} | jq  -r --arg USERNAME "$USERNAME" '.username = $USERNAME')
echo USERJSON = ${USERJSON}

echo ${USERJSON} > createuser.json

REALM="accounts"
#Update user in Keycloak
RESPONSE=$(curl -vk \
    -X POST \
    --data @createuser.json \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users)

echo "RESPONSE"=${RESPONSE}

REALM="payments"
#Update user in Keycloak
RESPONSE=$(curl -vk \
    -X POST \
    --data @createuser.json \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users)

echo "RESPONSE"=${RESPONSE}

rm -rf createuser.json