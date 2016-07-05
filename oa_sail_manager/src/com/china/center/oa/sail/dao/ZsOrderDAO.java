package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.ZsOrderBean;

public interface ZsOrderDAO extends DAO<ZsOrderBean, ZsOrderBean>
{
    public boolean updateStatus(String citicNo);

}
