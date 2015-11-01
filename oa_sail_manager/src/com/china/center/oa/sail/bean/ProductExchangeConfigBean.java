/**
 * File Name: SailTranApply.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2012-5-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.product.bean.ProductBean;
import java.io.Serializable;


/**
 * ProductExchangeConfigBean
 * 
 * @author simon
 * @version 2015-10-30
 * @see com.china.center.oa.sail.bean.ProductExchangeConfigBean
 * @since 3.0
 */
@Entity(name = "商品转换配置")
@Table(name = "T_CENTER_PRODUCT_EXCHANGE")
public class ProductExchangeConfigBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = ProductBean.class, type = JoinType.LEFT, alias = "P1")
    @Html(name = "srcProductName", title = "商品名", must = true)
    private String srcProductId;

    @Html(title = "商品数量", must = true, type = Element.NUMBER)
    private int srcAmount;


    @Join(tagClass = ProductBean.class, type = JoinType.LEFT, alias = "P2")
    @Html(name = "destProductName", title = "发货商品名", must = true)
    private String destProductId;

    @Html(title = "发货商品数量", must = true, type = Element.NUMBER)
    private int destAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getSrcAmount() {
        return srcAmount;
    }

    public void setSrcAmount(int srcAmount) {
        this.srcAmount = srcAmount;
    }

    public String getSrcProductId() {
        return srcProductId;
    }

    public void setSrcProductId(String srcProductId) {
        this.srcProductId = srcProductId;
    }

    public String getDestProductId() {
        return destProductId;
    }

    public void setDestProductId(String destProductId) {
        this.destProductId = destProductId;
    }

    public int getDestAmount() {
        return destAmount;
    }

    public void setDestAmount(int destAmount) {
        this.destAmount = destAmount;
    }

    @Override
    public String toString() {
        return "ProductExchangeConfigBean{" +
                "id='" + id + '\'' +
                ", srcProductId=" + srcProductId +
                ", srcAmount=" + srcAmount +
                ", destProductId=" + destProductId +
                ", destAmount=" + destAmount +
                '}';
    }
}
