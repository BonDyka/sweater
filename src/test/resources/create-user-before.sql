delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
  (1, true, '$2a$08$LnXAAzTbkrzv/1yKPmpyw.bgKqlg42493J3xorMBamxNL0wD9xUKO', 'admin'),
  (2, true, '$2a$08$LnXAAzTbkrzv/1yKPmpyw.bgKqlg42493J3xorMBamxNL0wD9xUKO', 'mike');

insert into user_role(user_id, roles) VALUES
  (1, 'USER'), (1, 'ADMIN'),
  (2, 'USER');