create table TextContext(
	recId int(10),
	fromUrl varchar(1000),
	title varchar(1000),
	titleUrl varchar(1000),
	context text,
	imgLocUrl varchar(1000),
	imgUrl varchar(1000),
	visitTimes int,
	status int,
	categories int,
	tags varchar(1000),
	gettedDt date,
	publishDt date);
--drop table TextContext;

create table contextType(
	id int,
	name varchar(200)
);