package com.sfx.qqclient.sevice;

import com.sfx.qqclient.utility.Utility;
import com.sfx.qqcommon.Message;
import com.sfx.qqcommon.MessageType;
import com.sfx.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/**
 * @projectName: QQClient
 * @package: com.sfx.qqclient.sevice
 * @className: ClientService
 * @author: 孙飞翔
 * @description: TODO
 * @date: 2024/4/17 11:05
 * @version: 1.0
 */
public class ClientService {
    //将user做成属性，以便以后在其他地方使用
    private User user = new User();
    //socket做成属性，以便使用
    private Socket socket;

    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        user.setUserId(userId);
        user.setPassword(pwd);
//        链接到服务器
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //发送user的信息
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            //接收返回的信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //将信息强转成message
            Message ms = (Message) ois.readObject();

            //开始判断返回的信息是否成功
            if (ms.getMesType().equals(MessageType.MESSAGE_PASS_SUCCEED)) {
                //成功返回true
                b = true;
                //System.out.println("创建成功");
                //如果成功，启动一个线程，
                ClintConnectServiceThread clintConnectServiceThread =
                        new ClintConnectServiceThread(socket);
                //System.out.println("创建成功");
                clintConnectServiceThread.start();
                //并将这个线程放到集合中以便后续管理
                ManageClintThread.addClintConnectServiceThread(userId, clintConnectServiceThread);
            } else {
                //登陆失败，那么关闭该线程
                socket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    //向服务端请求在线列表
    public void onlineList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_LIST);
        message.setSender(user.getUserId());
        //向服务端发送
        try {
            //通过线程集合得到该线程，再得到线程内的socket，再从中得到输出流
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClintThread.getClintConnectServiceThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //请求关闭
    public void exit() {
        //设置信息
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLINT_EXIT);
        message.setSender(user.getUserId());
        //发送信息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //结束线程
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);//这个地方不是很理解
        }
    }

    //单发消息
    public void sendToOne(String getter,String content){
        //设置信息
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setGetter(getter);
        message.setSendTime(new Date().toString());
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_COMMON);
        //发送信息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //群发消息
    public void sendAll(String content){
        //消息填充
        Message message = new Message();
        message.setContent(content);
        message.setSender(user.getUserId());
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        //发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //发送文件
    public void sendFile(String filePath,String receiver){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE);
        message.setSender(user.getUserId());
        message.setGetter(receiver);
        message.setBytes(Utility.inFile(filePath));
        //发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
