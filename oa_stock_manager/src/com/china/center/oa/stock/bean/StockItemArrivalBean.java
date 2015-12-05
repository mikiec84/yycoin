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
@Entity(name = "采购到货ITEM",inherit = true)
@Table(name = "T_CENTER_STOCKITEMARRIAL")
public class StockItemArrivalBean extends StockItemBean
{
    /**
     * 出货日期
     */
    private String deliveryDate = "";

    /**
     * 预计到货日期
     */
    private String arrivalDate = "";

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
    //
//    @Id(autoIncrement = true)
//    private String id = "";
//
//    @FK
//    @Join(tagClass = StockBean.class)
//    private String stockId = "";
//
//    @Join(tagClass = ProductBean.class)
//    private String productId = "";
//
//    /**
//     * REF的入库单
//     */
////    private String refOutId = "";
//
//    /**
//     * 数量
//     */
//    private int amount = 0;
//
//
//
//    private String logTime = "";
//
//    /**
//     * 本次入库数量,不存数据库，此值会存入对应生成的入库单中。
//     */
//    @Ignore
//    private int warehouseNum = 0;
//
//    /**
//     * 该商品累计已入库数量
//     */
//    @Ignore
//    private int totalWarehouseNum = 0;
//
//    @Ignore
//    private double price = 0.0d;
//
//    @Ignore
//    private double prePrice = 0.0d;
//
//    @Ignore
//    private String nearlyPayDate = "";
//
//    @Ignore
//    private double total = 0.0d;
//
//    @Ignore
//    private String description = "";
//
//    /**
//     * 供应商
//     */
//    @Ignore
//    @Join(tagClass = ProviderBean.class, type = JoinType.LEFT)
//    private String providerId = "";
//
//    /**
//     * 采用谁的询价
//     */
//    @Ignore
//    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
//    private String stafferId = "";
//
//    /**
//     * 拿货
//     */
//    private int fechProduct = StockConstant.STOCK_ITEM_FECH_NO;
//
//    /**
//     * 最终的仓区
//     */
//    private String depotpartId = "";
//
//    /**
//     * 平台的产品
//     */
//    private String showId = "";
//
//    @Join(tagClass = DutyBean.class, type = JoinType.LEFT)
//    private String dutyId = "";
//
//    /**
//     * 发票类型
//     */
//    private String invoiceType = "";
//
//
//    /**
//     * REF的入库单
//     */
//    private String refOutId = "";
//
//    /**
//     * 是否入库
//     */
//    private int hasRef = StockConstant.STOCK_ITEM_HASREF_NO;
//
//    public String getShowId() {
//        return showId;
//    }
//
//    public void setShowId(String showId) {
//        this.showId = showId;
//    }
//
//    public String getRefOutId() {
//        return refOutId;
//    }
//
//    public void setRefOutId(String refOutId) {
//        this.refOutId = refOutId;
//    }
//
//    public int getHasRef() {
//        return hasRef;
//    }
//
//    public void setHasRef(int hasRef) {
//        this.hasRef = hasRef;
//    }
//
//    public String getDutyId() {
//        return dutyId;
//    }
//
//    public void setDutyId(String dutyId) {
//        this.dutyId = dutyId;
//    }
//
//    public String getInvoiceType() {
//        return invoiceType;
//    }
//
//    public void setInvoiceType(String invoiceType) {
//        this.invoiceType = invoiceType;
//    }
//
//    public String getDepotpartId() {
//        return depotpartId;
//    }
//
//    public void setDepotpartId(String depotpartId) {
//        this.depotpartId = depotpartId;
//    }
//
//    public int getFechProduct() {
//        return fechProduct;
//    }
//
//    public void setFechProduct(int fechProduct) {
//        this.fechProduct = fechProduct;
//    }
//
//    public String getProviderId() {
//        return providerId;
//    }
//
//    public void setProviderId(String providerId) {
//        this.providerId = providerId;
//    }
//
//    public String getStafferId() {
//        return stafferId;
//    }
//
//    public void setStafferId(String stafferId) {
//        this.stafferId = stafferId;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getStockId() {
//        return stockId;
//    }
//
//    public void setStockId(String stockId) {
//        this.stockId = stockId;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
////
////    public String getRefOutId() {
////        return refOutId;
////    }
////
////    public void setRefOutId(String refOutId) {
////        this.refOutId = refOutId;
////    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//
//    public String getDeliveryDate() {
//        return deliveryDate;
//    }
//
//    public void setDeliveryDate(String deliveryDate) {
//        this.deliveryDate = deliveryDate;
//    }
//
//    public String getArrivalDate() {
//        return arrivalDate;
//    }
//
//    public void setArrivalDate(String arrivalDate) {
//        this.arrivalDate = arrivalDate;
//    }
//
//    public String getLogTime() {
//        return logTime;
//    }
//
//    public void setLogTime(String logTime) {
//        this.logTime = logTime;
//    }
//
//    public int getWarehouseNum() {
//        return warehouseNum;
//    }
//
//    public void setWarehouseNum(int warehouseNum) {
//        this.warehouseNum = warehouseNum;
//    }
//
//    public int getTotalWarehouseNum() {
//        return totalWarehouseNum;
//    }
//
//    public void setTotalWarehouseNum(int totalWarehouseNum) {
//        this.totalWarehouseNum = totalWarehouseNum;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public double getPrePrice() {
//        return prePrice;
//    }
//
//    public void setPrePrice(double prePrice) {
//        this.prePrice = prePrice;
//    }
//
//    public String getNearlyPayDate() {
//        return nearlyPayDate;
//    }
//
//    public void setNearlyPayDate(String nearlyPayDate) {
//        this.nearlyPayDate = nearlyPayDate;
//    }
//
//    public double getTotal() {
//        return total;
//    }
//
//    public void setTotal(double total) {
//        this.total = total;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
