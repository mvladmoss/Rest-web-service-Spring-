create table tag
(
  id   bigserial not null
    primary key,
  name varchar(30)
);

create table gift_certificate
(
  id                   bigserial   not null primary key,
  name                 varchar(30) not null,
  description          varchar(500),
  price                numeric(10, 4),
  date_of_creation     TIMESTAMP with TIME ZONE,
  date_of_modification TIMESTAMP with TIME ZONE,
  duration_in_days     integer
);

create table tag_gift_certificate
(
  gift_certificate_id bigserial not null references gift_certificate (id),
  tag_id              bigserial not null references tag (id),
  PRIMARY KEY (gift_certificate_id, tag_id)
);

create function find_certificate_by_name_and_description(nam varchar(30), descrip varchar(500))
  returns SETOF gift_certificate
language sql
as $$
SELECT
  id,
  name,
  description,
  price,
  date_of_creation,
  date_of_modification,
  duration_in_days
from gift_certificate
where name LIKE nam AND description LIKE descrip;
$$;

create function find_certificate_by_description(descrip varchar(600))
  returns SETOF gift_certificate
language sql
as $$
SELECT
  id,
  name,
  description,
  price,
  date_of_creation,
  date_of_modification,
  duration_in_days
from gift_certificate
where description LIKE descrip;
$$;

create function find_certificate_by_name(nam varchar(30))
  returns SETOF gift_certificate
language sql
as $$
SELECT
  id,
  name,
  description,
  price,
  date_of_creation,
  date_of_modification,
  duration_in_days
from gift_certificate
where name like nam;
$$;

