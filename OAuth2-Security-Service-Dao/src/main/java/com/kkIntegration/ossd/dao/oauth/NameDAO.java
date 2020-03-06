package com.kkIntegration.ossd.dao.oauth;

import com.kkIntegration.ossd.entity.oauth.Name;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository(value = "nameDAO")
public interface NameDAO {

    Name selectNameById();

    void insertName(Name name);

}
