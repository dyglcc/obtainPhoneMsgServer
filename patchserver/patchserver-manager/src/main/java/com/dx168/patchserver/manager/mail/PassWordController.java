package com.dx168.patchserver.manager.mail;

import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Validate;
import com.dx168.patchserver.manager.common.RestResponse;
import com.dx168.patchserver.manager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import static com.dx168.patchserver.manager.web.OrderController.gmailtls;

@Controller
public class PassWordController {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private AccountService accountService;

    @Value("${spring.mail.username}")
    private String from;
    @Value("${global_secret_key}")
    private String globalSecretKey;
    @Value("${spring.mail.password}")
    private String password;
    /**
     * @param email
     * @return
     */
    @RequestMapping(value = "/api/pass/sendResetPwdEmail", method = {RequestMethod.POST})
    public @ResponseBody
    RestResponse sendValidationEmail(HttpServletRequest req, String email) {
        RestResponse responseData = new RestResponse();
        BasicUser user = accountService.findByEmail(email);
        if (user == null) {
            responseData.setMessage("用户不存在，请核对后重试");
            return responseData;
        } else {


            // Get a Properties object
            Properties props = new Properties();
            //选择ssl方式
            gmailtls(props);

            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    });
            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            try {
                // 若允许重置密码，则在pm_validate表中插入一行数据，带有token
                Validate validate = new Validate();
                validateService.insertNewResetRecord(validate, user, UUID.randomUUID().toString());
                // 设置邮件内容
                String appUrl = req.getScheme() + "://" + req.getServerName();
                msg.setFrom(new InternetAddress(from));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email));
                msg.setSubject("MANGO云加速服务--重置密码");
                msg.setText("您正在申请重置密码，请点击此链接重置密码: \n" + appUrl + "/password/reset/resetpage-language=chinese.html?token=" + validate.getResetToken());
                msg.setSentDate(new Date());
                validateService.sendPasswordResetEmail(msg);

            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            responseData.setMessage(RestResponse.OK);


//            // 若允许重置密码，则在pm_validate表中插入一行数据，带有token
//            Validate validate = new Validate();
//            validateService.insertNewResetRecord(validate, user, UUID.randomUUID().toString());
//            // 设置邮件内容
//            String appUrl = req.getScheme() + "://" + req.getServerName();
//            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
//            passwordResetEmail.setFrom(from);
//            passwordResetEmail.setTo(email);
//            passwordResetEmail.setSubject("MANGO加速服务器--重置密码");
//            passwordResetEmail.setText("您正在申请重置密码，请点击此链接重置密码: \n" + appUrl + "/password/reset/resetpage-language=chinese.html?token=" + validate.getResetToken());
//            responseData.setMessage(RestResponse.OK);
        }
        return responseData;
    }

    /**
     * 将url的token和数据库里的token匹配，成功后便可修改密码，token有效期为60分钟
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/pass/checkResetPasstoken", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse checkResetPasstoken(String token) {
        RestResponse responseData = new RestResponse();
        // 通过token找到validate记录
        Validate validate = validateService.findUserByResetToken(token);
        if (validate == null) {
            responseData.setMessage("当前页面已过期");
        } else {
            responseData.setMessage(RestResponse.OK);
        }
        return responseData;
    }

    /**
     * 将url的token和数据库里的token匹配，成功后便可修改密码，token有效期为60分钟
     *
     * @param token
     * @param password
     * @param confirmPassword
     * @return
     */
    @RequestMapping(value = "/api/pass/resetPassword", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse resetPassword(String token,
                               String password,
                               String confirmPassword) {
        RestResponse responseData = new RestResponse();
        // 通过token找到validate记录
        Validate validate = validateService.findUserByResetToken(token);
        if (validate == null) {
            responseData.setMessage("重置token不存在,请重新重置密码");
        } else {
            Integer userId = validate.getUserid();
            if (password.equals(confirmPassword)) {
                BasicUser user = accountService.findById(userId);
                if (user == null) {
                    responseData.setMessage("用户不存在,请先注册账户");
                    return responseData;
                }
                user.setPassword(password);
                user.setUpdatedAt(new Date());
                accountService.updatePass(user);
                validateService.deleteByid(userId);
                responseData.setMessage(RestResponse.OK);
            } else {
                responseData.setMessage("确认密码和密码不一致，请重新输入");
            }
        }
        return responseData;
    }

}