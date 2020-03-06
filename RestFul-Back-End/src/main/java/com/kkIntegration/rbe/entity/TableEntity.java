package com.kkIntegration.rbe.entity;

import lombok.Data;

/**
 * description:
 * author: ckk
 * create: 2019-12-30 14:41
 */
@Data
public class TableEntity {

    public TableEntity() {
    }

    public TableEntity(String t_id, String t_name) {
        this.t_id = t_id;
        this.t_name = t_name;
    }

    String t_id;

    String t_name;

}
