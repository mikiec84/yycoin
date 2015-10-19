alter table t_center_out add column remoteAllocate int(11) DEFAULT 0
--2015/6/6 产品配置增加激励字段
alter table T_CENTER_ZJRCPRODUCT add column motivationMoney double DEFAULT '0'
alter table T_CENTER_ZJRCBASE add column motivationMoney double DEFAULT '0'

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
