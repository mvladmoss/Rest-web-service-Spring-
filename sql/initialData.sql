insert into gift_certificate (id, name, description, price, date_of_creation, date_of_modification, duration_in_days)
values (2, 'Certificate2', 'First description', 45.0, '2019-03-15 03:00:00', '2019-03-16', 6);

insert into gift_certificate (id, name, description, price, date_of_creation, date_of_modification, duration_in_days)
values (3, 'Certificate3', 'Second description', 1456.433, '2019-03-11', '2019-03-15', 7);

insert into gift_certificate (id, name, description, price, date_of_creation, date_of_modification, duration_in_days)
values (4, 'Certificate4', 'Third description', 4574.42, '2019-02-04', '2019-02-09', 8);

insert into tag (id, name) values (2, 'Tag2');
insert into tag (id, name) values (3, 'Tag3');
insert into tag (id, name) values (4, 'Tag4');

insert into tag_gift_certificate (tag_id, gift_certificate_id) values (2, 2);
insert into tag_gift_certificate (tag_id, gift_certificate_id) values (3, 3);
insert into tag_gift_certificate (tag_id, gift_certificate_id) values (4, 4);