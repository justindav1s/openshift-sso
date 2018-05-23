#!/bin/bash


KEYCLOAK=https://secure-sso.apps.ocp.datr.eu
KEYCLOAK_USER=amazin-angular
CLIENT_KEY=a4ed2f3b-9953-4b92-a966-5e0cf0cc9280
REALM=amazin
TIMEOUT=30

curl -v ${KEYCLOAK}/auth/realms/${REALM}/.well-known/openid-configuration | jq .

