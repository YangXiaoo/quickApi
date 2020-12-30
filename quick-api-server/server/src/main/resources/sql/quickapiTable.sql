drop table if exists `API_DATA`;
create table if not exists `API_DATA` (
    `DATA_API_ID` varchar(32) not null,
    `PROJECT_NAME` varchar(64) not null comment '项目名',
    `NAME` varchar(64) not null comment '菜单名',
    `GROUP` varchar(64) not null comment '所属组别名',
    `DESCRIPTION` varchar(256) not null comment '方法描述',
    `METHOD_NAME` varchar(64) not null comment '方法名',
    `CLASS_NAME` varchar(64) not null comment '方法所在类名',
    `REQUEST_TYPE` varchar(32) comment '请求类型',
    `CONTENT_TYPE` varchar(32) comment '响应类型',
    `URL` varchar(1000) not null comment '路由',
    `VERSION` varchar(32) comment '版本号',
    `AUTHOR` varchar(32) not null comment '作者',
    `CREATE_TIME` datetime default current_timestamp comment '创建时间',
    `UPDATE_TIME` datetime default current_timestamp comment '修改时间',
    `DOWNLOAD` char(2) not null comment '是否为下载方法,00不是, 01是',
    `TOKEN` char(2) not null comment '是否需要token,00不需要,01需要',
    `DELETE` char(2) not null default '00' comment '是否删除,00否,01删除',
    primary key (`DATA_API_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 文档信息
drop table if exists `API_DOC`;
CREATE TABLE IF not EXISTS `API_DOC` (
    `API_DOC_ID` varchar(32) not null,
    `PROJECT_NAME` varchar(64) not null comment '项目名',
    `URL` varchar(1000) not null comment '路由',
    `API_JSON_DATA` text not null comment '页面json数据',
    primary key (`API_DOC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 项目信息
drop table if exists `PROJECT_INFO`;
CREATE TABLE IF not EXISTS `PROJECT_INFO` (
    `PROJECT_INFO_ID` varchar(32) not null,
    `BASE_PACKAGES` varchar(256) not null comment '项目配置的扫描包',
    `PROJECT_NAME` varchar(256) not null comment '项目名',
    `DESCRIPTION` varchar(256) comment '项目描述',
    `SERVER_NAMES` varchar(500) not null comment '项目服务器名用,分开',
    `HOST_SERVICE_NAME` varchar(64) not null comment '数据管理服务器名',
    `LOCAL_SERVICE_NAME` varchar(64) not null comment '项目本地服务器名',
    `VERSION` varchar(32) comment '项目版本',
    `AUTHOR` varchar(256) comment '项目作者',
    `ENABLE` char(2) not null default '01' comment '默认开启,00不开启, 01开启',
    primary key (`PROJECT_INFO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;