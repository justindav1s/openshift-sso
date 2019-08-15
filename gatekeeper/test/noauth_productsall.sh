#!/bin/bash

URL=http://127.0.0.1:8081/api/products/all
URL=http://127.0.0.1:3000/api/products/all

curl -v -X GET \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    $URL

