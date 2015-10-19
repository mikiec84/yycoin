package com.china.center.oa.finance.manager;

import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.PreConsignBean;
import com.china.center.oa.sail.vo.OutVO;

import java.util.List;

public interface PackageManager {
	void createPackage();
	
	void createPackage(PreConsignBean pre, OutVO out) throws MYException;

    void createPackage(List<String> outIdList) throws MYException;
	
	void createInsPackage(PreConsignBean pre, String insId) throws MYException;

	/**2015/10/19
	 * 隔十分钟检查下状态为 “已出库”且发货方式不是“空发”的但没有生成CK单的订单，列入preconsign表
	 */
	void checkOrderWithoutCKJob();
}
