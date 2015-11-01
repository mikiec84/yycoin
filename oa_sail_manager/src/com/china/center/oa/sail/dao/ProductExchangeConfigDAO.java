/**
 * File Name: SailConfigDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-12-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;
import java.util.List;


/**
 * ProductExchangeConfigDAO
 * 
 * @author simon
 * @version 2015-10-30
 * @see com.china.center.oa.sail.dao.ProductExchangeConfigDAO
 * @since 3.0
 */
public interface ProductExchangeConfigDAO extends DAO<ProductExchangeConfigBean, ProductExchangeConfigVO>
{
    /**
     * queryVO
     * 
     * @param condition
     * @param page
     * @return
     */
    List<ProductExchangeConfigVO> queryVO(ConditionParse condition, PageSeparate page);

    /**
     * count
     * 
     * @param condition
     * @return
     */
    int count(ConditionParse condition);
}
