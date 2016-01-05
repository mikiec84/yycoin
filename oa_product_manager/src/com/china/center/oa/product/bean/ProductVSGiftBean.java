package com.china.center.oa.product.bean;

import java.io.Serializable;

import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;

@SuppressWarnings("serial")
@Entity(name = "中信产品对应赠品")
@Table(name = "T_CENTER_VS_GIFT")
public class ProductVSGiftBean implements Serializable
{
	@Id(autoIncrement = true)
	private String id = "";
	
	@FK
    @Html(title = "销售商品品名", type = Element.INPUT, name = "productName", readonly = true)
	@Join(tagClass = ProductBean.class, type = JoinType.LEFT, alias = "P1")
	private String productId = "";

    @Html(title = "赠送商品品名", type = Element.INPUT, name = "giftProductName", readonly = true)
	@Join(tagClass = ProductBean.class, type = JoinType.LEFT, alias = "P2")
	private String giftProductId = "";

    @Html(title = "赠送商品数量", must = true, maxLength = 100)
	private int amount = 0;

    @Html(title = "销售商品数量", must = true, maxLength = 100)
    private int sailAmount = 0;

    /**
     * 活动描述
     */
    @Html(title = "活动描述", must = true, maxLength = 100)
    private String activity = "";

    /**
     * 适用银行
     */
    @Html(title = "适用银行", must = true, maxLength = 100)
    private String bank = "";

    /**
     * 开始日期
     */
    @Html(title = "开始日期", must = true, type= Element.DATE)
    private String beginDate = "";

    /**
     * 结束日期
     */
    @Html(title = "结束日期", must = true,type=Element.DATE)
    private String endDate = "";

    /**
     * 备注
     */
    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 255)
    private String description = "";

    /**
     * 2016/1/5 #156 增加事业部，大区，部门，人员，城市的选择字段
     * 多选，用分号;分割
     */


    @Html(title = "事业部", maxLength = 100)
    private String industryName = "";

    @Html(title = "大区", maxLength = 100)
    private String industryName2 = "";

    @Html(title = "部门", maxLength = 100)
    private String industryName3 = "";


    /**
     * 城市
     */
    @Html(title = "城市", maxLength = 100)
    private String city = "";

    @Html(title = "人员", maxLength = 100)
    private String stafferName = "";

	public ProductVSGiftBean()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getGiftProductId()
	{
		return giftProductId;
	}

	public void setGiftProductId(String giftProductId)
	{
		this.giftProductId = giftProductId;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getSailAmount() {
        return sailAmount;
    }

    public void setSailAmount(int sailAmount) {
        this.sailAmount = sailAmount;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryName2() {
        return industryName2;
    }

    public void setIndustryName2(String industryName2) {
        this.industryName2 = industryName2;
    }

    public String getIndustryName3() {
        return industryName3;
    }

    public void setIndustryName3(String industryName3) {
        this.industryName3 = industryName3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStafferName() {
        return stafferName;
    }

    public void setStafferName(String stafferName) {
        this.stafferName = stafferName;
    }

    @Override
    public String toString() {
        return "ProductVSGiftBean{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", giftProductId='" + giftProductId + '\'' +
                ", amount=" + amount +
                ", sailAmount=" + sailAmount +
                ", activity='" + activity + '\'' +
                ", bank='" + bank + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", description='" + description + '\'' +
                ", industryName='" + industryName + '\'' +
                ", industryName2='" + industryName2 + '\'' +
                ", industryName3='" + industryName3 + '\'' +
                ", city='" + city + '\'' +
                ", stafferName='" + stafferName + '\'' +
                '}';
    }
}
