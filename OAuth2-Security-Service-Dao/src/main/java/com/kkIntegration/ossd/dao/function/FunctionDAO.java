package com.kkIntegration.ossd.dao.function;

import com.kkIntegration.ossd.entity.function.FunctionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "functionDAO")
public interface FunctionDAO {

    List<FunctionEntity> selectAllFun();

    List<FunctionEntity> selectFunLimit(Integer form_size, Integer to_size);

    void insertFun(FunctionEntity functionEntity);

    void updateFunId(String new_fun_id, Integer id, String fun_id);

    void updateFunName(String new_fun_name, Integer id, String fun_id);

    void deleteFun(Integer id, String fun_id);

    void updateFun(String new_fun_id, String new_fun_name, Integer id, String fun_id);

    int totalSize();
}
