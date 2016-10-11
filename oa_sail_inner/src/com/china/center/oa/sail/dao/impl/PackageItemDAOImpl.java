package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.PackageItemBean;
import com.china.center.oa.sail.dao.PackageItemDAO;
import com.china.center.tools.TimeTools;

public class PackageItemDAOImpl extends BaseDAO<PackageItemBean, PackageItemBean> implements PackageItemDAO
{
    @Override
    public int replaceProductName(String outId, String oldProductName, String newProductName) {
        String sql = "update t_center_package_item set productName = ? where outId= ? and productName= ?";

        return jdbcOperation.update(sql, newProductName, outId, oldProductName);
    }
}
