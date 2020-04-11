package com.dx168.patchserver.manager.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Test {


    public static final HashMap<Integer, BigDecimal> mapPrice = new HashMap();

    static {
        mapPrice.put(0, new BigDecimal(16.00));
        mapPrice.put(1, new BigDecimal(200));
        mapPrice.put(2, mapPrice.get(1).multiply(new BigDecimal(1.8)));
        mapPrice.put(3, mapPrice.get(1).multiply(new BigDecimal(2.4)));
    }

    public static void main(String[] args){
        DecimalFormat form = new DecimalFormat("0.00");
        System.out.println(mapPrice.get(2));
        System.out.println(mapPrice.get(3));
        BigDecimal decimal = mapPrice.get(3);
        System.out.println(form.format(decimal));
    }
}
