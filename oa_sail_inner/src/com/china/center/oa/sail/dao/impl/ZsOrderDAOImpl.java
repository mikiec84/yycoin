package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.ZsOrderBean;
import com.china.center.oa.sail.dao.ZsOrderDAO;


public class ZsOrderDAOImpl extends BaseDAO<ZsOrderBean, ZsOrderBean> implements ZsOrderDAO
{
    @Override
    public boolean updateStatus(String citicNo) {
        String sql = BeanTools.getUpdateHead(claz)
                + "set status = 1 where citicNo = ?";

        jdbcOperation.update(sql, citicNo);
        return true;
    }
}
