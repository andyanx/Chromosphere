# Host: localhost:3308  (Version: 5.1.44-community)
# Date: 2013-09-15 15:44:01
# Generator: MySQL-Front 5.3  (Build 4.13)

/*!40101 SET NAMES utf8 */;

#
# Source for table "ball"
#

DROP TABLE IF EXISTS `ball`;
CREATE TABLE `ball` (
  `entity_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '开奖期号',
  `red1` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第一个出现的红球',
  `red2` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第二个出现的红球',
  `red3` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第三个出现的红球',
  `red4` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第四个出现的红球',
  `red5` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第五个出现的红球',
  `red6` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖第六个出现的红球',
  `blue` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '摇奖出现的蓝球',
  `created_at` date DEFAULT NULL COMMENT '开奖日期',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开奖号码表';

#
# Source for table "award"
#

DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `entity_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '开奖期号',
  `betting_amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '投注总额',
  `pool_amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '奖池金额',
  `level1_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '一等奖注数',
  `level1_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '一等奖金额',
  `level2_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '二等奖注数',
  `level2_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '二等奖金额',
  `level3_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '三等奖注数',
  `level3_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '三等奖金额',
  `level4_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '四等奖注数',
  `level4_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '四等奖金额',
  `level5_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '五等奖注数',
  `level5_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '五等奖金额',
  `level6_nums` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '六等奖注数',
  `level6_award` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '六等奖金额',
  UNIQUE KEY `FK_AWARD_ENTITY_ID_BALL_ENTITY_ID` (`entity_id`),
  CONSTRAINT `FK_AWARD_ENTITY_ID_BALL_ENTITY_ID` FOREIGN KEY (`entity_id`) REFERENCES `ball` (`entity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='获奖金额表';

#
# Source for table "research"
#

DROP TABLE IF EXISTS `research`;
CREATE TABLE `research` (
  `entity_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '开奖期号',
  `value` varchar(12) NOT NULL COMMENT '红球序值',
  `sum` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '和值',
  `span` smallint(2) unsigned NOT NULL DEFAULT '0' COMMENT '跨度',
  `big_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '大球数',
  `small_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '小球数',
  `odd_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '奇球数',
  `even_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '偶球数',
  `prime_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '素数球数',
  `composite_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '合数球数',
  `consecutive_nums` smallint(1) unsigned NOT NULL DEFAULT '0' COMMENT '连号数',
  UNIQUE KEY `FK_RESEARCH_ENTITY_ID_BALL_ENTITY_ID` (`entity_id`),
  CONSTRAINT `FK_RESEARCH_ENTITY_ID_BALL_ENTITY_ID` FOREIGN KEY (`entity_id`) REFERENCES `ball` (`entity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='统计分析表';

#
# Source for table "webs"
#

DROP TABLE IF EXISTS `webs`;
CREATE TABLE `webs` (
  `web_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '网站标识',
  `name` varchar(30) NOT NULL COMMENT '网站名称',
  PRIMARY KEY (`web_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网站信息表';


#
# Data for table "webs"
#

INSERT INTO `webs` VALUES (1, 'NETEASE');
INSERT INTO `webs` VALUES (2, 'SINA');

#
# Source for table "sales"
#

DROP TABLE IF EXISTS `sales`;
CREATE TABLE `sales` (
  `entity_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '开奖期号',
  `web_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '网站标识',
  `red1` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球1',
  `red2` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球2',
  `red3` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球3',
  `red4` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球4',
  `red5` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球5',
  `red6` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球6',
  `red7` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球7',
  `red8` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球8',
  `red9` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球9',
  `red10` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球10',
  `red11` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球11',
  `red12` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球12',
  `red13` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球13',
  `red14` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球14',
  `red15` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球15',
  `red16` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球16',
  `red17` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球17',
  `red18` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球18',
  `red19` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球19',
  `red20` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球20',
  `red21` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球21',
  `red22` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球22',
  `red23` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球23',
  `red24` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球24',
  `red25` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球25',
  `red26` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球26',
  `red27` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球27',
  `red28` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球28',
  `red29` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球29',
  `red30` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球30',
  `red31` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球31',
  `red32` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球32',
  `red33` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红球33',
  `blue1` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球1',
  `blue2` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球2',
  `blue3` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球3',
  `blue4` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球4',
  `blue5` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球5',
  `blue6` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球6',
  `blue7` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球7',
  `blue8` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球8',
  `blue9` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球9',
  `blue10` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球10',
  `blue11` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球11',
  `blue12` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球12',
  `blue13` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球13',
  `blue14` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球14',
  `blue15` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球15',
  `blue16` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '蓝球16',
  UNIQUE KEY `FK_SALES_ENTITY_ID_WEB_ID` (`entity_id`, `web_id`),
  KEY `FK_SALES_ENTITY_ID_BALL_ENTITY_ID` (`entity_id`),
  KEY `FK_SALES_WEB_ID_WEBS_WEB_ID` (`web_id`),  
  CONSTRAINT `FK_SALES_ENTITY_ID_BALL_ENTITY_ID` FOREIGN KEY (`entity_id`) REFERENCES `ball` (`entity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_SALES_WEB_ID_WEBS_WEB_ID` FOREIGN KEY (`web_id`) REFERENCES `webs` (`web_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销量纪录表';