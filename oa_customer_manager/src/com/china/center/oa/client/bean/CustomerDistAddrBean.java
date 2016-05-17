package com.china.center.oa.client.bean;

import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.customer.bean.ExpressBean;

@SuppressWarnings("serial")
@Entity(name = "客户配送地址信息", inherit = true)
@Table(name = "T_CENTER_VS_CUSTADDR")
public class CustomerDistAddrBean extends AbstractCustomerDistAddrBean
{
	@Id
	private String id = "";

	@FK
	private String customerId = "";

    private int shipping = -1;

	/**
	 * 运输方式1
	 */
	@Join(tagClass = ExpressBean.class, type = JoinType.LEFT, alias = "e1")
	private int transport1 = 0;

	/**
	 * 运输方式2
	 */
	@Join(tagClass = ExpressBean.class, type = JoinType.LEFT, alias = "e2")
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
	 * 标注是新增还是删除的
	 */
	@Ignore
	protected int addOrDel = 0;
	
	@Ignore
	private CustomerDistAddrBean self = null;
	
	/**
	 * 
	 */
	public CustomerDistAddrBean()
	{
	}
	
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * @return the self
	 */
	public CustomerDistAddrBean getSelf()
	{
		return self;
	}

	/**
	 * @param self the self to set
	 */
	public void setSelf(CustomerDistAddrBean self)
	{
		this.self = self;
	}

	/**
	 * @return the addOrDel
	 */
	public int getAddOrDel()
	{
		return addOrDel;
	}

	/**
	 * @param addOrDel the addOrDel to set
	 */
	public void setAddOrDel(int addOrDel)
	{
		this.addOrDel = addOrDel;
	}

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

	public int getTransport1() {
		return transport1;
	}

	public void setTransport1(int transport1) {
		this.transport1 = transport1;
	}

	public int getTransport2() {
		return transport2;
	}

	public void setTransport2(int transport2) {
		this.transport2 = transport2;
	}

	public int getExpressPay() {
		return expressPay;
	}

	public void setExpressPay(int expressPay) {
		this.expressPay = expressPay;
	}

	public int getTransportPay() {
		return transportPay;
	}

	public void setTransportPay(int transportPay) {
		this.transportPay = transportPay;
	}
}
