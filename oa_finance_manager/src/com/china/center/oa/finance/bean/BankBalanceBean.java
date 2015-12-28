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
    @Id
    private String id = "";

    @FK
    @Join(tagClass = BankBean.class)
    private String bankId = "";

    private String date = "";

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
