package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.Relation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface RelationMapper {

    Integer insert(Relation relation);

    List<Relation> findRelation(String user_phone);
    List<Relation> findLeader(String relate_phone);

    void deleteByRelationPhone(String relate_phone);


}
