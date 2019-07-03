FROM centos

RUN yum update -y
RUN yum install -y wget
RUN mkdir -p /app
WORKDIR /app
RUN wget https://downloads.jboss.org/keycloak/4.8.3.Final/gatekeeper/keycloak-gatekeeper-linux-amd64.tar.gz
RUN tar zxvf keycloak-gatekeeper-linux-amd64.tar.gz && ls -ltr

COPY gatekeeper.yaml /app

EXPOSE 3000
CMD ["/app/keycloak-gatekeeper --config /app/gatekeeper.yaml"]