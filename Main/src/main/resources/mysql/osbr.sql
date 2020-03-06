/*
SQLyog ‰ºÅ‰∏öÁâà - MySQL GUI v8.14 
MySQL - 8.0.17 : Database - osbr
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`osbr` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `osbr`;

/*Table structure for table `function` */

DROP TABLE IF EXISTS `function`;

CREATE TABLE `function` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fun_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `fun_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `function` */

insert  into `function`(`id`,`fun_id`,`fun_name`) values (1,'/teacher/**','ËÄÅÂ∏àÊé•Âè£ÁÆ°ÁêÜ'),(2,'/student/**','Â≠¶ÁîüÊé•Âè£'),(3,'/roleMenu/**','Âà§Êñ≠ÂΩìÂâçÁî®Êà∑ÊòØÂê¶ÊúâËÆøÈóÆÂΩìÂâçËèúÂçïÁöÑÊùÉÈôê'),(4,'/**','ÊâÄÊúâÊé•Âè£'),(12,'/test/cecs/**','ÊµãËØïÊé•Âè£11'),(16,'/asdas/asdas','ÊµãËØïÊé•Âè£5');

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `menu_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ËèúÂçïid,Áà∂Ê†áÁ≠æ1-È°∫Â∫è1-Ê†áËÆ∞1 & uuid ;Ê†áËÆ∞ 1 ÊòØÁΩëÈ°µÔºå0 ÊòØ Á™óÂè£',
  `menu_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ËèúÂçïÂêç',
  `menu_url` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ËèúÂçïË∑ØÂæÑ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `menu` */

insert  into `menu`(`id`,`menu_id`,`menu_name`,`menu_url`) values (1,'00-01-1&7a883c43-8c33-4b84-8c57-396a2c4429fa','‰∏ÄÁ∫ßËèúÂçï','www'),(2,'00.01-01-0&a1f420e4-e6b5-4c69-b331-73oq9x8237si','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçï','222'),(3,'00.01-02-1&a1f420e4-e6b5-4c69-b331-73dfc14c125b','‰∏ÄÁ∫ßËèúÂçï‰∫åËèúÂçï','www'),(4,'00-02-0&6a508110-9ee7-4301-833f-450956cfcc5a','‰∫åÁ∫ßËèúÂçï','333'),(5,'00.02-01-0&b1f3031d-9b58-405c-ac65-0664a0bc1509','‰∫åÁ∫ßËèúÂçï‰∏ÄËèúÂçï','111'),(6,'00.02-02-0&581988e3-3d26-40f7-8ad3-4424033dfca0','‰∫åÁ∫ßËèúÂçï‰∫åËèúÂçï','555'),(7,'00.02-03-1&37caaf33-28b8-45bf-924b-52384f5181d7','‰∫åÁ∫ßËèúÂçï‰∏âËèúÂçï','www'),(8,'00-03-1&21fa5eda-1b14-408d-ba32-f7019982b4ad','‰∏âÁ∫ßËèúÂçï','www'),(9,'00.03-01-1&630a690c-2234-4d08-8f84-6f1eac7374b2','‰∏âÁ∫ßËèúÂçï‰∏ÄËèúÂçï','www'),(10,'00.03-02-1&c79d094b-0941-49b8-881b-76e02e2e6f3f','‰∏âÁ∫ßËèúÂçï‰∫åËèúÂçï','www'),(11,'00.03-03-0&e8d5d6f9-7581-40bd-b9d0-05991c574733','‰∏âÁ∫ßËèúÂçï‰∏âËèúÂçï','111'),(12,'00.03-04-1&89d1bf64-43b0-4c9a-a606-7e4d50a0aae6','‰∏âÁ∫ßËèúÂçïÂõõËèúÂçï','www'),(13,'all&00-01-1&a01a4281-ffac-4d34-b9df-fb8b09ef9b81','‰∏ÄÁ∫ßËèúÂçïÊâÄÊúâ','nil'),(14,'all&00-02-0&a2f25e56-50f4-4423-b38b-fd8992f8af5a','‰∫åÁ∫ßËèúÂçïÊâÄÊúâ','nil'),(15,'all&00-03-1&29149a98-811a-4e6b-ac3e-e3436bd9dbb3','‰∏âÁ∫ßËèúÂçïÊâÄÊúâ','nil'),(16,'all&00-00-0&0cb13df6-c7c7-48e0-8229-7b7d4cbf6615','ÊâÄÊúâËèúÂçï','nil'),(17,'00.01.01-01-0&f7e2a23d-998b-4f5b-9b82-29c8e6f64812','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçï‰∏ÄËèúÂçï','222'),(18,'00.01.01-02-0&f7e2oqma-998b-4f5b-9b82-29c8e6f21123','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçï‰∫åËèúÂçï','222'),(19,'all&00.01-01-0&a01a4281-ffac-4d34-b9df-fb0x2oqlxckse','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçïÊâÄÊúâ','nil'),(20,'00.01.01.02-01-0&f7e2a23d-998b-4f5b-9b82-29xiqox929xk','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçï‰∫åËèúÂçï‰∏ÄËèúÂçï','222'),(21,'00.01.01.02-02-1&f7e2a23d-998b-4f5b-9b82-2apslwkc9283','‰∏ÄÁ∫ßËèúÂçï‰∏ÄËèúÂçï‰∫åËèúÂçï‰∫åËèúÂçï','www'),(50,'00-04-0&ff32b9fde86c4ca68d4d2985ec49db11','ÂõõÁ∫ßËèúÂçï','asd'),(52,'00.04-01-1&12ff63924e8b496cabd3adbc38f5edc6','ÂõõÁ∫ßËèúÂçï‰∏ÄËèúÂçï','/asd/asd'),(53,'00.04-02-0&174d79fd41194a30a0a72dc235c59b79','ÂõõÁ∫ßËèúÂçï‰∫åËèúÂçï','asda'),(54,'all&00-04-0&ac9263046a974140b014d060eeca354e','ÂõõÁ∫ßËèúÂçïÊâÄÊúâ','nil'),(55,'00.04.02-01-1&06091b535341401b9bd6026e2fa7d9e3','ÂõõÁ∫ßËèúÂçï‰∫åËèúÂçï‰∏ÄËèúÂçï','/asd/asd'),(56,'all&00.04-02-0&d23274e200f24ff98f680aaa298ebf2b','ÂõõÁ∫ßËèúÂçï‰∫åËèúÂçïÊâÄÊúâ','nil');

/*Table structure for table `name` */

DROP TABLE IF EXISTS `name`;

CREATE TABLE `name` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `name` */

insert  into `name`(`id`,`name`) values (36,'ÊµãËØï2');

/*Table structure for table `oauth_access_token` */

DROP TABLE IF EXISTS `oauth_access_token`;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(255) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(255) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_access_token` */

