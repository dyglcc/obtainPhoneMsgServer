package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.SubTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface SubTicketMapper {

    Integer insert(SubTicket subTicket);

    List<SubTicket> findAllMessages(String user);

    void deleteById(Integer id);
    void updateStatus(SubTicket server);


}
