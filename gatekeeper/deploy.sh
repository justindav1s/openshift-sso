#!/usr/bin/env bash

oc login https://ocp.datr.eu:8443 -u justin

APP=gatekeeper
POJECT=$APP

oc delete project $PROJECT
oc new-project $PROJECT 2> /dev/null
while [ $? \> 0 ]; do
    sleep 1
    printf "."
oc new-project $PROJECT 2> /dev/null
done

oc project $PROJECT

oc delete imagestream ${APP}
oc delete buildconfig ${APP}-docker-build
oc delete deploymentconfig ${APP}
oc delete serviceaccounts ${APP}
oc delete service ${APP}
oc delete route ${APP}

oc new-app -f gatekeeper-template.yml \
    -p APPLICATION_NAME=${APP} \
    -p SOURCE_REPOSITORY_URL=https://github.com/justindav1s/openshift-sso.git \
    -p SOURCE_REPOSITORY_REF=master \
    -p DOCKERFILE_PATH="gatekeeper" \

