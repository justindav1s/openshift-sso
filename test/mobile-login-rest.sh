#!/bin/bash

function padBase64  {
    STR=$1
    #echo STR=$STR
    MOD=$((${#STR}%4))
    #echo ${MOD}
    if [[ $MOD -eq 1 ]]; then
       STR="${STR}="
    elif [[ $MOD -eq 2 ]]; then
       STR="${STR}=="
    fi
    echo ${STR}
}


KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
REALM="amazin"
GRANT_TYPE="password"
CLIENT="mobile-login"
CLIENT_SECRET="8a8f5a19-cc57-4219-959d-ce698b9924e3"
USER="amazin"
USER_PASSWORD="password"
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&username=${USER}&password=${USER_PASSWORD}"

echo "Keycloak host : $KEYCLOAK"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -sv \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .


./decodeToken.sh ${ACCESS_TOKEN}

curl -sv -H "Authorization: Bearer ${ACCESS_TOKEN}
" http://localhost:8081/products/all