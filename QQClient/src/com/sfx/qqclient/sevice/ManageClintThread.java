package com.sfx.qqclient.sevice;

import java.util.HashMap;

/**
 * @projectName: QQClient
 * @package: com.sfx.qqclient.sevice
 * @className: ManageClintThread
 * @author: 孙飞翔
 * @description: 该类用于管理客户端链接服务端的线程
 * @date: 2024/4/22 9:39
 * @version: 1.0
 */
public class ManageClintThread {
    //存放线程的集合，key为用户id
    private static HashMap<String, ClintConnectServiceThread> hm = new HashMap<>();

    //将某个线程放入该集合
    public static void addClintConnectServiceThread(String userId, ClintConnectServiceThread clintConnectServiceThread) {
        hm.put(userId, clintConnectServiceThread);
    }
    //通过id获得线程
    public static ClintConnectServiceThread getClintConnectServiceThread(String userId){
        return hm.get(userId);
    }
}
