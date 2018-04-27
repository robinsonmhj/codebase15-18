//used to monitor queries

drop table if exists  monitor.query_details;
create table monitor.query_details(
  STATEMENT_UUID varchar(500) ,
  SESSION_ID INTEGER not null,
  HOSTNAME varchar(128) not null,
  USER_ID varchar(50) not null,
  CLIENT_BIND_ADDRESS varchar(64) not null,
  SESSION_STATUS varchar(50),
  SESSION_BEGIN_TIME TIMESTAMP,
  STATEMENT VARCHAR(4096),
  STATEMENT_ELAPSED_TIME BIGINT,
  primary key (STATEMENT_UUID)
)
PARTITION BY  column (STATEMENT_UUID)
REDUNDANCY 1;


drop table if exists  monitor.query_status;
create table monitor.query_status(
    id bigint GENERATED BY DEFAULT AS IDENTITY,
    STATEMENT_UUID varchar(500) not null,
    STATEMENT_STATUS VARCHAR(32) not null,
    insert_time TIMESTAMP default current_timestamp not null,
    FOREIGN KEY (STATEMENT_UUID) REFERENCES monitor.query_details (STATEMENT_UUID)	ON DELETE RESTRICT
)
PARTITION BY column (STATEMENT_UUID)
colocate with (monitor.query_details)
redundancy 1;

drop table if exists  monitor.query_killed_log;
create table monitor.query_killed_log(
  id bigint GENERATED BY DEFAULT AS IDENTITY,
  STATEMENT_UUID varchar(500) not null,
  insert_time TIMESTAMP default current_timestamp not null,
  FOREIGN KEY (STATEMENT_UUID) REFERENCES monitor.query_details (STATEMENT_UUID)	ON DELETE RESTRICT

)
PARTITION BY column (STATEMENT_UUID)
colocate with (monitor.query_details)
redundancy 1;






drop table if exists monitor."USER";
create table monitor."USER"(
   user_id bigint GENERATED BY DEFAULT AS IDENTITY,
   user_name varchar(50) not null,
   primary key (user_id)
);


drop table if exists monitor."GROUP";
create table monitor."GROUP"(
   group_id bigint GENERATED BY DEFAULT AS IDENTITY,
   group_name varchar(100) not null,
   access_type varchar(200) not null,
   primary key (group_id)
);



drop table if exists monitor.privilege;
create table monitor.privilege(
    user_id bigint not null,
	group_id bigint not null,
	schema_name varchar(20) not null,
	table_name	varchar(100) not null,
	FOREIGN KEY (user_id) REFERENCES monitor."USER" (user_id)	ON DELETE RESTRICT,
	FOREIGN KEY (group_id) REFERENCES monitor."GROUP" (group_id)	ON DELETE RESTRICT
)
PARTITION BY column (user_id)
redundancy 2;





create table sandbox.AB_Report(
	job_id bigint primary key,
	job_name varchar(100) not null,
	start_time timestamp not null,
	complete_time timestamp not null,
	failed boolean,
	message text,
	env varchar(5)
)
DISTRIBUTED BY (job_id);