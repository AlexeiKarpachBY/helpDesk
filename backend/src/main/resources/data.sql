insert into users (email, password, first_name, last_name, role) values ('user1_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Naruto', 'Uzumaki', 'EMPLOYEE');
insert into users (email, password, first_name, last_name, role) values ('user2_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Sasuke', 'Uchiha', 'EMPLOYEE');

insert into users (email, password, first_name, last_name, role) values ('manager1_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Bill', 'Gates', 'MANAGER');
insert into users (email, password, first_name, last_name, role) values ('manager2_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Steve', 'Jobs', 'MANAGER');

insert into users (email, password, first_name, last_name, role) values ('engineer1_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Ivan', 'Kulibin', 'ENGINEER');
insert into users (email, password, first_name, last_name, role) values ('engineer2_mogilev@yopmail.com', '$2a$10$Lu2V401Upjbv.pT5l8pc7esxNbzbfyUWs2Gfof.W3qZ9x2H35qjui', 'Sergey', 'Korolev', 'ENGINEER');

insert into category (id, name ) values ('1', 'Application & Services');
insert into category (id, name ) values ('2', 'Benefits & Paper Work');
insert into category (id, name ) values ('3', 'Hardware & Software');
insert into category (id, name ) values ('4', 'People Management');
insert into category (id, name ) values ('5', 'Security & Access');
insert into category (id, name ) values ('6', 'Workplaces & Facilities');

insert into tickets (name, state, urgency, approver_id, assignee_id, owner_id, category_id, desired_resolution_date, description, created_on ) values ('FIRST_Ticket', 'DRAFT', 'CRITICAL', null, null, '1', '3', '2021-12-31', 'cosmic amazing', '2021-12-01');
insert into tickets (name, state, urgency, approver_id, assignee_id, owner_id, category_id, desired_resolution_date, description, created_on ) values ('SECOND_Ticket', 'DRAFT', 'CRITICAL', null, null, '1', '3', '2021-12-31', 'cosmic amazing', '2021-12-01');
insert into tickets (name, state, urgency, approver_id, assignee_id, owner_id, category_id, desired_resolution_date, description, created_on ) values ('THIRD_Ticket', 'DRAFT', 'CRITICAL', null, null, '1', '3', '2021-12-31', 'cosmic amazing', '2021-12-01');
insert into tickets (name, state, urgency, approver_id, assignee_id, owner_id, category_id, desired_resolution_date, description, created_on ) values ('FOURTH_Ticket', 'DRAFT', 'CRITICAL', null, null, '1', '3', '2021-12-31', 'cosmic amazing', '2021-12-01');

