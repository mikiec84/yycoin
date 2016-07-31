package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.OutBackItemBean;

public interface OutBackItemDAO extends DAO<OutBackItemBean, OutBackItemBean>
{
    @Deprecated
    public void updateOano(String itemId, String oaNo);

    public void updateOanoWithOutId(String outId, String oaNo);

    public void updateDescription(String itemId, String description);

}
