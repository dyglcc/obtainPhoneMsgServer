package com.dx168.patchserver.manager.mail;


import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Validate;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.Message;


public interface ValidateService {
    void sendPasswordResetEmail(Message email);// 直接发送邮件
    void sendUserPaidEmail(Message email);// 直接发送邮件


    int insertNewResetRecord(Validate validate, BasicUser users, String token);// 有重置邮箱的请求
    Validate findUserByResetToken(String resetToken);

    void deleteByid(Integer id);
//    boolean validateLimitation(String email, long requestPerDay, long interval, String token);
//    boolean sendValidateLimitation(String email, long requestPerDay, long interval);
}
