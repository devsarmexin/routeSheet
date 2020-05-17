-- create sequence hibernate_sequence start 1 increment 1;

DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS routesheet;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS usr;

create table addresses
(
    id              int8,
    departure_point varchar(2048),
    destination     varchar(2048),
    distance        int8,
    routesheet_id   int8,
    primary key (id)
);

create table routesheet
(
    id               int8,
    consumption_fact float8,
    consumption_norm float8,
    data             date,
    distance         int8,
    fuel_finish      float8,
    fuel_start       float8,
    fueling          int8,
    mileage_finish   int8,
    mileage_start    int8,
    number           int8,
    saving           float8,
    user_id          int8,
    primary key (id)
);

create table user_role
(
    user_id int8,
    roles   varchar(255)
);

create table usr
(
    id       int8,
    password varchar(255) not null,
    username varchar(255) not null,
    active boolean,
    primary key (id)
);

-- alter table if exists addresses
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