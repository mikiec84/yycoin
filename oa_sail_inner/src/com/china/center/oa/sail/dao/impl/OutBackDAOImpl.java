package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.OutBackBean;
import com.china.center.oa.sail.dao.OutBackDAO;
import com.china.center.oa.sail.vo.OutBackVO;

public class OutBackDAOImpl extends BaseDAO<OutBackBean, OutBackVO> implements
		OutBackDAO
{
    @Override
    public void updateDescription(String id, String description) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "update T_CENTER_OUTBACK set description = ? where id = ?";

        jdbcOperation.update(sql, description, id);
    }
}
