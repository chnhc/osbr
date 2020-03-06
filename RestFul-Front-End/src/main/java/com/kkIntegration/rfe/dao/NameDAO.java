package com.kkIntegration.rfe.dao;

import com.kkIntegration.rfe.entity.Name;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository(value = "nameDAO")
public interface NameDAO {

    Name selectNameById();

}
