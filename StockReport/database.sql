create database finance;





drop table if exists finance.company;
create table finance.company(
	id bigint NOT NULL AUTO_INCREMENT
	symbol varchar(5) not null,
	company_name varchar(50),
	address1 varchar(100),
	address2 varchar(20),
	county varchar(20),
	phone varchar(20),
	website varchar(20),
	sector varchar(20),
	industry varchar(20),
	primary key (id)
	);
	
LOAD DATA INFILE 'C:\work\company.sql' INTO TABLE finance.company
  FIELDS TERMINATED BY '||'  LINES STARTING BY '' TERMINATED BY ';' ;