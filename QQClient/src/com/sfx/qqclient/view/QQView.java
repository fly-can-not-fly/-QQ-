package com.sfx.qqclient.view;

import com.sfx.qqclient.utility.Utility;
import com.sfx.qqclient.sevice.ClientService;

public class QQView {
    //用于登陆服务，以及后面的注册服务
    private ClientService clientService=new ClientService();
    public static void main(String[] args) {
        new QQView().menu01();
    }
    //显示主菜单
    public void menu01() {
        boolean loop = true;
        char xz;//选择的选项
        String userId;//用户id
        String password;//用户密码
        while(loop){
            System.out.println("========欢迎登陆飞翔版QQ========");
            System.out.println("\t\t1 登录系统");
            System.out.println("\t\t9 退出系统");
            System.out.println("请选择：");
            xz= Utility.readOneChar();
            if(xz=='1'){
                System.out.println("请输入用户号");
                userId=Utility.readString();
                System.out.println("请输入密码");
                password=Utility.readString();
                //如果id 密码正确，则进入二级菜单
                //需要判断
                if(clientService.checkUser(userId ,password)){//这里到后面在补齐
                    menu02();
                    loop=false;
                }else {
                    System.out.println("账号或密码错误，请重新登录");
                }

            }else if(xz=='9'){
                System.out.println("退出系统");
                loop=false;
            }else {
                System.out.println("选择错误");
            }

        }
    }
    //显示二级菜单
    public void menu02() {
        //boolean loop=true;
        char xz;//选择的选项
        while (true) {
            System.out.println("========飞翔版二级菜单========");
            System.out.println("\t\t1 显示在线用户列表");
            System.out.println("\t\t2 群发消息");
            System.out.println("\t\t3 私发消息");
            System.out.println("\t\t4 发送文件");
            System.out.println("\t\t9 退出系统");
            System.out.println("请你选择");
            xz=Utility.readOneChar();
            switch (xz){
                case '1':
                    //System.out.println("====当前在线用户列表====");
                    clientService.onlineList();
                    break;
                case '2':
                    //System.out.println("群发消息");
                    System.out.println("输入想说的话");
                    String content_to_all = Utility.readString();
                    clientService.sendAll(content_to_all);
                    break;
                case '3':
                    //System.out.println("私发消息");
                    System.out.println("对方Id");
                    String getter=Utility.readString();
                    System.out.println("发送内容");
                    String content=Utility.readString();
                    clientService.sendToOne(getter,content);
                    break;
                case '4':
                    //System.out.println("发送文件");
                    System.out.println("文件地址:");
                    String filePath="C:\\壁纸图片\\p1.jpg";
                    System.out.println("接收方");
                    String receiver=Utility.readString();
                    clientService.sendFile(filePath,receiver);
                    break;
                case '9':
                    //System.out.println("退出系统");
                    clientService.exit();
                    return;

            }
        }
    }
}
