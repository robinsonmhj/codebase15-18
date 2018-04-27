drop table if exists logo;
create table logo(
	logo_id bigint primary key AUTO_INCREMENT,
	logo_url varchar(500)
) ENGINE=INNODB;

drop table if exists company;
create table company(
	company_id bigint primary key AUTO_INCREMENT,
	company_name varchar(200),
	company_ave varchar(200),
	company_city varchar(20),
	company_province varchar(20),
	company_county varchar(20),
	company_weichat varchar(50),
	company_email varchar(50),
	company_phone bigint not null,
	company_logo bigint,
	foreign key company_logo_fk (company_logo) references logo(logo_id)
) ENGINE=INNODB;

drop table if exists user;
create table user(
	user_name varchar(100) primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	avenue varchar(200) not null,
	province varchar(20) not null,
	city varchar(20) not null,
	county varchar(20) not null,
	weichat varchar(100) not null,
	qq bigint,
	email varchar(50),
	password char(32) not null,
	cellphone bigint not null,
	company_id bigint not null,
	foreign key user_fk (company_id) references company(company_id),
	unique key user_weichat_ux (weichat)
) ENGINE=INNODB;


drop table if exists link;
create table link(
	link_id smallint primary key AUTO_INCREMENT,
	link_name varchar(20),
	link_url varchar(100),
	parent_id smallint not null default -1
) ENGINE=INNODB;


drop table if exists privilege;
create table privilege(
	id integer primary key AUTO_INCREMENT,
	link_id smallint,
	user_name varchar(100),
	foreign key  privilege_link_fk (link_id) references link(link_id),
	foreign key privilege_user_fk (user_name) references user(user_name)
) ENGINE=INNODB;


#initial of the data
insert into logo (logo_url) values('/logo/multiplemedia.jpg');
insert into company (company_name,company_ave,company_city,company_province,company_county,company_weichat,company_email,company_phone,company_logo)
 values('multiplemedia','1 main ave','Scranton','PA','Lacawamma','weichat001','multiplemedia@gmail.com',5704140000,2);
insert into user (user_name,first_name,last_name,avenue,province,city,county,weichat,qq,email,password,cellphone,company_id)
 values('admin','Paul','Luffy','500 Rebecca Ave','PA','Stroudsburg','Morone','admin001',1000,'test@gmail.com','123456',8888018888,3);
	

#url

insert into link (link_url) values('/multipleMediaWeb/admin/index');
insert into link (link_url) values('/multipleMediaWeb/admin/validate');
insert into link (link_url) values('/multipleMediaWeb/admin/welcome');
insert into link (link_url) values('/multipleMediaWeb/admin/search/api/getSearchResult');

#privileges

insert into privilege (link_id,user_name) values (1,'admin'),(2,'admin'),(3,'admin'),(4,'admin');

	
	