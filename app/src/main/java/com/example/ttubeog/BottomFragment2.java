package com.example.ttubeog;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class BottomFragment2 extends Fragment {

    String TAG = "BottomFragment2_log";

    String id;
    String content;
    GeoPoint location;
    Array tag;

    String animal;
    String time;
    String length;
    String place;
    String purpose;

    int int_animal;
    int int_time;
    int int_length;
    int int_place;
    int int_purpose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        //return inflater.inflate(R.layout.fragment_bottom2, container, false);
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_bottom2, container, false);

        //태그 검색 클릭 리스너
        Button tag1 = (Button)rootview.findViewById(R.id.tag1);
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "test_tag1"; //태그 설정
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag2 = (Button)rootview.findViewById(R.id.tag2);
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "test_tag2";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag3 = (Button)rootview.findViewById(R.id.tag3);
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "test_tag3";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag4 = (Button)rootview.findViewById(R.id.tag4);
        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "가나다";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag5 = (Button)rootview.findViewById(R.id.tag5);
        tag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "test_tag1";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag6 = (Button)rootview.findViewById(R.id.tag6);
        tag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "test_tag2";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button course_1 = (Button)rootview.findViewById(R.id.rec_course1);
        course_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), information.class);
                //information.java로 course 이름 전달
                //test1 대신 course 이름 변수 입력
                String get_title = "test1";
                intent.putExtra("get_title", get_title);
                startActivity(intent);
            }
        });

        Button course_2 = (Button)rootview.findViewById(R.id.rec_course2);
        course_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //임시로 회원가입 창 뜨게 함
                //join.class 대신 information.class 넣기
                Intent intent = new Intent(getActivity(), Join.class);
                //information.java로 course 이름 전달
                //test1 대신 course 이름 변수 입력
                //String get_title = "test1";
                //intent.putExtra("get_title", get_title);
                startActivity(intent);
            }
        });

        //추가
        Button connect_btn = (Button)rootview.findViewById(R.id.connect_btn);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 데이터를 가져오는 작업이 잘 동작했을 떄
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                        // 데이터를 가져오는 작업이 에러났을 때
                        else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 데이터를 가져오는 작업이 잘 동작했을 떄
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> user = document.getData();
                                id = user.get("id").toString();
                                //"test_1" 대신 사용자 id 가져와서 넣기
                                if(id.equals("test_1")) {
                                    animal = user.get("animal").toString();
                                    time = user.get("time").toString();
                                    length = user.get("length").toString();
                                    place = user.get("place").toString();
                                    purpose = user.get("purpose").toString();

                                    if(animal.equals("유")){
                                        int_animal = 0;
                                    }else {
                                        int_animal = 1;
                                    }

                                    if(time.equals("낮")){
                                        int_time = 0;
                                    }else {
                                        int_time = 1;
                                    }

                                    if(length.equals("짧은 코스")){
                                        int_length = 0;
                                    }else {
                                        int_length = 1;
                                    }

                                    if(place.equals("숲")){
                                        int_place = 0;
                                    }else {
                                        int_place = 1;
                                    }

                                    if(purpose.equals("운동")){
                                        int_purpose = 0;
                                    }else {
                                        int_purpose = 1;
                                    }
                                }
                            }
                        }
                        // 데이터를 가져오는 작업이 에러났을 때
                        else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });
    }

    public static void recommend() {
        try {
            Socket socket = new Socket("192.168.123.104", 9999); // 소켓 서버에 접속
            System.out.println("socket 서버에 접속 성공!");
            //Log.d(TAG, document.getId() + " => " + document.getData());

            // OutputStream - 클라이언트에서 Server로 메세지 발송
            OutputStream out = socket.getOutputStream();
            // socket의 OutputStream 정보를 OutputStream out에 넣은 뒤
            PrintWriter writer = new PrintWriter(out, true);
            // PrintWriter에 위 OutputStream을 담아 사용

            writer.println("CLIENT TO SERVER");
            // 클라이언트에서 서버로 메세지 보내기

            // InputStream - Server에서 보낸 메세지 클라이언트로 가져옴
            InputStream input = socket.getInputStream();
            // socket의 InputStream 정보를 InputStream in에 넣은 뒤
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            // BufferedReader에 위 InputStream을 담아 사용

            System.out.println(reader.readLine());
            // 서버에서 온 메세지 확인
            System.out.println("CLIENT SOCKET CLOSE");
            socket.close(); // 소켓 종료

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}