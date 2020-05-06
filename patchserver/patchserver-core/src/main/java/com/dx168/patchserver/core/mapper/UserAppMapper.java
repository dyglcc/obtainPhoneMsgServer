package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.UserApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface UserAppMapper {

    Integer insert(UserApp userApp);

    List<UserApp> findAllGroups(String main_account);

    void deleteById(Integer id);
    void update(UserApp userApp);


}
