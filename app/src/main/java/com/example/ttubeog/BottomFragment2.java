package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.Map;

public class BottomFragment2 extends Fragment {

    String TAG = "BottomFragment2_log";

    String id;

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

    TextView connect_txt;

    String get_recommendations;
    String[] recommendations;
    String recommendation_1;
    String recommendation_1_name;
    String recommendation_2;
    String recommendation_2_name;

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
                String searchtag = "밤에도 걸을 수 있는"; //태그 설정
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag2 = (Button)rootview.findViewById(R.id.tag2);
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "코스 길이가 짧은";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag3 = (Button)rootview.findViewById(R.id.tag3);
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "코스 길이가 긴";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag4 = (Button)rootview.findViewById(R.id.tag4);
        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "숲을 즐길 수 있는";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag5 = (Button)rootview.findViewById(R.id.tag5);
        tag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "접근이 편한 도심에 있는";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag6 = (Button)rootview.findViewById(R.id.tag6);
        tag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "운동 목적인";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button course_1 = (Button)rootview.findViewById(R.id.rec_course1);
        course_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), information.class);
                String get_title = recommendation_1;
//                course_1.setText(recommendation_1_name);
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
                Intent intent = new Intent(getActivity(),Join.class);
                //information.java로 course 이름 전달
                //test1 대신 course 이름 변수 입력
                //String get_title = "test1";
                //intent.putExtra("get_title", get_title);
                startActivity(intent);
            }
        });

        connect_txt = (TextView)rootview.findViewById(R.id.connect_txt);
        GetUserDataThread getUserDataThread = new GetUserDataThread();
        getUserDataThread.start();

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //userID 불러와서 넣기
        DocumentReference docRef = db.collection("user").document("ttubeok@test.com");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        animal = (String) document.get("animal");
                        time = (String) document.get("time");
                        length = (String) document.get("length");
                        place = (String) document.get("place");
                        purpose = (String) document.get("purpose");

                        int_animal = Integer.parseInt(animal);
                        int_time = Integer.parseInt(time);
                        int_length = Integer.parseInt(length);
                        int_place = Integer.parseInt(place);
                        int_purpose = Integer.parseInt(purpose);

                        Log.d(TAG, "선호도 조사 결과 받아오기 (1)");

                    } else {
                        Log.d(TAG, "선호도 조사 결과 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DocumentReference docRef2 = db.collection("user_base").document(recommendations[0]);
        Log.d(TAG, "추천 코스 불러오기 (3)");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        recommendation_1 = (String) document.get("rec_1");
                        recommendation_1_name = (String) document.get("name_1");
                        recommendation_2 = (String) document.get("rec_2");
                        recommendation_2_name = (String) document.get("name_2");

                        Button course_1 = (Button)view.findViewById(R.id.rec_course1);
                        course_1.setText(recommendation_1_name);

                        Button course_2 = (Button)view.findViewById(R.id.rec_course2);
                        course_2.setText(recommendation_2_name);

                    } else {
                        Log.d(TAG, "추천 코스 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private class GetUserDataThread extends Thread {

        public void run(){
            Client client = new Client();
            get_recommendations = client.clientTest(int_animal, int_time, int_length, int_place, int_purpose);
            connect_txt.setText(get_recommendations);
            recommendations = get_recommendations.split(",");
            Log.d(TAG, "유사한 유저 받아오기 (2)");
            for(int i = 0; i< recommendations.length; i++){
                Log.d(TAG, recommendations[i]);
            }
        }
    }
}