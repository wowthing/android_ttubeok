package com.example.ttubeog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    String get_recommendations;
    String[] recommendations;
    String get_preference;
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

        GetUserDataThread getUserDataThread = new GetUserDataThread();
        getUserDataThread.start();

        return rootview;
    }

    private class GetUserDataThread extends Thread {

        public void run(){
            Preference preference = new Preference();
            get_preference = preference.preferenceTest("ttubeok@test.com");
            Log.d(TAG, "선호도 조사 결과 (1): " + get_preference);
            animal = get_preference.substring(0,1);
            time = get_preference.substring(1,2);
            length = get_preference.substring(2,3);
            place = get_preference.substring(3,4);
            purpose = get_preference.substring(4);

            int_animal = Integer.parseInt(animal);
            int_time = Integer.parseInt(time);
            int_length = Integer.parseInt(length);
            int_place = Integer.parseInt(place);
            int_purpose = Integer.parseInt(purpose);

            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Client client = new Client();
            get_recommendations = client.clientTest(int_animal, int_time, int_length, int_place, int_purpose);
            Log.d(TAG, "유사한 유저 받아오기 (2): " + get_recommendations);
            recommendations = get_recommendations.split(",");
            Log.d(TAG, "유사한 유저 받아오기 (2)");

            recommendation_1 = preference.recomTest(recommendations[0]);
            recommendation_2 = preference.recom2Test(recommendations[0]);
            Log.d(TAG, "rec_num: " + recommendation_1);
            Log.d(TAG, "rec_num: " + recommendation_2);

            recommendation_1_name = preference.recomName(recommendations[0]);
            recommendation_2_name = preference.recom2Name(recommendations[0]);
            Log.d(TAG, "rec_name: " + recommendation_1_name);
            Log.d(TAG, "rec_name: " + recommendation_2_name);

            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }
    }

    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Button course_1 = (Button)getView().findViewById(R.id.rec_course1);
            course_1.setText(recommendation_1_name);
            course_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), information.class);
                    String get_title = recommendation_1;
                    intent.putExtra("get_title", get_title);
                    startActivity(intent);
                }
            });

            Button course_2 = (Button)getView().findViewById(R.id.rec_course2);
            course_2.setText(recommendation_2_name);
            course_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), information.class);
                    String get_title = recommendation_2;
                    intent.putExtra("get_title", get_title);
                    startActivity(intent);
                }
            });
        }
    };
}