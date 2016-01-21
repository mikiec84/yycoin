alter table t_center_out add column remoteAllocate int(11) DEFAULT 0
--2015/6/6 产品配置增加激励字段
alter table T_CENTER_ZJRCPRODUCT add column motivationMoney double DEFAULT '0'
alter table T_CENTER_ZJRCBASE add column motivationMoney double DEFAULT '0'

--2015/7 外部品名配置
insert into t_center_oamenuitem values('0124','外部品名配置','../product/importProductVsBank.jsp','01',1,'0112',24,'外部品名配置')

--2015/8/15 新产品申请去掉产品管理部审批环节
delete from t_center_oamenuitem where menuitemname ='新产品-产品管理部审批' and id='1021'

--2015/8/16 批量更新中信产品配置
insert into t_center_oamenuitem values(9035,'批量更新产品配置','../sailImport/batchUpdateZJRCProduct.jsp',90,1,9030,99,'批量更新产品配置')

--2015/8/27 批量更新发票转移
insert into t_center_oamenuitem values(1492,'发票转移','../invoiceins/batchTransferInvoiceins.jsp',14,1,1402,99,'发票转移')

--2015/9/18 中信数据处理查询-〉批量预占功能
insert into t_center_oamenuitem values(9036,'批量预占库存','../sailImport/batchProcessSplitOut.jsp',90,1,9003,99,'批量预占库存')

--2015/9/29 中信导入增加客户姓名栏位
alter table t_center_out_import add column customerName varchar(200) DEFAULT ''

--2015/10/17 入库-换货功能
insert into t_center_oamenuitem values(1518,'入库-商品调换','../sail/out.do?method=preForAddBuyExchange&flag=1',15,1,1501,14,'商品调换')

--2015/10/30 商品换货
insert into t_center_oamenuitem values('0125','商品转换配置','../sail/queryProductExchange.jsp','01',1,'0112',25,'商品转换配置')
CREATE TABLE T_CENTER_PRODUCT_EXCHANGE (
id int(11) NOT NULL AUTO_INCREMENT,
srcProductId varchar(200) NOT NULL,
srcAmount int(11),
destProductId varchar(200) NOT NULL,
destAmount int(11),
PRIMARY KEY (id),
CONSTRAINT uc_ProductAmount UNIQUE (srcProductId,srcAmount)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2015/11/05 导入新产品
insert into t_center_oamenuitem values(1033,'批量导入新产品申请','../product/importProductApply.jsp',10,1,101001,26,'批量导入新产品申请')
alter table T_PRODUCT_APPLY add column className varchar(100) DEFAULT ''

--2015/11/16 取消采购入库预确认
delete from t_center_oamenuitem where menuitemname ='采购入库预确认'

--2015/11/21 把新产品申请里的销售周期/销售对象/纸币类型/外型栏位，分别改为 实物数量、包装数量、证书数量、产品克重
alter table T_PRODUCT_APPLY add column productAmount int(11) DEFAULT -1,add column packageAmount int(11) DEFAULT -1,add column certificateAmount int(11) DEFAULT -1,add column productWeight double DEFAULT 0 
alter table T_CENTER_PRODUCT add column productAmount int(11) DEFAULT -1,add column packageAmount int(11) DEFAULT -1,add column certificateAmount int(11) DEFAULT -1,add column productWeight double DEFAULT 0 

--2015/12/1 采购到货信息
CREATE TABLE `T_CENTER_STOCKITEMARRIAL` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stockId` varchar(40) DEFAULT NULL,
  `productId` varchar(40) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `totalWarehouseNum` int(11) DEFAULT NULL,
  `refOutId` varchar(40) DEFAULT NULL,
  `hasRef` int(11) DEFAULT '0',
  `stafferId` varchar(40) DEFAULT NULL,
  `providerId` varchar(40) DEFAULT NULL,
  `showId` varchar(40) DEFAULT NULL,
  `depotpartId` varchar(40) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `fechProduct` int(11) DEFAULT '0',
  `pay` int(11) DEFAULT '0',
  `price` double DEFAULT NULL,
  `prePrice` double DEFAULT NULL,
  `sailPrice` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `nearlyPayDate` varchar(40) DEFAULT NULL,
  `logTime` varchar(40) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `productNum` int(11) DEFAULT '0',
  `netAskId` varchar(40) DEFAULT NULL,
  `priceAskProviderId` varchar(40) DEFAULT NULL,
  `dutyId` varchar(40) DEFAULT NULL,
  `invoiceType` varchar(40) DEFAULT NULL,
  `mtype` int(11) DEFAULT '0',
  `extraStatus` int(11) DEFAULT '0',
  `deliveryDate` varchar(200) NOT NULL,
  `arrivalDate` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116910 DEFAULT CHARSET=utf8;

alter table T_CENTER_STOCKITEM add column totalWarehouseNum int(11) default 0

alter table T_CENTER_STOCKITEM modify refOutId varchar(200)

alter table T_CENTER_STOCKITEM add column deliveryDate varchar(200) default '', add column arrivalDate varchar(200) default ''

alter table T_CENTER_PACKAGE_ITEM modify productId varchar(200)

-- 2015/12/21 宁波银行邮件
alter table t_center_out_import add column nbyhNo varchar(200) default ''
alter table T_CENTER_PACKAGE add column sendMailFlagNbyh int(11) default -1

--2015/12/26 自动库管审批通过
insert into t_center_oamenuitem values(1493,'导入自动库管审批订单','../sailImport/importOutAutoApprove.jsp',14,1,1402,99,'自动库管审批通过')
CREATE TABLE t_center_auto_approve (
id int(11) NOT NULL AUTO_INCREMENT,
fullId varchar(200) NOT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2015/12/29 银行余额统计
CREATE TABLE T_CENTER_BANK_BALANCE (
id int(11) NOT NULL AUTO_INCREMENT,
bankId varchar(200) NOT NULL,
statDate varchar(200) NOT NULL,
balance double DEFAULT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8
insert into t_center_oamenuitem values(1654,'银行余额查询','../finance/bank.do?method=queryBankBalance&load=1',16,1,1312,99,'银行余额查询')

--2016/1/5 赠品平台优化
alter table T_CENTER_VS_GIFT add column industryName varchar(100) default '', add column industryName2 varchar(100) default '', add column industryName3 varchar(100) default '', add column city varchar(100) default '', add column stafferName varchar(100) default ''
alter table T_CENTER_VS_GIFT add column province varchar(100) default ''

--2016/1/13 导入客户
insert into t_center_oamenuitem values('0231','导入客户','../client/importCustomer.jsp','02',1,'0201',99,'导入客户')

--2016/1/18 导入汇款新增字段POS终端号
alter table T_CENTER_PAYMENT add column posTerminalNumber varchar(100) default ''

--2016/1/21 赠品B,C,D
alter table T_CENTER_VS_GIFT add column giftProductId2 varchar(100) default '', add column amount2 int(11) default 0, add column giftProductId3 varchar(100) default '', add column amount3 int(11) default 0

