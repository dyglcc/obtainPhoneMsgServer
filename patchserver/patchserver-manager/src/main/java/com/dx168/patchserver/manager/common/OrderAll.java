package com.dx168.patchserver.manager.common;

import java.io.Serializable;

/**
 * Created by tong on 8/11/15.
 */
public class OrderAll implements Serializable {
//    {amount: 168, detail: "购买个人云加速服务 年付", paid: 0, orderTime: 1572672134000, order_id: 24}
    private String amount;
    private String detail = "云加速服务 - 入门版";
    private String paid;
    private String orderTime;
    private int order_id;
    private String period;
    private String expiredAt;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }
}
