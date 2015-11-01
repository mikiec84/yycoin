package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.ProductVSGiftBean;
import com.china.center.oa.product.dao.ProductVSGiftDAO;
import com.china.center.oa.product.vo.ProductVSGiftVO;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;
import com.china.center.oa.sail.dao.ProductExchangeConfigDAO;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;

import java.util.List;

public class ProductExchangeConfigDAOImpl extends BaseDAO<ProductExchangeConfigBean, ProductExchangeConfigVO> implements ProductExchangeConfigDAO
{
    @Override
    public List<ProductExchangeConfigVO> queryVO(ConditionParse condition, PageSeparate page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int count(ConditionParse condition) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
