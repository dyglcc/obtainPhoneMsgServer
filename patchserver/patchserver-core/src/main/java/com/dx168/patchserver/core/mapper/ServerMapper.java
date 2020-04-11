package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.Server;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface ServerMapper {


    Integer insert(Server server);

    List<Server> findAllServers();

    void deleteById(Integer id);


}
