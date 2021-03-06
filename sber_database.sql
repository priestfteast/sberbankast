create table bonus (id bigint not null auto_increment, date date, description longtext, size bigint, operator_id bigint, primary key (id)) engine=InnoDB;
create table daily_stats (id bigint not null auto_increment, after_call_time_avrg bigint, date date, hold_time_avrg bigint, holded bigint, incoming bigint, incoming_avrg bigint, lost bigint, lost406 bigint, number varchar(255), outgoing_external bigint, outgoing_internal bigint, outgoing_total bigint, total_after_call_time bigint, total_external_out_going_time bigint, total_hold_time bigint, total_incoming_time bigint, total_not_ready_time bigint, total_out_going_time bigint, total_talking_time bigint, total_work_time bigint, operator_id bigint, primary key (id)) engine=InnoDB;
create table fine (id bigint not null auto_increment, date date, description longtext, size bigint, operator_id bigint, primary key (id)) engine=InnoDB;
create table notes (id bigint not null auto_increment, description longtext, operator_id bigint, primary key (id)) engine=InnoDB;
create table operator (id bigint not null auto_increment, additional_number varchar(255), card bit not null, company varchar(255), contract_type varchar(255), employement_date date, first_name varchar(255), image longblob, incoming bit not null, last_name varchar(255), number varchar(255), outgoing bit not null, shift varchar(255), stake bit not null, notes_id bigint, primary key (id)) engine=InnoDB;
create table operator_daily_stats (operator_id bigint not null, daily_stats_id bigint not null) engine=InnoDB;
create table operator_specialty (operator_id bigint not null, specialty_id bigint not null) engine=InnoDB;
create table outgoing (id bigint not null auto_increment, number varchar(255), primary key (id)) engine=InnoDB;
create table position (id bigint not null auto_increment, april varchar(255), august varchar(255), december varchar(255), february varchar(255), january varchar(255), july varchar(255), june varchar(255), march varchar(255), may varchar(255), november varchar(255), october varchar(255), september varchar(255), year varchar(255), primary key (id)) engine=InnoDB;
create table roles (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table specialty (id bigint not null auto_increment, description longtext, primary key (id)) engine=InnoDB;
create table user_role (user_id bigint not null, role_id bigint not null) engine=InnoDB;
create table users (id bigint not null auto_increment, login varchar(255), password varchar(255), primary key (id)) engine=InnoDB;
alter table operator_daily_stats add constraint UK_8w86jjj2feyut2uffmc6cv8je unique (daily_stats_id);
alter table bonus add constraint FK2hr6qyfw2aq095srjwte7n2rk foreign key (operator_id) references operator (id);
alter table daily_stats add constraint FKfq2qht0rbv10b14523bjq4vrw foreign key (operator_id) references operator (id);
alter table fine add constraint FK52furl6mgvrwc9won8momf3w9 foreign key (operator_id) references operator (id);
alter table notes add constraint FK3qy4ahwc0k7wshxgra26p6owp foreign key (operator_id) references operator (id);
alter table operator add constraint FKgkdbdjmsj5jchyueqp9jofdt0 foreign key (notes_id) references notes (id);
alter table operator_daily_stats add constraint FK2ucqhe28n4mivoxbh74geyide foreign key (daily_stats_id) references daily_stats (id);
alter table operator_daily_stats add constraint FKi89kqexdfr03qwe9l7ufwypbt foreign key (operator_id) references operator (id);
alter table operator_specialty add constraint FK4abqbee3hiltyo9ix586kvc7 foreign key (specialty_id) references specialty (id);
alter table operator_specialty add constraint FKlqyapb4vx2de5vjr20v9ynp1y foreign key (operator_id) references operator (id);
alter table user_role add constraint FKt7e7djp752sqn6w22i6ocqy6q foreign key (role_id) references roles (id);
alter table user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users (id);
