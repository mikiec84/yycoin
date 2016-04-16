package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;

import java.io.Serializable;

/**
 * 
 * 中信银行销售单接口-通过邮件附件下载
 *
 * @author  2016-04-14
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_citic_order")
public class CiticOrderBean implements Serializable{
	@Id(autoIncrement = true)
	private String id = "";

	/**
	 * 状态位，默认为0，如果成功创建SO单，就把状态位更新为1
	 */
	private int status = 0;

	/**
	 * 客户号
	 */
	private String customerId = "";

    /**
     * 客户姓名
     */
    private String customerName = "";

	/**
	 * 证件号码
	 */
	private String idCard = "";

	/**
	 * 买卖时间
	 */
	private String dealDate = "";

	/**
	 * 提货时间
	 */
	private String pickupDate = "";

	/**
	 * 提货标志
	 */
	private int pickupFlag = 0;

	/**
	 * 提货柜员号
	 */
	private String tellerId = "";


	/**
	 * 提货网点号
	 */
	private String pickupNode = "";

	/**
	 * 提货网点地址
	 */
	private String pickupAddress = "";

	/**
	 * 购买分行号
	 */
	private String branchId = "";
	/**
	 * 购买分行名称
	 */
	private String branchName = "";

	/**
	 * 二级分行名称
	 */
	private String secondBranch = "";

	/**
	 * 联行网点号
	 */
	private String comunicationBranch = "";

	/**
	 * 网点名称
	 */
	private String comunicatonBranchName = "";

	private String productId = "";

	/**
	 * 商品编码
	 */
	private String productCode = "";

	/**
	 * 企业产品编码
	 */
	private String enterpriseProductCode = "";

	/**
	 * 商品名称
	 */
	private String productName = "";


	/**
	 * 数量
	 */
	private int amount = 0;

	/**
	 * 单价
	 */
	private double price = 0.0d;

	/**
	 * 产品克重
	 */
	private double productWeight = 0.0d;

	/**
	 * 金额
	 */
	private double value = 0.0d;

	/**
	 * 手续费
	 */
	private double fee = 0.0d;

	/**
	 * 计划交付日期
	 */
	private String arriveDate = "";

	/**
	 * 订铺货标志
	 */
	private String orderOrShow = "";

	/**
	 * 是否现货
	 */
	private int spotFlag = 0;

	/**
	 * 中信订单号(唯一)
	 */
	private String citicNo = "";

	/**
	 * 开票性质
	 */
	private String invoiceNature = "";

	/**
	 * 开票抬头
	 */
	private String invoiceHead  = "";

	/**
	 * 开票要求
	 */
	private String invoiceCondition = "";

	/**
	 * 客户经理
	 */
	private String managerId = "";

	/**
	 * 客户经理姓名
	 */
	private String manager = "";

	/**
	 * 发起方标志
	 */
	private String originator = "";

	private String provinceId = "";

	private String provinceName = "";

	private String cityId = "";

	private String city = "";

	/**
	 * 客户地址
	 */
	private String address = "";

	/**
	 * 分行收货人
	 */
	private String receiver = "";

	/**
	 * 分行收货人手机
	 */
	private String receiverMobile = "";

	/**
	 * 联系电话
	 */
	private String handPhone = "";

	/**
	 * 重量
	 */
	private double weight = 0.0d;

	/**
	 * 基础金价
	 */
	private double goldPrice = 0.0d;

	/**
	 * 金银标志
	 */
	private String materialType = "";

	/**
	 * 产品属性
	 */
	private String productType = "";

	/**
	 * 取货方式
	 */
	private String pickupType = "";

	/**
	 * 操作柜员
	 */
	private String teller = "";

	/**
	 * 时间
	 */
	private String logTime = "";

	/**
	 * 购买日期  yyyy-MM-dd
	 */
	private String citicOrderDate = "";

	/**
	 * 贵金属企业名称
	 */
	private String enterpriseName = "";


	/**
	 *
	 */
	public CiticOrderBean()
	{
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getTellerId() {
		return tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSecondBranch() {
		return secondBranch;
	}

	public void setSecondBranch(String secondBranch) {
		this.secondBranch = secondBranch;
	}

	public String getComunicationBranch() {
		return comunicationBranch;
	}

	public void setComunicationBranch(String comunicationBranch) {
		this.comunicationBranch = comunicationBranch;
	}

	public String getComunicatonBranchName() {
		return comunicatonBranchName;
	}

	public void setComunicatonBranchName(String comunicatonBranchName) {
		this.comunicatonBranchName = comunicatonBranchName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getEnterpriseProductCode() {
		return enterpriseProductCode;
	}

	public void setEnterpriseProductCode(String enterpriseProductCode) {
		this.enterpriseProductCode = enterpriseProductCode;
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

	public double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(double productWeight) {
		this.productWeight = productWeight;
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

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getOrderOrShow() {
		return orderOrShow;
	}

	public void setOrderOrShow(String orderOrShow) {
		this.orderOrShow = orderOrShow;
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

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getHandPhone() {
		return handPhone;
	}

	public void setHandPhone(String handPhone) {
		this.handPhone = handPhone;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(double goldPrice) {
		this.goldPrice = goldPrice;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPickupType() {
		return pickupType;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public String getTeller() {
		return teller;
	}

	public void setTeller(String teller) {
		this.teller = teller;
	}


	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public String getCiticOrderDate() {
		return citicOrderDate;
	}

	public void setCiticOrderDate(String citicOrderDate) {
		this.citicOrderDate = citicOrderDate;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

    public int getPickupFlag() {
        return pickupFlag;
    }

    public void setPickupFlag(int pickupFlag) {
        this.pickupFlag = pickupFlag;
    }

    public int getSpotFlag() {
        return spotFlag;
    }

    public void setSpotFlag(int spotFlag) {
        this.spotFlag = spotFlag;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPickupNode() {
		return pickupNode;
	}

	public void setPickupNode(String pickupNode) {
		this.pickupNode = pickupNode;
	}

	@Override
	public String toString() {
		return "CiticOrderBean{" +
				"id='" + id + '\'' +
				", status=" + status +
				", customerId='" + customerId + '\'' +
				", customerName='" + customerName + '\'' +
				", idCard='" + idCard + '\'' +
				", dealDate='" + dealDate + '\'' +
				", pickupDate='" + pickupDate + '\'' +
				", pickupFlag=" + pickupFlag +
				", tellerId='" + tellerId + '\'' +
				", pickupNode='" + pickupNode + '\'' +
				", pickupAddress='" + pickupAddress + '\'' +
				", branchId='" + branchId + '\'' +
				", branchName='" + branchName + '\'' +
				", secondBranch='" + secondBranch + '\'' +
				", comunicationBranch='" + comunicationBranch + '\'' +
				", comunicatonBranchName='" + comunicatonBranchName + '\'' +
				", productId='" + productId + '\'' +
				", productCode='" + productCode + '\'' +
				", enterpriseProductCode='" + enterpriseProductCode + '\'' +
				", productName='" + productName + '\'' +
				", amount=" + amount +
				", price=" + price +
				", productWeight=" + productWeight +
				", value=" + value +
				", fee=" + fee +
				", arriveDate='" + arriveDate + '\'' +
				", orderOrShow='" + orderOrShow + '\'' +
				", spotFlag=" + spotFlag +
				", citicNo='" + citicNo + '\'' +
				", invoiceNature='" + invoiceNature + '\'' +
				", invoiceHead='" + invoiceHead + '\'' +
				", invoiceCondition='" + invoiceCondition + '\'' +
				", managerId='" + managerId + '\'' +
				", manager='" + manager + '\'' +
				", originator='" + originator + '\'' +
				", provinceId='" + provinceId + '\'' +
				", provinceName='" + provinceName + '\'' +
				", cityId='" + cityId + '\'' +
				", city='" + city + '\'' +
				", address='" + address + '\'' +
				", receiver='" + receiver + '\'' +
				", receiverMobile='" + receiverMobile + '\'' +
				", handPhone='" + handPhone + '\'' +
				", weight=" + weight +
				", goldPrice=" + goldPrice +
				", materialType='" + materialType + '\'' +
				", productType='" + productType + '\'' +
				", pickupType='" + pickupType + '\'' +
				", teller='" + teller + '\'' +
				", logTime='" + logTime + '\'' +
				", citicOrderDate='" + citicOrderDate + '\'' +
				", enterpriseName='" + enterpriseName + '\'' +
				'}';
	}
}
