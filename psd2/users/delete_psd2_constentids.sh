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

echo "GET ACCESS TOKEN**************************************************************"
# Get Access Token with Client Credentials Request
KEYCLOAK=http://127.0.0.1:8080
REALM="master"
GRANT_TYPE="client_credentials"
CLIENT="psd2-registration"
CLIENT_SECRET="0d64225f-8e1e-402c-b479-bc4ffa853ae6"

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


USERNAME=fireconsentid2

echo "DELETE USER FROM accounts**************************************************************"
REALM="accounts"
#Get Users matching filter
RESPONSE=$(curl -vk \
    -X GET \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users?username=${USERNAME})

echo ${RESPONSE} | jq .

#Find my user's id
MYUSER=$(echo ${RESPONSE} | jq -r --arg USERNAME "$USERNAME" '.[] | select(.username==$USERNAME)')
echo $MYUSER | jq .
MYUSERID=$(echo ${MYUSER} | jq -r ."id")
echo MYUSERID=$MYUSERID

#Get my client
RESPONSE=$(curl -vk \
    -X DELETE \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users/$MYUSERID)

echo "RESPONSE"=${RESPONSE}

echo ${RESPONSE} | jq .

echo "DELETE CLIENT FROM payments**************************************************************"
REALM="payments"
#Get Users matching filter
RESPONSE=$(curl -vk \
    -X GET \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users?username=${USERNAME})

echo ${RESPONSE} | jq .

#Find my user's id
MYUSER=$(echo ${RESPONSE} | jq -r --arg USERNAME "$USERNAME" '.[] | select(.username==$USERNAME)')
echo $MYUSER | jq .
MYUSERID=$(echo ${MYUSER} | jq -r ."id")
echo MYUSERID=$MYUSERID

#Get my client
RESPONSE=$(curl -vk \
    -X DELETE \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    ${KEYCLOAK}/auth/admin/realms/${REALM}/users/$MYUSERID)

echo "RESPONSE"=${RESPONSE}

echo ${RESPONSE} | jq .