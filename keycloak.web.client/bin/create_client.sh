#!/usr/bin/env bash
set -x

TPP_APP_URL="http://127.0.0.1:9090/newclient2"

CLIENT_ID=newclient5
CLIENT_SECRET="change me"
CLIENT_NAME="My_New_Client"
CLIENT_REDIRECT_URI=http://127.0.0.1:9090/authcode


echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -v \
    --data "{ \"name\": \"${CLIENT_NAME}\", \"callback_uri\": \"${CLIENT_REDIRECT_URI}\", \"client_id\": \"${CLIENT_ID}\", \"client_secret\": \"${CLIENT_SECRET}\" }" \
    -H "Content-Type: application/json" \
    ${TPP_APP_URL})

echo "RESPONSE"=${RESPONSE}
