package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.PackageItemBean;

public interface PackageItemDAO extends DAO<PackageItemBean, PackageItemBean>
{
    public int replaceInvoiceNum(String outId, String oldInvoiceNum, String newInvoiceNum);

}
