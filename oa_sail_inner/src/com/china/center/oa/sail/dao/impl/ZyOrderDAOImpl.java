package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.ZyOrderBean;
import com.china.center.oa.sail.dao.ZyOrderDAO;


public class ZyOrderDAOImpl extends BaseDAO<ZyOrderBean, ZyOrderBean> implements ZyOrderDAO
{
    @Override
    public boolean updateStatus(String citicNo) {
        String sql = BeanTools.getUpdateHead(claz)
                + "set status = 1 where citicNo = ?";

        jdbcOperation.update(sql, citicNo);
        return true;
    }
}
