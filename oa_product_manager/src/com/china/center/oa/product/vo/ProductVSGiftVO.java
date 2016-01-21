package com.china.center.oa.product.vo;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.product.bean.ProductVSGiftBean;

@SuppressWarnings("serial")
@Entity(inherit = true)
public class ProductVSGiftVO extends ProductVSGiftBean
{
	@Relationship(relationField = "productId")
	private String productName = "";
	
	@Relationship(relationField = "giftProductId")
	private String giftProductName = "";

	@Relationship(relationField = "giftProductId2")
	private String giftProductName2 = "";

	@Relationship(relationField = "giftProductId3")
	private String giftProductName3 = "";

	public ProductVSGiftVO()
	{
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getGiftProductName()
	{
		return giftProductName;
	}

	public void setGiftProductName(String giftProductName)
	{
		this.giftProductName = giftProductName;
	}

	public String getGiftProductName2() {
		return giftProductName2;
	}

	public void setGiftProductName2(String giftProductName2) {
		this.giftProductName2 = giftProductName2;
	}

	public String getGiftProductName3() {
		return giftProductName3;
	}

	public void setGiftProductName3(String giftProductName3) {
		this.giftProductName3 = giftProductName3;
	}
}
