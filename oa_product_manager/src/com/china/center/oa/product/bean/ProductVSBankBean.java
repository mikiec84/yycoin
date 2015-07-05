package com.china.center.oa.product.bean;

import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.JoinType;

import java.io.Serializable;

@SuppressWarnings("serial")
@Entity(name = "产品品名对照表")
@Table(name = "T_CENTER_PRODUCT_VS_BANK")
public class ProductVSBankBean implements Serializable
{
	@Id(autoIncrement = true)
	private String id = "";

    /**
     * 产品编码
     */
	private String code = "";

    /**
     *  中信品名
     */
	private String citicProductName = "";

    /**
     *  招行品名
     */
    private String zhProductName = "";

    /**
     *  浦发品名
     */
    private String pufaProductName = "";

    /**
     *  紫金品名
     */
    private String zjProductName = "";

    /**
     *  广州农商品名
     */
    private String gznsProductName = "";

    /**
     *  江南农商品名
     */
    private String jnnsProductName = "";

    /**
     * 交行品名
     */
    private String jtProductName = "";

    /**
     * 预留10个栏位
     */
    private String reserve1 = "";

    private String reserve2 = "";

    private String reserve3 = "";

    private String reserve4 = "";

    private String reserve5 = "";

    private String reserve6 = "";

    private String reserve7 = "";

    private String reserve8 = "";

    private String reserve9 = "";

    private String reserve10 = "";

	public ProductVSBankBean()
	{
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCiticProductName() {
        return citicProductName;
    }

    public void setCiticProductName(String citicProductName) {
        this.citicProductName = citicProductName;
    }

    public String getZhProductName() {
        return zhProductName;
    }

    public void setZhProductName(String zhProductName) {
        this.zhProductName = zhProductName;
    }

    public String getPufaProductName() {
        return pufaProductName;
    }

    public void setPufaProductName(String pufaProductName) {
        this.pufaProductName = pufaProductName;
    }

    public String getZjProductName() {
        return zjProductName;
    }

    public void setZjProductName(String zjProductName) {
        this.zjProductName = zjProductName;
    }

    public String getGznsProductName() {
        return gznsProductName;
    }

    public void setGznsProductName(String gznsProductName) {
        this.gznsProductName = gznsProductName;
    }

    public String getJnnsProductName() {
        return jnnsProductName;
    }

    public void setJnnsProductName(String jnnsProductName) {
        this.jnnsProductName = jnnsProductName;
    }

    public String getJtProductName() {
        return jtProductName;
    }

    public void setJtProductName(String jtProductName) {
        this.jtProductName = jtProductName;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }

    public String getReserve6() {
        return reserve6;
    }

    public void setReserve6(String reserve6) {
        this.reserve6 = reserve6;
    }

    public String getReserve7() {
        return reserve7;
    }

    public void setReserve7(String reserve7) {
        this.reserve7 = reserve7;
    }

    public String getReserve8() {
        return reserve8;
    }

    public void setReserve8(String reserve8) {
        this.reserve8 = reserve8;
    }

    public String getReserve9() {
        return reserve9;
    }

    public void setReserve9(String reserve9) {
        this.reserve9 = reserve9;
    }

    public String getReserve10() {
        return reserve10;
    }

    public void setReserve10(String reserve10) {
        this.reserve10 = reserve10;
    }

    @Override
    public String toString() {
        return "ProductVSBankBean{" +
                "jtProductName='" + jtProductName + '\'' +
                ", id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", citicProductName='" + citicProductName + '\'' +
                ", zhProductName='" + zhProductName + '\'' +
                ", pufaProductName='" + pufaProductName + '\'' +
                ", zjProductName='" + zjProductName + '\'' +
                ", gznsProductName='" + gznsProductName + '\'' +
                ", jnnsProductName='" + jnnsProductName + '\'' +
                '}';
    }
}
