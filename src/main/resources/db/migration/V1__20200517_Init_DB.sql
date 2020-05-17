create sequence hibernate_sequence start 1 increment 1;

create table route
(
    id                int8 primary key,
    departure_point   varchar(2048) not null,
    destination_point varchar(2048) not null,
    distance          int8          not null,
    routesheet_id     int8
);

create table routesheet
(
    id               int8 primary key,
    consumption_fact float8 not null,
    consumption_norm float8 not null,
    data             date   not null,
    distance         int8,
    fuel_finish      float8 not null,
    fuel_start       float8 not null,
    fueling          int8   not null,
    mileage_finish   int8   not null,
    mileage_start    int8   not null,
    number           int8   not null,
    saving           float8 not null,
    user_id          int8
);

create table user_role
(
    user_id int8         not null,
    roles   varchar(255) not null
);

create table usr
(
    id       int8 primary key,
    active   boolean      not null,
    password varchar(255) not null,
    username varchar(255) not null
);

-- alter table if exists route
--     add constraint address_fk
--         foreign key (routesheet_id) references routesheet;
--
-- alter table if exists routesheet
--     add constraint message_user_fk
--         foreign key (user_id) references usr;
--
-- alter table if exists user_role
--     add constraint message_role_user_fk
--         foreign key (user_id) references usr