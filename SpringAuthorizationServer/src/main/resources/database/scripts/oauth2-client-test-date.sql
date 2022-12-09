------------------------------------------------------- oauth2-authorization-code-client -----------------------------

insert into oauth2_client (client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at, authentication_method, authorization_grant_type, scopes, redirect_uris, registered, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'authorization-code-client-id', CURRENT_TIMESTAMP, 'authorization-code-client-name', '{noop}secret1', null, 'client_secret_post', 'authorization_code,refresh_token', 'message.read,message.write', 'http://127.0.0.1:8080/springauthserverclient/authorized', 0, 
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
    
insert into oauth2_client_setting (client_id, require_authorization_consent, created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'authorization-code-client-id', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
  
insert into oauth2_client_token_setting (client_id, access_token_time, access_token_time_unit, refresh_token_time, refresh_token_time_unit, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'authorization-code-client-id', 1, 'day', 4, 'day', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
  
  
------------------------------------------------------ Client-Credentials-client -------------------------------------

insert into oauth2_client (client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at, authentication_method, authorization_grant_type, scopes, redirect_uris, registered, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'client-credentials-client-id', CURRENT_TIMESTAMP, 'client-credentials-client-name', '{noop}secret2', null, 'client_secret_basic', 'client_credentials,refresh_token', 'message.read,message.write', null, 0, 
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
    

insert into oauth2_client_token_setting (client_id, access_token_time, access_token_time_unit, refresh_token_time, refresh_token_time_unit, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'client-credentials-client-id', 1, 'hour', 4, 'hour', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
  
------------------------------------------------------ Password-client -------------------------------------

insert into oauth2_client (client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at, authentication_method, authorization_grant_type, scopes, redirect_uris, registered, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'password-client-id', CURRENT_TIMESTAMP, 'password-client-name', '{noop}secret3', null, 'client_secret_post', 'password,refresh_token', 'message.read,message.write', null, 0, 
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);
    

insert into oauth2_client_token_setting (client_id, access_token_time, access_token_time_unit, refresh_token_time, refresh_token_time_unit, 
    created_date, created_by, updated_date, updated_by, deleted_date)
  VALUES ( -- password
  'password-client-id', 30, 'minute', 1, 'day', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);