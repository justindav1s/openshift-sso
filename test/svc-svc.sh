#!/bin/bash

function padBase64  {
    STR=$1
    MOD=$((${#STR}%4))
    if [[ $MOD -eq 1 ]]; then
       STR="${STR}="
    elif [[ $MOD -eq 2 ]]; then
       STR="${STR}=="
    elif [[ $MOD -eq 3 ]]; then
       STR="${STR}=="
    fi
    echo ${STR}
}


KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
REALM="amazin"
GRANT_TYPE="client_credentials"
CLIENT="svc-svc"
CLIENT_SECRET="2b4107ed-96d2-45bd-aa51-87309d12afc8"
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}"

echo "Keycloak host : $KEYCLOAK"
echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -svk  \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

echo ""
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -d | jq .


./decodeToken.sh ${ACCESS_TOKEN}

curl -sv -H "Authorization: Bearer ${ACCESS_TOKEN}
" http://localhost:8081/products/all