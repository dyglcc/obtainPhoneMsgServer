package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.AppShares;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface AppShareMapper {


    Integer insert(AppShares appShares);

    List<AppShares> findAllAppShares(String main_account);

    void deleteById(Integer id);


}
