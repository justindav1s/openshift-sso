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

echo ""

USER="amazin"

REFRESH_TOKEN=$(cat ${USER}.rt.txt)
ACCESS_TOKEN=$(cat ${USER}.at.txt)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
REALM="amazin"
CLIENT="web-login"
CLIENT_SECRET="44c56fa8-c2ec-4850-aed7-764561bb6b1b"

POST_BODY="client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&refresh_token=${REFRESH_TOKEN}"

curl -sv \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/logout?encodedRedirectUri=https%3A%2F%2Fwww.google.co.uk%2F