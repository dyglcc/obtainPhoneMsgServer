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
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping(value = "/api/v1/addRelation", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse ticket(HttpServletRequest req, String name, String main_account, String sub_account,String group_id) {
        RestResponse restResponse = new RestResponse();

        if(VStringUtils.isEmpty(main_account)){
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        if(VStringUtils.isEmpty(group_id)){
            restResponse.setMessage("用户组不能是空");
            return restResponse;
        }
        if(VStringUtils.isEmpty(sub_account)){
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
            relation.setGroup_id(Integer.parseInt(group_id));
            relation.setSub_account(sub_account);
            relation.setMain_account(main_account);
//            BasicUser basicUser = accountService.findByMobile(main_account);
//            if(basicUser!=null){
//                relation.setMain_account_name(basicUser.getFirstName());
//            }
            relation.setCreated_at(new Date());

            orderService.insert(relation);
            Relation se = new Relation();
            se.setMain_account(main_account);
            se.setGroup_id(Integer.parseInt(group_id));
            List<Relation> list =orderService.findRelation(se);
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
    RestResponse relation(HttpServletRequest req, String main_account,String group_id) {
        RestResponse restResponse = new RestResponse();

        if(VStringUtils.isEmpty(main_account)){
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        try{
            Relation relation = new Relation();
            relation.setMain_account(main_account);
            relation.setGroup_id(Integer.parseInt(group_id));
            List<Relation> list =orderService.findRelation(relation);
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
//    @RequestMapping(value = "/api/v1/findMainAccount", method = RequestMethod.POST)
//    public @ResponseBody
//    RestResponse findLeader(HttpServletRequest req, String mobile) {
//        RestResponse restResponse = new RestResponse();
//
//        if(VStringUtils.isEmpty(mobile)){
//            restResponse.setMessage("用户手机号不存在");
//            return restResponse;
//        }
//        try{
//            List<Relation> list =orderService.findLeader(mobile);
//            restResponse.setMessage(RestResponse.OK);
//            restResponse.getData().put("data",list);
//            return restResponse;
//        } catch (BizException e) {
//            e.printStackTrace();
//            restResponse.setMessage(e.getMessage());
//            return restResponse;
//        } finally {
//            restResponse.toString();
//        }
//    }
    @RequestMapping(value = "/api/v1/findSubAccount", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse findSubAccount(HttpServletRequest req, String sub_account) {
        RestResponse restResponse = new RestResponse();
        System.out.println("");

        if(VStringUtils.isEmpty(sub_account)){
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        try{
            List<Relation> list =orderService.findLeader(sub_account);
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
    @RequestMapping(value = "/api/v1/dSubAccount", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse delete(HttpServletRequest req, HttpServletResponse res, String main_account,String sub_account,String group_id) {
        if(VStringUtils.isEmpty(sub_account)){
            RestResponse restR = new RestResponse();
            restR.setMessage("子帐号不能为空");
            return restR;
        }
//        RestResponse restR = new RestResponse();
//        appsService.deleteById(id);
        // 并不是真的删除只是改状态为0
        Relation relation = new Relation();
        relation.setGroup_id(Integer.parseInt(group_id));
        relation.setMain_account(main_account);
        relation.setSub_account(sub_account);
        orderService.deleteById(relation);
//        restR.setMessage(RestResponse.OK);

        return relation(req,main_account,group_id);
    }

}
