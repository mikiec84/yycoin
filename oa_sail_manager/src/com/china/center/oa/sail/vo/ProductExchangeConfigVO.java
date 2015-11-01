/**
 * File Name: SailConfigVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-12-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;


/**
 * ProductExchangeConfigVO
 * 
 * @author simon
 * @version 2015-10-30
 * @see com.china.center.oa.sail.vo.ProductExchangeConfigVO
 * @since 3.0
 */
@Entity(inherit = true)
public class ProductExchangeConfigVO extends ProductExchangeConfigBean
{
    @Relationship(relationField = "srcProductId")
    private String srcProductName = "";

    @Relationship(relationField = "destProductId")
    private String destProductName = "";

    public String getSrcProductName() {
        return srcProductName;
    }

    public void setSrcProductName(String srcProductName) {
        this.srcProductName = srcProductName;
    }

    public String getDestProductName() {
        return destProductName;
    }

    public void setDestProductName(String destProductName) {
        this.destProductName = destProductName;
    }

    @Override
    public String toString() {
        return super.toString()+" ProductExchangeConfigVO{" +
                "srcProductName='" + srcProductName + '\'' +
                ", destProductName='" + destProductName + '\'' +
                '}';
    }
}
