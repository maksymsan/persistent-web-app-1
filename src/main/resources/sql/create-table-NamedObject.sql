create table NamedObject (
primaryKey varchar2(50) not null constraint PK_NamedObject primary key,
name varchar2(50),
description varchar2(100),
updatedTimestamp timestamp
);