/**
 * File Name: SailConfigManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-12-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;


/**
 * ProductExchangeConfigManager
 * 
 * @author simon
 * @version 2015-10-31
 * @see com.china.center.oa.sail.manager.ProductExchangeConfigManager
 * @since 3.0
 */
public interface ProductExchangeConfigManager
{
    boolean deleteBean(User user, String id)
        throws MYException;

    ProductExchangeConfigVO findVO(String id)
        throws MYException;

    boolean addBean(User user, ProductExchangeConfigBean bean)
        throws MYException;

    boolean updateBean(User user, ProductExchangeConfigBean bean)
        throws MYException;

}
