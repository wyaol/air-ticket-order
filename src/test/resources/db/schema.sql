create table t_order
(
    id                      bigint not null auto_increment,
    user_id                 bigint,
    flight_order_id         varchar(200),
    luggage_weight_order_id varchar(200),
    insurance_order_id      varchar(200),
    amount                  DECIMAL,
    create_time             bigint,
    update_time             bigint,
    primary key (id)
);

create table t_invoice
(
    id              bigint not null auto_increment,
    flight_order_id varchar(200),
    bank            varchar(200),
    bank_account    varchar(200),
    email           varchar(200),
    address         varchar(200),
    number          varchar(200),
    status          varchar(200),
    update_time     bigint,
    primary key (id)
);
