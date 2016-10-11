package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.PackageItemBean;

public interface PackageItemDAO extends DAO<PackageItemBean, PackageItemBean>
{
    public int replaceProductName(String outId, String oldProductName, String newProductName);

}
