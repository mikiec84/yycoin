# MySQL-Front 5.1  (Build 4.13)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;


# Host: 127.0.0.1    Database: uportal
# ------------------------------------------------------
# Server version 5.5.19-log

#
# Source for table t_center_outback_item
#

CREATE TABLE `t_center_outback_item` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `outbackid` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `outid` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `reoano` varchar(255) DEFAULT NULL,
  `status` varchar(2) NOT NULL DEFAULT '0',
  `type` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
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
# Dumping data for table t_center_outback_item
#

INSERT INTO `t_center_outback_item` VALUES (1,'T2016060104580772','LY1605251053439900960',NULL,'0','赠送','系统内置供应商','银茶滤（普17）','银茶滤（普17）','2','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (2,'T2016060104591880','LY1605251053439900960',NULL,'0','赠送','系统内置供应商','银茶滤（普17）','银茶滤（普17）','3','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (3,'T2016060105201185','LY1605251053439900960',NULL,'0','赠送','系统内置供应商','银茶滤（普17）','银茶滤（普17）','2','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (4,'T2016061912502052','LY1606180927460300881',NULL,'0','个人领样','公共客户','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (5,'T2016061912520093','LY1606180927460300881',NULL,'0','个人领样','公共客户','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (6,'T2016061912530259','LY1606180927460300881',NULL,'0','个人领样','公共客户','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','YZ0080900 顾景舟诞辰100周年银壶纪念套装（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (7,'T2016061912534356','LY1606181206460392453',NULL,'0','个人领样','公共客户','YZ0140800 2016年法国欧洲杯2欧元纪念币（普17）','YZ0140800 2016年法国欧洲杯2欧元纪念币（普17）','2','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (8,'T2016061912534356','LY1606181256460423322',NULL,'0','个人领样','公共客户','YZ0140800 2016年法国欧洲杯2欧元纪念币（普17）','YZ0140800 2016年法国欧洲杯2欧元纪念币（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (9,'T2016061912560448','SO1606181404460466579',NULL,'0','销售出库','浦发银行广州轻纺城支行-银行','YZ0136400 银茶滤（普17）','YZ0136400 银茶滤（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (10,'T2016061912561109','SO1606181404460466579',NULL,'0','销售出库','浦发银行广州轻纺城支行-银行','YZ0136400 银茶滤（普17）','YZ0136400 银茶滤（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (11,'T2016061912583144','SO1606181404460466579',NULL,'0','销售出库','浦发银行广州轻纺城支行-银行','YZ0136400 银茶滤（普17）','YZ0136400 银茶滤（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (12,'T2016071611494913','SO1607151057486574978',NULL,'0','销售出库','中信银行重庆分行营业部-零售','YZ0074100 徐悲鸿诞辰120周年30克银章+3克金章纪念套装（普17)','YZ0074100 徐悲鸿诞辰120周年30克银章+3克金章纪念套装（普17)','4','申请原因',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (13,'T2016072803320401','LY1607271152496829504',NULL,'0','1','公共客户','YZ0133200 奥运冠军签名银币和奥运流通纪念币四组套装（普17）','YZ0133200 奥运冠军签名银币和奥运流通纪念币四组套装（普17）','1','13433334444',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (14,'T2016072904172701','XZ1603231524395591655',NULL,'0','赠送','兴业银行龙岩分行-银行','YZ0011800 魅の金狐狸','YZ0011800 魅の金狐狸','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (15,'T2016072904390872','XZ1603231524395591655',NULL,'0','赠送','兴业银行龙岩分行-银行','YZ0011800 魅の金狐狸','YZ0011800 魅の金狐狸','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (16,'T2016072904463288','SO1607271625497016792',NULL,'0','销售出库','深圳黄金投资有限公司','YZ0136400 银茶滤（普17）','YZ0136400 银茶滤（普17）','1','0',NULL,'');
INSERT INTO `t_center_outback_item` VALUES (17,'T2016072904473419','SO1607271625497016792',NULL,'0','销售出库','深圳黄金投资有限公司','YZ0136400 银茶滤（普17）','YZ0136400 银茶滤（普17）','1','0',NULL,'');

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
