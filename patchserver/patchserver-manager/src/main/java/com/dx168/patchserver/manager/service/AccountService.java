package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.utils.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * Created by tong on 16/10/25.
 */
@Service
public class AccountService {
    @Autowired
    private UserMapper userMapper;

    @Value("${global_secret_key}")
    private String globalSecretKey;

    public boolean authenticate(BasicUser basicUser,String pass) {
//        BasicUser basicUser = findByUsername(username);
        if (basicUser == null) {
            return false;
        }
        String correctPassword = DigestUtils.md5DigestAsHex((globalSecretKey + "_" + pass).getBytes());
        return basicUser.getPassword().equals(correctPassword);
    }

    public void updatePass(BasicUser basicUser){
        basicUser.setPassword(DigestUtils.md5DigestAsHex((globalSecretKey + "_" + basicUser.getPassword()).getBytes()));
        userMapper.updateUserPass(basicUser);
    }

    public BasicUser findByUsername(String username) {
        return userMapper.findByEmail(username);
    }
    public BasicUser findByEmail(String email) {

        return userMapper.findByEmail(email);
    }

    public void save(BasicUser basicUser) {
        basicUser.setPassword(DigestUtils.md5DigestAsHex((globalSecretKey + "_" + basicUser.getPassword()).getBytes()));
        basicUser.setCreatedAt(new Date());
        basicUser.setUpdatedAt(new Date());
        Integer id = userMapper.insert(basicUser);
        basicUser.setId(id);
    }
    public void updateinfo(BasicUser basicUser) {
        userMapper.updateUserInfo(basicUser);
    }

    public BasicUser findById(Integer id) {
        return userMapper.findById(id);
    }

    public List<BasicUser> findAllChildUser(Integer id) {
        return userMapper.findAllChildUser(id);
    }

    public Integer getRootUserId(BasicUser basicUser) {
        if (basicUser.isChildAccount()) {
            return basicUser.getParentId();
        }
        return basicUser.getId();
    }

    public void removeChildAcc(BasicUser rootUser, Integer id) {
        if (id == null) {
            throw new BizException("childUserId is null!!");
        }

        BasicUser childUser = findById(id);
        if (childUser == null) {
            throw new BizException("账号不存在");
        }
        if (childUser.getParentId() != rootUser.getId()) {
            throw new BizException("不是当前用户的子账户不能删除");
        }

        deleteById(id);
    }

    private void deleteById(Integer id) {
        userMapper.deleteById(id);
    }
}
