create table game (
	id varchar(64) not null default random_uuid(),
    player varchar(128) not null,
    word varchar(100) not null,
    max_failed_hits integer not null,
    guess_hits varchar(100),
    status varchar(100),
    registration_date TIMESTAMP WITH TIME ZONE not null default now(),
    primary key (id)
);
