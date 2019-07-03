FROM golang:1.10.3

RUN mkdir -p /go/src/github.com/gatekeeper
RUN cd /go/src/github.com/gatekeeper/ && \
    git clone https://github.com/keycloak/keycloak-gatekeeper.git

WORKDIR /go/src/github.com/gatekeeper/keycloak-gatekeeper
RUN git checkout tags/6.0.1

RUN make && make test

FROM scratch
WORKDIR /
COPY --from=0 bin/keycloak-gatekeeper /keycloak-gatekeeper
EXPOSE 8080
CMD ["/keycloak-gatekeeper"]