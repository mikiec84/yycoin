/**
 * File Name: TcpApplyDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.tcp.bean.BankBuLevelBean;

import java.util.List;


public interface BankBuLevelDAO extends DAO<BankBuLevelBean, BankBuLevelBean>
{
    public List<BankBuLevelBean> queryByBearType(String bearType);

    public List<BankBuLevelBean> queryByBearTypeAndManager(String bearType, String manager);

}
