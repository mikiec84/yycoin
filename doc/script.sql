alter table t_center_out add column remoteAllocate int(11) DEFAULT 0
--2015/6/6 ��Ʒ�������Ӽ����ֶ�
alter table T_CENTER_ZJRCPRODUCT add column motivationMoney double DEFAULT '0'
alter table T_CENTER_ZJRCBASE add column motivationMoney double DEFAULT '0'

--2015/7 �ⲿƷ������
insert into t_center_oamenuitem values('0124','�ⲿƷ������','../product/importProductVsBank.jsp','01',1,'0112',24,'�ⲿƷ������')

--2015/8/15 �²�Ʒ����ȥ����Ʒ��������������
delete from t_center_oamenuitem where menuitemname ='�²�Ʒ-��Ʒ����������' and id='1021'

--2015/8/16 �����������Ų�Ʒ����
insert into t_center_oamenuitem values(9035,'�������²�Ʒ����','../sailImport/batchUpdateZJRCProduct.jsp',90,1,9030,99,'�������²�Ʒ����')

--2015/8/27 �������·�Ʊת��
insert into t_center_oamenuitem values(1492,'��Ʊת��','../invoiceins/batchTransferInvoiceins.jsp',14,1,1402,99,'��Ʊת��')

--2015/9/18 �������ݴ�����ѯ-������Ԥռ����
insert into t_center_oamenuitem values(9036,'����Ԥռ����','../sailImport/batchProcessSplitOut.jsp',90,1,9003,99,'����Ԥռ����')

--2015/9/29 ���ŵ������ӿͻ�������λ
alter table t_center_out_import add column customerName varchar(200) DEFAULT ''

--2015/10/17 ����-��������
insert into t_center_oamenuitem values(1518,'����-��Ʒ����','../sail/out.do?method=preForAddBuyExchange&flag=1',15,1,1501,14,'��Ʒ����')

