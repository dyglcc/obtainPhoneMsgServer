package com.dx168.patchserver.manager.common;

import com.dx168.patchserver.manager.payperiod.PayPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * Created by tong on 16/10/25.
 */
public class Constants {

    public static final String SESSION_LOGIN_USER = "username__";
    public static final String COOKIE_LOGINFIRTNAME = "firstName";
    public static final String COOKIE_LOGINLASTNAME = "lastName";
    public static final String COOKIE_EMAIL = "email";
    public static final String COOKIE_PHONE = "mobile";
//    String COOKIE_LOGINPWD = "loginpwd";

    /**
     * cookie有效期
     */
    public static final int COOKIE_EXPIRY_DATE = 30 * 24 * 60 * 60;

    public static class PayMethod {
//        账单类型 三年，二年，年付，半年付，季付，月付,周付，天付
        public static final int pay_period_year = 2;
        public static final int pay_period_2year = 3;
        public static final int pay_period_3year = 4;
        public static final int pay_period_half_year = 5;
        public static final int pay_period_season_year = 6;
        public static final int pay_period_month = 1;
        public static final int pay_period_week = 7;
        public static final int pay_period_day = 8;
        public static final LinkedHashMap<Integer, PayPeriod> mapPrice = new LinkedHashMap();

        static {
            BigDecimal yearPrice = new BigDecimal(200.00).setScale(2, RoundingMode.HALF_UP);
            mapPrice.put(pay_period_3year, new PayPeriod("三年",pay_period_3year,"triennially",yearPrice.multiply(new BigDecimal(2.4)).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_2year, new PayPeriod("两年",pay_period_2year,"biennially",yearPrice.multiply(new BigDecimal(1.8)).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_year, new PayPeriod("年付",pay_period_year,"annually",yearPrice));
            mapPrice.put(pay_period_half_year,new PayPeriod("半年",pay_period_half_year,"halfYearly",yearPrice.multiply(new BigDecimal(0.6)).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_season_year,new PayPeriod("季付",pay_period_season_year,"quarterly",new BigDecimal(66.00).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_month, new PayPeriod("月付",pay_period_month,"monthly",new BigDecimal(25.00).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_week, new PayPeriod("周付",pay_period_week,"weekly",new BigDecimal(10.00).setScale(2, RoundingMode.HALF_UP)));
            mapPrice.put(pay_period_day, new PayPeriod("日付",pay_period_day,"day",new BigDecimal(1.00).setScale(2, RoundingMode.HALF_UP)));
        }
    }

    public static class Product {
        // 产品类型 1.表示个人2。表示ipv6 3。表示企业版本
        public static final int PRODUCT_TYPE_PERSIONAL = 0;
        public static final int PRODUCT_TYPE_IPV6 = 1;
        public static final int PRODUCT_TYPE_ENTERPRIZE = 2;

    }


    public static class Status {
        public static final int order_status_unpayed = 1;
        public static final int order_status_opening = 2;
        public static final int order_status_using = 3;
        public static final int order_status_done = 4;

    }

}
