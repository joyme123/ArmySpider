package com.myway5.www.searchEngine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class SocketClient {
    private String server = "127.0.0.1";
    private int port = 9999;
    private Socket socket;
    private OutputStream os;
    InputStreamReader reader;

    public SocketClient(){
        try {
            socket = new Socket(server, port);
            os = socket.getOutputStream();
            reader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public SocketClient(String server,int port){
    	this.server = server;
    	this.port = port;
        try {
            socket = new Socket(server, port);
            os = socket.getOutputStream();
            reader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send(byte[] b){
        try {
            os.write(b);
            os.flush();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void recv(){
        char[] c = new char[1024];
        StringBuilder sb = new StringBuilder();
        try {
            while((reader.read(c)) != -1){
                sb.append(new String(c));
                System.out.println(sb);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            os.close();
            socket.close();
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}