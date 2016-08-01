/**
 * File Name: OutDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import com.china.center.common.MYException;
import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.OlOutBean;



/**
 * OlOutDAO
 * 
 * @author ZHUZHU
 * @version 2016-07-27
 * @see OlOutDAO
 * @since 1.0
 */
public interface OlOutDAO extends DAO<OlOutBean, OlOutBean>
{
    public void updateStatus(String olFullId, int status);

    public void updateDescription(String olFullId, String description);
}
