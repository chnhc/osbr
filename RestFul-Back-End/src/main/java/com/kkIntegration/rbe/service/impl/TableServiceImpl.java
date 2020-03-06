package com.kkIntegration.rbe.service.impl;

import com.kkIntegration.rbe.dao.table.TableDAO;
import com.kkIntegration.rbe.entity.TableEntity;
import com.kkIntegration.rbe.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * description:
 * author: ckk
 * create: 2019-12-30 14:43
 */
@Service("tableService")
public class TableServiceImpl implements TableService {

    @Autowired
    TableDAO tableDAO;


    @Override
    public int addTableBatch(Integer nums) {
        List<TableEntity> entities = new ArrayList<>();

        for(int num = 0 ; num <= 100; num++){
            entities.clear();
            // 格式化 String.format("%012d", i);
            for(int i = (nums+num)*10000 ; i < (nums+num+1)*10000; i++){
                String id = String.format("%012d", i)+"a&"+ UUID.randomUUID().toString();
                entities.add(new TableEntity(id, i+""));
            }
            tableDAO.addTableBatch(entities);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 10000;
    }
}
