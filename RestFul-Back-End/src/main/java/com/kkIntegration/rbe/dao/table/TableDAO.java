package com.kkIntegration.rbe.dao.table;

import com.kkIntegration.rbe.entity.TableEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "tableDAO")
public interface TableDAO {

    int addTableBatch(@Param("tables") List<TableEntity> tables);

}
