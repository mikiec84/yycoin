package com.china.center.oa.sail.dao.impl;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.PackageItemBean;
import com.china.center.oa.sail.dao.PackageItemDAO;
import com.china.center.tools.TimeTools;

public class PackageItemDAOImpl extends BaseDAO<PackageItemBean, PackageItemBean> implements PackageItemDAO
{
    @Override
    public int replaceInvoiceNum(String outId, String oldInvoiceNum, String newInvoiceNum) {
        String sql = "update t_center_package_item set productId = ?, productName = ? where outId= ? and productId= ?";

        return jdbcOperation.update(sql, newInvoiceNum, "发票号："+newInvoiceNum, outId, oldInvoiceNum);
    }
}
