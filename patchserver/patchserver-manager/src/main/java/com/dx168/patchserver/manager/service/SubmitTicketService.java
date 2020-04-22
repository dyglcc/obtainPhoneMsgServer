package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.SubTicket;
import com.dx168.patchserver.core.mapper.SubTicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tong on 16/10/25.
 */
@Service
public class SubmitTicketService{
    @Autowired
    private SubTicketMapper serverMapper;

    public Integer insert(SubTicket server) {
        return serverMapper.insert(server);
    }

    public List<SubTicket> findAllMessages(String user) {
        return serverMapper.findAllMessages(user);
    }

    public void deleteById(Integer id) {

        serverMapper.deleteById(id);
    }

    public void updateStatus(SubTicket server) {
        serverMapper.updateStatus(server);

    }

}
