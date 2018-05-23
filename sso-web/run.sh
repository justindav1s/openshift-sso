#!/bin/bash

mvn spring-boot:run \
	-Djavax.net.ssl.trustStore=/Users/jusdavis/github/openshift-sso/bin/datr.eu.jks \
	-Djavax.net.ssl.trustStorePassword=password \
	-Djavax.net.debug=all
