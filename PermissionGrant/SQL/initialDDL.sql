
drop table if exists monitor."USERS";
create table monitor."USERS"(
   user_id bigint GENERATED BY DEFAULT AS IDENTITY,
   user_name varchar(50) not null,
   primary key (user_id)
)
PARTITION BY column (user_id)
redundancy 2
EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT'DISK_STORE' ASYNCHRONOUS;


drop table if exists monitor."GROUPS";
create table monitor."GROUPS"(
   group_id bigint GENERATED BY DEFAULT AS IDENTITY,
   group_name varchar(100) not null,
   privilege varchar(200) not null,
   primary key (group_id)
)
EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT'DISK_STORE' ASYNCHRONOUS;



drop table if exists monitor.privilege;
create table monitor.privilege(
    user_id bigint not null CONSTRAINT user_id_fk REFERENCES monitor."USERS" ON DELETE RESTRICT,
    group_id bigint not null CONSTRAINT group_id_fk REFERENCES monitor."GROUPS"	ON DELETE RESTRICT,
    schema_name varchar(20) not null,
    table_name varchar(100) not null,
    primary key (user_id,group_id)
)
PARTITION BY column (user_id)
redundancy 2
EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT'DISK_STORE' ASYNCHRONOUS;


insert into monitor."GROUPS" (group_name,privilege) values('DEV_GEMFIRE_DBA_SG','select,update,insert,delete,trigger,alter,references');
insert into monitor."GROUPS" (group_name,privilege) values('DEV_GEMFIRE_Deployment_SG','select,update,insert,delete,trigger,alter,references');
insert into monitor."GROUPS" (group_name,privilege) values('DEV_GEMFIRE_ReadOnly_SG','select');
insert into monitor."GROUPS" (group_name,privilege) values('DEV_GEMFIRE_RW_SG','select,update,insert,delete');
insert into monitor."GROUPS" (group_name,privilege) values('DEV_GEMFIRE_RWE_SG','select,update,insert,delete,trigger,alter,references');