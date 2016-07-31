# Host: 127.0.0.1  (Version: 5.5.19)
# Date: 2016-07-30 13:31:37
# Generator: MySQL-Front 5.3  (Build 4.214)

/*!40101 SET NAMES gb2312 */;

#
# Structure for table "t_center_outback_item"
#

DROP TABLE IF EXISTS `t_center_outback_item`;
CREATE TABLE `t_center_outback_item` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `outbackid` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `outid` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `reoano` varchar(255) DEFAULT NULL,
  `status` varchar(2) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL DEFAULT '0',
  `customername` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `productid` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `productname` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `amount` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `invoicenum` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `changetime` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

#
# Data for table "t_center_outback_item"
#

/*!40000 ALTER TABLE `t_center_outback_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_center_outback_item` ENABLE KEYS */;
