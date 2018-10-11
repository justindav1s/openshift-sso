#!/bin/bash

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

#KEYCLOAK=https://192.168.33.10:8443
KEYCLOAK=https://sso.datr.eu:8443
REALM="PSD2"
GRANT_TYPE="password"
CLIENT="tpp-registration-client"
CLIENT_SECRET="c22faa6a-778d-4076-96bc-dce205292e63"
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

PART1_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f1)
PART1_BASE64=$(padBase64 ${PART1_BASE64})
echo "HEADER"
echo ${PART1_BASE64} | base64 -D | jq .

PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo "PAYLOAD"
echo ${PART2_BASE64} | base64 -D | jq .


echo ""
echo ""
echo "VALIDATE VALID TOKEN"
echo ACCESS_TOKEN=${ACCESS_TOKEN}
curl -k -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${ACCESS_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect"
echo ""
echo "VALIDATE INVALID TOKEN"

#Manipulate token substrings to test validation
SUB1=${ACCESS_TOKEN:27:5}
SUB2=${ACCESS_TOKEN:127:5}
ACCESS_TOKEN=${ACCESS_TOKEN/$SUB2/$SUB1}

echo ACCESS_TOKEN=${ACCESS_TOKEN}
curl -k -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${ACCESS_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect"



