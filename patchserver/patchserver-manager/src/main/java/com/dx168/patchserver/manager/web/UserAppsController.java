package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.UserApp;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.AccountService;
import com.dx168.patchserver.manager.service.OrderService;
import com.dx168.patchserver.manager.service.UserAppService;
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
public class UserAppsController {
    @Autowired
    private AccountService accountService;
todo ,设计一对多关联关系，
    @Autowired
    private UserAppService appsService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/v1/groups", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getGroups(HttpServletRequest req, HttpServletResponse res) {
        RestResponse restR = new RestResponse();

        List<UserApp> appsList = appsService.findAllGroups();
        restR.setMessage(RestResponse.OK);
        restR.getData().put("apps", appsList);
//        restR.getData().put("uuid",order.getUuid());
//        restR.getData().put("traffic",order.getTraffic());
        return restR;
    }

    @RequestMapping(value = "/api/v1/addGroup", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse insert(HttpServletRequest req, HttpServletResponse res, String app_id,String mobile,String group_id) {
        RestResponse restR = new RestResponse();
        if(VStringUtils.isEmpty(mobile)){
            restR.setMessage("用户手机号不存在");
            return restR;
        }
        if(VStringUtils.isEmpty(mobile)){
            restR.setMessage("app id 不能为空");
            return restR;
        }
        if(!VStringUtils.isEmpty(group_id)){
            // 存在groupid，重新启用
            update(req,res,Integer.parseInt(group_id),1);
            restR.setMessage(RestResponse.OK);
            return restR;
        }
        UserApp userApp = new UserApp();
        userApp.setApp_id(Integer.parseInt(app_id));
        userApp.setMain_account(mobile);
        userApp.setCreated_at(new Date());
        appsService.insert(userApp);
        restR.setMessage(RestResponse.OK);
        return restR;
    }

    @RequestMapping(value = "/api/v1/dGroup", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse delete(HttpServletRequest req, HttpServletResponse res, Integer id) {
        RestResponse restR = new RestResponse();
//        appsService.deleteById(id);
        // 并不是真的删除只是改状态为0
        UserApp userApp = new UserApp();
        userApp.setId(id);
        userApp.setStatus(0);
        appsService.update(userApp);
        restR.setMessage(RestResponse.OK);
        return restR;
    }
    @RequestMapping(value = "/api/v1/update", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse update(HttpServletRequest req, HttpServletResponse res, Integer id,Integer status) {
        UserApp userApp = new UserApp();
        userApp.setId(id);
        userApp.setStatus(status);
        appsService.update(userApp);
        return null;
    }
}
