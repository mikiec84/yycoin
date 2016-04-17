package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/**
 * 
 * 中原银行销售单接口-通过邮件附件下载
 *
 * @author  2016-04-17
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_zy_order")
public class ZyOrderBean implements Serializable{
	@Id(autoIncrement = true)
	private String id = "";

	/**
	 * 状态位，默认为0，如果成功创建SO单，就把状态位更新为1
	 */
	private int status = 0;

    /**
     * 流水号
     */
    private String serialNumber = "";

    /**
     * 渠道流水号
     */
    private String channelSerialNumber = "";

	/**
	 * 客户号
	 */
	private String customerId = "";

    /**
     * 客户姓名
     */
    private String customerName = "";

    /**
     * 证件类型
     */
    private String idType = "";

	/**
	 * 证件号码
	 */
	private String idCard = "";

    /**
     * 代理人证件类型
     */
    private String proxyIdType = "";

    /**
     * 代理人证件号码
     */
    private String proxyIdCard = "";

    /**
     * 银行帐号
     */
    private String bankAccount = "";

    /**
     * 交易渠道
     */
    private String channel = "";

    /**
     * 原交易渠道
     */
    private String originalChannel = "";

    /**
     * 终端号
     */
    private String terminal = "";

	/**
	 * 交易日期
	 */
	private String dealDate = "";

    /**
     * 交易时间
     */
    private String dealTime = "";

    /**
     * 交易代码
     */
    private String dealCode = "";

    /**
     * 交易机构
     */
    private String dealAgent = "";

    /**
     * 原交易机构
     */
    private String originalDealAgent = "";

    /**
     * 客户类型
     */
    private String customerType = "";

    /**
     * 客户组别
     */
    private String customerGroup = "";

	/**
	 * 柜员号
	 */
	private String tellerId = "";


	/**
	 * 提金网点
	 */
	private String pickupNode = "";

    /**
     * 发主机流水号
     */
    private String sendMainframeId = "";

    /**
     * 主机对账日期
     */
    private String mainframeCheckDate = "";

    /**
     * 主机交易码
     */
    private String mainframeTradeCode = "";

    /**
     * 主机日期
     */
    private String mainframeDate = "";

    /**
     * 主机流水号
     */
    private String mainframeId = "";

    /**
     * 返回码
     */
    private String returnCode = "";

    /**
     * 返回信息
     */
    private String returnMessage = "";

    /**
     * 受理方式
     */
    private String acceptMethod = "";

    /**
     * 交易状态
     */
    private String tradeStatus = "";

    /**
     * 对公帐号
     */
    private String corporateAccount = "";

    /**
     * 是否提金结束
     */
    private int finished = 0;

    /**
     * 资金处理是否异常
     */
    private int exceptional = 0;

    /**
     * 是否预约到货
     */
    private int appointmentOfArrival = 0;



	/**
	 * 产品代码
	 */
	private String productCode = "";


	/**
	 * 产品名称
	 */
	private String productName = "";

    /**
     * 规格代码
     */
    private String specCode = "";

    /**
     * 规格名称
     */
    private String specName = "";

    /**
     * 规格
     */
    private double spec = 0.0d;

    /**
     * 业务类型
     */
    private String businessType = "";

    /**
     * 关联日期
     */
    private String associateDate = "";

    /**
     * 关联流水号
     */
    private String associateId = "";


	/**
	 * 数量
	 */
	private int amount = 0;

    /**
     * 购买单位数
     */
    private int buyUnit = 0;

	/**
	 * 单价
	 */
	private double price = 0.0d;

	/**
	 * 金额
	 */
	private double value = 0.0d;

    /**
     * 币种
     */
    private String currency = "";

    /**
     * 钞汇方式
     */
    private String paymentMethod = "";

    /**
     * 剩余可提金量
     */
    private int remainAmount = 0;

    /**
     * 保管费
     */
    private double storageCost = 0.0d;

	/**
	 * 手续费
	 */
	private double fee = 0.0d;

    /**
     * 折扣率
     */
    private double discountRate = 0.0d;


	/**
	 * 发票信息
	 */
	private String invoiceHead  = "";

    /**
     * 财务状态
     */
    private String financialStatus = "";


	/**
	 * 客户经理
	 */
	private String manager = "";


	/**
	 * 购买提金方式
	 */
	private String pickupType = "";

	/**
	 * 授权柜员
	 */
	private String teller = "";

	/**
	 * 时间
	 */
	private String logTime = "";


    /**
     * 公司代码
     */
    private String enterpriseCode = "";

	/**
	 * 公司名称
	 */
	private String enterpriseName = "";


	/**
	 *
	 */
	public ZyOrderBean()
	{
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getChannelSerialNumber() {
        return channelSerialNumber;
    }

    public void setChannelSerialNumber(String channelSerialNumber) {
        this.channelSerialNumber = channelSerialNumber;
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

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getProxyIdType() {
        return proxyIdType;
    }

    public void setProxyIdType(String proxyIdType) {
        this.proxyIdType = proxyIdType;
    }

    public String getProxyIdCard() {
        return proxyIdCard;
    }

    public void setProxyIdCard(String proxyIdCard) {
        this.proxyIdCard = proxyIdCard;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOriginalChannel() {
        return originalChannel;
    }

    public void setOriginalChannel(String originalChannel) {
        this.originalChannel = originalChannel;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
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

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getDealAgent() {
        return dealAgent;
    }

    public void setDealAgent(String dealAgent) {
        this.dealAgent = dealAgent;
    }

    public String getOriginalDealAgent() {
        return originalDealAgent;
    }

    public void setOriginalDealAgent(String originalDealAgent) {
        this.originalDealAgent = originalDealAgent;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public String getTellerId() {
        return tellerId;
    }

    public void setTellerId(String tellerId) {
        this.tellerId = tellerId;
    }

    public String getPickupNode() {
        return pickupNode;
    }

    public void setPickupNode(String pickupNode) {
        this.pickupNode = pickupNode;
    }

    public String getSendMainframeId() {
        return sendMainframeId;
    }

    public void setSendMainframeId(String sendMainframeId) {
        this.sendMainframeId = sendMainframeId;
    }

    public String getMainframeCheckDate() {
        return mainframeCheckDate;
    }

    public void setMainframeCheckDate(String mainframeCheckDate) {
        this.mainframeCheckDate = mainframeCheckDate;
    }

    public String getMainframeTradeCode() {
        return mainframeTradeCode;
    }

    public void setMainframeTradeCode(String mainframeTradeCode) {
        this.mainframeTradeCode = mainframeTradeCode;
    }

    public String getMainframeDate() {
        return mainframeDate;
    }

    public void setMainframeDate(String mainframeDate) {
        this.mainframeDate = mainframeDate;
    }

    public String getMainframeId() {
        return mainframeId;
    }

    public void setMainframeId(String mainframeId) {
        this.mainframeId = mainframeId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getAcceptMethod() {
        return acceptMethod;
    }

    public void setAcceptMethod(String acceptMethod) {
        this.acceptMethod = acceptMethod;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getCorporateAccount() {
        return corporateAccount;
    }

    public void setCorporateAccount(String corporateAccount) {
        this.corporateAccount = corporateAccount;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getExceptional() {
        return exceptional;
    }

    public void setExceptional(int exceptional) {
        this.exceptional = exceptional;
    }

    public int getAppointmentOfArrival() {
        return appointmentOfArrival;
    }

    public void setAppointmentOfArrival(int appointmentOfArrival) {
        this.appointmentOfArrival = appointmentOfArrival;
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

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public double getSpec() {
        return spec;
    }

    public void setSpec(double spec) {
        this.spec = spec;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAssociateDate() {
        return associateDate;
    }

    public void setAssociateDate(String associateDate) {
        this.associateDate = associateDate;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(int buyUnit) {
        this.buyUnit = buyUnit;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(int remainAmount) {
        this.remainAmount = remainAmount;
    }

    public double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(double storageCost) {
        this.storageCost = storageCost;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getFinancialStatus() {
        return financialStatus;
    }

    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    @Override
    public String toString() {
        return "ZyOrderBean{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", serialNumber='" + serialNumber + '\'' +
                ", channelSerialNumber='" + channelSerialNumber + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", idType='" + idType + '\'' +
                ", idCard='" + idCard + '\'' +
                ", proxyIdType='" + proxyIdType + '\'' +
                ", proxyIdCard='" + proxyIdCard + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", channel='" + channel + '\'' +
                ", originalChannel='" + originalChannel + '\'' +
                ", terminal='" + terminal + '\'' +
                ", dealDate='" + dealDate + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", dealCode='" + dealCode + '\'' +
                ", dealAgent='" + dealAgent + '\'' +
                ", originalDealAgent='" + originalDealAgent + '\'' +
                ", customerType='" + customerType + '\'' +
                ", customerGroup='" + customerGroup + '\'' +
                ", tellerId='" + tellerId + '\'' +
                ", pickupNode='" + pickupNode + '\'' +
                ", sendMainframeId='" + sendMainframeId + '\'' +
                ", mainframeCheckDate='" + mainframeCheckDate + '\'' +
                ", mainframeTradeCode='" + mainframeTradeCode + '\'' +
                ", mainframeDate='" + mainframeDate + '\'' +
                ", mainframeId='" + mainframeId + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", acceptMethod='" + acceptMethod + '\'' +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", corporateAccount='" + corporateAccount + '\'' +
                ", finished=" + finished +
                ", exceptional=" + exceptional +
                ", appointmentOfArrival=" + appointmentOfArrival +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", specCode='" + specCode + '\'' +
                ", specName='" + specName + '\'' +
                ", spec=" + spec +
                ", businessType='" + businessType + '\'' +
                ", associateDate='" + associateDate + '\'' +
                ", associateId='" + associateId + '\'' +
                ", amount=" + amount +
                ", buyUnit=" + buyUnit +
                ", price=" + price +
                ", value=" + value +
                ", currency='" + currency + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", remainAmount=" + remainAmount +
                ", storageCost=" + storageCost +
                ", fee=" + fee +
                ", discountRate=" + discountRate +
                ", invoiceHead='" + invoiceHead + '\'' +
                ", financialStatus='" + financialStatus + '\'' +
                ", manager='" + manager + '\'' +
                ", pickupType='" + pickupType + '\'' +
                ", teller='" + teller + '\'' +
                ", logTime='" + logTime + '\'' +
                ", enterpriseCode='" + enterpriseCode + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                '}';
    }
}
