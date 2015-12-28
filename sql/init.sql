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


create table wzly_users_details(
	userid  varchar(200),
	name  varchar(200),
	sex  varchar(200),
	marry  varchar(200),
	age  varchar(200),
	edu  varchar(200),
	height  varchar(200),
	_sr  varchar(200),
	_xz  varchar(200),
	_sx  varchar(200),
	_szd  varchar(200),
	_jg  varchar(200),
	_nxdb  varchar(200),
	_xx  varchar(200),
	_mz  varchar(200),
	_ywzn  varchar(200),
	_gcqk  varchar(200),
	_zfqk  varchar(200),
	xgxm_gxms  varchar(200),
	xgxm_zp  varchar(200),
	xgxm_tz  varchar(200),
	xgxm_tx  varchar(200),
	xgxm_mlbw  varchar(200),
	xgxm_fx  varchar(200),
	xgxm_lx  varchar(200),
	gzxx_sr  varchar(200),
	gzxx_gzzk  varchar(200),
	gzxx_xl  varchar(200),
	gzxx_zy  varchar(200),
	gzxx_zhiy  varchar(200),
	yq_sex  varchar(200),
	yq_age  varchar(200),
	yq_photo  varchar(200),
	yq_height  varchar(200),
	yq_type  varchar(200),
	yq_marryhis  varchar(200),
	yq_edu  varchar(200),
	yq_cx  varchar(200),
	yq_dq  varchar(200),
	shms_  Text,
	xqah_ Text
);

create table wzly_users(
	userid varchar(200),
	name varchar(200),
	content varchar(200),
	head_img_url varchar(200),
	from_url varchar(200),
	status varchar(200)
);

