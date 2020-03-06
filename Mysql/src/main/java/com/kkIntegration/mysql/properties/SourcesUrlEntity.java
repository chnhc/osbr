package com.kkIntegration.mysql.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * description:
 * author: ckk
 * create: 2020-01-08 15:12
 */
@Getter
@Setter
public class SourcesUrlEntity {

    private String url;

    private String sourceName;


    public SourcesUrlEntity() {

    }

    public SourcesUrlEntity(String url, String sourceName) {
        this.url = url;
        this.sourceName = sourceName;
    }
}
