package com.resto.shop.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/8/10.
 */
public class Announce implements Serializable {

    private static final long serialVersionUID = 4007104407337839816L;
    private String content;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("content", content)
                .append("type", type)
                .toString();
    }
}
