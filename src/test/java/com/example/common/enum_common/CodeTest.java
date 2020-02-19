package com.example.common.enum_common;


import ch.qos.logback.core.encoder.EchoEncoder;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author cenkang
 * @date 2019/12/28 - 15:40
 */
public class CodeTest extends Thread{
    // 发送线程
    private Socket socket;
    public CodeTest(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            while (true){
                Scanner scanner = new Scanner(System.in);
                String string = scanner.next();
                dataOutputStream.writeUTF(string);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class RecieveThread extends Thread{

    // 接收线程
    private Socket socket;
    public RecieveThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream outputStream = socket.getInputStream();
            DataInputStream dataOutputStream = new DataInputStream(outputStream);
            while (true){
                System.out.println(dataOutputStream.readUTF());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Server{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket accept = serverSocket.accept();
        new CodeTest(accept).start();
        new RecieveThread(accept).start();
    }
}
class Client{
    public static void main(String[] args) throws Exception {
      Socket socket = new Socket(InetAddress.getLocalHost(),8888);
        new CodeTest(socket).start();
        new RecieveThread(socket).start();
    }
}