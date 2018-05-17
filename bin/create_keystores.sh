#!/usr/bin/env bash

. ./env.sh

openssl req \
    -new \
    -newkey rsa:4096 \
    -x509 \
    -keyout xpaas.key \
    -out xpaas.crt \
    -days 365 \
    -subj "/CN=xpaas-sso-demo.ca"

keytool -genkeypair \
    -keyalg RSA \
    -keysize 2048 \
    -dname "CN=secure-${CN}" \
    -alias sso-https-key \
    -keystore sso-https.jks

keytool -certreq \
    -keyalg rsa \
    -alias sso-https-key \
    -keystore sso-https.jks \
    -file sso.csr

openssl x509 -req \
    -CA xpaas.crt \
    -CAkey xpaas.key \
    -in sso.csr \
    -out sso.crt \
    -days 365 -CAcreateserial

keytool -import \
    -file xpaas.crt \
    -alias xpaas.ca \
    -keystore sso-https.jks

keytool -import \
    -file sso.crt \
    -alias sso-https-key \
    -keystore sso-https.jks

keytool -import \
    -file xpaas.crt \
    -alias xpaas.ca \
    -keystore truststore.jks

keytool -import \
    -file xpaas.key \
    -keystore truststore.jks

keytool -genseckey \
    -alias jgroups \
    -storetype JCEKS \
    -keystore jgroups.jceks
