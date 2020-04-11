package com.dx168.patchserver.manager.grpc;

import com.dx168.patchserver.core.domain.Order;
import com.dx168.patchserver.core.domain.Server;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.Constants;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.OrderService;
import com.dx168.patchserver.manager.service.ServerService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class V2rayServerController {
    @Autowired
    private ServerService serversService;
    @Autowired
    private OrderService orderService;
    @Value("${grpc_port}")
    private int gRPC_Port;

    @RequestMapping(value = "/api/v1/grpc", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getServers(HttpServletRequest req, HttpServletResponse res, String email, String orderid) {
        RestResponse restR = new RestResponse();
        restR.setMessage(RestResponse.OK);

        return restR;
    }
    @RequestMapping(value = "/api/v1/confirm", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse syncServersAddUsers(HttpServletRequest req, HttpServletResponse res, String orderid) {
        RestResponse restR = new RestResponse();

        if (VStringUtils.isEmpty(orderid)) {
            restR.setMessage("orderid null");
            return restR;
        }
        Order order = orderService.findById(Integer.parseInt(orderid));
        if (order == null || order.getEmail() == null || order.getUuid() == null) {
            restR.setMessage("order not exist ");
            return restR;
        }
        // set status is using
        order.setStatus(Constants.Status.order_status_using);
        order.setUpdated_at(new Date());

        orderService.updateOrder2Using(order);


        OkHttpClient client = new OkHttpClient();
        List<Server> servers = serversService.findAllServers();
        // 想server中添加用户
        for (Server server : servers) {
            String addr = server.getAddress();
            String url = "http://"+addr+":10086/addUser";
            if (!VStringUtils.isEmpty(url)) {
                RequestBody body = new FormBody.Builder()
                        .add("email", order.getEmail())
                        .add("uuid", order.getUuid()).build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        restR.setMessage(RestResponse.OK);
        return restR;
    }

}
