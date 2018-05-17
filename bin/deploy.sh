#!/usr/bin/env bash

. ./env.sh

oc login https://${IP}:8443 -u $USER

oc delete project $PROJECT
oc new-project $PROJECT 2> /dev/null
while [ $? \> 0 ]; do
    sleep 1
    printf "."
    oc new-project $PROJECT 2> /dev/null
done

oc policy add-role-to-user view system:serviceaccount:${PROJECT}:default

oc secret new sso-jgroup-secret jgroups.jceks
oc secret new sso-ssl-secret sso-https.jks truststore.jks
oc secrets link default sso-jgroup-secret sso-ssl-secret

oc new-app -f ../templates/sso72-ephem.yml \
    -p APPLICATION_NAME=${PROJECT} \
    -p HOSTNAME_HTTPS=secure-${CN} \
    -p HOSTNAME_HTTP=s${CN} \
    -p HTTPS_KEYSTORE=sso-https.jks \
    -p HTTPS_PASSWORD=password \
    -p HTTPS_SECRET=sso-ssl-secret \
    -p JGROUPS_ENCRYPT_KEYSTORE=jgroups.jceks \
    -p JGROUPS_ENCRYPT_PASSWORD=password \
    -p JGROUPS_ENCRYPT_SECRET=sso-jgroup-secret \