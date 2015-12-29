/**
 * File Name: StatBankBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import com.china.center.jdbc.annotation.*;
import java.io.Serializable;


/**
 * BankBalanceBean
 * 
 * @author simon
 * @version 2015-12-28
 * @see com.china.center.oa.finance.bean.BankBalanceBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_BANK_BALANCE")
public class BankBalanceBean implements Serializable
{
    @Id(autoIncrement = true)
    private int id = 0;

    @FK
    @Join(tagClass = BankBean.class)
    private String bankId = "";

    private String statDate = "";

    /**
     * 余额
     */
    private double balance = 0.0d;

    /**
     * default constructor
     */
    public BankBalanceBean()
    {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
