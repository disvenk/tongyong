package com.resto.api.brand.dto;

/**
 * Created by carl on 2017/8/8.
 */
public class MdlUpload {
    private String type;
    private String media_id;
    private String created_at;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMedia_id() {
        return media_id;
    }
    public void setMedia_id(String mediaId) {
        media_id = mediaId;
    }
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String createdAt) {
        created_at = createdAt;
    }
    public MdlUpload() {
        super();
    }
    @Override
    public String toString() {
        return "MdlUpload [created_at=" + created_at + ", media_id=" + media_id + ", type=" + type + "]";
    }
}
