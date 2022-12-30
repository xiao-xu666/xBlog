/*
SQLyog Trial v13.1.8 (64 bit)
MySQL - 8.0.28 : Database - xBlog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xblog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xblog`;

/*Table structure for table `articleinfo` */

DROP TABLE IF EXISTS `articleinfo`;

CREATE TABLE `articleinfo` (
  `articleId` int NOT NULL AUTO_INCREMENT COMMENT '文章编号',
  `userId` int NOT NULL COMMENT '编写用户编号',
  `userName` varchar(50) NOT NULL COMMENT '编写用户名称',
  `articleTitle` varchar(200) NOT NULL COMMENT '文章标题',
  `articleClassifyId` int NOT NULL COMMENT '文章分类id',
  `articleClassifyName` varchar(100) DEFAULT NULL COMMENT '文章分类名称',
  `articleDase` varchar(500) NOT NULL COMMENT '文章描述',
  `articleImgLitimg` blob NOT NULL COMMENT '文章缩略图',
  `articleContent` longtext NOT NULL COMMENT '文章内容',
  `publishTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `articleState` int NOT NULL COMMENT '文章状态',
  `articlePass` int DEFAULT '1' COMMENT '后台审核是否通过',
  `commentState` int NOT NULL COMMENT '评论状态',
  `click` int NOT NULL DEFAULT '0' COMMENT '阅读次数',
  `review` int NOT NULL DEFAULT '0' COMMENT '评论次数',
  PRIMARY KEY (`articleId`),
  KEY `fk_articleInfo_userId` (`userId`),
  KEY `fk_articleInfo_articleClassifyId` (`articleClassifyId`),
  CONSTRAINT `fk_articleInfo_userId` FOREIGN KEY (`userId`) REFERENCES `userinfo` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章管理表';

/*Data for the table `articleinfo` */

insert  into `articleinfo`(`articleId`,`userId`,`userName`,`articleTitle`,`articleClassifyId`,`articleClassifyName`,`articleDase`,`articleImgLitimg`,`articleContent`,`publishTime`,`articleState`,`articlePass`,`commentState`,`click`,`review`) values 
(1,1,'admin','xBlog免费开源博客项目《版权声明》',6,'其他','本项目归xBlog所有，禁止转载售卖，仅供学习使用，如您是从第三方途径付费购买的此项目欢迎检举，请第一时间联系邮箱：2529023117@qq.com，谢谢配合！','','<h1 style=\"text-align: center;\">版权声明</h1><p>本项目归<strong>xBlog</strong>所有，禁止转载售卖，仅供学习使用，如您是从第三方途径付费购买的此项目欢迎检举，请第一时间联系邮箱：2529023117@qq.com，谢谢配合！</p><hr/><p>本资源来自xBlog个人免费分享</p><p>哔哩哔哩账号：xBlog<span style=\"font-size: 12px;\">（UID：383877246）</span></p>','2022-11-03 10:10:46',1,2,1,2,0),
(61,1,'admin','测试文章1测试文章1测试文章1测试文章1测试文章1',1,'技术','测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1','','<p>测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1测试文章1</p>','2022-11-03 10:21:33',1,2,1,0,0),
(62,1,'admin','测试文章2测试文章2测试文章2测试文章2测试文章',2,'资源','测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2','','<p>测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2测试文章2</p>','2022-11-03 10:21:55',1,2,1,0,0),
(63,1,'admin','测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3',3,'杂文','测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3','','<p>测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3测试文章3</p>','2022-11-03 10:22:13',1,2,1,0,0),
(64,1,'admin','测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4',4,'生活','测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4','','<p>测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4测试文章4</p>','2022-11-03 10:22:27',1,2,1,0,0),
(65,1,'admin','测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5',5,'情感','测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5','','<p>测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5测试文章5</p>','2022-11-03 10:22:40',1,2,1,0,0);

/*Table structure for table `backstagemenuinfo` */

DROP TABLE IF EXISTS `backstagemenuinfo`;

CREATE TABLE `backstagemenuinfo` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `chineseName` varchar(255) NOT NULL COMMENT '菜单中文名称',
  `title` varchar(255) NOT NULL COMMENT '菜单标题',
  `path` varchar(255) DEFAULT NULL COMMENT '菜单路径',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `parentMenuId` int DEFAULT NULL COMMENT '父菜单id',
  `menuStatus` tinyint DEFAULT NULL COMMENT '菜单是否可见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台菜单表';

/*Data for the table `backstagemenuinfo` */

insert  into `backstagemenuinfo`(`id`,`name`,`chineseName`,`title`,`path`,`icon`,`parentMenuId`,`menuStatus`) values 
(1,'backstageHome','后台首页','xBlog - 后台首页','/backstageHome','House',0,1),
(2,'articleOperation','文章操作','xBlog - 文章操作','/articleOperation','Reading',0,1),
(3,'addArticle','新建文章','xBlog - 新建文章','/addArticle','EditPen',2,1),
(4,'articleManager','文章管理','xBlog - 文章管理','/articleManager','Edit',2,1),
(5,'classifyManager','分类管理','xBlog - 分类管理','/classifyManager','FolderOpened',2,0),
(6,'commentManager','评论管理','xBlog - 评论管理','/commentManager','ChatDotSquare',0,1),
(7,'userManager','用户管理','xBlog - 用户管理','/userManager','MagicStick',0,0),
(8,'linkManager','链接管理','xBlog - 链接管理','/linkManager','Link',0,0),
(9,'personSetup','个人设置','xBlog - 个人设置','/personSetup','User',0,1),
(10,'systemSetup','系统设置','xBlog - 系统设置','/systemSetup','Setting',0,0);

/*Table structure for table `classifyinfo` */

DROP TABLE IF EXISTS `classifyinfo`;

CREATE TABLE `classifyinfo` (
  `classifyIntroduce` varchar(300) NOT NULL COMMENT '分类介绍',
  `classifyId` int NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `classifyName` varchar(100) NOT NULL COMMENT '分类名称',
  `articleNumber` int NOT NULL DEFAULT '0' COMMENT '文章数量',
  `color1` varchar(50) NOT NULL COMMENT '渐变色1',
  `color2` varchar(50) NOT NULL COMMENT '渐变色2',
  PRIMARY KEY (`classifyId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类管理表';

/*Data for the table `classifyinfo` */

insert  into `classifyinfo`(`classifyIntroduce`,`classifyId`,`classifyName`,`articleNumber`,`color1`,`color2`) values 
('日常挖坑集',1,'技术',1,'#6D80FE','#23D2FD'),
('创意无限大',2,'资源',1,'#FBF40F','#FFA9AB'),
('七七八八十分精彩',3,'杂文',1,'#09B0E8','#29F49A'),
('记录美好生活',4,'生活',1,'#717CFE','#FC83EC'),
('emo患者区',5,'情感',1,'#535b9a','#30bcd7'),
('踩坑喜加一',6,'其他',1,'#FF988B','#FF6B88');

/*Table structure for table `commentinfo` */

DROP TABLE IF EXISTS `commentinfo`;

CREATE TABLE `commentinfo` (
  `commentId` int NOT NULL AUTO_INCREMENT COMMENT '评论编号',
  `articleId` int NOT NULL COMMENT '文章代号',
  `userId` int NOT NULL COMMENT '评论用户编号',
  `content` text NOT NULL COMMENT '评论内容',
  `parentId` int DEFAULT '0' COMMENT '父评论',
  `commentDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论日期',
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `commentinfo` */

/*Table structure for table `effects` */

DROP TABLE IF EXISTS `effects`;

CREATE TABLE `effects` (
  `effects01` tinyint(1) DEFAULT '0' COMMENT '特效1',
  `effects02` tinyint(1) DEFAULT '0' COMMENT '特效2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `effects` */

insert  into `effects`(`effects01`,`effects02`) values 
(0,0);

/*Table structure for table `logininfo` */

DROP TABLE IF EXISTS `logininfo`;

CREATE TABLE `logininfo` (
  `loginId` int NOT NULL COMMENT '登录的用户编号',
  `loginTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录管理表';

/*Data for the table `logininfo` */

insert  into `logininfo`(`loginId`,`loginTime`) values 
(1,'2022-11-02 17:19:36'),
(1,'2022-11-03 09:40:57'),
(1,'2022-11-03 13:02:24');

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `messageId` int NOT NULL AUTO_INCREMENT COMMENT '留言编号',
  `messageName` varchar(100) NOT NULL COMMENT '留言昵称',
  `messageQQ` varchar(100) NOT NULL COMMENT '留言QQ',
  `content` text NOT NULL COMMENT '留言内容',
  `replyContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '回复留言内容',
  `messageDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言日期',
  PRIMARY KEY (`messageId`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `message` */

/*Table structure for table `systemsetup` */

DROP TABLE IF EXISTS `systemsetup`;

CREATE TABLE `systemsetup` (
  `effects02` tinyint(1) DEFAULT '0' COMMENT '特效2',
  `effects01` tinyint(1) DEFAULT '0' COMMENT '特效1',
  `stickArticle` varchar(50) DEFAULT NULL COMMENT '置顶',
  `allArticle` varchar(100) DEFAULT NULL COMMENT '文章展示',
  `featuredArticle` varchar(100) DEFAULT NULL COMMENT '左侧精选文章',
  `technologyArticle` varchar(100) DEFAULT NULL COMMENT '左侧技术专区文章',
  `resourceArticle` varchar(100) DEFAULT NULL COMMENT '右侧资源专区文章',
  `advertising1` varchar(300) DEFAULT NULL COMMENT '左侧广告图1',
  `advertisingLink1` varchar(300) DEFAULT NULL COMMENT '左侧广告链接1',
  `advertising2` varchar(300) DEFAULT NULL COMMENT '右侧广告图1',
  `advertisingLink2` varchar(300) DEFAULT NULL COMMENT '左侧广告链接1',
  `advertising3` varchar(300) DEFAULT NULL COMMENT '右侧广告图2',
  `advertisingLink3` varchar(300) DEFAULT NULL COMMENT '左侧广告链接1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `systemsetup` */

insert  into `systemsetup`(`effects02`,`effects01`,`stickArticle`,`allArticle`,`featuredArticle`,`technologyArticle`,`resourceArticle`,`advertising1`,`advertisingLink1`,`advertising2`,`advertisingLink2`,`advertising3`,`advertisingLink3`) values 
(0,0,'1','[61,1,62]','[1,61,62,63,64,65]','[61]','[62]','','','','','','');

/*Table structure for table `urlinfo` */

DROP TABLE IF EXISTS `urlinfo`;

CREATE TABLE `urlinfo` (
  `urlId` int NOT NULL AUTO_INCREMENT COMMENT '链接编号',
  `urlName` varchar(500) NOT NULL COMMENT '网站名称',
  `urlAddres` varchar(500) NOT NULL COMMENT '网站地址',
  `urlReferral` varchar(500) NOT NULL COMMENT '网站介绍',
  `urlLitimg` varchar(500) NOT NULL COMMENT '网站缩略图',
  `webmasterEmail` varchar(50) NOT NULL COMMENT '站长邮箱',
  `urlPass` int DEFAULT '1' COMMENT '后台审核是否通过',
  `urlType` int DEFAULT NULL COMMENT '链接类型',
  PRIMARY KEY (`urlId`),
  UNIQUE KEY `urlAddres` (`urlAddres`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='链接管理表';

/*Data for the table `urlinfo` */

/*Table structure for table `userinfo` */

DROP TABLE IF EXISTS `userinfo`;

CREATE TABLE `userinfo` (
  `userId` int NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `userType` int NOT NULL DEFAULT '1' COMMENT '用户类型',
  `userName` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `userPass` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `userEmail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `userRegdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `userSignature` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '个性签名',
  `userIcon` varchar(500) DEFAULT NULL COMMENT '个人头像',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userName` (`userName`),
  UNIQUE KEY `userEmail` (`userEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='注册用户管理表';

/*Data for the table `userinfo` */

insert  into `userinfo`(`userId`,`userType`,`userName`,`userPass`,`userEmail`,`userRegdate`,`userSignature`,`userIcon`) values 
(1,0,'admin','123456','123456@qq.com','2022-07-31 10:10:56','相信有一天, 理想主义终将所向披靡.','');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
