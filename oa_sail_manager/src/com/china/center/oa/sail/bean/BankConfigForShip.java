package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/**
 * Created by wxjsj on 2017/2/22.
 */
@Entity
@Table(name = "T_CENTER_BankConfigForShip")
public class BankConfigForShip implements Serializable {
    @Id(autoIncrement = true)
    private String id = "";

    private String bank;

    private String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "BankConfigForShip{" +
                "id='" + id + '\'' +
                ", bank='" + bank + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
