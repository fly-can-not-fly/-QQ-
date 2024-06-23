package com.sfx.qqclient.utility;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Utility {
    private static Scanner sc=new Scanner(System.in);
    //读取一个字符
    public static char readOneChar() {
        return sc.next().charAt(0);
    }
    public  static String readString(){
        return sc.next();
    }

    //写入文件
    public static byte[] inFile(String filePath) {
        BufferedInputStream bufferedInputStream=null;
        //文件输入流
        try {
             bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            byte[] bytes = new byte[1024];
            int len=-1;
            StringBuilder s = new StringBuilder();
            while((len=bufferedInputStream.read(bytes ,0,1024))!=-1){
                s.append(Arrays.toString(bytes)) ;
            }
            return  s.toString().getBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭输入流
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //写出文件
    public  static void outFile(String fileDes,byte[] bytes){
        BufferedOutputStream bufferedOutputStream=null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileDes));
            bufferedOutputStream.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
