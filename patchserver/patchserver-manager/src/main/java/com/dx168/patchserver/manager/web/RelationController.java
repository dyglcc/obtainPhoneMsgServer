package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.Relation;
import com.dx168.patchserver.core.utils.BizException;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class RelationController {
    @Autowired
    private RelationService orderService;

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/addrelation", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse ticket(HttpServletRequest req, String name, String user_phone, String relate_phone) {
        RestResponse restResponse = new RestResponse();

        if(VStringUtils.isEmpty(user_phone)){
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        if(VStringUtils.isEmpty(relate_phone)){
            restResponse.setMessage("关联手机号不存在");
            return restResponse;
        }
        if(VStringUtils.isEmpty(name)){
            restResponse.setMessage("关联名称空了");
            return restResponse;
        }
        Relation relation = new Relation();
        try{
            relation.setName(name);
            relation.setRelate_phone(relate_phone);
            relation.setUser_phone(user_phone);
            relation.setCreated_at(new Date());

            orderService.insert(relation);
            List<Relation> list =orderService.findRelation(user_phone);
            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("data",list);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/relations", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse relation(HttpServletRequest req, String mobile) {
        RestResponse restResponse = new RestResponse();

        if(VStringUtils.isEmpty(mobile)){
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        try{
            List<Relation> list =orderService.findRelation(mobile);
            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("data",list);
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
