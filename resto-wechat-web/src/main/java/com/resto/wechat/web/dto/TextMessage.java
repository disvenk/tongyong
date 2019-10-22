package com.resto.wechat.web.dto;

import java.util.List;

public class TextMessage {
    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MsgType;

    private String Content;
    private String MsgId;
    private String Event;
    private String SuccOrderId;
    private String FailOrderId;
    private String AuthorizeAppId;
    private String Source;

    public String getAuthorizeAppId() {
        return AuthorizeAppId;
    }

    public void setAuthorizeAppId(String authorizeAppId) {
        AuthorizeAppId = authorizeAppId;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getSuccOrderId() {
        return SuccOrderId;
    }

    public void setSuccOrderId(String succOrderId) {
        SuccOrderId = succOrderId;
    }

    public String getFailOrderId() {
        return FailOrderId;
    }

    public void setFailOrderId(String failOrderId) {
        FailOrderId = failOrderId;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
