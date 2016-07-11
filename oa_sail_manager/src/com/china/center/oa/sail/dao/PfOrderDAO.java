package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.PfOrderBean;

public interface PfOrderDAO extends DAO<PfOrderBean, PfOrderBean>
{
    public boolean updateStatus(String citicNo);

}
