package com.kkIntegration.ossd.service.function;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.entity.function.FunctionEntity;

import java.util.List;

public interface FunctionService {

    List<FunctionEntity> selectAllFun();

    PageEntity getLimitFun(int currentSize, int pageSize);

    void insertFun(FunctionEntity functionEntity);

    void updateFunId(String new_fun_id, Integer id, String fun_id);

    void updateFunName(String new_fun_name, Integer id, String fun_id);

    void updateFun(String new_fun_id, String new_fun_name, Integer id, String fun_id);

    void deleteFun(Integer id, String fun_id);
}
