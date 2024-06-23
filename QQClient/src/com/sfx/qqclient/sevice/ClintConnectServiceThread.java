package com.sfx.qqclient.sevice;

import com.sfx.qqclient.utility.Utility;
import com.sfx.qqcommon.Message;
import com.sfx.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @projectName: QQClient
 * @package: com.sfx.qqclient.sevice
 * @className: ClintConnectServiceThread
 * @author: 孙飞翔
 * @description: TODO
 * @date: 2024/4/22 9:01
 * @version: 1.0
 */
public class ClintConnectServiceThread extends Thread {
    private Socket socket;
    public ClintConnectServiceThread(Socket socket){
        this.socket=socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());//如果没有信息将会阻塞在这里
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  ObjectInputStream ois=null;



    @Override
    public void run() {
        //该线程需要一直存在，保持通讯
        while(true){
            //从客户端接收信息
            try {
                 //if(ois==null) {
                //ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());
                 //}

                //如果没有信息，线程会阻塞在这里
                Message ms = (Message) ois.readObject();
                //如果这个mes是返回信息列表
                if(ms.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_LIST)){
                    //将信息进行分割for打印列表
                    String[] users=ms.getContent().split(" ");
                    System.out.println("\n====当前在线用户列表====");
                    for (int i = 0; i < users.length; i++) {
                        System.out.println("用户："+users[i]);
                    }
                }
                //接收单发消息
                else if (ms.getMesType().equals(MessageType.MESSAGE_COMMON)) {
                    System.out.println("\n"+ms.getSender()+":"+ms.getContent());
                    System.out.println(ms.getSendTime());

                }
                //接受群发消息
                else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {
                    System.out.println("\n" + ms.getSender() + ":");
                    System.out.println(ms.getContent());
                }
                //接收文件
                else if(ms.getMesType().equals(MessageType.MESSAGE_FILE)){
                    System.out.println(ms.getSender()+"给你发送了文件，你要放在那里");
                    String fileDes= "c:\\file_to_java\\p03.jpg";
                    Utility.outFile(fileDes,ms.getBytes());
                    System.out.println("接收完毕");

                }
                else{
                    System.out.println("其他信息，暂不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
