create sequence hibernate_sequence start 1 increment 1;

create table route
(
    id                INTEGER primary key,
    departure_point   VARCHAR(2048) not null,
    destination_point VARCHAR(2048) not null,
    distance          SMALLINT      not null,
    route_sheet_id    INTEGER
);

create table route_sheet
(
    id               INTEGER primary key,
    consumption_fact FLOAT    not null,
    consumption_norm FLOAT    not null,
    trip_date        date     not null,
    distance         INTEGER,
    fuel_start       FLOAT    not null,
    fuel_finish      FLOAT    not null,
    fueling          SMALLINT not null,
    mileage_start    INTEGER  not null,
    mileage_finish   INTEGER  not null,
    waybill_number   SMALLINT not null,
    saving           FLOAT    not null,
    user_id          INTEGER
);

create table user_role
(
    user_id INTEGER      not null,
    roles   VARCHAR(255) not null
);

create table usr
(
    id       INTEGER primary key,
    active   boolean      not null default true,
    username VARCHAR(255) not null,
    password VARCHAR(255) not null
);

alter table if exists route
    add constraint address_fk
        foreign key (route_sheet_id) references route_sheet;

alter table if exists route_sheet
    add constraint message_user_fk
        foreign key (user_id) references usr;

alter table if exists user_role
    add constraint message_role_user_fk
        foreign key (user_id) references usr;