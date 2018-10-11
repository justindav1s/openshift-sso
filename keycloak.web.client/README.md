# Install notes

## RHEL7 RPM install

   * subscription-manager repos --enable=jb-eap-7.1-for-rhel-7-server-rpms
   * subscription-manager repos --enable=rh-sso-7.2-for-rhel-7-server-rpms
   * yum groupinstall rh-sso7

RPM install get all patches to date.

## DB setup

yum install postgresql.x86_64 postgresql-jdbc.noarch postgresql-server.x86_64

As postgres

   * initdb -D /var/lib/pgsql/data

as root

   * systemctl status postgresql.service
   * systemctl enable postgresql.service
   * systemctl start postgresql.service
   
   
## RPM install file locations

### Config
   * /opt/rh/rh-sso7/root/usr/share/keycloak/standalone/configuration/
   * /etc/opt/rh/rh-sso7/keycloak/standalone/standalone-ha.xml

### Start script

   * cd /opt/rh/rh-sso7/root/usr/share/keycloak/bin
   * ./standalone.sh --server-config=standalone-ha.xml

### Modules 
/opt/rh/rh-sso7/root/usr/share/keycloak/modules

### Install JDBC module
$HOME/modules/system/layers/keycloak/postgresql/main

module.xml

```
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-9.4.1212.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```
also add jdbc driver jar file

### Configure Database connection and pool

declare and load driver

```
<subsystem xmlns="urn:jboss:domain:datasources:4.0">
...
    <datasources>
        <drivers>
            <driver name="postgresql" module="org.postgresql">
                <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
            </driver>
            <driver name="h2" module="com.h2database.h2">
                <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
            </driver>
        </drivers>
    </datasources>
...    
</subsystem>
```

configure datasource

```
<subsystem xmlns="urn:jboss:domain:datasources:4.0">
    <datasources>
    ...
        <datasource jndi-name="java:jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true" use-java-context="true">
            <connection-url>jdbc:postgresql://127.0.0.1:5432/rh_sso</connection-url>
            <driver>postgresql</driver>
            <pool>
                <max-pool-size>20</max-pool-size>
            </pool>
            <security>
                <user-name>sso</user-name>
                <password>sso</password>
            </security>
        </datasource>
    ...
    </datasources>
 </subsystem>
```

### Connect to database

psql -h 127.0.0.1 -p 5432 -d rh_sso -U sso

# Keycloak API integrations

## Endpoints for a realm named Demo

### Realm meta-data including public key
   * http://127.0.0.1:8080/auth/realms/demo

### Well Known Endpoint : data about all endpoints

   * https://sso.datr.eu:8443/auth/realms/psd2/.well-known/openid-configuration

### Authorization : The authorization endpoint performs authentication of the end-user   
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/auth
   
### Token Endpoint : get a new token  
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/token
   
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/token/introspect
   
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/userinfo
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/logout
   * http://127.0.0.1:8080/auth/realms/demo/protocol/openid-connect/certs
   * http://127.0.0.1:8080/auth/realms/demo/clients-registrations/openid-connect
   
### Login form for users   
   * http://127.0.0.1:8080/auth/realms/demo/account
   
### Client Registration
   * http://127.0.0.1:8080/auth/realms/demo/client-registrations/default   
   
JWT validation using openssl and perl

https://gist.github.com/rolandyoung/176dd310a6948e094be6   

Infinispan TCP multicasting

http://www.jgroups.org/manual/html/user-advanced.html

search for TCPPING


Turn on logging for success events

in : 

<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">

add a new spi

            <spi name="eventsListener">
                <default-provider>jboss-logging</default-provider>
                <provider name="jboss-logging" enabled="true">
                    <properties>
                        <property name="success-level" value="info"/>
                        <property name="error-level" value="warn"/>
                    </properties>
                </provider>
            </spi>
            ...
        </subsystem>

 