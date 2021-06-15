package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class resultScore extends Activity implements OnMapReadyCallback{

    String TAG = "resultScore_log";

    private RatingBar ratingBar;
    float current_rating;
    int rating_count = 0; //별점 개수
    float rating_total = 0; //별점 누적 기록
    float rating_avg = 0; //별점 평균
    float past_time_count;
    float time_count;
    int course_count; // 코스 누적 기록
    int past_step_count;
    float step_count; // 걸음 수 누적 기록
    float course_length;
    float past_course_length;
    String loc_long;
    String loc_la;

    //코스 측정화면에서 get_name으로 코스 이름 받아오기
    //코스 측정화면에서 get_user로 유저 이름 받아오기
    String get_user = "test_1";
    String get_name = "course_1";

    private MapView mapView;
    private static NaverMap naverMap;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_resultscore);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //BottomFragment에서 코스 이름 가져오기
        //measure.java에서 걸은 시간, 걸음 수 가져옴
        Intent secondIntent = getIntent();
        time_count = secondIntent.getFloatExtra("time_count", time_count);
        step_count = secondIntent.getFloatExtra("step_count", step_count);
        course_length = secondIntent.getFloatExtra("course_length", course_length);
        Log.d(TAG, "받아온 time_count: " + time_count);

        TextView course_result = (TextView)findViewById(R.id.course_result);
        String set_text = "산책 시간: " + time_count + "분" + "\n"
                        + "걸은 걸음: " + step_count + "걸음" + "\n"
                        + "이동한 거리: " + course_length + "km";
        course_result.setText(set_text);

        //코스 측정화면에서 그린 이동 기록 받아온 후 DB에 저장 https://dvlv.tistory.com/35

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new Listener());

        //DB에서 별점 값 불러오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("course").document(get_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        rating_count = document.getLong("rating_count").intValue();
                        rating_total = document.getLong("rating_total").intValue();
                        loc_la = document.getString("loc_la");
                        loc_long = document.getString("loc_long");
                        //Log.d(TAG, "가져온 rating_count: " + rating_count);
                        //Log.d(TAG, "가져온 rating_total: " + rating_total);
                        Log.d(TAG, "가져온 location: " + loc_la + loc_long);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRef2 = db.collection("user").document(get_user);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        course_count = document.getLong("course_count").intValue();
                        past_time_count = document.getLong("time_count");
                        past_step_count = document.getLong("step_count").intValue();
                        past_course_length = document.getLong("course_length");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        float loc_latitude = Float.parseFloat(loc_la);
        float loc_longitude = Float.parseFloat(loc_long);
        CameraPosition cameraPosition = new CameraPosition(
        new LatLng(loc_latitude, loc_longitude), 16
        );
        naverMap.setCameraPosition(cameraPosition);
    }

    class Listener implements RatingBar.OnRatingBarChangeListener{
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingBar.setRating(rating);
            rating = ratingBar.getRating();
            current_rating = rating;
        }
    }

    public void btn_choose(View v) {

        //별점 누적 계산
        rating_count = rating_count + 1;
        rating_total = rating_total + current_rating;
        rating_avg = rating_total / rating_count;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //DB에 별점 저장하기
        Map<String, Object> result_data = new HashMap<>();
        result_data.put("rating_count", rating_count);
        result_data.put("rating_total", rating_total);
        result_data.put("rating_avg", rating_avg);
        db.collection("course").document(get_name)
                .set(result_data, SetOptions.merge())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        //DB에 측정 시간, 걸음 수, 완료코스+1 저장하기
        Map<String, Object> user_data = new HashMap<>();
        user_data.put("time_count", past_time_count + time_count);
        user_data.put("step_count", past_step_count + step_count);
        user_data.put("course_length", past_course_length + course_length);
        user_data.put("course_count", course_count + 1);
        //현재요일 구해서 운동한 날 DB에 저장
        Calendar cal=Calendar.getInstance();
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
        switch(dayOfWeek){
            case 1:
                user_data.put("daySun",true);
                break;
            case 2:
                user_data.put("dayMon",true);
                break;
            case 3:
                user_data.put("dayTue",true);
                break;
            case 4:
                user_data.put("dayWen",true);
                break;
            case 5:
                user_data.put("dayThu",true);
                break;
            case 6:
                user_data.put("dayFri",true);
                break;
            case 7:
                user_data.put("daySat",true);
                break;
        }

        db.collection("user").document(get_user)
                .set(user_data, SetOptions.merge())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        finish();
    }
}
