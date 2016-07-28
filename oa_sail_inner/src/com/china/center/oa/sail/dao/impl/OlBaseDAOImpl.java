package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.sail.bean.OlBaseBean;
import com.china.center.oa.sail.dao.OlBaseDAO;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 16-7-27
 * Time: 下午9:16
 * To change this template use File | Settings | File Templates.
 */
public class OlBaseDAOImpl extends BaseDAO<OlBaseBean, OlBaseBean> implements OlBaseDAO {

    @Override
    public void updateOaNo(String outId, String oaNo) {
        String sql = "update t_center_olbase set oaNo = ? where outId = ?";

        jdbcOperation.update(sql, oaNo, outId);
    }
}
