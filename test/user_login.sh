#!/bin/bash

set

POST_BODY="{ \"username\": \"justin\", \"password\": \"123456\" }"

echo ${POST_BODY}

curl -sv \
    http://localhost:8090/user/login \
    -X POST \
    -d "${POST_BODY}" \
    -H "Content-Type: application/json"

