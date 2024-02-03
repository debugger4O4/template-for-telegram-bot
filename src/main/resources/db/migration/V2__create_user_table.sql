create table users (
    id serial primary key,
    chat_id integer,
    user_name varchar(400),
    first_name varchar(400),
    last_name varchar(400),
    registered_at timestamp default current_date,
    phone_number varchar(400),
    address varchar(400)
)