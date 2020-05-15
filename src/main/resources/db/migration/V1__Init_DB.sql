create sequence hibernate_sequence start 1 increment 1;

create table addresses
(
    id              int8          not null,
    departure_point varchar(2048) not null,
    destination     varchar(2048) not null,
    distance        int8          not null,
    routesheet_id   int8,
    primary key (id)
);

create table routesheet
(
    id               int8   not null,
    consumption_fact float8 not null,
    consumption_norm float8 not null,
    data             date   not null,
    distance         int8   not null,
    fuel_finish      float8 not null,
    fuel_start       float8 not null,
    fueling          int8   not null,
    mileage_finish   int8   not null,
    mileage_start    int8   not null,
    number           int8   not null,
    saving           float8 not null,
    user_id          int8   not null,
    primary key (id)
);

create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);

create table usr
(
    id       int8         not null,
    active   boolean      not null,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

alter table if exists addresses
    add constraint address_fk
        foreign key (routesheet_id) references routesheet;

alter table if exists routesheet
    add constraint message_user_fk
        foreign key (user_id) references usr;

alter table if exists user_role
    add constraint message_role_user_fk
        foreign key (user_id) references usr