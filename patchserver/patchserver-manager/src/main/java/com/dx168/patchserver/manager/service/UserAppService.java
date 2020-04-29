package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.UserApp;
import com.dx168.patchserver.core.mapper.UserAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tong on 16/10/25.
 */
@Service
public class UserAppService {
    @Autowired
    private UserAppMapper appShareMapper;


    public Integer insert(UserApp appShares) {
        return appShareMapper.insert(appShares);
    }

    public List<UserApp> findAllGroups() {
        return appShareMapper.findAllGroups();
    }

    public void deleteById(Integer id) {
        appShareMapper.deleteById(id);
    }
    public void update(UserApp userApp){
        appShareMapper.update(userApp);
    }
}
