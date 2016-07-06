package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/**
 *  #269
 * 招商银行销售单接口-通过邮件附件下载
 *
 * @author  2016-07-05
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_zs_order")
public class ZsOrderBean implements Serializable{
	@Id(autoIncrement = true)
	private String id = "";

    /**
     * 订单序号
     */
    private String sn = "";

    /**
     *  邮件标题+时间
     */
    private String mailId = "";

	/**
	 * 状态位，默认为0，如果成功创建SO单，就把状态位更新为1
	 */
	private int status = 0;

    /**
     * 订单状态
     */
    private String orderStatus = "";


	/**
	 * 交易日期
	 */
	private String dealDate = "";

    /**
     * 交易时间
     */
    private String dealTime = "";

    /**
     * 交易户名
     */
    private String account = "";

    /**
     * 供应商编码
     */
    private String providerId = "";


	/**
	 * 领取机构
	 */
	private String pickupNode = "";


	/**
	 * 交易分行
	 */
	private String branchName = "";

	/**
	 * 网点名称
	 */
	private String comunicatonBranchName = "";

    /**
     * 库存控制类型
     */
    private String storageControlType = "";

	/**
	 * 产品编码
	 */
	private String productCode = "";

	/**
	 * 产品名称
	 */
	private String productName = "";

    /**
     * 产品规格
     */
    private String productSpec = "";


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
	 * 手续费
	 */
	private double fee = 0.0d;


	/**
	 * 订单号码(唯一)
	 */
	private String citicNo = "";

	/**
	 * 开票方式
	 */
	private String invoiceNature = "";

	/**
	 * 开票抬头
	 */
	private String invoiceHead  = "";

	/**
	 * 开票备注
	 */
	private String invoiceCondition = "";

	/**
	 * 产品类型
	 */
	private String materialType = "";


	/**
	 * 时间
	 */
	private String logTime = "";


	/**
	 *
	 */
	public ZsOrderBean()
	{
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPickupNode() {
        return pickupNode;
    }

    public void setPickupNode(String pickupNode) {
        this.pickupNode = pickupNode;
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

    public String getStorageControlType() {
        return storageControlType;
    }

    public void setStorageControlType(String storageControlType) {
        this.storageControlType = storageControlType;
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

    public String getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getCiticNo() {
        return citicNo;
    }

    public void setCiticNo(String citicNo) {
        this.citicNo = citicNo;
    }

    public String getInvoiceNature() {
        return invoiceNature;
    }

    public void setInvoiceNature(String invoiceNature) {
        this.invoiceNature = invoiceNature;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getInvoiceCondition() {
        return invoiceCondition;
    }

    public void setInvoiceCondition(String invoiceCondition) {
        this.invoiceCondition = invoiceCondition;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "ZsOrderBean{" +
                "id='" + id + '\'' +
                ", mailId='" + mailId + '\'' +
                ", status=" + status +
                ", orderStatus='" + orderStatus + '\'' +
                ", dealDate='" + dealDate + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", account='" + account + '\'' +
                ", providerId='" + providerId + '\'' +
                ", pickupNode='" + pickupNode + '\'' +
                ", branchName='" + branchName + '\'' +
                ", comunicatonBranchName='" + comunicatonBranchName + '\'' +
                ", storageControlType='" + storageControlType + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", productSpec='" + productSpec + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", value=" + value +
                ", fee=" + fee +
                ", citicNo='" + citicNo + '\'' +
                ", invoiceNature='" + invoiceNature + '\'' +
                ", invoiceHead='" + invoiceHead + '\'' +
                ", invoiceCondition='" + invoiceCondition + '\'' +
                ", materialType='" + materialType + '\'' +
                ", logTime='" + logTime + '\'' +
                '}';
    }
}
