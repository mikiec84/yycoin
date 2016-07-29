package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.OlOutBean;
import com.china.center.oa.sail.dao.OlOutDAO;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 16-7-27
 * Time: 下午9:16
 * To change this template use File | Settings | File Templates.
 */
public class OlOutDAOImpl extends BaseDAO<OlOutBean, OlOutBean> implements OlOutDAO {

    @Override
    public void updateStatus(String olFullId, int status) {
        String sql = "update t_center_olout set status = ? where olfullid = ?";

        jdbcOperation.update(sql, status, olFullId);
    }
}
