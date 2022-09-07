create table division(
    id serial primary key,
    name text not null
);
insert into division(name) values ('web division');
insert into division(name) values ('testing division');
insert into division(name) values ('production division');
insert into division(name) values ('accounting division');

create table employee(
    id serial primary key,
    name text not null,
    birth_date date not null,
    division_id int not null references division (id)
);

insert into employee(name, birth_date, division_id)
values ('Иванов Иван Иванович', '1985-01-05', 2);
insert into employee(name, birth_date, division_id)
values ('Сидоров Сидр Сидорович', '1990-12-15', 3);
insert into employee(name, birth_date, division_id)
values ('Петров Петр Петрович', '1989-06-09', 1);
insert into employee(name, birth_date, division_id)
values ('Иванова Илона Ивановна', '1985-04-30', 4);
insert into employee(name, birth_date, division_id)
values ('Самошникова Тамара Евгеньевна', '1979-11-05', 4);
insert into employee(name, birth_date, division_id)
values ('Сноу Джон', '1985-07-15', 1);
insert into employee(name, birth_date, division_id)
values ('Таргариен Марина Дмитриевна', '1988-02-23', 2);
insert into employee(name, birth_date, division_id)
values ('Тотошкин Валентин Евграфович', '1991-10-31', 3);
insert into employee(name, birth_date, division_id)
values ('Микрочипов Станислав Валерьянович', '1975-11-01', 2);
insert into employee(name, birth_date, division_id)
values ('Понтов Виталий Аркадьевич', '1991-05-03', 1);

create table meeting(
    id serial primary key,
    theme text not null,
    time date not null,
    division_org int references division (id),
    responsible int references employee (id)
);

insert into meeting(theme, time, division_org, responsible)
values ('Web technologies', '2022-08-31', 1, 3);
insert into meeting(theme, time, division_org, responsible)
values ('Data science', '2022-09-12', 3, 8);
insert into meeting(theme, time, division_org, responsible)
values ('Weekend party', '2022-09-16', 4, 5);
insert into meeting(theme, time, division_org, responsible)
values ('Consequences of the weekend party', '2022-09-19', 4, 4);
insert into meeting(theme, time, division_org, responsible)
values ('Company birthday', '2022-10-15', 3, 2);
insert into meeting(theme, time, division_org, responsible)
values ('New Year', '2022-12-30', 1, 6);

create table participant(
    meeting_id int references meeting (id),
    employee_id int references employee (id),
    primary key (meeting_id, employee_id)
);

insert into participant(meeting_id, employee_id) values (1, 3);
insert into participant(meeting_id, employee_id) values (1, 6);
insert into participant(meeting_id, employee_id) values (1, 10);
insert into participant(meeting_id, employee_id) values (1, 1);
insert into participant(meeting_id, employee_id) values (1, 7);
insert into participant(meeting_id, employee_id) values (1, 9);
insert into participant(meeting_id, employee_id) values (2, 2);
insert into participant(meeting_id, employee_id) values (2, 8);
insert into participant(meeting_id, employee_id) values (2, 10);
insert into participant(meeting_id, employee_id) values (3, 4);
insert into participant(meeting_id, employee_id) values (3, 5);
insert into participant(meeting_id, employee_id) values (4, 4);
insert into participant(meeting_id, employee_id) values (4, 5);
insert into participant(meeting_id, employee_id) values (5, 1);
insert into participant(meeting_id, employee_id) values (5, 2);
insert into participant(meeting_id, employee_id) values (5, 3);
insert into participant(meeting_id, employee_id) values (5, 4);
insert into participant(meeting_id, employee_id) values (5, 5);
insert into participant(meeting_id, employee_id) values (5, 6);
insert into participant(meeting_id, employee_id) values (5, 7);
insert into participant(meeting_id, employee_id) values (5, 8);
insert into participant(meeting_id, employee_id) values (5, 9);
insert into participant(meeting_id, employee_id) values (5, 10);
insert into participant(meeting_id, employee_id) values (6, 1);
insert into participant(meeting_id, employee_id) values (6, 2);
insert into participant(meeting_id, employee_id) values (6, 3);
insert into participant(meeting_id, employee_id) values (6, 4);
insert into participant(meeting_id, employee_id) values (6, 5);
insert into participant(meeting_id, employee_id) values (6, 6);
insert into participant(meeting_id, employee_id) values (6, 7);
insert into participant(meeting_id, employee_id) values (6, 8);
insert into participant(meeting_id, employee_id) values (6, 9);
insert into participant(meeting_id, employee_id) values (6, 10);