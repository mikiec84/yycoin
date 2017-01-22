package com.china.center.oa.sail.bean;

import java.io.Serializable;

import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;

/**
 * 
 * 中信银行销售单接口-中间表
 *
 * @author  2013-4-16
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_out_import")
public class OutImportBean implements Serializable
{
	private int id = 0;
	
	@Id(autoIncrement = true)
	private String fullId = "";
	
	@FK
	private String batchId = "";
	
	private int type = 0;
	
	private String customerId = "";

    /**
     * 2015/9/29 导入时新增"客户姓名"
     */
    private String customerName = "";
	
	/**
	 * 分行
	 */
	private String branchName = "";
	
	private String secondBranch = "";
	
	private String comunicationBranch = "";

	/**
	 * #409 新增"联行网点"栏位，comunicationBranch这个栏位有点问题，不能覆盖掉
	 */
	private String lhwd = "";
	
	/** 网点为客户 */
	private String comunicatonBranchName = "";
	
	private String productId = "";
	
	private String productCode = "";
	
	private String productName = "";
	
	private String firstName = "";
	
	private int amount = 0;
	
	private double price = 0.0d;
	
	private String style = "";
	
	private double value = 0.0d;
	
	/**
	 * 中收金额
	 */
	private double midValue = 0.0d;
	
	/**
	 * 计划到货日期
	 */
	private String arriveDate = "";
	
	private int storageType = 0;
	
	/**
	 * 中信单号
	 */
	private String citicNo = "";
	
	private int invoiceNature = 0;
	
	private String invoiceHead  = "";
	
	private String invoiceCondition = "";
	
	private String bindNo = "";
	
	private int invoiceType = 0;
	
	private String invoiceName = "";
	
	private double invoiceMoney = 0.0;
	
	private String provinceId = "";
	
	private String cityId = "";
	
	private String address = "";
	
	private String receiver = "";
	
	private String handPhone = "";
	
	/**
	 * 状态 0:初始状态 1：处理中 2：成功 3：异常
	 */
	private int status = 0;

	/**
	 * #222 导入失败原因
	 */
	private String result = "成功";

    /**
     * #222 邮件下载标志：0 非邮件，1 邮件下载
     */
    private int importFromMail = 0;
	
	/**
	 * 关联OA的单号
	 */
	@FK(index = AnoConstant.FK_FIRST)
	@Join(tagClass = OutBean.class, type = JoinType.LEFT)
	private String OANo = "";

	/**
	 * 2015/12/21 宁波银行订单编号
	 */
	private String nbyhNo = "";
	
	/**
	 * 异常原因
	 */
	private String reason = "";
	
	/**
	 * 时间
	 */
	private String logTime = "";
	
	/**
	 * 中信订单日期  yyyy-MM-dd
	 */
	private String citicOrderDate = "";
	
	/**
	 * 类型 0 中信 1 普通 2 招行
	 */
	private int itype = 0;
	
	/**
	 * 订单类型
	 */
	private int outType = -1;
	
	/**
	 * 仓库
	 */
	private String depotId = "";
	
	/**
	 * 仓区
	 */
	private String depotpartId = "";
	
	private String stafferId = "";
	
	private String description = "";
	
	private int shipping = -1;
	
	private int transport1 = 0;
	
	/**
	 * 运输方式2
	 */
	private int transport2 = 0;
	
    /**
     * 快递运费支付方式 - deliverPay
     */
    private int expressPay = -1;
    
    /**
     * 货运运费支付方式 - deliverPay
     */
    private int transportPay = -1;
    
    /**
     * 0：未预占， 1：已预占
     */
    private int preUse = 0;
    
    private int reday = 0;
    
    /**
     * 赠送类型
     */
    private int presentFlag = 0;

    /**2015/04/11 中收激励功能
     * 中收金额
     */
    private double ibMoney = 0.0d;

    /**2015/04/11 中收激励功能
     * 激励金额
     */
    private double motivationMoney = 0.0d;
    
    @Ignore
    private int mayAmount = 0;

	/**
	 * 
	 */
	public OutImportBean()
	{
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getBatchId()
	{
		return batchId;
	}

	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getBranchName()
	{
		return branchName;
	}

	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}

	public String getSecondBranch()
	{
		return secondBranch;
	}

	public void setSecondBranch(String secondBranch)
	{
		this.secondBranch = secondBranch;
	}

	public String getComunicationBranch()
	{
		return comunicationBranch;
	}

	public void setComunicationBranch(String comunicationBranch)
	{
		this.comunicationBranch = comunicationBranch;
	}

	public String getComunicatonBranchName()
	{
		return comunicatonBranchName;
	}

	public void setComunicatonBranchName(String comunicatonBranchName)
	{
		this.comunicatonBranchName = comunicatonBranchName;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public double getMidValue()
	{
		return midValue;
	}

	public void setMidValue(double midValue)
	{
		this.midValue = midValue;
	}

	public String getArriveDate()
	{
		return arriveDate;
	}

	public void setArriveDate(String arriveDate)
	{
		this.arriveDate = arriveDate;
	}

	public int getStorageType()
	{
		return storageType;
	}

	public void setStorageType(int storageType)
	{
		this.storageType = storageType;
	}

	public String getCiticNo()
	{
		return citicNo;
	}

	public void setCiticNo(String citicNo)
	{
		this.citicNo = citicNo;
	}

	public int getInvoiceNature()
	{
		return invoiceNature;
	}

	public void setInvoiceNature(int invoiceNature)
	{
		this.invoiceNature = invoiceNature;
	}

	public String getInvoiceHead()
	{
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead)
	{
		this.invoiceHead = invoiceHead;
	}

	public String getInvoiceCondition()
	{
		return invoiceCondition;
	}

	public void setInvoiceCondition(String invoiceCondition)
	{
		this.invoiceCondition = invoiceCondition;
	}

	public String getBindNo()
	{
		return bindNo;
	}

	public void setBindNo(String bindNo)
	{
		this.bindNo = bindNo;
	}

	public int getInvoiceType()
	{
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType)
	{
		this.invoiceType = invoiceType;
	}

	public String getInvoiceName()
	{
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName)
	{
		this.invoiceName = invoiceName;
	}

	public double getInvoiceMoney()
	{
		return invoiceMoney;
	}

	public void setInvoiceMoney(double invoiceMoney)
	{
		this.invoiceMoney = invoiceMoney;
	}

	public String getProvinceId()
	{
		return provinceId;
	}

	public void setProvinceId(String provinceId)
	{
		this.provinceId = provinceId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}

	public String getHandPhone()
	{
		return handPhone;
	}

	public void setHandPhone(String handPhone)
	{
		this.handPhone = handPhone;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getOANo()
	{
		return OANo;
	}

	public void setOANo(String oANo)
	{
		OANo = oANo;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public String getFullId()
	{
		return fullId;
	}

	public void setFullId(String fullId)
	{
		this.fullId = fullId;
	}

	public String getLogTime()
	{
		return logTime;
	}

	public void setLogTime(String logTime)
	{
		this.logTime = logTime;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getCiticOrderDate()
	{
		return citicOrderDate;
	}

	public void setCiticOrderDate(String citicOrderDate)
	{
		this.citicOrderDate = citicOrderDate;
	}

	/**
	 * @return the shipping
	 */
	public int getShipping()
	{
		return shipping;
	}

	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(int shipping)
	{
		this.shipping = shipping;
	}

	/**
	 * @return the transport1
	 */
	public int getTransport1()
	{
		return transport1;
	}

	/**
	 * @param transport1 the transport1 to set
	 */
	public void setTransport1(int transport1)
	{
		this.transport1 = transport1;
	}

	/**
	 * @return the transport2
	 */
	public int getTransport2()
	{
		return transport2;
	}

	/**
	 * @param transport2 the transport2 to set
	 */
	public void setTransport2(int transport2)
	{
		this.transport2 = transport2;
	}

	/**
	 * @return the expressPay
	 */
	public int getExpressPay()
	{
		return expressPay;
	}

	/**
	 * @param expressPay the expressPay to set
	 */
	public void setExpressPay(int expressPay)
	{
		this.expressPay = expressPay;
	}

	/**
	 * @return the transportPay
	 */
	public int getTransportPay()
	{
		return transportPay;
	}

	/**
	 * @param transportPay the transportPay to set
	 */
	public void setTransportPay(int transportPay)
	{
		this.transportPay = transportPay;
	}

	/**
	 * @return the itype
	 */
	public int getItype()
	{
		return itype;
	}

	/**
	 * @param itype the itype to set
	 */
	public void setItype(int itype)
	{
		this.itype = itype;
	}

	public int getOutType()
	{
		return outType;
	}

	public void setOutType(int outType)
	{
		this.outType = outType;
	}

	public String getDepotId()
	{
		return depotId;
	}

	public void setDepotId(String depotId)
	{
		this.depotId = depotId;
	}

	public String getDepotpartId()
	{
		return depotpartId;
	}

	public void setDepotpartId(String depotpartId)
	{
		this.depotpartId = depotpartId;
	}

	public String getStafferId()
	{
		return stafferId;
	}

	public void setStafferId(String stafferId)
	{
		this.stafferId = stafferId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the preUse
	 */
	public int getPreUse()
	{
		return preUse;
	}

	/**
	 * @param preUse the preUse to set
	 */
	public void setPreUse(int preUse)
	{
		this.preUse = preUse;
	}

	/**
	 * @return the mayAmount
	 */
	public int getMayAmount()
	{
		return mayAmount;
	}

	/**
	 * @param mayAmount the mayAmount to set
	 */
	public void setMayAmount(int mayAmount)
	{
		this.mayAmount = mayAmount;
	}

	/**
	 * @return the reday
	 */
	public int getReday()
	{
		return reday;
	}

	/**
	 * @param reday the reday to set
	 */
	public void setReday(int reday)
	{
		this.reday = reday;
	}

	/**
	 * @return the presentFlag
	 */
	public int getPresentFlag() {
		return presentFlag;
	}

	/**
	 * @param presentFlag the presentFlag to set
	 */
	public void setPresentFlag(int presentFlag) {
		this.presentFlag = presentFlag;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

	public String getNbyhNo() {
		return nbyhNo;
	}

	public void setNbyhNo(String nbyhNo) {
		this.nbyhNo = nbyhNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

    public int getImportFromMail() {
        return importFromMail;
    }

    public void setImportFromMail(int importFromMail) {
        this.importFromMail = importFromMail;
    }

	public String getLhwd() {
		return lhwd;
	}

	public void setLhwd(String lhwd) {
		this.lhwd = lhwd;
	}

	@Override
    public String toString() {
        return "OutImportBean{" +
                "id=" + id +
                ", fullId='" + fullId + '\'' +
                ", batchId='" + batchId + '\'' +
                ", type=" + type +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", secondBranch='" + secondBranch + '\'' +
                ", comunicationBranch='" + comunicationBranch + '\'' +
                ", comunicatonBranchName='" + comunicatonBranchName + '\'' +
                ", productId='" + productId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", style='" + style + '\'' +
                ", value=" + value +
                ", midValue=" + midValue +
                ", arriveDate='" + arriveDate + '\'' +
                ", storageType=" + storageType +
                ", citicNo='" + citicNo + '\'' +
                ", invoiceNature=" + invoiceNature +
                ", invoiceHead='" + invoiceHead + '\'' +
                ", invoiceCondition='" + invoiceCondition + '\'' +
                ", bindNo='" + bindNo + '\'' +
                ", invoiceType=" + invoiceType +
                ", invoiceName='" + invoiceName + '\'' +
                ", invoiceMoney=" + invoiceMoney +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", address='" + address + '\'' +
                ", receiver='" + receiver + '\'' +
                ", handPhone='" + handPhone + '\'' +
                ", status=" + status +
                ", OANo='" + OANo + '\'' +
                ", nbyhNo='" + nbyhNo + '\'' +
                ", reason='" + reason + '\'' +
                ", logTime='" + logTime + '\'' +
                ", citicOrderDate='" + citicOrderDate + '\'' +
                ", itype=" + itype +
                ", outType=" + outType +
                ", depotId='" + depotId + '\'' +
                ", depotpartId='" + depotpartId + '\'' +
                ", stafferId='" + stafferId + '\'' +
                ", description='" + description + '\'' +
                ", shipping=" + shipping +
                ", transport1=" + transport1 +
                ", transport2=" + transport2 +
                ", expressPay=" + expressPay +
                ", transportPay=" + transportPay +
                ", preUse=" + preUse +
                ", reday=" + reday +
                ", presentFlag=" + presentFlag +
                ", ibMoney=" + ibMoney +
                ", motivationMoney=" + motivationMoney +
                ", mayAmount=" + mayAmount +
                '}';
    }
}
