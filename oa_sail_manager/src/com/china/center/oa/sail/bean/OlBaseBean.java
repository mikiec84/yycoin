/**
 * File Name: BaseBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-26
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import com.china.center.jdbc.annotation.*;
import java.io.Serializable;


/**
 * #281
 * 
 * @author ZHUZHU
 * @version 2016-07-26
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_OLBASE")
public class OlBaseBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String outId = "";

    private String oaNo = "";

    private String productCode = "";

    private String productName = "";

    private int amount = 0;

    /**
     * 业务员销售价格
     */
    private double price = 0.0d;


    /**2015/04/11 中收激励功能
     * 中收金额
     */
    private double ibMoney = 0.0d;

    /**2015/04/11 中收激励功能
     * 激励金额
     */
    private double motivationMoney = 0.0d;

    private String changeTime = "";

    /**
     * default constructor
     */
    public OlBaseBean()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getOaNo() {
        return oaNo;
    }

    public void setOaNo(String oaNo) {
        this.oaNo = oaNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getIbMoney() {
        return ibMoney;
    }

    public void setIbMoney(double ibMoney) {
        this.ibMoney = ibMoney;
    }

    public double getMotivationMoney() {
        return motivationMoney;
    }

    public void setMotivationMoney(double motivationMoney) {
        this.motivationMoney = motivationMoney;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "OlBaseBean{" +
                "id='" + id + '\'' +
                ", outId='" + outId + '\'' +
                ", oaNo='" + oaNo + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", ibMoney=" + ibMoney +
                ", motivationMoney=" + motivationMoney +
                ", changeTime='" + changeTime + '\'' +
                '}';
    }
}
