/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.6.27-log : Database - resto
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`resto` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `resto`;

/*Table structure for table `brand` */

DROP TABLE IF EXISTS `brand`;

CREATE TABLE `brand` (
  `id` varchar(36) NOT NULL COMMENT '品牌信息',
  `brand_name` varchar(90) NOT NULL COMMENT '品牌名称\n',
  `brand_sign` varchar(45) NOT NULL COMMENT '二级域名的 Sign ，可以通过二级域名寻找到对应的brand 和对应的配置',
  `create_time` datetime NOT NULL,
  `wechat_config_id` varchar(36) NOT NULL,
  `database_config_id` varchar(36) NOT NULL,
  `brand_user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `brand_sign_UNIQUE` (`brand_sign`),
  KEY `fk_brand_wechat_config1_idx` (`wechat_config_id`),
  KEY `fk_brand_database_config1_idx` (`database_config_id`),
  KEY `fk_brand_brand_user1_idx` (`brand_user_id`),
  CONSTRAINT `fk_brand_brand_user1` FOREIGN KEY (`brand_user_id`) REFERENCES `brand_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_brand_database_config1` FOREIGN KEY (`database_config_id`) REFERENCES `database_config` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_brand_wechat_config1` FOREIGN KEY (`wechat_config_id`) REFERENCES `wechat_config` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `brand` */

insert  into `brand`(`id`,`brand_name`,`brand_sign`,`create_time`,`wechat_config_id`,`database_config_id`,`brand_user_id`) values ('2189424e-5edb-41b5-b35d-65d662583cd9','12312asdf','wewefwef','2016-03-17 17:29:52','','',''),('f25e7d5b-e7f9-490d-b70f-0c37488f2122','123123dddd','123123123123','2016-03-17 17:29:57','','','');

/*Table structure for table `brand_user` */

DROP TABLE IF EXISTS `brand_user`;

CREATE TABLE `brand_user` (
  `id` varchar(36) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `brand_user` */

/*Table structure for table `database_config` */

DROP TABLE IF EXISTS `database_config`;

CREATE TABLE `database_config` (
  `id` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '数据库名称\n',
  `url` varchar(200) NOT NULL COMMENT '数据库URL',
  `driver_class_name` varchar(50) NOT NULL DEFAULT 'com.mysql.jdbc.Driver' COMMENT '数据库驱动',
  `username` varchar(45) NOT NULL COMMENT '数据库用户名\n',
  `password` varchar(45) NOT NULL COMMENT '数据库密码\n',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `database_config` */

/*Table structure for table `distribution_mode` */

DROP TABLE IF EXISTS `distribution_mode`;

CREATE TABLE `distribution_mode` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `distribution_mode` */

/*Table structure for table `shop_detail` */

DROP TABLE IF EXISTS `shop_detail`;

CREATE TABLE `shop_detail` (
  `id` varchar(36) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '店铺名称\n',
  `address` varchar(45) DEFAULT NULL COMMENT '店铺地址\n',
  `longitude` varchar(45) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL COMMENT '店铺电话\n',
  `open_time` time DEFAULT NULL,
  `close_time` time DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `shop_mode` int(11) NOT NULL,
  `add_user` varchar(32) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间\n',
  `update_time` timestamp NULL DEFAULT NULL,
  `brand_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_shopdetails_brand1_idx` (`brand_id`),
  KEY `fk_shopdetails_shop_mode1_idx` (`shop_mode`),
  CONSTRAINT `fk_shopdetails_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_shopdetails_shop_mode1` FOREIGN KEY (`shop_mode`) REFERENCES `shop_mode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `shop_detail` */

/*Table structure for table `shop_mode` */

DROP TABLE IF EXISTS `shop_mode`;

CREATE TABLE `shop_mode` (
  `id` int(11) NOT NULL COMMENT '店铺经营模式ID\n',
  `name` varchar(45) NOT NULL COMMENT '模式名称',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `shop_mode` */

/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_menu` bit(1) NOT NULL,
  `menu_icon` varchar(255) DEFAULT NULL,
  `permission_name` varchar(255) NOT NULL,
  `permission_sign` varchar(255) NOT NULL,
  `sort` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `menu_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e2qmceo1cx7e0k4m2emgvqm3l` (`parent_id`),
  CONSTRAINT `FK_e2qmceo1cx7e0k4m2emgvqm3l` FOREIGN KEY (`parent_id`) REFERENCES `sys_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

/*Data for the table `sys_permission` */

insert  into `sys_permission`(`id`,`is_menu`,`menu_icon`,`permission_name`,`permission_sign`,`sort`,`parent_id`,`menu_type`) values (1,'','fa fa-yelp','权限管理','menu:permissionManager',999,NULL,NULL),(3,'','fa fa-users','角色管理','role/list',1,1,1),(4,'','fa fa-user','用户管理','user/list',2,1,1),(5,'\0',NULL,'添加','user/create',NULL,4,4),(6,'\0',NULL,'修改','user/modify',NULL,4,4),(7,'\0',NULL,'修改','role/edit',NULL,3,4),(8,'\0',NULL,'删除','role/delete',NULL,3,4),(9,'\0',NULL,'添加','role/add',NULL,3,4),(10,'\0',NULL,'分配权限','role/assign',NULL,3,4),(11,'','fa fa-lock','系统设置','/system/manager',1000,NULL,NULL),(12,'','fa fa-database','数据库监控','druid',1,11,2),(13,'\0',NULL,'删除用户','user/delete',NULL,4,4),(14,'\0',NULL,'分配角色','user/role',NULL,4,4),(15,'','fa-cart-plus','商家管理','brand:manager',1,NULL,1),(16,'','','商家列表','brand/list',1,15,1),(17,'\0',NULL,'brand:add','brand/add',NULL,16,4),(18,'\0',NULL,'brand:delete','brand/delete',NULL,16,4),(19,'\0',NULL,'brand:edit','brand/edit',NULL,16,4),(20,'','','商家用户','branduser/list',2,15,1),(21,'\0',NULL,'branduser:add','branduser/add',NULL,20,4),(22,'\0',NULL,'branduser:delete','branduser/delete',NULL,20,4),(23,'\0',NULL,'branduser:edit','branduser/edit',NULL,20,4),(24,'','','数据库配置','databaseconfig/list',10,15,1),(25,'\0',NULL,'databaseconfig:add','databaseconfig/add',NULL,24,4),(26,'\0',NULL,'databaseconfig:delete','databaseconfig/delete',NULL,24,4),(27,'\0',NULL,'databaseconfig:edit','databaseconfig/edit',NULL,24,4),(28,'','','店铺详情','shopdetail/list',3,15,1),(29,'\0',NULL,'shopdetail:add','shopdetail/add',NULL,28,4),(30,'\0',NULL,'shopdetail:delete','shopdetail/delete',NULL,28,4),(31,'\0',NULL,'shopdetail:edit','shopdetail/edit',NULL,28,4),(32,'','','店铺模式管理','shopmode/list',1,48,1),(33,'\0',NULL,'shopmode:add','shopmode/add',NULL,32,4),(34,'\0',NULL,'shopmode:delete','shopmode/delete',NULL,32,4),(35,'\0',NULL,'shopmode:edit','shopmode/edit',NULL,32,4),(36,'','','微信配置','wechatconfig/list',9,15,1),(37,'\0',NULL,'wechatconfig:add','wechatconfig/add',NULL,36,4),(38,'\0',NULL,'wechatconfig:delete','wechatconfig/delete',NULL,36,4),(39,'\0',NULL,'wechatconfig:edit','wechatconfig/edit',NULL,36,4),(44,'','','微信模板消息类型','wechattemptype/list',2,48,1),(45,'\0',NULL,'wechattemptype:add','wechattemptype/add',NULL,44,4),(46,'\0',NULL,'wechattemptype:delete','wechattemptype/delete',NULL,44,4),(47,'\0',NULL,'wechattemptype:edit','wechattemptype/edit',NULL,44,4),(48,'','fa fa-cogs','业务设置','business:manager',2,NULL,NULL),(50,'','','模板消息设置','wechattempid/list',4,15,1),(51,'\0',NULL,'wechattempid:add','wechattempid/add',NULL,50,4),(52,'\0',NULL,'wechattempid:delete','wechattempid/delete',NULL,50,4),(53,'\0',NULL,'wechattempid:edit','wechattempid/edit',NULL,50,4);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) NOT NULL,
  `role_sign` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9hu75f2xm03o8d7jglror7dcn` (`role_sign`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `sys_role` */

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `FK_q2ti5gwjeyma4xval3r4ino3e` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_permission` */

insert  into `sys_role_permission`(`role_id`,`permission_id`) values (2,3),(1,4),(1,5),(2,5),(1,6),(2,15),(2,16),(2,17),(2,18);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_51bvuyvihefoh4kp5syh2jpi4` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `sys_user` */

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_i648ekgdh9g18oc1m2irr0i9u` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values (1,1),(4,1),(16,5),(22,5),(15,6),(22,6);

/*Table structure for table `wechat_config` */

DROP TABLE IF EXISTS `wechat_config`;

CREATE TABLE `wechat_config` (
  `id` varchar(36) NOT NULL COMMENT '商家的微信配置表',
  `appid` varchar(100) NOT NULL,
  `appsecret` varchar(100) NOT NULL,
  `mchid` varchar(45) DEFAULT NULL,
  `mchkey` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `wechat_config` */

/*Table structure for table `wechat_temp_id` */

DROP TABLE IF EXISTS `wechat_temp_id`;

CREATE TABLE `wechat_temp_id` (
  `wechat_temp_id` varchar(45) NOT NULL COMMENT '微信模板的ID',
  `wechat_config_id` varchar(36) NOT NULL,
  `wechat_temp_type_id` bigint(20) NOT NULL,
  KEY `fk_r_wechat_temp_id_r_wechat_config1_idx` (`wechat_config_id`),
  KEY `fk_r_wechat_temp_id_r_wechat_temp_type1_idx` (`wechat_temp_type_id`),
  CONSTRAINT `fk_r_wechat_temp_id_r_wechat_config1` FOREIGN KEY (`wechat_config_id`) REFERENCES `wechat_config` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_r_wechat_temp_id_r_wechat_temp_type1` FOREIGN KEY (`wechat_temp_type_id`) REFERENCES `wechat_temp_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `wechat_temp_id` */

/*Table structure for table `wechat_temp_type` */

DROP TABLE IF EXISTS `wechat_temp_type`;

CREATE TABLE `wechat_temp_type` (
  `id` bigint(20) NOT NULL COMMENT '模板类型',
  `type_name` varchar(45) NOT NULL COMMENT '模板名称\n',
  `type_sign` varchar(45) NOT NULL COMMENT '模板识别 SIGN 唯一的\n',
  `type_json` text NOT NULL COMMENT '模板JSON字符串',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_sign_UNIQUE` (`type_sign`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `wechat_temp_type` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
