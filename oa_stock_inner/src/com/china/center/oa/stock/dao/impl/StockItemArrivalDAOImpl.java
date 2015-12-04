/**
 * File Name: StockItemDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.stock.bean.StockItemArrivalBean;
import com.china.center.oa.stock.dao.StockItemArrivalDAO;
import com.china.center.oa.stock.vo.StockItemArrivalVO;

import java.util.List;


/**
 * StockItemArrivalDAOImpl
 * 
 * @author Simon
 * @version 2015-12-02
 * @see com.china.center.oa.stock.dao.impl.StockItemArrivalDAOImpl
 * @since 1.0
 */
public class StockItemArrivalDAOImpl extends BaseDAO<StockItemArrivalBean, StockItemArrivalVO> implements StockItemArrivalDAO
{
    @Override
    public List<StockItemArrivalBean> queryByStock(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
