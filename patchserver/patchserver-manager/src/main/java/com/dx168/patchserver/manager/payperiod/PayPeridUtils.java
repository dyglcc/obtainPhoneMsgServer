package com.dx168.patchserver.manager.payperiod;

import com.dx168.patchserver.manager.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayPeridUtils {
    public static List<PayPeriod> getPeriodList() {
        ArrayList<PayPeriod> list = new ArrayList<>();
        for(Map.Entry entry: Constants.PayMethod.mapPrice.entrySet()){
            list.add((PayPeriod) entry.getValue());
        }
        return list;
    }
}
