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

    public List<Relation> findRelation(Relation relation) {
        return serverMapper.findRelation(relation);
    }
    public List<Relation> findLeader(String sub_account) {
        return serverMapper.findLeader(sub_account);
    }

    public void deleteById(Relation relation) {

        serverMapper.deleteByRelationPhone(relation);
    }



}
