package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.AppShares;
import com.dx168.patchserver.core.mapper.AppShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tong on 16/10/25.
 */
@Service
public class AppShareService {
    @Autowired
    private AppShareMapper appShareMapper;


    public Integer insert(AppShares appShares) {
        return appShareMapper.insert(appShares);
    }

    public List<AppShares> findAllAppShares() {
        return appShareMapper.findAllAppShares();
    }

    public void deleteById(Integer id) {
        appShareMapper.deleteById(id);
    }
}
