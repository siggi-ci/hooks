--
-- create tables for hookaccounts
--
create table hookusers(
	username text not null primary key,
	password text not null,
	enabled boolean not null
);

create table hookauthorities (
	username text not null,
	authority text not null,
	constraint fk_hookauthorities_users foreign key(username) references hookusers(username)
);

create unique index ix_hookauth_username on hookauthorities (username,authority);