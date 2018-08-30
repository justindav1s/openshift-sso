#!/bin/bash

KEYCLOAK=http://127.0.0.1:8080
REALM="test"
GRANT_TYPE="authorization_code"
CLIENT="web-login"
CLIENT_SECRET="2dde44a5-2c3f-4461-93a8-1faf9754b7f0"
USER="justin"
USER_PASSWORD="123456"

#Get Code
GET_BODY="scope=openid&response_type=code&client_id=${CLIENT}&redirect_uri=http://127.0.0.1:9090/getcode"

RESPONSE=$(curl -sk -D headers.txt \
    -u ${USER}:${USER_PASSWORD} \
    -X GET \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/auth?${GET_BODY})

LOC=$(grep Location headers.txt)
rm -rf headers.txt
CODE=`echo ${LOC} | awk -F'[=&]' '{print $4}' | tr -cd "[:print:]\n"`

echo ${CODE}