insert  into `oauth_access_token`(`token_id`,`token`,`authentication_id`,`user_name`,`client_id`,`authentication`,`refresh_token`) values ('273ea4bce9d3fc95902cf415a015cbc7','¨Ì\0sr\03com.kkIntegration.common.entity.MyOAuth2AccessToken≤û6$˙Œ\0L\0tokenEntityt\0;Lcom/kkIntegration/common/entity/MyOauth2AccessTokenEntity;xpsr\09com.kkIntegration.common.entity.MyOauth2AccessTokenEntity¶«GS«KFÁ\0Z\0\risAutoRefreshL\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xp\0sr\0java.util.Collections$EmptyMapY6ÖZ‹Á–\0\0xpsr\0java.util.DatehjÅKYt\0\0xpw\0\0o©YÅ^xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/ﬂGcù–…∑\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens·\ncT‘^\0L\0valueq\0~\0xpt\0$dbe92b44-5ac0-4bdc-a382-bc02afedafe6sq\0~\0w\0\0p>‰Wxsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxt\0MyBearerTypet\0$597aa828-1fee-4785-a6d8-b4305c40dbd1','6af28a856b4f481efd18fd5673d0fa7b','18316613106','admin','¨Ì\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2AuthenticationΩ@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationToken”™(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList¸%1µÏé\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0cq\0~\0xpsr\0java.util.ArrayListxÅ“ô«aù\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0osbr_studentsq\0~\0\rt\0osbr_teacherxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>£qiΩ\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0adminsr\0%java.util.Collections$UnmodifiableMapÒ•®˛tıB\0L\0mq\0~\0xpsr\0java.util.HashMap⁄¡√`—\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\018316613106xsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xq\0~\0	sr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsq\0~\0?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0q\0~\0xq\0~\0/sr\0java.util.LinkedHashMap4¿N\\l¿˚\0Z\0accessOrderxq\0~\0?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0 q\0~\0!x\0psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0\"sr\0java.util.TreeSet›òPìïÌá[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0q\0~\0xpq\0~\0!','df1097db02929a1c3058d2300b373438'),('7099f9e209d700fda6526553a7da3615','¨Ì\0sr\03com.kkIntegration.common.entity.MyOAuth2AccessToken≤û6$˙Œ\0L\0tokenEntityt\0;Lcom/kkIntegration/common/entity/MyOauth2AccessTokenEntity;xpsr\09com.kkIntegration.common.entity.MyOauth2AccessTokenEntity¶«GS«KFÁ\0Z\0\risAutoRefreshL\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xp\0sr\0java.util.Collections$EmptyMapY6ÖZ‹Á–\0\0xpsr\0java.util.DatehjÅKYt\0\0xpw\0\0oßOvExsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/ﬂGcù–…∑\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens·\ncT‘^\0L\0valueq\0~\0xpt\0$71494ca7-048a-47e8-8e43-f153515d010fsq\0~\0w\0\0p>\0ñbxsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxt\0MyBearerTypet\0$899de0ed-4308-4df2-a067-6acda9e870d9','f3227e36627289deceaeb4264c93a976','ÁÆ°ÁêÜÂëò','admin','¨Ì\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2AuthenticationΩ@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationToken”™(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList¸%1µÏé\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0cq\0~\0xpsr\0java.util.ArrayListxÅ“ô«aù\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0osbr_adminerxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>£qiΩ\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0adminsr\0%java.util.Collections$UnmodifiableMapÒ•®˛tıB\0L\0mq\0~\0xpsr\0java.util.HashMap⁄¡√`—\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0	ÁÆ°ÁêÜÂëòxsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xq\0~\0	sr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\0-sr\0java.util.LinkedHashMap4¿N\\l¿˚\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSet›òPìïÌá[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0xpq\0~\0','a1e9fa09829429c4fa39af8bc962eae0');

