package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.Validate;
import org.apache.ibatis.annotations.Mapper;

/**
 */
@Mapper
public interface ValidateMapper {
    Validate findUserByresetToken(String username);

//    BasicUser findById(Integer id);

    Integer insert(Validate validate);


    void deleteByUserId(Integer id);
}
