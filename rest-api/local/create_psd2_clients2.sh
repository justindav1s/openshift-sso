#!/usr/bin/env bash


KEYCLOAK=http://127.0.0.1:8080
echo "Keycloak host : $KEYCLOAK"

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


NEW_CLIENT_SECRET="changeme"
NEW_CLIENT_NAME="amazon"
NEW_CLIENT_REDIRECT_URI=http://127.0.0.1:9090/authcode

echo "GET ACCESS TONKEN FOR REGISTRATION CLIENT*****************************************"
# Get Access Token with Client Credentials Request
REALM="master"
GRANT_TYPE="client_credentials"
CLIENT="kbc-registration"
CLIENT_SECRET="5b77200c-381e-47a8-90bb-41b101f0038b"

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


echo "CONSTRUCT JSON*******************************************************************"
REALM="aisp"
CLIENTJSON=`cat client_template.json`
echo CLIENTJSON = ${CLIENTJSON}

#update user's json representation with new consent id
CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg CLIENT_NAME "$NEW_CLIENT_NAME" '.clientId = $CLIENT_NAME')
echo CLIENTJSON = ${CLIENTJSON}

CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg CLIENT_NAME "$NEW_CLIENT_NAME" '.name = $CLIENT_NAME')
echo CLIENTJSON = ${CLIENTJSON}

CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg CLIENT_SECRET "$NEW_CLIENT_SECRET" '.secret = $CLIENT_SECRET')
echo CLIENTJSON = ${CLIENTJSON}

CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg CLIENT_REDIRECT_URI "$NEW_CLIENT_REDIRECT_URI" '.redirectUris[0] = $CLIENT_REDIRECT_URI')
echo CLIENTJSON = ${CLIENTJSON}

CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg REALM "$REALM" '.defaultRoles[0] = $REALM')
echo CLIENTJSON = ${CLIENTJSON}

echo ${CLIENTJSON} > ${NEW_CLIENT_NAME}_${REALM}.json


echo "REGISTER CLIENT IN AISP*******************************************************************"

RESPONSE=$(curl -vk \
    -X POST \
    --data @${NEW_CLIENT_NAME}_${REALM}.json \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/clients)

echo "RESPONSE"=${RESPONSE}

echo "CONSTRUCT JSON*******************************************************************"
REALM="pisp"
CLIENTJSON=$(echo ${CLIENTJSON} | jq  -r --arg REALM "$REALM" '.defaultRoles[0] = $REALM')
echo CLIENTJSON = ${CLIENTJSON}

echo ${CLIENTJSON} > ${NEW_CLIENT_NAME}_${REALM}.json


echo "REGISTER CLIENT IN PISP*******************************************************************"
RESPONSE=$(curl -vk \
    -X POST \
    --data @${NEW_CLIENT_NAME}_${REALM}.json \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/clients)

echo "RESPONSE"=${RESPONSE}