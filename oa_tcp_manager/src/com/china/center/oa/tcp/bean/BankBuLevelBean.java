package com.china.center.oa.tcp.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/** #231
 * 银行业务部层级表
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 16-4-30
 * Time: 上午8:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_CENTER_BANKBU_LEVEL")
public class BankBuLevelBean implements Serializable {
    /**
     * 专员编码
     */
    @Id
    private String id;

    /**
     * 专员姓名
     */
    private String name;

    /**
     * 省级经理编码
     */
    private String provinceManagerId;

    /**
     * 省级经理姓名
     */
    private String provinceManager;

    /**
     * 区域经理编码
     */
    private String regionalManagerId;

    /**
     * 区域经理姓名
     */
    private String regionalManager;

    /**
     * 大区总编码
     */
    private String regionalDirectorId;

    /**
     * 大区总姓名
     */
    private String regionalDirector;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceManagerId() {
        return provinceManagerId;
    }

    public void setProvinceManagerId(String provinceManagerId) {
        this.provinceManagerId = provinceManagerId;
    }

    public String getProvinceManager() {
        return provinceManager;
    }

    public void setProvinceManager(String provinceManager) {
        this.provinceManager = provinceManager;
    }

    public String getRegionalManagerId() {
        return regionalManagerId;
    }

    public void setRegionalManagerId(String regionalManagerId) {
        this.regionalManagerId = regionalManagerId;
    }

    public String getRegionalManager() {
        return regionalManager;
    }

    public void setRegionalManager(String regionalManager) {
        this.regionalManager = regionalManager;
    }

    public String getRegionalDirectorId() {
        return regionalDirectorId;
    }

    public void setRegionalDirectorId(String regionalDirectorId) {
        this.regionalDirectorId = regionalDirectorId;
    }

    public String getRegionalDirector() {
        return regionalDirector;
    }

    public void setRegionalDirector(String regionalDirector) {
        this.regionalDirector = regionalDirector;
    }

    @Override
    public String toString() {
        return "BankBuLevelBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", provinceManagerId='" + provinceManagerId + '\'' +
                ", provinceManager='" + provinceManager + '\'' +
                ", regionalManagerId='" + regionalManagerId + '\'' +
                ", regionalManager='" + regionalManager + '\'' +
                ", regionalDirectorId='" + regionalDirectorId + '\'' +
                ", regionalDirector='" + regionalDirector + '\'' +
                '}';
    }
}
