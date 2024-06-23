package com.sfx.service;

import com.sfx.qqcommon.Message;
import com.sfx.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @projectName: QQServer
 * @package: com.sfx.service
 * @className: ServerConnectClientThread
 * @author: 孙飞翔
 * @description: 该类用于让服务端与客户端保持通讯
 * @date: 2024/4/22 18:18
 * @version: 1.0
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;
    //用于存放离线留言,String表示Id
    private static HashMap<String, ArrayList<Message>> liu_yan_messages = new HashMap<>();
    private static ArrayList<Message> arrayList1 = new ArrayList<>();
    private static ArrayList<Message> arrayList2 = new ArrayList<>();
    private static ArrayList<Message> arrayList3 = new ArrayList<>();
    private static ArrayList<Message> arrayList4 = new ArrayList<>();

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    //发送留言方法
    public void send() {
        HashMap<String, ServerConnectClientThread> hm = ManageServerThread.getHm();
        //Set<String> Ids = hm.keySet();//在线Id
        Set<String> getters = liu_yan_messages.keySet();//留言Id
        for (String getter : getters) {
            if (hm.get(getter) != null) {
                try {
                    ObjectOutputStream oos =
                            new ObjectOutputStream(hm.get(getter).getSocket().getOutputStream());
                    for (Message ms : liu_yan_messages.get(getter)) {
                        //ObjectOutputStream oos =
                                //new ObjectOutputStream(hm.get(getter).getSocket().getOutputStream());
                        oos.writeObject(ms);

                    }
                    System.out.println("留言发送成功");
                    //删除留言
                    liu_yan_messages.remove(getter);
                    delArraylist(getter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除相应的arraylist
    public void delArraylist(String Id) {
        if (Id.equals("100")) {
            arrayList1.clear();
        }
        if (Id.equals("200")) {
            arrayList2.clear();
        }
        if (Id.equals("300")) {
            arrayList3.clear();
        }
        if (Id.equals("400")) {
            arrayList4.clear();
        }
        System.out.println("留言删除成功");
    }

    //保存留言
    public void save(Message ms) {
        if (ms.getGetter().equals("100")) {
            arrayList1.add(ms);
            liu_yan_messages.put(ms.getGetter(), arrayList1);
        }
        if (ms.getGetter().equals("200")) {
            arrayList2.add(ms);
            liu_yan_messages.put(ms.getGetter(), arrayList2);

        }
        if (ms.getGetter().equals("300")) {
            arrayList3.add(ms);
            liu_yan_messages.put(ms.getGetter(), arrayList3);
        }
        if (ms.getGetter().equals("400")) {
            arrayList4.add(ms);
            liu_yan_messages.put(ms.getGetter(), arrayList4);
        }
        System.out.println("信息保存成功");
    }

    @Override
    public void run() {
        while (true) {
            try {
                //发送留言消息
                send();
                System.out.println("服务端与客户端" + userId + "保持通讯");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms = (Message) ois.readObject();

                //这里处理要求返回用户列表
                if (ms.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_LIST)) {
                    System.out.println(ms.getSender() + "请求用户列表");
                    String userList = ManageServerThread.getUserList();
                    //设施message的信息
                    Message message = new Message();
                    message.setMesType(MessageType.MESSAGE_RET_ONLINE_LIST);
                    message.setContent(userList);
                    message.setGetter(ms.getSender());
                    //返回信息
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message);
                }
                //退出线程
                else if (ms.getMesType().equals(MessageType.MESSAGE_CLINT_EXIT)) {
                    System.out.println(ms.getSender() + "请求退出程序");
                    //移除该线程
                    ManageServerThread.removeThread(userId);
                    //关闭通道
                    socket.close();
                    //关闭通道后，线程依然进行,必须break跳出run方法,结束线程
                    break;
                }
                //单发消息
                else if (ms.getMesType().equals(MessageType.MESSAGE_COMMON)) {
                    System.out.println(ms.getSender() + "发消息给" + ms.getGetter());
                    //得到对方线程
                    ServerConnectClientThread serverConnectClientThread =
                            ManageServerThread.getServerConnectClientThread(ms.getGetter());
                    //得到socket
                    if (serverConnectClientThread != null) {//对方在线
                        Socket socket1 = serverConnectClientThread.getSocket();
                        //发送信息
                        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                        oos.writeObject(ms);
                    } else {//对方不在线,将信息放入集合
                        save(ms);
                    }
                }
                //群发消息
                else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {
                    //得到线程集合
                    //遍历线程
                    //每个socket都发送,除自身
                    HashMap<String, ServerConnectClientThread> hm = ManageServerThread.getHm();
                    for (String userId : hm.keySet()) {
                        if (!userId.equals(ms.getSender())) {
                            ObjectOutputStream oos = new
                                    ObjectOutputStream(hm.get(userId).getSocket().getOutputStream());
                            oos.writeObject(ms);
                        }
                    }
                    System.out.println("群发已完成");

                }
                //发送文件
                else if (ms.getMesType().equals(MessageType.MESSAGE_FILE)) {
                    System.out.println(ms.getSender() + "发文件给" + ms.getGetter());
                    //得到对方线程
                    ServerConnectClientThread serverConnectClientThread =
                            ManageServerThread.getServerConnectClientThread(ms.getGetter());
                    //得到socket
                    Socket socket1 = serverConnectClientThread.getSocket();
                    //发送信息
                    ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                    oos.writeObject(ms);
                    System.out.println("文件发送完毕");
                } else {
                    //其他情况再是不处理
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
