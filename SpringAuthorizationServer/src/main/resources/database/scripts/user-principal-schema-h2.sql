create table UserPrincipal (
  	UserId BIGINT auto_increment not null PRIMARY KEY,
  	Username varchar2(36) not null,
  	HashedPassword nvarchar2(128) NOT null,
  	AccountNonExpired boolean not null,
  	AccountNonLocked boolean not null,
  	CredentialsNonExpired boolean not null,
  	Enabled boolean not null,
  	created_date timestamp NOT null,
  	created_by BIGINT default 0 NOT null,
  	updated_date timestamp,
  	updated_by BIGINT,
  	deleted_date timestamp,
  	constraint UC_UserPrincipal_Username unique (Username)
);

create table UserPrincipalAuthority (
  	UserId BIGINT not null,
  	Authority varchar2(100) not null,
  	constraint UC_UserPrincipalAuthority_User_Authority unique (UserId, Authority),
  	constraint FK_UserPrincipalAuthority_UserId foreign key (UserId) 
	references UserPrincipal (UserId) on delete cascade
);

