package com.dx168.patchserver.manager.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong on 8/11/15.
 */
public class RestResponse implements Serializable {
    public static final String OK = "ok";
    protected String message;
    protected Map<String, Object> data = new HashMap();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString(){

        System.out.println(this.message + "\n" + this.data.toString());
        return null;
    }
}
