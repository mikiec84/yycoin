/*
 * File Name: Product.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-25
 * Grant: open source to everybody
 */
package com.china.center.oa.product.bean;


import com.china.center.jdbc.annotation.*;
import java.io.Serializable;


/**#235 #222
 * 产品导入
 * 
 * @author simon
 * @version 2016-5-10
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_PRODUCT_IMPORT")
public class ProductImportBean implements Serializable
{
    @Id
    private String id = "";

    /**
     * 银行名称
     */
    private String bank = "";

    /**
     * 银行产品编码
     */
    private String bankProductCode = "";

    /**
     * OA品名
     */
    private String name = "";

    /**
     * OA产品CODE
     */
    private String code = "";

    /**
     * 银行产品条码
     */
    private String bankProductBarcode = "";

    /**
     * 银行产品名称
     */
    private String bankProductName = "";

    /**
     * 特殊属性
     */
    private String properties = "";

    /**
     * 克重
     */
    private String weight="";

    /**
     * 材质
     */
    private String material = "";

    /**
     * 零售价
     */
    private double retailPrice = 0.0d;

    /**
     * 供货价
     */
    private double costPrice = 0.0d;

    /**
     * 中收
     */
    private double ibMoney = 0.0d;

    /**
     * 激励
     */
    private double motivationMoney = 0.0d;

    /**
     * 可支配毛利
     */
    private double grossProfit = 0.0d;

    /**
     * 是否回购
     */
    private int buyBack = -1;

    /**
     * 调价或上市时间
     */
    private String onMarketDate = "";

    /**
     * 下线时间
     */
    private String offlineDate = "";

    /**
     * 分行范围
     */
    private String branchRange = "";

    /**
     * 税率
     */
    private double taxRate =0.0d;

    /**
     * 发票类型
     */
    private String invoiceType = "";

    /**
     * 可开发票内容
     */
    private String invoiceContent = "";

    private double cash =0.0d;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankProductCode() {
        return bankProductCode;
    }

    public void setBankProductCode(String bankProductCode) {
        this.bankProductCode = bankProductCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankProductBarcode() {
        return bankProductBarcode;
    }

    public void setBankProductBarcode(String bankProductBarcode) {
        this.bankProductBarcode = bankProductBarcode;
    }

    public String getBankProductName() {
        return bankProductName;
    }

    public void setBankProductName(String bankProductName) {
        this.bankProductName = bankProductName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
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

    public double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public int getBuyBack() {
        return buyBack;
    }

    public void setBuyBack(int buyBack) {
        this.buyBack = buyBack;
    }

    public String getOnMarketDate() {
        return onMarketDate;
    }

    public void setOnMarketDate(String onMarketDate) {
        this.onMarketDate = onMarketDate;
    }

    public String getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(String offlineDate) {
        this.offlineDate = offlineDate;
    }

    public String getBranchRange() {
        return branchRange;
    }

    public void setBranchRange(String branchRange) {
        this.branchRange = branchRange;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "ProductImportBean{" +
                "id='" + id + '\'' +
                ", bank='" + bank + '\'' +
                ", bankProductCode='" + bankProductCode + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", bankProductBarcode='" + bankProductBarcode + '\'' +
                ", bankProductName='" + bankProductName + '\'' +
                ", weight=" + weight +
                ", material='" + material + '\'' +
                ", retailPrice=" + retailPrice +
                ", costPrice=" + costPrice +
                ", ibMoney=" + ibMoney +
                ", motivationMoney=" + motivationMoney +
                ", grossProfit=" + grossProfit +
                ", buyBack=" + buyBack +
                ", onMarketDate='" + onMarketDate + '\'' +
                ", offlineDate='" + offlineDate + '\'' +
                ", branchRange='" + branchRange + '\'' +
                ", taxRate='" + taxRate + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                '}';
    }
}
