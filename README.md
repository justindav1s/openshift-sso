# Using Keycloak (RedHat SSO)

## Deploying Keycloak/RHsso

Use scripts in deploy_keycloak folder  to set up java keystores and to deploy a keycloak pod to Openshift

## Setting to secure an application

1. Setup your own realm
2. Setup two clients within this realm, say :
   * a login client, lets call it "web-login"
   * a REST resource client, lets call it "app-rest"

You'll need two clients because each client requires different configuration. 

### Setting up with "web-login" client

This is used in the situation where a request for a particular webpage is secured and the user is sent to the Keycloak login page. in Keycloak/RHssp this called the "Standard flow", in OIDC terms it's called "Authorization Code flow"

To make this work : 
   * create a realm role called something like "role-web-login"
   * within your realm create a default group and assign the realm role "role-web-login" to it  
   * create the client
   * in the client, turn off all switches except "Enabled" and "Standard flow"
       * Client Protocol : openid-connect
       * Access Type : 
          * confidential 
            * provide to webapp : keycloak.public-client=false, keycloak.resource=web-login, keycloak.credentials.secret=<your client secret>
          * public 
            * provide to webapp : keycloak.public-client=true, keycloak.resource=web-login      
       * Valid Redirect URIs : a URL that redirects to your app.
       * On the Scope tab turn Full Scope Allowed to ON.
   * create a user and make sure they are in the default group
   * add the following dependecies to your pom file
    
```
    <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.keycloak.bom</groupId>
                <artifactId>keycloak-adapter-bom</artifactId>
                <version>3.4.3.Final</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>        

```       

   * add the following to your spring-boot application.properties
```

logging.level.org.springframework.security: trace
logging.level.org.keycloak: trace
keycloak.auth-server-url=https://<your keycloak host>/auth
keycloak.realm=amazin
keycloak.resource=web-login
keycloak.credentials.secret=<your client secret>
keycloak.public-client=false
keycloak.principal-attribute=preferred_username
keycloak.security-constraints[0].authRoles[0]=role-web-login
keycloak.security-constraints[0].securityCollections[0].patterns[0]=/greeting
keycloak.security-constraints[0].securityCollections[0].patterns[1]=/products

```       
   
  

