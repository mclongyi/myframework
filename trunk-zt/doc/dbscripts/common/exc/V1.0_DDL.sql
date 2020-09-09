use `search`;
CREATE TABLE IF NOT EXISTS `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `company_name` varchar(255) NOT NULL,
  `is_delete` tinyint(1) NOT NULL,
  `category_tree_id` mediumtext,
  `short_name` varchar(255) DEFAULT NULL,
  `virtual_company_id` bigint(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `saas_common_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `index_name` varchar(128) NOT NULL COMMENT '索引名称',
  `app_type` tinyint(1) DEFAULT NULL COMMENT '应用类型，0-普通搜索，1-电商通用搜索B2c, 2-电商通用搜索O2O',
  `status` tinyint(5) DEFAULT NULL COMMENT '索引注册状态信息：0-初始状态；3-注册未完成： 5-注册完成正常使用; 7-暂时禁用',
  `es_cluster_id` bigint(20) DEFAULT NULL COMMENT '关联的cluster集群配置id',
  `setting_id` bigint(20) DEFAULT NULL COMMENT '关联的setting配置id',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;


CREATE TABLE IF NOT EXISTS `saas_datasource_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(11) NOT NULL COMMENT '公司id',
  `db_type` varchar(20) NOT NULL COMMENT '数据库类别:merchant,osc,price,product,stock',
  `username` varchar(20) NOT NULL COMMENT '数据库用户名',
  `password` varchar(20) NOT NULL COMMENT '数据库密码',
  `jdbc_url` varchar(100) NOT NULL COMMENT 'jdbc连接信息',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

CREATE TABLE IF NOT EXISTS `saas_dump_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `index_name` varchar(128) NOT NULL COMMENT '索引名称',
  `job_type` tinyint(1) NOT NULL COMMENT 'dump任务类型:0-周期执行；1-定时执行',
  `period` bigint(20) DEFAULT NULL COMMENT '周期执行的时间间隔，单位分钟',
  `cron_expression` varchar(32) DEFAULT NULL COMMENT '定时执行的cron表达式',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

CREATE TABLE IF NOT EXISTS `saas_dump_sql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `sql_name` varchar(64) NOT NULL COMMENT 'sql名称',
  `db_source` varchar(32) NOT NULL COMMENT 'sql所对应的数据库信息分类',
  `sql_content` varchar(2000) NOT NULL COMMENT '具体的sql语句',
  `id_filter_field` varchar(32) DEFAULT NULL COMMENT '实时索引筛选id时指定的字段名称 id_filter_field in (id list)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sql_name` (`sql_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='索引端sql信息';



CREATE TABLE IF NOT EXISTS `saas_escluster_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(32) NOT NULL COMMENT 'es集群名称',
  `cluster_nodes` varchar(255) NOT NULL COMMENT 'es集群节点ip',
  `admin_url` varchar(255) NOT NULL COMMENT 'es集群admin链接',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

CREATE TABLE IF NOT EXISTS `company_sql_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `company_id` bigint(20) NOT NULL COMMENT '公司id',
  `sql_name` varchar(64) NOT NULL COMMENT 'sql名称',
  `sql_content` varchar(2000) NOT NULL COMMENT '具体的sql语句',
  `is_deleted` tinyint(4) NOT NULL,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sql_name` (`sql_name`,`company_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='搜索端sql信息';

CREATE TABLE IF NOT EXISTS `search_versions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `v_name` varchar(20) NOT NULL COMMENT '版本名称',
  `type` varchar(20) DEFAULT NULL COMMENT 'sql类型',
  `description` varchar(200) DEFAULT NULL COMMENT 'sql功能描述',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否已删除，用于回滚脚本',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` bigint(20) DEFAULT NULL COMMENT '通用SQL company_id=0，专用SQl填对应的company_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

