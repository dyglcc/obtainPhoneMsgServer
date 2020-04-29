
/*
**** 修改了哪些东西记录在这里
*/

/**
每添加调整规则，需要添加discout（比较固定，设置一次，以年付为基准，两年0.9,三年0.8,季度付款就是1.2,月付1.4,周付款1.6，天付款，2.0,设定也是看人去设定。）
，discout需要认为设定，divider需要统一都改，调整顺序。

id自增长
index 表示显示顺序，第一个未默认值。
 * divider 所有的数要除以divider，比如天，那么按每天是多少钱来计算。divider就是秒，divider名称就是每月，每年，每季度，每天，每周
 name pay_period的名称，显示为三年，两年，季度付，周付，月付，天付
 折扣就是自己算出来的。
 价格设定，人要设置好的，每一条价格要人为设置好，像名称一样
 price，价格决定了优惠的幅度，
 period_time使用时间，年365*24*3600 秒为单位，月，季度等
 created_at
 updated_at
 每一个付款周期需要有一个 sql时间可以执行的代码，例如3 YEAR 2 MONTH 1 DAY，
 UPDATE table SET date = DATE_ADD(date, INTERVAL 1 YEAR)，日前就被累加了一年
    alter table t_order add traffic BIGINT default '0';
 */

DROP TABLE IF EXISTS `t_pay_period`;
CREATE TABLE `t_pay_period` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `index`     INT           DEFAULT 0,
  `divider_second`     BIGINT           DEFAULT 86400,
  `name`      VARCHAR(32)               DEFAULT NULL,
  `price`    DECIMAL(20,2)          NOT NULL,
  `uuid`        VARCHAR(64)          DEFAULT NULL,
  `pay_type`     int           DEFAULT 1,
  `pay_period`     int           DEFAULT 2,
  `product_type`     int           DEFAULT 1,
  `status`     int           DEFAULT 1,
  `traffic`     BIGINT           DEFAULT 0,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;





/**
id,
user email
email varchar
name varchar
message varchar
subject varchar
updated_at
`created_at`
status 0未处理，1已经处理；
*/
DROP TABLE IF EXISTS `t_relation`;
CREATE TABLE `t_relation` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `group_id`           INT         DEFAULT 0,
  `user_phone`      VARCHAR(32)               DEFAULT NULL,
  `relate_phone`      VARCHAR(32)               DEFAULT NULL,
  `name`      VARCHAR(32)               DEFAULT NULL,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `t_subticket`;
CREATE TABLE `t_subticket` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `status`           INT         DEFAULT 0,
  `user`      VARCHAR(32)               DEFAULT NULL,
  `email`      VARCHAR(32)               DEFAULT NULL,
  `name`      VARCHAR(32)               DEFAULT NULL,
  `subject`      VARCHAR(250)               DEFAULT NULL,
  `message`    VARCHAR(500)          DEFAULT NULL,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- server_location 服务器名称 '香港2'
-- port 端口
-- add 地址 ip或者域名
-- ratio 流量倍率



DROP TABLE IF EXISTS `t_app_shares`;
CREATE TABLE `t_app_shares` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `icon_url`      VARCHAR(32)               DEFAULT NULL,
  `app_name`      VARCHAR(32)               DEFAULT NULL,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- main_account  sub_account_id  are  mobiles
DROP TABLE IF EXISTS `t_shares`;
CREATE TABLE `t_shares` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `main_account`      VARCHAR(32)               DEFAULT NULL,
  `app_id`      INT               DEFAULT NULL,
  `status`      INT               DEFAULT 1,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;





-- order表表-----------------
-- 账单id 自增id
-- parentid //续费会产生一条账单记录，没有parentid的将是新的记录。
-- email
-- {userid}
-- 账单金额千分值{amount}
-- 账单时间创建账单时间{create_at}
-- 付款周期 （pay_period）1.月付2。年付。3。两年付款4。三年付款
-- 产品类型 {product_type}1。个人2。ipv6，3。企业
-- 支付时间{charge_at}
-- 支付类型{pay_type} 1.支付宝2。比特币
-- 什么时候开始{start_at} 开始时间，没有parent的情况下是当前时间开始，有续费的话时间的开始时间,续费的话，父账单就失效了
-- 什么时候到期{end_at} 支付完成后开始计算 // 新账单是付费开始，加上月付的话加上一个月，年付，两年付和三年付款加相应的年份。/如果是续费在上一个过期时间上加
-- uuid客户端使用的id，{uuid}续费id不变，新帐号将会产生新的id
-- {status}状态（1未支付状态,2开通中3使用中和4用完了）
/*用户表*/
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `parent_id`    INT                  DEFAULT NULL,
  `email`      VARCHAR(32)               DEFAULT NULL,
  `amount`    DECIMAL(20,2)          NOT NULL,
  `uuid`        VARCHAR(64)          DEFAULT NULL,
  `pay_type`     int           DEFAULT 1,
  `pay_period`     int           DEFAULT 2,
  `product_type`     int           DEFAULT 1,
  `status`     int           DEFAULT 1,
  `traffic`     BIGINT           DEFAULT 0,
  `start_at`   DATETIME             DEFAULT NULL,
  `end_at`   DATETIME             DEFAULT NULL,
  `charge_at`   DATETIME             DEFAULT NULL,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*用户表*/
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `parent_id`    INT                  DEFAULT NULL,
  `firstname`    VARCHAR(32)          DEFAULT NULL,
  `lastname`     VARCHAR(32)          DEFAULT NULL,
  `mobile`       VARCHAR(16)          NOT NULL,
  `email`        VARCHAR(32)          DEFAULT NULL,
  `password`     VARCHAR(32)          DEFAULT NULL,
  `created_at`   DATETIME             DEFAULT NULL,
  `updated_at`   DATETIME             DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile` (`mobile`),
  FOREIGN KEY (parent_id) REFERENCES t_user (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- 产品类型表
-- ----------------------------
DROP TABLE IF EXISTS `pm_product`;
CREATE TABLE `pm_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `desc` varchar(200) NOT NULL,
  `p_name` varchar(30) NOT NULL,
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- 邮箱验证功能
-- ----------------------------
DROP TABLE IF EXISTS `pm_validate`;
CREATE TABLE `pm_validate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `email` varchar(40) NOT NULL,
  `reset_token` varchar(40) NOT NULL,
  `type` varchar(20) NOT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;