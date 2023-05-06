There are three projects

1) SpringAuthorizationServer
2) SpringResourceServer
3) SpringOAuthClient

These are the same projects as of repository spring-authorization-server-password-grant-type-support with few changes.

1) It is using JPA instead of JDBC
2) All projects are now using Spring Boot 3 with base Java version 17
3) For Spring Boot 3 it is using Authorization server version 1.0.2
4) Instead of creating the Oauth2 authorization server client in code. Now a database script is using. From the database it loads the clients and then
   clients converted to Registered Clients.
5) Instead of one application.properties file. Properties files are now split. The split properties files are now importing in application.properties file

The main classes are OAuth2ResourceOwnerPasswordAuthenticationConverter, OAuth2ResourceOwnerPasswordAuthenticationToken and OAuth2ResourceOwnerPasswordAuthenticationProvider. These classes are present in SpringAuthorizationServer in package oauth2.authentication.

In SpringOAuthClient project. Class WebClientConfig has the configuration for password grant type support (contextAttributesMapper). Changes can be done according to need.

All are eclipse based gradle projects. All the settings are in application.properties file for all three projects. Like contextpath, port etc. SpringAuthorizationServer is using H2 database.

These are the same projects offered by Spring. I just add the Password grant type in the AuthorizationServer and Client as Spring is not providing support for it becasue of OAuth2.1 draft.

This project is just showing how you can add custom grant type in the SpringAuthorizationServer. Like in my case I added password grant type support to use in my project. Changes can be made according to need in the code. Right now it is using version 1.0.0 which is the latest version. Things can be change in upcoming versions of Spring authorization server. So if you update the version in future and have some problem then ask on the Spring forum.

This project is just for demonstration purpose to add custom grant type.

All projects should be imported in eclipse fine.

1) By default SpringAuthorizationServer will run on port 9000 with context path /springauthserver
2) SpringResourceServer will run on port 8090 with context path /springresourceserver
3) SpringOAuthClient will run on port 8080 with context path /springauthserverclient
4) The database scripts for SpringAuthorizationServer are present in database/scripts folder. The database settings are defined in database.proeprties.

The Urls are also configure in application.proeprties file

1) After running all three projects. Open the url http://127.0.0.1:8080/springauthserverclient (This can be change if you change the properties file)
2) Already a user1 with password field is populated. Login with the user.
3) There will be three grant types. Client Credentials, Authorization Code and Password.
4) Click on password grant type and the result will come.
