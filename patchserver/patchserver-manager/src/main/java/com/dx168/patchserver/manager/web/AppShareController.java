package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.AppShares;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.AccountService;
import com.dx168.patchserver.manager.service.AppShareService;
import com.dx168.patchserver.manager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class AppShareController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AppShareService appsService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/v1/apps", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getServers(HttpServletRequest req, HttpServletResponse res) {
        RestResponse restR = new RestResponse();

        List<AppShares> appsList = appsService.findAllAppShares();
        restR.setMessage(RestResponse.OK);
        restR.getData().put("apps", appsList);
//        restR.getData().put("uuid",order.getUuid());
//        restR.getData().put("traffic",order.getTraffic());
        return restR;
    }

    @RequestMapping(value = "/api/v1/inAppShare", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse insert(HttpServletRequest req, HttpServletResponse res, String app_name,String icon_url) {
        RestResponse restR = new RestResponse();

        AppShares ser = new AppShares();
        ser.setApp_name(app_name);
        ser.setIcon_url(icon_url);
        ser.setCreated_at(new Date());
        appsService.insert(ser);
        restR.setMessage(RestResponse.OK);
        return restR;
    }

    @RequestMapping(value = "/api/v1/dAppShare", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse delete(HttpServletRequest req, HttpServletResponse res, Integer id) {
        RestResponse restR = new RestResponse();
        appsService.deleteById(id);
        restR.setMessage(RestResponse.OK);
        return restR;
    }
}
