package com.kkIntegration.ossd.service.function.impl;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.dao.function.FunctionDAO;
import com.kkIntegration.ossd.entity.function.FunctionEntity;
import com.kkIntegration.ossd.service.function.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2020-01-02 14:30
 */
@Service(value = "functionService")
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    FunctionDAO functionDAO;

    @Override
    public List<FunctionEntity> selectAllFun() {
        return functionDAO.selectAllFun();
    }

    @Override
    public PageEntity getLimitFun(int currentSize, int pageSize) {
        int totalSize = functionDAO.totalSize();
        PageEntity pageEntity = new PageEntity();
        // data数据
        pageEntity.setDataList(functionDAO.selectFunLimit((currentSize-1)*pageSize, pageSize));
        // 当前位置
        pageEntity.setCurrentSize(currentSize);
        // 每页数量
        pageEntity.setPageSize(pageSize);
        // 是否最后一页
        pageEntity.setLast(currentSize*pageSize >= totalSize);
        // 总的个数
        pageEntity.setTotalSize(totalSize);
        return pageEntity;
    }

    @Override
    public void insertFun(FunctionEntity functionEntity) {
        functionDAO.insertFun(functionEntity);
    }

    @Override
    public void updateFunId(String new_fun_id, Integer id, String fun_id) {
        functionDAO.updateFunId(new_fun_id, id, fun_id);
    }

    @Override
    public void updateFunName(String new_fun_name, Integer id, String fun_id) {
        functionDAO.updateFunName(new_fun_name, id, fun_id);
    }

    @Override
    public void updateFun(String new_fun_id, String new_fun_name, Integer id, String fun_id) {
        functionDAO.updateFun(new_fun_id, new_fun_name, id, fun_id);
    }

    @Override
    public void deleteFun(Integer id, String fun_id) {
        functionDAO.deleteFun(id, fun_id);
    }
}
