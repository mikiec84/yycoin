package com.china.center.oa.sail.bean;


import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.sail.constanst.OutConstant;
import java.io.Serializable;


/**
 * 2016/7/27 #281
 * 
 * @author ZHUZHU
 * @see OlOutBean
 * @since 1.0
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_CENTER_OLOUT")
public class OlOutBean implements Serializable
{
    @Id
    private String olFullId = "";

    /**
     * 排序ID
     */
    private String id = "";

    /**
     * 0:保存 1:提交 2:驳回 3:通过 4:会计审核通过 6:总经理审核通过 7:物流管理员通过<br>
     * (一般此通过即是销售单已经OK status in (3, 4))<br>
     * 预占库存 status in (1, 6, 7)
     */
    private int status = OutConstant.STATUS_SAVE;

    /**
     * 0:销售单 1:入库单
     */
    private int type = OutConstant.OUT_TYPE_OUTBILL;


    private String stafferName = "";

    private String stafferId = "";


    @Join(tagClass = UnitViewBean.class, type = JoinType.LEFT)
    private String customerId = "";

    private String customerName = "";

    private String express = "";

    private String expressCompany = "";

    private String expressPay = "";

    private String provinceId = "";

    private String cityId = "";

    private String address = "";

    private String receiver = "";

    private String telephone = "";

    private String description = "";


    /**
     * 销售单 紧急 标识 1:紧急
     */
    private int emergency = 0;

    private int priceStatus = 0;

    private int ibMotStatus = 0;

    /**
     * 库存移动时间
     */
    private String changeTime = "";



    /**
     * default constructor
     */
    public OlOutBean()
    {
    }

    public String getOlFullId() {
        return olFullId;
    }

    public void setOlFullId(String olFullId) {
        this.olFullId = olFullId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStafferName() {
        return stafferName;
    }

    public void setStafferName(String stafferName) {
        this.stafferName = stafferName;
    }

    public String getStafferId() {
        return stafferId;
    }

    public void setStafferId(String stafferId) {
        this.stafferId = stafferId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressPay() {
        return expressPay;
    }

    public void setExpressPay(String expressPay) {
        this.expressPay = expressPay;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public int getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(int priceStatus) {
        this.priceStatus = priceStatus;
    }

    public int getIbMotStatus() {
        return ibMotStatus;
    }

    public void setIbMotStatus(int ibMotStatus) {
        this.ibMotStatus = ibMotStatus;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "OlOutBean{" +
                "olFullId='" + olFullId + '\'' +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", stafferName='" + stafferName + '\'' +
                ", stafferId='" + stafferId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", express='" + express + '\'' +
                ", expressCompany='" + expressCompany + '\'' +
                ", expressPay='" + expressPay + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", address='" + address + '\'' +
                ", receiver='" + receiver + '\'' +
                ", telephone='" + telephone + '\'' +
                ", description='" + description + '\'' +
                ", emergency=" + emergency +
                ", priceStatus=" + priceStatus +
                ", ibMotStatus=" + ibMotStatus +
                ", changeTime='" + changeTime + '\'' +
                '}';
    }
}
