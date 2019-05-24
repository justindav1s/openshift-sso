#!/usr/bin/env bash
set -x

TPP_APP_URL="http://127.0.0.1:9090/newclient"

CLIENT_ID=paypal
CLIENT_SECRET="changeme"
CLIENT_NAME="paypal"
CLIENT_REDIRECT_URI=http://127.0.0.1:9090/authcode


CLIENT_REALM=aisp
echo POST_BODY=${POST_BODY}
RESPONSE=$(curl -v \
    --data "{ \"name\": \"${CLIENT_NAME}\", \"callback_uri\": \"${CLIENT_REDIRECT_URI}\", \"client_id\": \"${CLIENT_ID}\", \"realm\": \"${CLIENT_REALM}\", \"client_secret\": \"${CLIENT_SECRET}\" }" \
    -H "Content-Type: application/json" \
    ${TPP_APP_URL})

echo "RESPONSE"=${RESPONSE}



CLIENT_REALM=pisp
echo POST_BODY=${POST_BODY}
RESPONSE=$(curl -v \
    --data "{ \"name\": \"${CLIENT_NAME}\", \"callback_uri\": \"${CLIENT_REDIRECT_URI}\", \"client_id\": \"${CLIENT_ID}\", \"realm\": \"${CLIENT_REALM}\", \"client_secret\": \"${CLIENT_SECRET}\" }" \
    -H "Content-Type: application/json" \
    ${TPP_APP_URL})

echo "RESPONSE"=${RESPONSE}