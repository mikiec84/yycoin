package com.china.center.oa.finance.vo;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.finance.bean.BankBalanceBean;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 15-12-29
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
@Entity(inherit = true)
public class BankBalanceVO extends BankBalanceBean{
    @Relationship(relationField = "bankId")
    private String bankName = "";

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
