CREATE SCHEMA `videodb` DEFAULT CHARACTER SET utf8;

use videodb;

create table user(
    id bigint auto_increment primary key,
    user_name varchar(255) unique not null,
    password char(64) not null,
    created_date datetime not null,
    last_modified_date datetime not null
);

create table video(
	id bigint auto_increment,
    name varchar(255) not null,
    user_id bigint not null,
    url varchar(255) not null,
	created_date datetime not null,
    last_modified_date datetime not null,

    primary key (id),
    foreign key (user_id) REFERENCES user(id)
);

create table aws_credential(
    id bigint auto_increment,
    access_key_id varchar(255) not null,
    secret_access_key varchar(255) not null,

    primary key (id)
);