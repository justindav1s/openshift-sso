#!/usr/bin/env bash

oc login https://ocp.datr.eu:8443 -u justin


APP=sso
PROJECT=$APP

oc delete project $PROJECT
oc new-project $PROJECT 2> /dev/null
while [ $? \> 0 ]; do
    sleep 1
    printf "."
oc new-project $PROJECT 2> /dev/null
done

oc project $PROJECT

oc policy add-role-to-user view system:serviceaccount:${PROJECT}:default

oc create secret generic sso-jgroup-secret --from-file=certs/jgroups.jceks
oc create secret generic sso-ssl-secret --from-file=certs/datr.eu.jks
oc create secret generic sso-app-secret --from-file=certs/datr.eu.jks
oc secrets link default sso-jgroup-secret sso-ssl-secret sso-app-secret

oc new-app --template=sso73-https \
 -p HTTPS_SECRET="sso-ssl-secret" \
 -p HTTPS_KEYSTORE="datr.eu.jks" \
 -p HTTPS_NAME="sso" \
 -p HTTPS_PASSWORD="changeme" \
 -p JGROUPS_ENCRYPT_SECRET="sso-jgroup-secret" \
 -p JGROUPS_ENCRYPT_KEYSTORE="jgroups.jceks" \
 -p JGROUPS_ENCRYPT_NAME="jgroups" \
 -p JGROUPS_ENCRYPT_PASSWORD="changeme" \
 -p SSO_ADMIN_USERNAME="admin" \
 -p SSO_ADMIN_PASSWORD="admin" \
 -p SSO_REALM="demorealm" \
 -p SSO_TRUSTSTORE="datr.eu.jks" \
 -p SSO_TRUSTSTORE_PASSWORD="changeme" \
 -p SSO_TRUSTSTORE_SECRET="sso-app-secret"