package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.SubTicket;
import com.dx168.patchserver.core.utils.BizException;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.SubmitTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class SubmitTicketController {
    @Autowired
    private SubmitTicketService orderService;

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/submitticket", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse ticket(String user,String email,String message,String subject,String name) {
        RestResponse restResponse = new RestResponse();

        if(VStringUtils.isEmpty(email)){
            restResponse.setMessage("请输入邮箱");
            return restResponse;
        }
        if(VStringUtils.isEmpty(message)){
            restResponse.setMessage("请输入您遇到的问题");
            return restResponse;
        }
        if(!VStringUtils.isEmpty(subject)){
            if(subject.length()>250){
                restResponse.setMessage("主题字数超限了");
                return restResponse;
            }
        }
        if(message.length() > 500){
            restResponse.setMessage("消息字数超限了");
            return restResponse;
        }
        SubTicket subTicket = new SubTicket();
        try{
            subTicket.setEmail(email);
            subTicket.setUser(user);
            subTicket.setMessage(message);
            subTicket.setSubject(subject);
            subTicket.setName(name);
            subTicket.setCreated_at(new Date());

            orderService.insert(subTicket);
            restResponse.setMessage(RestResponse.OK);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }


}
