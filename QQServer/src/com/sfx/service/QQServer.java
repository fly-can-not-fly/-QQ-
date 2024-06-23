package com.sfx.service;

import com.sfx.qqcommon.Message;
import com.sfx.qqcommon.MessageType;
import com.sfx.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: QQServer
 * @package: com.sfx.service
 * @className: QQServer
 * @author: 孙飞翔
 * @description: 作为qq服务端，
 * @date: 2024/4/22 17:49
 * @version: 1.0
 */
public class QQServer {
    //创建一个集合存放我们的合法用户
    //hashmap线程不安全，ConcurrentHashMap
    private static ConcurrentHashMap<String ,User> validUser=new ConcurrentHashMap<>();
    private ServerSocket ss = null;

    //来一个静态代码块，在加载类时就创建我们的合法用户
    static {
        validUser.put("100" , new User("100","123"));
        validUser.put("200" , new User("200","123"));
        validUser.put("300" , new User("300","123"));
        validUser.put("400" , new User("400","123"));
    }
    //检查账户是否正确
    public boolean check(String userId,String pwd){
        User user=validUser.get(userId);
        if (user == null) {
            return false;
        }
        if (!(user.getPassword().equals("123"))) {
            return false;
        }
        return true;
    }
    public QQServer() {
        //创建服务端9999
        try {
            System.out.println("服务端在9999端口监听");
            ss = new ServerSocket(9999);
            //循环监听
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //第一次接收的是user判断用户登录是否通过
                User user = (User) ois.readObject();
                Message ms = new Message();
                //判断登录
                if (check(user.getUserId(), user.getPassword())) {
                    //登陆成功,返回信息，同时建立一个线程
                    ms.setMesType(MessageType.MESSAGE_PASS_SUCCEED);
                    oos.writeObject(ms);
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    //将该线程放入一个集合中
                    ManageServerThread.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                } else {
                    //登陆失败
                    ms.setMesType(MessageType.MESSAGE_PASS_FAILED);
                    oos.writeObject(ms);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
