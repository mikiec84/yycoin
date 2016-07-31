package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.OutBackItemBean;
import com.china.center.oa.sail.dao.OutBackItemDAO;
import com.china.center.tools.TimeTools;

public class OutBackItemDAOImpl extends BaseDAO<OutBackItemBean, OutBackItemBean> implements
        OutBackItemDAO
{
    @Deprecated
    @Override
    public void updateOano(String itemId, String oaNo) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "update t_center_outback_item set reoano = ?,status='9' where id = ?";

        jdbcOperation.update(sql, oaNo, itemId);
    }

    @Override
    public void updateOanoWithOutId(String outId, String oaNo) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "update t_center_outback_item set reoano = ?,status='9',changeTime=? where outId = ?";

        jdbcOperation.update(sql, oaNo, TimeTools.now(), outId);
    }

    @Override
    public void updateDescription(String itemId, String description) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "update t_center_outback_item set description = ?,changeTime=? where id = ?";

        jdbcOperation.update(sql, description, TimeTools.now(), itemId);
    }

}
