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
