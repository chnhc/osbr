package com.kkIntegration.ossd.service.role.impl;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.dao.role.RoleUserDAO;
import com.kkIntegration.ossd.service.role.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 用户权限服务
 * author: ckk
 * create: 2020-01-14 11:24
 */
@Service("roleUserService")
public class RoleUserServiceImpl implements RoleUserService {

    @Autowired
    RoleUserDAO roleUserDAO;


    @Override
    public PageEntity getLimitRoleUser(int currentSize, int pageSize) {
        int totalSize = roleUserDAO.totalSize();
        PageEntity pageEntity = new PageEntity();
        // data数据
        pageEntity.setDataList(roleUserDAO.selectRoleUserLimit((currentSize - 1) * pageSize, pageSize));
        // 当前位置
        pageEntity.setCurrentSize(currentSize);
        // 每页数量
        pageEntity.setPageSize(pageSize);
        // 是否最后一页
        pageEntity.setLast(currentSize * pageSize >= totalSize);
        // 总的个数
        pageEntity.setTotalSize(totalSize);
        return pageEntity;

    }

    @Override
    public boolean authRoleUser(String target_id, List<String> target_roles) {
        List<String> roles = roleUserDAO.selectRoleUserByUserID(target_id);
        for(String s : roles) {
            if (!target_roles.contains(s)) {
                // 删除权限 s
                roleUserDAO.deleteRoleUser(target_id, s);
            }
        }
        for(String s : target_roles){
            if(!roles.contains(s)){
                // 添加权限 s
                roleUserDAO.insertRoleUser(target_id, s);
            }
        }
        return true;
    }
}
