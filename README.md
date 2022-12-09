There are three projects

1) SpringAuthorizationServer
2) SpringResourceServer
3) SpringOAuthClient

These are the same projects as of repository spring-authorization-server-password-grant-type-support with few changes.

1) It is using JPA instead of JDBC
2) All projects are now using Spring Boot 3 with base Java version 17
3) For Spring Boot 3 it is using Athorization server version 1.0.0
4) Instead of creating the Oauth2 authorization server client in code. Now a database script is using. From the database it loads the clients and then
   clients converted to Registered Clients.
5) Instead of one application.properties file. Properties files are now split. The split properties files are now importing in application.properties file

Note:
   When I was runnung the project then org.thymeleaf.extras:thymeleaf-extras-springsecurity6 was not downloading to my local gradle cache. I tried it several times. 
   delete the local cahche but the result was same. So I manually downloaded it and use it. In the build.gradle file of SpringAuthorizationServer and 
   SpringOAuthClient. You can check the comments.

The main classes are OAuth2ResourceOwnerPasswordAuthenticationConverter, OAuth2ResourceOwnerPasswordAuthenticationToken and OAuth2ResourceOwnerPasswordAuthenticationProvider. These classes are present in SpringAuthorizationServer in package oauth2.authentication.

In SpringOAuthClient project. Class WebClientConfig has the configuration for password grant type support (contextAttributesMapper). Changes can be done according to need.

All are eclipse based gradle projects. All the settings are in application.properties file for all three projects. Like contextpath, port etc. SpringAuthorizationServer is using H2 database.

These are the same projects offered by Spring. I just add the Password grant type in the AuthorizationServer and Client as Spring is not providing support for it becasue of OAuth2.1 draft.

This project is just showing how you can add custom grant type in the SpringAuthorizationServer. Like in my case I added password grant type support to use in my project. Changes can be made according to need in the code. Right now it is using version 1.0.0 which is the latest version. Things can be change in upcoming versions of Spring authorization server. So if you update the version in future and have some problem then ask on the Spring forum.

This project is just for demonstration purpose to add custom grant type.

All projects should be imported in eclipse fine.

By default SpringAuthorizationServer will run on port 9000 with context path /springauthserver
SpringResourceServer will run on port 8090 with context path /springresourceserver
SpringOAuthClient will run on port 8080 with context path /springauthserverclient
The database scripts for SpringAuthorizationServer are present in database/scripts folder. The database settings are defined in database.proeprties.

The Urls are also configure in application.proeprties file

After running all three projects. Open the url http://127.0.0.1:8080/springauthserverclient (This can be change if you change the properties file)
Already a user1 with password field is populated. Login with the user.
There will be three grant types. Client Credentials, Authorization Code and Password.
Click on password grant type and the result will come.
