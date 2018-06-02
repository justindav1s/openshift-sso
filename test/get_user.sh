#!/bin/bash

set

POST_BODY="{ \"username\": \"justin\" }"

echo ${POST_BODY}

curl -sv \
    http://localhost:8090/user/get \
    -X POST \
    -d "${POST_BODY}" \
    -H "Content-Type: application/json"

