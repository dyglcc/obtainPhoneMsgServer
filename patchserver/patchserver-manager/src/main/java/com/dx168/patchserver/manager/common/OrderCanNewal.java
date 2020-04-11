package com.dx168.patchserver.manager.common;

import java.io.Serializable;

/**
 * Created by tong on 8/11/15.
 */
public class OrderCanNewal implements Serializable {

    private int id;
    private String productName = "入门版";
    private String amount;
    private String nextPayment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(String nextPayment) {
        this.nextPayment = nextPayment;
    }


}
