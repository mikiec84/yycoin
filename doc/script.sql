alter table t_center_out add column remoteAllocate int(11) DEFAULT 0

--2015/8/15 新产品申请去掉产品管理部审批环节
delete from t_center_oamenuitem where menuitemname ='新产品-产品管理部审批' and id='1021'
