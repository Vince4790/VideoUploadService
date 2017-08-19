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