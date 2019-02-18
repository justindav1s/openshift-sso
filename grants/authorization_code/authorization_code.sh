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
REALM="demo"
GRANT_TYPE="authorization_code"
CLIENT="yolt"
USER="test_user2"
USER_PASSWORD="123456"
REDIRECT_URI="yolt%3A%2F%2Fauthconsent"


#Get Code : performed by App
GET_BODY="scope=openid%20AIS_trans%20AIS_bal&response_type=code&client_id=${CLIENT}&redirect_uri=${REDIRECT_URI}"

RESPONSE=$(curl -vk -D headers.txt \
    -u ${USER}:${USER_PASSWORD} \
    -X GET \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/auth?${GET_BODY})

LOC=$(grep Location headers.txt)
rm -rf headers.txt
CODE=`echo ${LOC} | awk -F'[=&]' '{print $4}' | tr -cd "[:print:]\n"`

echo "CODE"=${CODE}
echo ${#CODE}



#Get Token : performed by Thirdparty
CLIENT=yolt
CLIENT_SECRET=edd2db87-bb96-4736-9b8d-5dcc9784a8be
POST_BODY="grant_type=${GRANT_TYPE}&redirect_uri=${REDIRECT_URI}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&code="
POST_BODY=${POST_BODY}${CODE}
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -vk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo "********RESPONSE**************"

#echo "RESPONSE"=${RESPONSE}
echo ${RESPONSE} | jq .

echo "*******ACCESS TOKEN***********"

ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

echo "********ID TOKEN**************"

#echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .id_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .