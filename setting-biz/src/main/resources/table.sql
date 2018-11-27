# 公共配置表

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `code` varchar(32) NOT NULL COMMENT 'key',
  `type` TINYINT(2) NOT NULL COMMENT '类型 1 String 2 Set 3 Map',
  `value` varchar(255) NOT NULL DEFAULT '' COMMENT '值',
  `content` varchar(255) NOT NULL COMMENT 'value的content(主要用于map结构)',
  `remark` varchar(32) NOT NULL DEFAULT '' COMMENT '描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_created_at` (`created_at`),
  KEY `ix_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公共配置表'

-- ————————————————————————————————————————————————————————————————