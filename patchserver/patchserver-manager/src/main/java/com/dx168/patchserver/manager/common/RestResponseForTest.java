package com.dx168.patchserver.manager.common;

import java.io.Serializable;

/**
 * Created by tong on 8/11/15.
 */
public class RestResponseForTest implements Serializable {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }

    public Object getExps() {
        return exps;
    }

    public void setExps(Object exps) {
        this.exps = exps;
    }

    protected Object datas;
    protected Object exps;



}
