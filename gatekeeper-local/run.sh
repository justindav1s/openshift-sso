#!/usr/bin/env bash

set -x

FILE=keycloak-gatekeeper

if [ ! -f "$FILE" ]; then
    wget https://downloads.jboss.org/keycloak/4.8.3.Final/gatekeeper/keycloak-gatekeeper-darwin-amd64.tar.gz
    tar zxvf keycloak-gatekeeper-darwin-amd64.tar.gz
fi

./keycloak-gatekeeper --encryption-key=aaaaaaaaaaaaaaaa \
                      --config gatekeeper.yaml \
                      --verbose

