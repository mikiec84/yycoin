/**
 *
 */
package com.china.center.oa.stock.bean;


import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.stock.constant.StockConstant;

import java.io.Serializable;
import java.util.List;


/**
 * 2015/12/1 采购到货行
 * @author Simon
 */
@Entity(name = "采购到货ITEM")
@Table(name = "T_CENTER_STOCKITEMARRIAL")
public class StockItemArrivalBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = StockBean.class)
    private String stockId = "";

    @Join(tagClass = ProductBean.class)
    private String productId = "";

    /**
     * REF的入库单
     */
//    private String refOutId = "";

    /**
     * 数量
     */
    private int amount = 0;

    /**
     * 出货日期
     */
    private String deliveryDate = "";

    /**
     * 预计到货日期
     */
    private String arrivalDate = "";

    private String logTime = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
//
//    public String getRefOutId() {
//        return refOutId;
//    }
//
//    public void setRefOutId(String refOutId) {
//        this.refOutId = refOutId;
//    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }
}
