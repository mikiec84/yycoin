package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/**
 *  #268
 * 浦发银行销售单接口-通过邮件附件下载
 *
 * @author  2016-07-09
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_pf_order")
public class PfOrderBean implements Serializable{
	@Id(autoIncrement = true)
	private String id = "";

    /**
     *  邮件标题+时间
     */
    private String mailId = "";

	/**
	 * 状态位，默认为0，如果成功创建SO单，就把状态位更新为1
	 */
	private int status = 0;


	/**
	 * 交易日期
	 */
	private String dealDate = "";

    /**
     * 交易时间
     */
    private String dealTime = "";



	/**
	 * 所属分行
	 */
	private String branchName = "";

	/**
	 * 网点名称
	 */
	private String comunicatonBranchName = "";

    /**
     * 配货机构
     */
    private String shippingOrg = "";


	/**
	 * 产品编号
	 */
	private String productCode = "";

	/**
	 * 产品名称
	 */
	private String productName = "";


	/**
	 * 交易数量
	 */
	private int amount = 0;

	/**
	 * 交易价格
	 */
	private double price = 0.0d;

	/**
	 * 交易金额
	 */
	private double value = 0.0d;

    /**
     * 到货日
     */
    private String arrivalDate = "";


	/**
	 * 销售流水(唯一)
	 */
	private String citicNo = "";

    /**
     * 方式
     */
    private String method = "";

    /**
     * POS付款方
     */
    private String pos = "";

    /**
     * 备注
     */
    private String description = "";



	/**
	 * 时间
	 */
	private String logTime = "";


	/**
	 *
	 */
	public PfOrderBean()
	{
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getComunicatonBranchName() {
        return comunicatonBranchName;
    }

    public void setComunicatonBranchName(String comunicatonBranchName) {
        this.comunicatonBranchName = comunicatonBranchName;
    }

    public String getShippingOrg() {
        return shippingOrg;
    }

    public void setShippingOrg(String shippingOrg) {
        this.shippingOrg = shippingOrg;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCiticNo() {
        return citicNo;
    }

    public void setCiticNo(String citicNo) {
        this.citicNo = citicNo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "PfOrderBean{" +
                "id='" + id + '\'' +
                ", mailId='" + mailId + '\'' +
                ", status=" + status +
                ", dealDate='" + dealDate + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", branchName='" + branchName + '\'' +
                ", comunicatonBranchName='" + comunicatonBranchName + '\'' +
                ", shippingOrg='" + shippingOrg + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", value=" + value +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", citicNo='" + citicNo + '\'' +
                ", method='" + method + '\'' +
                ", pos='" + pos + '\'' +
                ", description='" + description + '\'' +
                ", logTime='" + logTime + '\'' +
                '}';
    }
}