/*Table structure for table `oauth_refresh_token` */

DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(255) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_refresh_token` */

insert  into `oauth_refresh_token`(`token_id`,`token`,`authentication`) values ('a1e9fa09829429c4fa39af8bc962eae0','¨Ì\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/ﬂGcù–…∑\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens·\ncT‘^\0L\0valuet\0Ljava/lang/String;xpt\0$71494ca7-048a-47e8-8e43-f153515d010fsr\0java.util.DatehjÅKYt\0\0xpw\0\0p>\0ñbx','¨Ì\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2AuthenticationΩ@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationToken”™(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList¸%1µÏé\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0cq\0~\0xpsr\0java.util.ArrayListxÅ“ô«aù\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0osbr_adminerxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>£qiΩ\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0adminsr\0%java.util.Collections$UnmodifiableMapÒ•®˛tıB\0L\0mq\0~\0xpsr\0java.util.HashMap⁄¡√`—\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0	ÁÆ°ÁêÜÂëòxsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xq\0~\0	sr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\0-sr\0java.util.LinkedHashMap4¿N\\l¿˚\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSet›òPìïÌá[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0xpq\0~\0'),('df1097db02929a1c3058d2300b373438','¨Ì\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/ﬂGcù–…∑\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens·\ncT‘^\0L\0valuet\0Ljava/lang/String;xpt\0$dbe92b44-5ac0-4bdc-a382-bc02afedafe6sr\0java.util.DatehjÅKYt\0\0xpw\0\0p>‰Wx','¨Ì\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2AuthenticationΩ@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationToken”™(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList¸%1µÏé\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0ÄÀ^˜\0L\0cq\0~\0xpsr\0java.util.ArrayListxÅ“ô«aù\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0osbr_studentsq\0~\0\rt\0osbr_teacherxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>£qiΩ\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0adminsr\0%java.util.Collections$UnmodifiableMapÒ•®˛tıB\0L\0mq\0~\0xpsr\0java.util.HashMap⁄¡√`—\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\018316613106xsr\0%java.util.Collections$UnmodifiableSetÄí—èõÄU\0\0xq\0~\0	sr\0java.util.LinkedHashSetÿl◊Zï›*\0\0xr\0java.util.HashSet∫DÖïñ∏∑4\0\0xpw\0\0\0?@\0\0\0\0\0t\0allxsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsq\0~\0?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsq\0~\0%w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0q\0~\0xq\0~\0/sr\0java.util.LinkedHashMap4¿N\\l¿˚\0Z\0accessOrderxq\0~\0?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0 q\0~\0!x\0psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0\"sr\0java.util.TreeSet›òPìïÌá[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0q\0~\0xpq\0~\0!');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `role_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `role` */

insert  into `role`(`id`,`role_id`,`role_name`) values (1,'osbr_student','Â≠¶Áîü'),(2,'osbr_teacher','ËÄÅÂ∏à'),(3,'osbr_adminer','ÁÆ°ÁêÜÂëò'),(19,'ROLE_ANONYMOUS','Ê∏∏ÂÆ¢'),(20,'osbr_JieKouGuanLi','Êé•Âè£ÁÆ°ÁêÜ'),(22,'osbr_CeShiJieKou','ÊµãËØïÊé•Âè£');

/*Table structure for table `role_fun` */

DROP TABLE IF EXISTS `role_fun`;

CREATE TABLE `role_fun` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `fun_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `role_fun` */

insert  into `role_fun`(`id`,`role_id`,`fun_id`) values (1,'osbr_teacher','/teacher/**'),(2,'osbr_student','/student/**'),(4,'osbr_adminer','/**'),(6,'ROLE_ANONYMOUS','/roleMenu/**'),(7,'osbr_teacher','/student/**'),(11,'osbr_CeShiJieKou',''),(12,'osbr_CeShiJieKou','/test/cecs/**'),(13,'osbr_CeShiJieKou','/asdas/asdas');

/*Table structure for table `role_menu` */

DROP TABLE IF EXISTS `role_menu`;

CREATE TABLE `role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `menu_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `role_menu` */

insert  into `role_menu`(`id`,`role_id`,`menu_id`) values (1,'osbr_student','all&00.01-01-0&a01a4281-ffac-4d34-b9df-fb0x2oqlxckse'),(3,'osbr_teacher','all&00-02-0&a2f25e56-50f4-4423-b38b-fd8992f8af5a'),(5,'osbr_adminer','all&00-00-0&0cb13df6-c7c7-48e0-8229-7b7d4cbf6615');

/*Table structure for table `role_user` */

DROP TABLE IF EXISTS `role_user`;

CREATE TABLE `role_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `role_user` */

insert  into `role_user`(`id`,`role_id`,`user_id`) values (1,'osbr_teacher','user_1'),(2,'osbr_student','user_2'),(3,'osbr_adminer','adminer'),(7,'osbr_student','user_1');

/*Table structure for table `t_table` */

DROP TABLE IF EXISTS `t_table`;

CREATE TABLE `t_table` (
  `t_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `t_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `t_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'student,user',
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `t_table` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `user_pwd` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`user_id`,`user_name`,`user_pwd`) values (1,'user_1','18316613106','$2a$10$AAPdYAzRCeWPYjuGyhtbROLhb9Nm6wY1Kw.tLLZ8BJLltBzcrTE4u'),(2,'user_2','ËµµÂõõ','$2a$10$AAPdYAzRCeWPYjuGyhtbROLhb9Nm6wY1Kw.tLLZ8BJLltBzcrTE4u'),(3,'adminer','ÁÆ°ÁêÜÂëò','$2a$10$sqv/VZyo8F1ln4weAd3nUe/6uIqOERm.eShXnnDVuIKTNANmudunG'),(65,'a2f25e56-50f4-4423-b38b-fd8992aokxlw','ÊµãËØï1','123123123');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
