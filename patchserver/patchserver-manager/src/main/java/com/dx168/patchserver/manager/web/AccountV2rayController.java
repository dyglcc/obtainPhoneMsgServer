package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Order;
import com.dx168.patchserver.core.utils.BizException;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.Constants;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.AccountService;
import com.dx168.patchserver.manager.service.OrderService;
import com.dx168.patchserver.manager.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class AccountV2rayController {
    @Value("${open_regist}")
    private boolean openRegist;
    @Value("${global_secret_key}")
    private String globalSecretKey;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/v1/login", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse login(HttpServletRequest req, HttpServletResponse res, String mobile, String email, String password, String rememberPwd) {
        RestResponse restR = new RestResponse();
        if(VStringUtils.isEmpty(mobile)){
            restR.setMessage("用户手机号不存在");
            return restR;
        }
        BasicUser basicUser = accountService.findByMobile(mobile);
        boolean result = accountService.authenticate(basicUser, password);
        if (result) {
            if ("on".equals(rememberPwd)) {
                //记录一个月
                CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, email, Constants.COOKIE_EXPIRY_DATE);
                CookieUtil.addCookie(res, Constants.COOKIE_LOGINFIRTNAME, basicUser.getFirstName(), Constants.COOKIE_EXPIRY_DATE);
                CookieUtil.addCookie(res, Constants.COOKIE_LOGINLASTNAME, basicUser.getLastName(), Constants.COOKIE_EXPIRY_DATE);
                CookieUtil.addCookie(res, Constants.COOKIE_PHONE, basicUser.getMobile(), Constants.COOKIE_EXPIRY_DATE);
//                CookieUtil.addCookie(res, Constants.COOKIE_LOGINPWD, password, Constants.COOKIE_EXPIRY_DATE);
            } else {
                CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, email, 3600);
                CookieUtil.addCookie(res, Constants.COOKIE_LOGINFIRTNAME, basicUser.getFirstName(), 3600);
                CookieUtil.addCookie(res, Constants.COOKIE_LOGINLASTNAME, basicUser.getLastName(), 3600);
                CookieUtil.addCookie(res, Constants.COOKIE_PHONE, basicUser.getMobile(), 3600);
            }
            List<Order> orderList = orderService.findUnpaysOrder(email);

            SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");
            if (!orderList.isEmpty()) {
                Order order = orderList.get(0);
                String descPeriod = "";
                if (order.getPay_period() == 2) {
                    descPeriod = "年付";
                } else if (order.getPay_period() == 3) {
                    descPeriod = "两年付";
                } else if (order.getPay_period() == 4) {
                    descPeriod = "三年付";
                }
                restR.getData().put("order_id", order.getId());
                restR.getData().put("amount", order.getAmount());
                restR.getData().put("paid", 0);
                restR.getData().put("detail", "购买个人云加速服务 " + descPeriod);
                restR.getData().put("orderTime", format.format(order.getCreated_at()));
            }


            restR.setMessage(RestResponse.OK);

        } else {
            CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, null, 0); // 清除Cookie
            CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, null, 0);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINFIRTNAME, null, 0);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINLASTNAME, null, 0);
            CookieUtil.addCookie(res, Constants.COOKIE_PHONE, null, 0);

            restR.setMessage("用户名或者密码不正确,请重新登录");
        }
        return restR;
    }

    @RequestMapping(value = "/api/v1/logout", method = RequestMethod.GET)
    public @ResponseBody
    RestResponse logout(HttpServletRequest req) {
        req.getSession().removeAttribute(Constants.COOKIE_LOGINFIRTNAME);
        req.getSession().removeAttribute(Constants.COOKIE_LOGINLASTNAME);
        req.getSession().removeAttribute(Constants.COOKIE_EMAIL);
        req.getSession().removeAttribute(Constants.COOKIE_PHONE);
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage(RestResponse.OK);
        return restResponse;
    }

    @RequestMapping(value = "/api/v1/regist", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse regist(HttpServletRequest req, HttpServletResponse res, String firstName, String email, String lastName, String password, String mobile) {
        RestResponse restResponse = new RestResponse();
        try {
            if (VStringUtils.isEmpty(password)) {
                restResponse.setMessage("password不能为空");
                return restResponse;
            }
            if (!openRegist) {
                restResponse.setMessage("暂不接受注册");
                return restResponse;
            }
            BasicUser basicUser = accountService.findByUsername(email);
            if (basicUser != null) {
                restResponse.setMessage(email + "已经被注册");
                return restResponse;
            }
            basicUser = new BasicUser();
            basicUser.setEmail(email);
            basicUser.setFirstName(firstName);
            basicUser.setLastName(lastName);
            basicUser.setMobile(mobile);
            basicUser.setPassword(password);
            accountService.save(basicUser);

//            req.getSession().removeAttribute(Constants.SESSION_LOGIN_USER);
            //req.getSession().setAttribute(Constants.SESSION_LOGIN_USER,basicUser);

            CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, email, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINFIRTNAME, firstName, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINLASTNAME, lastName, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_PHONE, mobile, Constants.COOKIE_EXPIRY_DATE);

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

    @RequestMapping(value = "/api/v1/update", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse update(HttpServletRequest req,HttpServletResponse res, String firstName, String email, String lastName, String mobile) {
        RestResponse restResponse = new RestResponse();
        try {
            if (VStringUtils.isEmpty(email)) {
                restResponse.setMessage("email不能为空");
                return restResponse;
            }
            if (!email.contains("@")) {
                restResponse.setMessage("email格式不正确");
                return restResponse;
            }
            if (VStringUtils.isEmpty(firstName)) {
                restResponse.setMessage("firstName不能为空");
                return restResponse;
            }
            if (VStringUtils.isEmpty(lastName)) {
                restResponse.setMessage("lastName不能为空");
                return restResponse;
            }
            BasicUser basicUser = accountService.findByUsername(email);
            if (basicUser == null) {
                restResponse.setMessage(email + "不存在，请先注册帐号");
                return restResponse;
            }
            basicUser.setEmail(email);
            basicUser.setFirstName(firstName);
            basicUser.setLastName(lastName);
            basicUser.setMobile(mobile);
            accountService.updateinfo(basicUser);

//            req.getSession().removeAttribute(Constants.SESSION_LOGIN_USER);
            //req.getSession().setAttribute(Constants.SESSION_LOGIN_USER,basicUser);

            CookieUtil.addCookie(res, Constants.COOKIE_EMAIL, email, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINFIRTNAME, firstName, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_LOGINLASTNAME, lastName, Constants.COOKIE_EXPIRY_DATE);
            CookieUtil.addCookie(res, Constants.COOKIE_PHONE, mobile, Constants.COOKIE_EXPIRY_DATE);

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

    @RequestMapping(value = "/api/v1/changePWD", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse changePwd(HttpServletRequest req,HttpServletResponse res, String email, String newPass, String oldPass) {
        RestResponse restResponse = new RestResponse();
        try {
            BasicUser basicUser = accountService.findByUsername(email);
            boolean auth = accountService.authenticate(basicUser,oldPass);
            if(!auth){
                restResponse.setMessage("旧密码不正确，请重新输入");
                return restResponse;
            }
            if (VStringUtils.isEmpty(email)) {
                restResponse.setMessage("email不能为空");
                return restResponse;
            }
            if (!email.contains("@")) {
                restResponse.setMessage("email格式不正确");
                return restResponse;
            }
            if(VStringUtils.isEmpty(newPass)){
                restResponse.setMessage("新密码不能为空");
                return restResponse;
            }
            if(VStringUtils.isEmpty(oldPass)){
                restResponse.setMessage("旧密码不能为空");
                return restResponse;
            }
            basicUser.setPassword(newPass);
            accountService.updatePass(basicUser);

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


    @RequestMapping(value = "/api/v1/health", method = RequestMethod.GET)
    public @ResponseBody
    RestResponse health() throws Exception {
        RestResponse restR = new RestResponse();
        restR.setMessage("I am still alive");
        return restR;
    }
}
