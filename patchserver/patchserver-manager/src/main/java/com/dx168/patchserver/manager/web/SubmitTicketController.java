package com.dx168.patchserver.manager.web;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.dx168.patchserver.core.domain.SubTicket;
import com.dx168.patchserver.core.utils.BizException;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.SubmitTicketService;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SubmitTicketController {
    public static final String APP_KEY = "3c1bafd2c4145faeaa7da1bb";
    public static final String MASTER_SECRET = "5938bf4944d90f81a3e75aa9";
    public static final String TITLE = "Test from API example";
    public static final String ALERT = "Test from API Example - alert";
    public static final String MSG_CONTENT = "Test from API Example - msgContent";
    public static final String REGISTRATION_ID = "0900e8d85ef";
    public static final String TAG = "tag_api";
    @Autowired
    private SubmitTicketService orderService;
    protected static final Logger LOG = LoggerFactory.getLogger(SubmitTicketController.class);

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/submitticket", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse ticket(String mobile, String message) {
        RestResponse restResponse = new RestResponse();

        if (VStringUtils.isEmpty(message)) {
            restResponse.setMessage("请输入您遇到的问题");
            return restResponse;
        }
        SubTicket subTicket = new SubTicket();
        try {
            subTicket.setUser(mobile);
            subTicket.setMessage(message);
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

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/messages", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse relation(HttpServletRequest req, String mobile) {
        RestResponse restResponse = new RestResponse();

        if (VStringUtils.isEmpty(mobile)) {
            restResponse.setMessage("用户手机号不存在");
            return restResponse;
        }
        try {
            List<SubTicket> list = orderService.findAllMessages(mobile);
            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("data", list);
            pushCode(mobile);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }


    public static PushPayload buildPushObject_android_tagAnd_alertWithExtrasAndMessage() {
        JsonObject sound = new JsonObject();
        sound.add("critical", new JsonPrimitive(1));
        sound.add("name", new JsonPrimitive("default"));
        sound.add("volume", new JsonPrimitive(0.2));
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setMutableContent(false)
//                                .setSound("happy")
                                .setSound(sound)
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(MSG_CONTENT))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }
    public void pushCode(String mobile) {

        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_android_tagAnd_alertWithExtrasAndMessage();

        try {
            PushResult result = jpushClient.sendAndroidMessageWithAlias("title_from_server","nishoudao一个yanzhengma",mobile);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }
}
