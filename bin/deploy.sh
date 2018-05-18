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
oc secret new sso-ssl-secret datr.eu.jks
oc secret new sso-app-secret datr.eu.jks
oc secrets link default sso-jgroup-secret sso-ssl-secret sso-app-secret


oc new-app -f ../templates/sso72-ephem.yml \
    -p APPLICATION_NAME=${PROJECT} \
    -p HOSTNAME_HTTPS=secure-${CN} \
    -p HOSTNAME_HTTP=s${CN} \
    -p HTTPS_KEYSTORE=datr.eu.jks \
    -p HTTPS_PASSWORD=password \
    -p HTTPS_SECRET=sso-ssl-secret \
    -p JGROUPS_ENCRYPT_KEYSTORE=jgroups.jceks \
    -p JGROUPS_ENCRYPT_PASSWORD=password \
    -p JGROUPS_ENCRYPT_SECRET=sso-jgroup-secret \
    -p SSO_ADMIN_USERNAME=admin \
    -p SSO_ADMIN_PASSWORD=admin \
    -p SSO_TRUSTSTORE=datr.eu.jks \
    -p SSO_TRUSTSTORE_PASSWORD=password \
    -p SSO_REALM=amazin \
    -p SSO_SERVICE_USERNAME=amazin \
    -p SSO_SERVICE_PASSWORD=password


oc volume dc/sso --add --claim-size 512M --mount-path /opt/eap/standalone/configuration/standalone_xml_history --name standalone-xml-history