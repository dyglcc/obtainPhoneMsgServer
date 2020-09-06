package com.dx168.patchserver.manager.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.appadhoc.javasdk.AdhocSDK;
import com.dx168.patchserver.core.domain.UserApp;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.common.RestResponseForTest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class UserAppsController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserAppService appsService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/v1/groups", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getGroups(HttpServletRequest req, HttpServletResponse res,String main_account) {
        RestResponse restR = new RestResponse();

        if(VStringUtils.isEmpty(main_account)){
            restR.setMessage("用户手机号不存在");
            return restR;
        }

        List<UserApp> appsList = appsService.findAllGroups(main_account);
        restR.setMessage(RestResponse.OK);
        restR.getData().put("apps", appsList);

        return restR;
    }

    @RequestMapping(value = "/api/v1/addGroup", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse insert(HttpServletRequest req, HttpServletResponse res, String app_id,String main_account,String group_id,String add) {
        RestResponse restR = new RestResponse();
        if(VStringUtils.isEmpty(main_account)){
            restR.setMessage("用户手机号不存在");
            return restR;
        }
        if(VStringUtils.isEmpty(main_account)){
            restR.setMessage("app id 不能为空");
            return restR;
        }
        if(!VStringUtils.isEmpty(group_id)){
            // 存在groupid，重新启用
            if(!VStringUtils.isEmpty(add)){
                if(Boolean.parseBoolean(add)){
                    update(req,res,Integer.parseInt(group_id),1);
                }else{
                    delete(req,res,Integer.parseInt(group_id));
                }
            }
            restR.setMessage(RestResponse.OK);
            return restR;
        }
        UserApp userApp = new UserApp();
        userApp.setApp_id(Integer.parseInt(app_id));
        userApp.setMain_account(main_account);
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
        update(req,res,id,0);
        restR.setMessage(RestResponse.OK);
        return restR;
    }
//    @RequestMapping(value = "/api/v1/updateGroup", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse update(HttpServletRequest req, HttpServletResponse res, Integer id,Integer status) {
        UserApp userApp = new UserApp();
        userApp.setId(id);
        userApp.setStatus(status);
        appsService.update(userApp);
        return null;
    }


    private static class Student{

        private int id;
        private String name;
        private int age;
        private String gender;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }


    //{"datas":[
    //{"id":0,"name":"xiaoming","age":10,"gender":"male"},
    //{"id":1,"name":"xiaohong","age":11,"gender":"female"},
    //{"id":2,"name":"xiaozhang","age":12,"gender":"male"},
    //],"exp":[{
    //                    "id": "1a2faf9e-2d7f-49b9-bc89-14f3de554763",
    //                    "name": "版本1_TEST_REACT_NATIVE",
    //                    "flags": [
    //                        "flag_int"
    //                    ],
    //                    "stats": [
    //                        "isJoinedClick"
    //                    ]
    //                }]
    //}
    @RequestMapping(value = "/api/v1/getlist", method = RequestMethod.GET )
    public @ResponseBody
    RestResponseForTest forappadhocTest(HttpServletRequest req, HttpServletResponse res, String clientid) {
        RestResponseForTest restR = new RestResponseForTest();
        if(VStringUtils.isEmpty(clientid)){
            restR.setMessage("clientId不能为空");
            return restR;
        }
        Student student = new Student();
        student.setAge(20);
        student.setGender("male");
        student.setId(0);
        student.setName("xiaoming");
        Student student2 = new Student();
        student2.setAge(10);
        student2.setGender("female");
        student2.setId(1);
        student2.setName("xiaohong");
        ArrayList<Student> data1 = new ArrayList();
        data1.add(student);
        data1.add(student2);

        AdhocSDK.init("ADHOC_6ea75f1a-e4f6-4caf-b98b-93dcec08836a");
        JSONObject object = AdhocSDK.getAbtestInfo(clientid);
        JSONArray exps = AdhocSDK.getVersions(object);

//        restR.getData().put("datas", data1);
//        restR.getData().put("exps",exps);
        HashMap map = new HashMap();
        map.put("datas",data1);
        map.put("exps",exps);
        restR.setDatas(data1);
        restR.setExps(exps);
        restR.setMessage(RestResponse.OK);
        return restR;
    }
}
