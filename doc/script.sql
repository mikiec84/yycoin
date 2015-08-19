alter table t_center_out add column remoteAllocate int(11) DEFAULT 0
--2015/6/6 产品配置增加激励字段
alter table T_CENTER_ZJRCPRODUCT add column motivationMoney double DEFAULT '0'
alter table T_CENTER_ZJRCBASE add column motivationMoney double DEFAULT '0'

--2015/8/15 新产品申请去掉产品管理部审批环节
delete from t_center_oamenuitem where menuitemname ='新产品-产品管理部审批' and id='1021'

--2015/8/16 批量更新中信产品配置
insert into t_center_oamenuitem values(9035,'批量更新产品配置','../sailImport/batchUpdateZJRCProduct.jsp',90,1,9030,99,'批量更新产品配置')
