package com.example.ttubeog;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    String res;
    Socket socket = null;            //Server와 통신하기 위한 Socket
    InetAddress ia = null;

    public String clientTest(){

        try {
            ia = InetAddress.getByName("192.168.123.104");    //서버로 접속
            socket = new Socket(ia,9999);

            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            outStream.writeObject("0,0,0,0,0");
            outStream.flush();
            Log.d("ClientThread","서버로 보냄.");

            byte[] byteArr = null;
            String input = null;

            byteArr = new byte[100];
            InputStream instream = socket.getInputStream();
            int readByteCount = instream.read(byteArr);
            input = new String(byteArr, 0, readByteCount, "UTF-8"); // instream.readObject(); 서버로 부터 받은 데이터 , 핸들러 접근 가능하게 final 상수처럼 취급
            Log.d("ClientThread", "받은 데이터 : " + input);

            res = input;
        }catch(IOException e) {
            e.printStackTrace();
            res = "fail";
        }

        return res;
    }

}
