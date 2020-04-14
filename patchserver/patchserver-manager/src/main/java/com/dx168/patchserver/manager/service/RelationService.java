package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.Relation;
import com.dx168.patchserver.core.mapper.RelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class RelationService {
    @Autowired
    private RelationMapper serverMapper;

    public Integer insert(Relation relation) {
        return serverMapper.insert(relation);
    }

    public List<Relation> findRelation(String user_phone) {
        return serverMapper.findRelation(user_phone);
    }

    public void deleteById(String relate_phone) {

        serverMapper.deleteByRelationPhone(relate_phone);
    }



}
