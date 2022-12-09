create table oauth2_client (
    id bigint auto_increment not null PRIMARY KEY,
    client_id varchar(200) not null,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_name varchar(200) not null,
	client_secret varchar(200) not null,
	client_secret_expires_at timestamp DEFAULT NULL,
    authentication_method varchar(100) not null,
	authorization_grant_type varchar(100) not null,
	scopes varchar(1000) not null,
	redirect_uris varchar(1000),
	registered boolean NOT null default 0,
	created_date timestamp NOT null,
  	created_by BIGINT default 0 NOT null,
  	updated_date timestamp,
  	updated_by BIGINT,
  	deleted_date timestamp,
  	constraint UC_oauth2_client_id_client_id UNIQUE (id, client_id),
	constraint UC_oauth2_client_client_id UNIQUE (client_id)
);


create table oauth2_client_token_setting (
  	id bigint auto_increment not null primary key,
	client_id varchar(200) not null,
  	access_token_time integer not null,
	access_token_time_unit varchar(50) not null,
	refresh_token_time integer NOT NULL,
	refresh_token_time_unit varchar(50) not null,
	created_date timestamp NOT null,
  	created_by BIGINT default 0 NOT null,
  	updated_date timestamp,
  	updated_by BIGINT,
  	deleted_date timestamp,
  	constraint FK_oauth2_client_token_setting_client_id foreign key (client_id) 
	references oauth2_client (client_id) on delete cascade
);

create table oauth2_client_setting (
  	id bigint auto_increment not null primary key,
	client_id varchar(200) not null,
  	require_authorization_consent boolean null,
  	created_date timestamp NOT null,
  	created_by BIGINT default 0 NOT null,
  	updated_date timestamp,
  	updated_by BIGINT,
  	deleted_date timestamp,
  	constraint FK_oauth2_client_setting_client_id foreign key (client_id) 
	references oauth2_client (client_id) on delete cascade
);

