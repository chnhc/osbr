package com.kkIntegration.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * description: 统一分页实体
 * author: ckk
 * create: 2020-01-03 14:07
 */
@Getter
@Setter
public class PageEntity {

    int pageSize;

    int totalSize;

    int currentSize;

    boolean isLast;

    Object dataList;
}
