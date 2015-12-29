/**
 * File Name: StatBankDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.BankBalanceBean;
import com.china.center.oa.finance.dao.BankBalanceDAO;
import com.china.center.oa.finance.vo.BankBalanceVO;

import java.util.List;


/**
 * BankBalanceDAOImpl
 * 
 * @author simon
 * @version 2015-12-28
 * @see com.china.center.oa.finance.dao.impl.BankBalanceDAOImpl
 * @since 3.0
 */
public class BankBalanceDAOImpl extends BaseDAO<BankBalanceBean, BankBalanceVO> implements BankBalanceDAO
{
    @Override
    public List<BankBalanceVO> query(String bank, String beginDate, String endDate) {
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addWhereStr();
        conditionParse.addCondition("bank","like","%"+bank+"%");
        conditionParse.addCondition("date",">=", beginDate);
        conditionParse.addCondition("date","<=", endDate);
        return this.queryEntityVOsByCondition(conditionParse);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
