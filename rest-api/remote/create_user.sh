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
KEYCLOAK=https://sso-sso.apps.ocp.datr.eu
REALM="demo"
GRANT_TYPE="client_credentials"
CLIENT="userapi"
CLIENT_SECRET="ff533736-cb9a-4909-b25f-963d89000844"

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

USERNAME=tonyh
FIRSTNAME=Anthony
LASTNAME=Hopkins
EMAIL="tony@email.com"
CONSENTID=123456

USERJSON=`cat user_template.json`
echo USERJSON = ${USERJSON}

#update user's json representation with new consent id
USERJSON=$(echo ${USERJSON} | jq  -r --arg USERNAME "$USERNAME" '.username = $USERNAME')
echo USERJSON = ${USERJSON}

USERJSON=$(echo ${USERJSON} | jq  -r --arg FIRSTNAME "$FIRSTNAME" '.firstName = $FIRSTNAME')
echo USERJSON = ${USERJSON}

USERJSON=$(echo ${USERJSON} | jq  -r --arg LASTNAME "$LASTNAME" '.lastName = $LASTNAME')
echo USERJSON = ${USERJSON}

USERJSON=$(echo ${USERJSON} | jq  -r --arg EMAIL "$EMAIL" '.email = $EMAIL')
echo USERJSON = ${USERJSON}

USERJSON=$(echo ${USERJSON} | jq  -r --arg CONSENTID "$CONSENTID" '.attributes.consentid[0] = $CONSENTID')
echo USERJSON = ${USERJSON}


echo ${USERJSON} > createuser.json

#Update user in Keycloak
RESPONSE=$(curl -vk \
    -X POST \
    --data @createuser.json \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users)

echo "RESPONSE"=${RESPONSE}