create table if not exists users
(
    id            bigserial primary key,
    username      varchar(72) not null unique,
    password_hash varchar(72) not null,
    created_at    timestamp   not null default current_timestamp
)