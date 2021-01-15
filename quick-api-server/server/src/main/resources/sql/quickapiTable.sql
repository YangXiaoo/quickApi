create schema quickapi collate utf8_general_ci;

create table api_doc
(
	API_DOC_ID varchar(32) not null
		primary key,
	PROJECT_NAME varchar(64) not null comment '项目名',
	METHOD_URL varchar(1000) not null comment '路由',
	API_JSON_DATA text not null comment '页面json数据',
	USER_NAME varchar(64) null comment '创建者',
	USER_ID varchar(32) null comment '创建者ID',
	CREATE_TIME datetime null,
	UPDATE_TIME datetime null comment '更新时间'
);

create table method_model
(
	DATA_API_ID varchar(32) not null
		primary key,
	PROJECT_NAME varchar(64) not null comment '项目名',
	NAME varchar(64) not null comment '菜单名',
	METHOD_GROUP varchar(64) not null comment '所属组别名',
	METHOD_DESCRIPTION varchar(256) not null comment '方法描述',
	METHOD_NAME varchar(64) not null comment '方法名',
	CLASS_NAME varchar(64) not null comment '方法所在类名',
	REQUEST_TYPE varchar(32) null comment '请求类型',
	CONTENT_TYPE varchar(32) null comment '响应类型',
	URL varchar(1000) not null comment '路由',
	VERSION varchar(32) null comment '版本号',
	AUTHOR varchar(32) null comment '作者',
	CREATE_TIME datetime default CURRENT_TIMESTAMP null comment '创建时间',
	UPDATE_TIME datetime default CURRENT_TIMESTAMP null comment '修改时间',
	DOWNLOAD varchar(10) null comment '是否为下载方法,00不是, 01是',
	TOKEN varchar(10) not null comment '是否需要token,00不需要,01需要',
	DELETE_FLAG char(2) default '00' not null comment '是否删除,00否,01删除'
);

create table project_info
(
	PROJECT_INFO_ID varchar(32) not null
		primary key,
	BASE_PACKAGES varchar(256) not null comment '项目配置的扫描包',
	PROJECT_NAME varchar(256) not null comment '项目名',
	DESCRIPTION varchar(256) null comment '项目描述',
	SERVER_NAMES varchar(500) not null comment '项目服务器名用,分开',
	HOST_SERVICE_NAME varchar(64) not null comment '数据管理服务器名',
	LOCAL_SERVICE_NAME varchar(64) not null comment '项目本地服务器名',
	VERSION varchar(32) null comment '项目版本',
	AUTHOR varchar(256) null comment '项目作者',
	ENABLE varchar(10) default 'Yes' not null comment '默认开启,00不开启, 01开启',
	CREATE_TIME datetime null comment '创建时间'
);

create table user_api
(
	USER_API_ID varchar(32) not null
		primary key,
	METHOD_ID varchar(32) null comment '方法ID',
	METHOD_URL varchar(1000) null,
	USER_ID varchar(32) null comment '用户ID',
	USER_NAME varchar(64) null comment '用户姓名',
	CREATE_TIME datetime default CURRENT_TIMESTAMP null,
	API_JSON_DATA text null comment '接口页面信息',
	DELETE_FLAG varchar(2) null,
	UPDATE_TIME datetime default CURRENT_TIMESTAMP null
)
comment '我的接口';

create table user_method
(
	USER_METHOD_ID varchar(32) not null
		primary key,
	URL varchar(1000) null,
	METHOD_NAME varchar(64) null,
	METHOD_GROUP varchar(128) null,
	USER_NAME varchar(32) null,
	USER_ID varchar(32) null,
	CREATE_TIME datetime default CURRENT_TIMESTAMP null,
	UPDATE_TIME datetime default CURRENT_TIMESTAMP null,
	DELETE_FLAG varchar(4) null
)
comment '用户接口方法';

create table user_project_info
(
	USER_PROJECT_INFO_ID varchar(32) not null
		primary key,
	PROJECT_INFO_ID varchar(32) null,
	PROJECT_NAME varchar(64) null,
	PROJECT_DESC varchar(256) null,
	USER_NAME varchar(64) null,
	CREATE_TIME datetime default CURRENT_TIMESTAMP null,
	UPDATE_TIME datetime default CURRENT_TIMESTAMP null,
	DELETE_FLAG varchar(4) null,
	USER_ID varchar(32) null
)
comment '用户拥有的项目信息';

