package com.dx168.patchserver.manager.mail;

import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Validate;
import com.dx168.patchserver.core.mapper.ValidateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.util.Date;

@Service
public class ValidateServiceImpl implements ValidateService {

    @Autowired
    private ValidateMapper validateMapper;

    /**
     * 发送邮件：@Async进行异步调用发送邮件接口
     * @param msg
     */
    @Override
    @Async
    public void sendPasswordResetEmail(Message msg){
        try {
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Async
    @Override
    public void sendUserPaidEmail(Message email) {
        try {
            Transport.send(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在pm_validate表中插入一条validate记录，userid，email属性来自pm_user表，token由UUID生成
     * @param validate
     * @param users
     * @param token
     * @return
     */
    @Override
    public int insertNewResetRecord(Validate validate, BasicUser users, String token){
        validate.setUserid(users.getId());
        validate.setEmail(users.getEmail());
        validate.setResetToken(token);
        validate.setType("passwordReset");
        validate.setGmtCreate(new Date());
        validate.setGmtUpdate(new Date());
        return validateMapper.insert(validate);
    }

    /**
     * pm_validate表中，通过token查找重置申请记录
     * @param token
     * @return
     */
    @Override
    public Validate findUserByResetToken(String token){
        return validateMapper.findUserByresetToken(token);
    }

    @Override
    public void deleteByid(Integer id) {
        validateMapper.deleteByUserId(id);
    }

}