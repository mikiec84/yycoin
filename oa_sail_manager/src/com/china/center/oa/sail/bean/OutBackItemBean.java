package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.client.bean.CustomerBean;
import com.china.center.oa.publics.bean.AttachmentBean;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.ProvinceBean;
import com.china.center.oa.sail.constanst.OutConstant;

import java.io.Serializable;
import java.util.List;

/**
 * #283
 */
@SuppressWarnings("serial")
@Entity(name = "线下入库单")
@Table(name = "t_center_outback_item")
public class OutBackItemBean implements Serializable
{
	@Id
	private String id = "";

    private String outBackId = "";

    private String outId = "";

    private String reoano = "";

    private String status = "";

    private int type;

    private String customerName = "";

    private String productId = "";

    private String productName = "";

    private String amount = "";

    private String invoiceNum = "";

    private String description = "";

    private String changeTime = "";

    private String depot = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutBackId() {
        return outBackId;
    }

    public void setOutBackId(String outBackId) {
        this.outBackId = outBackId;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getReoano() {
        return reoano;
    }

    public void setReoano(String reoano) {
        this.reoano = reoano;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    @Override
    public String toString() {
        return "OutBackItemBean{" +
                "id='" + id + '\'' +
                ", outBackId='" + outBackId + '\'' +
                ", outId='" + outId + '\'' +
                ", reoano='" + reoano + '\'' +
                ", status='" + status + '\'' +
                ", type=" + type +
                ", customerName='" + customerName + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount='" + amount + '\'' +
                ", invoiceNum='" + invoiceNum + '\'' +
                ", description='" + description + '\'' +
                ", changeTime='" + changeTime + '\'' +
                ", depot='" + depot + '\'' +
                '}';
    }
}
