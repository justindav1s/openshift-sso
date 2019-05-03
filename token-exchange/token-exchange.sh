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
# Direct Grant Request
KEYCLOAK=http://192.168.33.10:8080
REALM="master"
GRANT_TYPE="password"
CLIENT="web1"
CLIENT_SECRET="3d4b20a0-3f0b-4438-b4b5-10923d761cd5"
USER="justindav1s"
USER_PASSWORD="password"

echo "Keycloak host : $KEYCLOAK"
echo "Full URL : ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token"

#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&username=${USER}&password=${USER_PASSWORD}"

#echo "Keycloak host : $KEYCLOAK"
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


#Token validation using the Keycloak introspection end point.
echo VALIDATE VALID TOKEN
echo ACCESS_TOKEN=${ACCESS_TOKEN}
curl -u "${CLIENT}:${CLIENT_SECRET}" -d "token=${ACCESS_TOKEN}" "${KEYCLOAK}/auth/realms/$REALM/protocol/openid-connect/token/introspect"
echo ""
echo ""
echo ""

echo "*****Token Exchange 1****"

GRANT_TYPE="urn:ietf:params:oauth:grant-type:token-exchange"
CLIENT="service1"
CLIENT_SECRET="c61a0e94-d658-4602-90c9-26d65b28bf76"
SUBJECT_TOKEN=$ACCESS_TOKEN

#Get Token
POST_BODY="scope=email&grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&subject_token=${SUBJECT_TOKEN}"

echo POST_BODY=${POST_BODY}

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
echo ""
echo "*****Token Exchange 2****"

GRANT_TYPE="urn:ietf:params:oauth:grant-type:token-exchange"
CLIENT="service2"
CLIENT_SECRET="bd5fa9a3-4649-4bfc-8069-79d10f8e6438"
SUBJECT_TOKEN=$ACCESS_TOKEN

#Get Token
POST_BODY="scope=access&grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&subject_token=${SUBJECT_TOKEN}"

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

echo "********************************************************************************"
