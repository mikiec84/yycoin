package com.china.center.oa.stock.vo;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.stock.bean.StockItemArrivalBean;

/**
 * Created by user on 2015/12/4.
 */
@Entity(inherit = true)
public class StockItemArrivalVO extends StockItemArrivalBean{
    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    @Relationship(relationField = "providerId", tagField = "name")
    private String providerName = "";

    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    public String getStafferName() {
        return stafferName;
    }

    public void setStafferName(String stafferName) {
        this.stafferName = stafferName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
