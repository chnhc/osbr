package com.kkIntegration.rfe.service.impl;

import com.kkIntegration.rfe.dao.NameDAO;
import com.kkIntegration.rfe.entity.Name;
import com.kkIntegration.rfe.service.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:
 * author: ckk
 * create: 2019-06-29 11:18
 */

@Service(value = "nameService")
public class NameServiceImpl implements NameService {

    @Autowired
    NameDAO nameDAO;

    @Override
    public Name selectNameById() {
        return nameDAO.selectNameById();
    }
}
