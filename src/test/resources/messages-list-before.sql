delete from message;

insert into message(id, text, tag, user_id) values
  (1, 'first', 'my_tag', 1),
  (2, 'first', 'some', 1),
  (3, 'first', 'my_tag', 1),
  (4, 'first', 'another', 1);

alter sequence hibernate_sequence restart with 10;