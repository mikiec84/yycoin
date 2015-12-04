/**
 * File Name: StockItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.stock.bean.StockItemArrivalBean;
import com.china.center.oa.stock.vo.StockItemArrivalVO;

import java.util.List;


/**
 * StockItemArrivalDAO
 * 
 * @author Simon
 * @version 2015-12-02
 * @see com.china.center.oa.stock.dao.StockItemArrivalDAO
 * @since 1.0
 */
public interface StockItemArrivalDAO extends DAO<StockItemArrivalBean, StockItemArrivalVO>
{
    List<StockItemArrivalBean> queryByStock(String stockId);

}