--2015/10/30 ��Ʒ����
insert into t_center_oamenuitem values('0125','��Ʒת������','../sail/queryProductExchange.jsp','01',1,'0112',25,'��Ʒת������')
CREATE TABLE T_CENTER_PRODUCT_EXCHANGE (
id int(11) NOT NULL AUTO_INCREMENT,
srcProductId varchar(200) NOT NULL,
srcAmount int(11),
destProductId varchar(200) NOT NULL,
destAmount int(11),
PRIMARY KEY (id),
CONSTRAINT uc_ProductAmount UNIQUE (srcProductId,srcAmount)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2015/11/05 �����²�Ʒ
insert into t_center_oamenuitem values(1033,'���������²�Ʒ����','../product/importProductApply.jsp',10,1,101001,26,'���������²�Ʒ����')
alter table T_PRODUCT_APPLY add column className varchar(100) DEFAULT ''

--2015/11/16 ȡ���ɹ�����Ԥȷ��
delete from t_center_oamenuitem where menuitemname ='�ɹ�����Ԥȷ��'

--2015/11/21 ���²�Ʒ����������������/���۶���/ֽ������/������λ���ֱ���Ϊ ʵ����������װ������֤����������Ʒ����
alter table T_PRODUCT_APPLY add column productAmount int(11) DEFAULT -1,add column packageAmount int(11) DEFAULT -1,add column certificateAmount int(11) DEFAULT -1,add column productWeight double DEFAULT 0 
alter table T_CENTER_PRODUCT add column productAmount int(11) DEFAULT -1,add column packageAmount int(11) DEFAULT -1,add column certificateAmount int(11) DEFAULT -1,add column productWeight double DEFAULT 0 

--2015/12/1 �ɹ�������Ϣ
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

-- 2015/12/21 ���������ʼ�
alter table t_center_out_import add column nbyhNo varchar(200) default ''
alter table T_CENTER_PACKAGE add column sendMailFlagNbyh int(11) default -1

--2015/12/26 �Զ���������ͨ��
insert into t_center_oamenuitem values(1493,'�����Զ�������������','../sailImport/importOutAutoApprove.jsp',14,1,1402,99,'�Զ���������ͨ��')
CREATE TABLE t_center_auto_approve (
id int(11) NOT NULL AUTO_INCREMENT,
fullId varchar(200) NOT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2015/12/29 ��������ͳ��
CREATE TABLE T_CENTER_BANK_BALANCE (
id int(11) NOT NULL AUTO_INCREMENT,
bankId varchar(200) NOT NULL,
statDate varchar(200) NOT NULL,
balance double DEFAULT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8
insert into t_center_oamenuitem values(1654,'����������ѯ','../finance/bank.do?method=queryBankBalance&load=1',16,1,1312,99,'����������ѯ')

--2016/1/5 ��Ʒƽ̨�Ż�
alter table T_CENTER_VS_GIFT add column industryName varchar(100) default '', add column industryName2 varchar(100) default '', add column industryName3 varchar(100) default '', add column city varchar(100) default '', add column stafferName varchar(100) default ''
alter table T_CENTER_VS_GIFT add column province varchar(100) default ''

--2016/1/13 �����ͻ�
insert into t_center_oamenuitem values('0231','�����ͻ�','../client/importCustomer.jsp','02',1,'0201',99,'�����ͻ�')

--2016/1/18 �������������ֶ�POS�ն˺�
alter table T_CENTER_PAYMENT add column posTerminalNumber varchar(100) default ''

--2016/1/21 ��ƷB,C,D
alter table T_CENTER_VS_GIFT add column giftProductId2 varchar(100) default '', add column amount2 int(11) default 0, add column giftProductId3 varchar(100) default '', add column amount3 int(11) default 0

--2016/2/13 ��������δ����ԭ��
insert into t_center_oamenuitem values(1494,'��������δ����ԭ��','../sailImport/batchUpdateReason.jsp',14,1,1402,99,'��������δ����ԭ��')
insert into T_CENTER_ENUMDEFINE values(118, '310','����_��������δ����ԭ��','',0)
alter table t_center_out add column reason varchar(100) DEFAULT ''

--2016/2/15 ��Ʊ���̱���
alter table T_CENTER_INVOICEINS_IMPORT add column virtualInvoiceNum varchar(100) default ''
alter table T_CENTER_INVOICEINS_IMPORT add column productId varchar(100) default '',add column productName varchar(100) default '',add column amount int(11) default 0, add column splitFlag int(11) default 0
insert into t_center_oamenuitem values(9037,'�������뿪Ʊ����','../invoiceins/importInvoiceinsApply.jsp',90,1,9031,99,'�������뿪Ʊ����')

--2016/2/29 stored function ��ȡ���۵�����
drop function get_out_type
CREATE FUNCTION get_out_type(out_type int, type2 int) returns varchar(100)
  DETERMINISTIC
BEGIN
  DECLARE result varchar(100);
  /*���۵�*/
  if type2 = 0 then
    if out_type = 0 then
       set result = '���۳���';
    elseif out_type = 1 then
       set result = '��������';
    elseif out_type = 2 then
       set result = '����';
    elseif out_type = 3 then
       set result = 'ί�д���';
    elseif out_type = 4 then
       set result = '����';  
    elseif out_type = 5 then
       set result = '�ͻ��̻�'; 
    elseif out_type = 6 then
       set result = 'Ѳչ����'; 
    elseif out_type = 7 then
       set result = '��������';     
    END if;
  /*���ⵥ*/
  elseif type2 = 1 then
    if out_type = 0 then
       set result = '�ɹ�����';
    elseif out_type = 1 then
       set result = '����';
    elseif out_type = 2 then
       set result = '����';
    elseif out_type = 3 then
       set result = 'ϵͳ����';
    elseif out_type = 4 then
       set result = '�����˿�';  
    elseif out_type = 5 then
       set result = '�����˿�'; 
    elseif out_type = 6 then
       set result = '�ɹ��˻�'; 
    elseif out_type = 7 then
       set result = '��Ʒ�˻�';  
    elseif out_type = 8 then
       set result = '��Ʒ����';  
    elseif out_type = 99 then
       set result = '��������'; 
    elseif out_type = 25 then
       set result = '��������'; 
    elseif out_type = 98 then
       set result = 'ί���˻�';    
    END if;
  END if;
  return (result);
END 

--2016/3/10 �������²�Ʒ��
insert into t_center_oamenuitem values(9038,'�����������۵�Ʒ��','../sailImport/batchUpdateProductName.jsp',90,1,9031,99,'�����������۵�Ʒ��')

--2016/3/16 �����������ռ�������
insert into t_center_oamenuitem values(1495,'�����������ռ�������','../tcp/batchUpdateIbMoney.jsp',14,1,1402,99,'�����������ռ�������')


--2016/3/18 ������ֵ˰��Ʊ17
insert into T_CENTER_INVOICE values('90000000000000000034','��ֵ˰ר�÷�Ʊ17%',0,1,1,17,'��ֵ˰ר�÷�Ʊ17%')
insert into T_CENTER_VS_DUTYINV values('16',4, '90000000000000000034')
alter table T_CENTER_INVOICEINS_IMPORT add column zzsInfo varchar(200) default '';
alter table T_CENTER_INVOICEINS add column zzsInfo varchar(200) default '';

--2016/3/19 �˿�ʱ������λ���ݵ���
alter table t_center_out add column transportNo varchar(100) default ''
alter table T_CENTER_BATCHSWATCH add column transportNo varchar(100) default ''

--2016/3/20 �ջ��Ǽ����̱���
alter table T_CENTER_OUTBACK add column note varchar(1024) default '',add column handoverReason varchar(100) default ''
alter table T_CENTER_OUTBACK add column handoverChecker varchar(16) default '',add column handoverCheckTime varchar(100) default '', add column confirmChecker varchar(16) default '',add column confirmCheckTime varchar(100) default ''
insert into t_center_oamenuitem values(1519,'��������','../sail/queryOutBack.jsp?mode=4',15,1,1501,12,'��������')
insert into t_center_oamenuitem values(1520,'����ȷ��','../sail/queryOutBack.jsp?mode=5',15,1,1501,13,'��������')
update t_center_oamenuitem set indexPos = indexPos+2 where id in ('1516','1517','1518')

--2016/4/14 �ʼ������������۵�
create table T_CENTER_CITIC_ORDER (	
id int(11) NOT NULL AUTO_INCREMENT,
mailId varchar(100) NOT NULL,
status int(11) default 0,
customerId varchar(40) NOT NULL,
customerName varchar(40) NOT NULL ,
idCard varchar(20) NOT NULL,
dealDate varchar(40),
pickupDate varchar(40),
pickupFlag int(11),
tellerId varchar(40),
pickupNode varchar(40),
pickupAddress varchar(200),
branchId varchar(40),
branchName varchar(40) NOT NULL,
secondBranch varchar(40) NOT NULL,
comunicationBranch varchar(40) NOT NULL,
comunicatonBranchName varchar(40) NOT NULL,
productId varchar(40),
productCode varchar(40) NOT NULL,
enterpriseProductCode varchar(40),
productName varchar(100) NOT NULL,
amount int(11) NOT NULL,
price double default '0' NOT NULL,
productWeight double default '0',
value double default '0' NOT NULL,
fee double default '0' NOT NULL,
arriveDate varchar(40),
orderOrShow varchar(40),
spotFlag int(40),
citicNo varchar(40) NOT NULL,
invoiceNature varchar(40) NOT NULL,
invoiceHead varchar(40) NOT NULL,
invoiceCondition varchar(40) NOT NULL,
managerId varchar(40) NOT NULL,
manager varchar(40) NOT NULL,
originator varchar(40),
provinceId varchar(40),
provinceName varchar(40),
cityId varchar(40),
city varchar(40) NOT NULL,
address varchar(40) NOT NULL,
receiver varchar(40) NOT NULL,
receiverMobile varchar(40) NOT NULL,
handPhone varchar(40),
weight double default '0',
goldPrice double default '0',
materialType varchar(40),
productType varchar(40),
pickupType varchar(40),
teller varchar(40),
logTime varchar(40),
citicOrderDate varchar(40),
enterpriseName varchar(40) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY `INX_ALL` (`citicNo`,`productName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

create table T_CENTER_ZY_ORDER (	
id int(11) NOT NULL AUTO_INCREMENT,
mailId varchar(100) NOT NULL,
status int(11) default 0,
innerCustomerId varchar(40) NOT NULL,
customerId varchar(40) NOT NULL,
customerName varchar(40) NOT NULL,
customerType varchar(20) NOT NULL,
customerGroup varchar(20) NOT NULL,
idType varchar(20) NOT NULL,
idCard varchar(20) NOT NULL,
proxyIdType varchar(20),
proxyIdCard varchar(20),
bankAccount varchar(40) NOT NULL,
channel varchar(40) NOT NULL,
originalChannel varchar(40),
terminal varchar(40),
dealDate varchar(40) NOT NULL,
dealTime varchar(40) NOT NULL,
dealCode varchar(40) NOT NULL,
comunicatonBranchName varchar(100) NOT NULL,
originalDealAgent varchar(40),
tellerId varchar(40),
sendMainframeId varchar(40) NOT NULL,
mainframeCheckDate varchar(40) NOT NULL,
mainframeTradeCode varchar(40) NOT NULL,
mainframeDate varchar(40),
mainframeId varchar(40) NOT NULL,
returnCode varchar(20) NOT NULL,
returnMessage varchar(20) NOT NULL,
tradeStatus varchar(20) NOT NULL,
acceptMethod varchar(20),
corporateAccount varchar(40) NOT NULL,
finished int(11) NOT NULL default 0,
exceptional int(11) default 0,
appointmentOfArrival int(11) default 0,
pickupNode varchar(40),
specCode varchar(40) NOT NULL,
specName varchar(40) NOT NULL,
spec double default '0',
businessType varchar(40),
associateDate varchar(40),
associateId varchar(40),
citicNo varchar(40) NOT NULL,
channelSerialNumber varchar(40) NOT NULL,
productId varchar(40),
productCode varchar(40) NOT NULL,
productName varchar(100) NOT NULL,
amount int(11) NOT NULL,
buyUnit int(11) NOT NULL,
price double default '0' NOT NULL,
value double default '0' NOT NULL,
fee double default '0' NOT NULL,
arriveDate varchar(40),
invoiceHead varchar(40) NOT NULL,
financialStatus varchar(40),
currency varchar(40) NOT NULL,
manager varchar(40) NOT NULL,
paymentMethod varchar(40),
remainAmount int(11) default 0,
storageCost double default '0',
discountRate double default '0',
productType varchar(40),
pickupType varchar(40),
teller varchar(40),
logTime varchar(40),
enterpriseCode varchar(40) NOT NULL,
enterpriseName varchar(40) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY `INX_ALL` (`citicNo`,`productName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2016/4/20 �����ʼ���������Ա
alter table T_CENTER_PACKAGE add column sendMailFlagSails int(11) default -1

--2016/4/23 Ʊ�������޸�
CREATE TABLE `T_CENTER_TEMPCONSIGN` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `outId` varchar(1024) DEFAULT NULL,
  `insId` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--2016/4/30 ����ҵ�񲿲㼶��ϵ����
insert into t_center_oamenuitem values('1924','����ҵ�񲿲㼶��ϵ����','../tcp/importBankBuLevel.jsp','19',1,'0000',21,'����ҵ�񲿲㼶��ϵ����')
CREATE TABLE `T_CENTER_BANKBU_LEVEL` (
  `id` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `provinceManagerId` varchar(40) DEFAULT NULL,
  `provinceManager` varchar(40) DEFAULT NULL,
  `regionalManagerId` varchar(40) DEFAULT NULL,
  `regionalManager` varchar(40) DEFAULT NULL,
  `regionalDirectorId` varchar(40) DEFAULT NULL,
  `regionalDirector` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table T_CENTER_COMPOSE add column description varchar(1024) default ''

alter table T_CENTER_TEMPCONSIGN modify column outId varchar(1024);

--2016/5/17 �����ͻ���ַ��Ϣ
alter table T_CENTER_VS_CUSTADDR add column shipping int(11) DEFAULT -1,add column transport1 int(11) DEFAULT 0,add column transport2 int(11) DEFAULT 0, add column expressPay int(11) DEFAULT -1,add column transportPay int(11) DEFAULT -1
insert into t_center_oamenuitem values('0232','�����ͻ���ַ��Ϣ','../client/importCustomerAddress.jsp','02',1,'0201',99,'�����ͻ���ַ��Ϣ')

--2016/5/25 ��������Ʒ����
CREATE TABLE `T_CENTER_PRODUCT_IMPORT` (
  `id` varchar(40) NOT NULL,
  `bank` varchar(40) NOT NULL,
  `bankProductCode` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `code` varchar(40) NOT NULL,
  `bankProductBarcode` varchar(40) NOT NULL,
  `bankProductName` varchar(40) NOT NULL,
  `properties` varchar(100) NOT NULL,
  `weight` varchar(40) DEFAULT '',
  `material` varchar(40) DEFAULT NULL,
  `retailPrice` double DEFAULT 0,
  `costPrice` double DEFAULT 0,
  `ibMoney` double DEFAULT 0,
  `motivationMoney` double DEFAULT 0,
  `grossProfit` double DEFAULT 0,
  `buyBack` int(11) DEFAULT -1,
  `onMarketDate` varchar(40) DEFAULT '',
  `offlineDate` varchar(40) DEFAULT '',
  `branchRange` varchar(40) DEFAULT '',
  `taxRate` double DEFAULT 0,
  `invoiceType` varchar(40) NOT NULL,
  `invoiceContent` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into t_center_oamenuitem values(1034,'����������Ʒ������','../product/importProductForMailOut.jsp',10,1,101001,27,'����������Ʒ������')

--2016/6/5 �²�Ʒ��������
insert into t_center_oamenuitem values(1021,'�²�Ʒ-�����ܼ�����','../product/queryProductApply.jsp?forward=1',10,1,101003,14,null)
update t_center_oamenuitem set menuitemname='�²�Ʒ-ս������' where id=1022 and menuitemname='�²�Ʒ-��Ӫ��������'

--2016/6/5 #256
drop PROCEDURE if exists auth_user
CREATE PROCEDURE auth_user(in auth_id varchar(100))
BEGIN
   DECLARE u_id varchar(100) DEFAULT "";
   DECLARE u_name varchar(100) DEFAULT "";
   DECLARE u_roleId varchar(100) DEFAULT "";
   DECLARE no_more_rows BOOLEAN;
   DECLARE role_auth_count int(11) default -1;
   
   DEClARE user_cursor CURSOR FOR select id,name,roleId from T_CENTER_OAUSER;
   DECLARE CONTINUE HANDLER FOR NOT FOUND
    SET no_more_rows = TRUE;
          
   OPEN user_cursor;
   the_loop: LOOP
   FETCH user_cursor INTO u_id,u_name,u_roleId;
    IF no_more_rows THEN
        CLOSE user_cursor;
        LEAVE the_loop;
    END IF;
   #SELECT u_id,u_name,u_roleId;  
   select count(id) into role_auth_count from t_center_roleauth where roleid=u_roleId and authid=auth_id;
   if role_auth_count = 0 then 
      #select 'insert t_center_roleauth', u_roleId, auth_id;
      insert into t_center_roleauth(roleid,authid) values(u_roleId,auth_id); 
   end if;
   
   #select role_auth_count;
   END LOOP the_loop; 
END 
call auth_user('1501')

--2016/6/24 #222
alter table t_center_out_import add column result varchar(100) default ''
alter table t_center_out_import add column importFromMail int(11) default 0

--2016/7/5 #269
create table T_CENTER_ZS_ORDER (	
id int(11) NOT NULL AUTO_INCREMENT,
sn varchar(40) DEFAULT '',
mailId varchar(100) NOT NULL,
status int(11) default 0,
orderStatus varchar(40) default '',
dealDate varchar(40),
dealTime varchar(40),
account varchar(40),
providerId varchar(100),
pickupNode varchar(40),
branchName varchar(40) NOT NULL,
comunicatonBranchName varchar(40) NOT NULL,
storageControlType varchar(40) default '',
productCode varchar(40) NOT NULL,
productName varchar(100) NOT NULL,
productSpec varchar(100) default '',
amount int(11) NOT NULL,
price double default '0' NOT NULL,
value double default '0' NOT NULL,
fee double default '0' NOT NULL,
citicNo varchar(40) NOT NULL,
invoiceNature varchar(40) NOT NULL,
invoiceHead varchar(40) NOT NULL,
invoiceCondition varchar(40) NOT NULL,
materialType varchar(40),
logTime varchar(40),
PRIMARY KEY (id),
UNIQUE KEY `INX_ALL` (`citicNo`,`productName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

--2016/7/9 #268
create table T_CENTER_PF_ORDER (	
id int(11) NOT NULL AUTO_INCREMENT,
mailId varchar(100) NOT NULL,
status int(11) default 0,
dealDate varchar(40),
dealTime varchar(40),
branchName varchar(40) NOT NULL,
comunicatonBranchName varchar(40) NOT NULL,
productCode varchar(40) NOT NULL,
productName varchar(100) NOT NULL,
amount int(11) NOT NULL,
price double default '0' NOT NULL,
value double default '0' NOT NULL,
arrivalDate varchar(40) default '',
citicNo varchar(40) NOT NULL,
logTime varchar(40),
method varchar(40),
pos varchar(40),
description varchar(200),
shippingOrg varchar(100),
PRIMARY KEY (id),
UNIQUE KEY `INX_ALL` (`citicNo`,`productName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8


alter table T_CENTER_TCPIBREPORT_ITEM add column price double default '0'

--#287
alter table T_CENTER_OUTPRODUCT add column shipping int(11) default 0, add column pay int(11) default -1

-- #294
CREATE TABLE t_center_tempout (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `outId` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8
drop PROCEDURE if exists update_pay
CREATE PROCEDURE update_pay(in po_date varchar(100))
BEGIN
   DECLARE outId varchar(100) DEFAULT "";
   DECLARE total int(11) default -1;
   DECLARE total_in int(11) default -1;
   DECLARE no_more_rows BOOLEAN;
   
   DEClARE out_cursor CURSOR FOR select OutBean.fullId,OutBean.total from T_CENTER_out OutBean where OutBean.poDate>=po_date and OutBean.type=0 and OutBean.pay=0  and exists (select OutBean2.* from T_CENTER_out OutBean2 where OutBean2.refOutFullId=OutBean.fullId and OutBean2.type=1 and OutBean2.status in(3,4));
   DECLARE CONTINUE HANDLER FOR NOT FOUND
    SET no_more_rows = TRUE;
          
   OPEN out_cursor;
   the_loop: LOOP
   FETCH out_cursor INTO outId,total;
    IF no_more_rows THEN
        CLOSE out_cursor;
        LEAVE the_loop;
    END IF;
   #select outId,total;
   select sum(OutBean2.total) into total_in from T_CENTER_out OutBean2 where OutBean2.type=1 and OutBean2.status in(3,4) and OutBean2.refOutFullId=outId;
   #select total_in;
   #select abs(total-total_in);
   if abs(total-total_in)<=0.001 then 
      update t_center_out set pay=1 where fullid=outId;
      insert into t_center_tempout(outid) values(outId);
      #SELECT ROW_COUNT();
   end if;
   END LOOP the_loop; 
   select * from t_center_tempout;
END 

call update_pay('2016-08-22')

#294
show processlist;
SET GLOBAL event_scheduler = ON;
DROP EVENT IF EXISTS update_pay_minutely
CREATE EVENT update_pay_minutely
    ON SCHEDULE
      EVERY 10 MINUTE
    DO
      BEGIN
        call update_pay('2016-01-01');
        insert into t_center_tempout(outid) values(now());
      END

#300
alter table T_CENTER_PACKAGE add column zsFollowOut integer DEFAULT 1      