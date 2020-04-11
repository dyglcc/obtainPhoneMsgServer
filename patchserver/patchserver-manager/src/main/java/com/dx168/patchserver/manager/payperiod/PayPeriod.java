package com.dx168.patchserver.manager.payperiod;

import java.math.BigDecimal;

public  class PayPeriod {

    private String name;
    private int type;
    private String desc;
    private BigDecimal price;
    public PayPeriod(String name,int type,String desc,BigDecimal price){
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
