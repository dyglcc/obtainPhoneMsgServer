package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.Server;
import com.dx168.patchserver.core.mapper.ServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tong on 16/10/25.
 */
@Service
public class ServerService {
    @Autowired
    private ServerMapper serverMapper;


    public Integer insert(Server server) {
        return serverMapper.insert(server);
    }

    public List<Server> findAllServers() {
        return serverMapper.findAllServers();
    }

    public void deleteById(Integer id) {
        serverMapper.deleteById(id);
    }
}
