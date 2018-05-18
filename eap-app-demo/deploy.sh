#!/usr/bin/env bash

export IP=ocp.datr.eu
export USER=justin

export PROJECT=eap-app-demo

oc login https://${IP}:8443 -u $USER

oc delete project $PROJECT
oc new-project $PROJECT 2> /dev/null
while [ $? \> 0 ]; do
    sleep 1
    printf "."
    oc new-project $PROJECT 2> /dev/null
done

oc policy add-role-to-user view system:serviceaccount:${PROJECT}:default

oc secret new eap-ssl-secret eapkeystore.jks
oc secret new eap-jgroup-secret eapjgroups.jceks
oc secrets link default eap-ssl-secret eap-jgroup-secret
