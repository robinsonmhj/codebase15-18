
drop table if exists odm.gf_trace;
create table odm.gf_trace(
	host varchar(50),
	executeTS timestamp,
	action varchar(20),
	threadId bigint,
	tId bigint,
	sql text
)

distributed by (threadId,tId);