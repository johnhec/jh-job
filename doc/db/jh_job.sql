CREATE database if NOT EXISTS `jh_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `jh_job`;

CREATE TABLE `job_info` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
                            `cron` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
                            `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
                            `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
                            `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
                            `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
                            `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `job_group` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(64) NOT NULL COMMENT '执行器名称',
                             `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `job_group`(`id`, `name`, `address_list`) VALUES (1, 'xxx', 'http://127.0.0.1:8088');
INSERT INTO `job_info`(`id`, `job_group`, `cron`, `executor_handler`, `executor_param`) VALUES (1, 1, '0 0/1 * * * ?', 'test', NULL);

commit;

