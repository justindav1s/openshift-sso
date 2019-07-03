FROM centos

RUN mkdir -p /app
WORKDIR /app
RUN wget https://downloads.jboss.org/keycloak/4.8.3.Final/gatekeeper/keycloak-gatekeeper-linux-amd64.tar.gz
RUN tar zxvf keycloak-gatekeeper-linux-amd64.tar.gz && ls -ltr

EXPOSE 8080
CMD ["/app/keycloak-gatekeeper"]