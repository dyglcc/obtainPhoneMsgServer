package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Order;
import com.dx168.patchserver.core.domain.Server;
import com.dx168.patchserver.core.utils.V2rayUtils;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.AccountService;
import com.dx168.patchserver.manager.service.OrderService;
import com.dx168.patchserver.manager.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class ServerController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ServerService serverService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/v1/servers", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getServers(HttpServletRequest req, HttpServletResponse res, String email,String orderid) {
        RestResponse restR = new RestResponse();
        BasicUser basicUser = accountService.findByEmail(email);
        if(VStringUtils.isEmpty(orderid)){
            restR.setMessage("order id need none null");
            return restR;
        }
        Order order = orderService.findById(Integer.parseInt(orderid));
        if(order == null){
            restR.setMessage("order does not exist");
            return restR;
        }
        if (basicUser == null) {
            restR.setMessage("user does not exist");
            return restR;
        }

        List<Server> serverList = serverService.findAllServers();
        restR.setMessage(RestResponse.OK);
        restR.getData().put("servers", serverList);
        restR.getData().put("uuid",order.getUuid());
        restR.getData().put("traffic",order.getTraffic());
        return restR;
    }

    @RequestMapping(value = "/api/v1/inServer", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse insert(HttpServletRequest req, HttpServletResponse res, String server_location, String address, String ratio, int port) {
        RestResponse restR = new RestResponse();

        Server ser = new Server();
        ser.setRatio(ratio);
        ser.setAddress(address);
        ser.setPort(port);
        ser.setServer_location(server_location);
        ser.setCreated_at(new Date());
        serverService.insert(ser);
        restR.setMessage(RestResponse.OK);
        return restR;
    }

    @RequestMapping(value = "/api/v1/dServer", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse delete(HttpServletRequest req, HttpServletResponse res, Integer id) {
        RestResponse restR = new RestResponse();
        serverService.deleteById(id);
        restR.setMessage(RestResponse.OK);
        return restR;
    }
}
