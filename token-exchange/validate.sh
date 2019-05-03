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

KEYCLOAK=http://192.168.33.10:8080
REALM="master"

echo "*****Login Starting Client : service1 ****"
GRANT_TYPE="client_credentials"
CLIENT="service1"
CLIENT_SECRET="c61a0e94-d658-4602-90c9-26d65b28bf76"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}"

#echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -sk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

#echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

echo ""
echo ""

echo "*****Login Target Client : target-client ****"
GRANT_TYPE="client_credentials"
TARGET_CLIENT="target-client"
TARGET_CLIENT_SECRET="680dd85c-9a40-4ad7-bc54-3ab16bded6de"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${TARGET_CLIENT}&client_secret=${TARGET_CLIENT_SECRET}"

#echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -sk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

#echo "RESPONSE"=${RESPONSE}
SUBJECT_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${SUBJECT_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo SUBJECT_TOKEN
echo ${PART2_BASE64} | base64 -D | jq .

# Now toggle target client enabled/disabled
read  -n 1 -p "Validate ? " validate

echo ""
echo ""

#Validate the subject_token we are going to use to impersonate
#Token validation using the Keycloak introspection end point.
echo "*****Validate target client token ****"
RESPONSE=$(curl -sk -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${SUBJECT_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect")
echo ${RESPONSE} | jq .


