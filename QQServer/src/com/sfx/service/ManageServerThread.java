package com.sfx.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @projectName: QQServer
 * @package: com.sfx.service
 * @className: ManageServerThread
 * @author: 孙飞翔
 * @description: TODO管理服务端线程
 * @date: 2024/4/22 18:33
 * @version: 1.0
 */
public class ManageServerThread {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static void addServerConnectClientThread
            (String userId, ServerConnectClientThread serverConnectClientThread) {
            hm.put(userId,serverConnectClientThread);
    }

    //移除该线程
    public static void removeThread(String userId){
        hm.remove(userId);
    }
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }

    //得到用户列表
    public static String getUserList(){
        //hm里面key都是user
        Iterator<String> iterator = hm.keySet().iterator();
        StringBuilder userList= new StringBuilder();
        while (iterator.hasNext()){
            userList.append(iterator.next()).append(" ");
        }
        return userList.toString();
    }

}
