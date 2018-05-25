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

Create a realm role called something like "role-web-login" 

This is used in the situation where a request for a particular webpage is secured and the user is sent to the Keycloak login page. in Keycloak/RHssp this called the "Standard flow", in OIDC terms it's called "Authorization Code flow"

To make this work : 
   * create the client
   * then turn off all switches except "Enabled" and "Standard flow"
   * Client Protocol : openid-connect
   * Access Type : 
      * confidential 
        * provide to webapp : keycloak.public-client=false, keycloak.resource=web-login, keycloak.credentials.secret=<your client secret>
      * public 
        * provide to webapp : keycloak.public-client=true, keycloak.resource=web-login      
   * Valid Redirect URIs : a URL that redirects to your app.
   * On the Scope tab turn Full Scope Allowed to ON.
   * Within your realm create a default group and assign the realm role "role-web-login" to it 
   
  

