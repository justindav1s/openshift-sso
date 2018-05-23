#!/usr/bin/env bash

. ../../env.sh

set -x

OCP=https://ocp.datr.eu:8443
USER=justin
PROJECT=shibboleth
IMAGE=shibboleth-idp:latest
REGISTRY_HOST=docker-registry-default.apps.ocp.datr.eu:443

oc login ${OCP} -u ${USER}

#docker run -it -v $(pwd):/ext-mount --rm unicon/shibboleth-idp init-idp.sh

oc project ${PROJECT}

docker build -t $IMAGE .

docker tag $IMAGE $REGISTRY_HOST/${PROJECT}/$IMAGE

TOKEN=`oc whoami -t`

docker login -p $TOKEN -u justin $REGISTRY_HOST

sleep 5

docker push $REGISTRY_HOST/${PROJECT}/$IMAGE
