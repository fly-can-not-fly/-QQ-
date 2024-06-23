package com.sfx.qqcommon;

import java.io.Serializable;

/**
 * 客户端和服务端信息传递的对象
 */
public class Message implements Serializable {
    //进行序列化时一定加上这句话
    private static final long serialVersionUID = 1L;
    private String sender;//信息的发送者
    private String getter;//信息的接收者
    private String content;//信息的内容
    private String mesType;//信息的类型
    private String sendTime;//信息的发送时间
    private byte[] bytes;//将文件以字节数组的形式发送

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Message(String sender, String getter, String content, String mesType, String sendTime) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.mesType = mesType;
        this.sendTime = sendTime;
    }

    public Message() {
    }
}
