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
GRANT_TYPE="refresh_token"
CLIENT="yolt"

REFRESH_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDSnB6OTYwN1JyTURxOTRibGRySjZ4eWJvSEduU1hfa01Jd2tGVjlzVzZzIn0.eyJqdGkiOiIwMDA4YWFhMi1jNTc2LTQ5MWItOTI1MC01NThlZjc3MWNhZTQiLCJleHAiOjE1NTQzNzI2NDAsIm5iZiI6MCwiaWF0IjoxNTU0MzcyMDQwLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwODAvYXV0aC9yZWFsbXMvZGVtbyIsImF1ZCI6InlvbHQiLCJzdWIiOiJiMGU4OTBlYS05MTQyLTRlZWUtOTE5Ni1kMzY3NThhZTE5ODEiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoieW9sdCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6Ijc5M2VlNjkxLWI4MjItNDU5MS05ODg1LTllYzcwNWY1ZTk3MyIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJBSVNfdHJhbnMiLCJBSVNfYmFsIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9fQ.N4iR2zMaF6h5AzSlhIkQqRLE9jplI_Ib9WPfc0XMkpV1dr6deDTcSdp2UErWl2UvrAy60O7JhxaLKMxNxeUT68imydb3Ee4JOw_cc66Y12bWmli5ApahF2kdy84HII13J5dNmNFqB4z1puXAafv_mNPOOc8C-VMNyQwBV3lXxvhFMHGVe410ahEyk3jT3jI83ZgQTTDQyfsChS4qfQFSN-rR3AMqR7b_krRdxtRnNLbXvwpJpvpgk5sbee7IKWD11b0e68Fv_7NslUz3iEDzgF2gawjD4iEc1pB8Jcq9klGYxsyDTT5Z05SKj_hZP4UhbGHYnzA-O0mz2gATPhH1ng
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&refresh_token=${REFRESH_TOKEN}"

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

echo "*******REFRESH TOKEN***********"

ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .refresh_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

echo "********ID TOKEN**************"

#echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .id_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .