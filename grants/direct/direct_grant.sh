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

# Direct Grant Request
KEYCLOAK=http://127.0.0.1:8080
REALM="demo"
GRANT_TYPE="authorization_code"
CLIENT="tpp1"
USER="test_user2"
USER_PASSWORD="123456"

echo "Keycloak host : $KEYCLOAK"
echo "Full URL : ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&username=${USER}&password=${USER_PASSWORD}"

echo "Keycloak host : $KEYCLOAK"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -vk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .


#Token validation using the Keycloak introspection end point.
echo VALIDATE VALID TOKEN
echo ACCESS_TOKEN=${ACCESS_TOKEN}
curl -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${ACCESS_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect"
echo ""


echo "VALIDATE INVALID TOKEN"
ACCESS_TOKEN="this_is_a_nonsense_token"
echo ACCESS_TOKEN=${ACCESS_TOKEN}
curl -v -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${ACCESS_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect"



