package com.sfx.qqcommon;

public interface MessageType {
    String MESSAGE_PASS_SUCCEED = "1";//“1”表示信息发送成功
    String MESSAGE_PASS_FAILED="2";//“2”表示信息发送失败
    String MESSAGE_COMMON="3";//表示普通信息包
    String MESSAGE_GET_ONLINE_LIST="4";//表示要求返回在线用户列表
    String MESSAGE_RET_ONLINE_LIST="5";//返回在线列表
    String MESSAGE_CLINT_EXIT="6";//表示客户端请求退出
    String MESSAGE_TO_ALL="7";//表示群发消息
    String MESSAGE_FILE="8";
}
