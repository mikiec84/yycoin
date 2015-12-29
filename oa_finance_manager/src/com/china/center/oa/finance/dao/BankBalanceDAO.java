/**
 * File Name: StatBankDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.finance.bean.BankBalanceBean;
import com.china.center.oa.finance.vo.BankBalanceVO;

import java.util.List;


/**
 * BankBalanceDAO
 * 
 * @author simon
 * @version 2015-12-28
 * @see com.china.center.oa.finance.dao.BankBalanceDAO
 * @since 3.0
 */
public interface BankBalanceDAO extends DAO<BankBalanceBean, BankBalanceVO>
{
    List<BankBalanceVO> query(String bank, String beginDate, String endDate);
}
