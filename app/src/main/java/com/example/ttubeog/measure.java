package com.example.ttubeog;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class measure extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {
    /*private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;*/

    String TAG = "measure_log";

    //이전 화면에서 코스 이름, 유저 이름 가져오는 것 필요
    String get_name = "course_1";

    private Button mStartBtn, mStopBtn, mPauseBtn;
    private TextView mTimeTextView;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    /*//infromation에서 넘어온 course_name 받아서 코스 이름으로 저장
    Intent secondIntent = getIntent();
    String c_name=secondIntent.getStringExtra("name");*/
    TextView course_name;

    SensorManager sensorManager;
    Sensor stepCountSensor;
    // 현재 걸음 수
    int currentSteps;

    float course_length;
    String loc_long;
    String loc_la;

    @SuppressLint("DefaultLocale") String result;
    float time;

    //지도 추가
    private MapView mapView;
    private static NaverMap naverMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_measure);


        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
//        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //코스 이름
        course_name = (TextView) findViewById(R.id.course_name);

        //DB에서 코스 거리 불러오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("course").document(get_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        course_length = document.getLong("length");
                        loc_la = document.getString("loc_la");
                        loc_long = document.getString("loc_long");
                        //Log.d(TAG, "가져온 rating_count: " + rating_count);
                        //Log.d(TAG, "가져온 rating_total: " + rating_total);
                        Log.d(TAG, "가져온 location: " + loc_la + loc_long);
                        String name = document.getString("name");
                        course_name.setText(name);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#698474"));
        }

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        //시작,일시정지,종료 버튼 만들기
        mStartBtn = (Button) findViewById(R.id.start_button);
        mStopBtn = (Button) findViewById(R.id.stop_button);
        mPauseBtn = (Button) findViewById(R.id.pause_button);
        //동작되는 타이머를 표시할 텍스트 뷰
        mTimeTextView = (TextView) findViewById(R.id.timeView);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                currentSteps = 0;
                mPauseBtn.setVisibility(v.VISIBLE);
                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeThread.interrupt();
                //종료시 resultScore로 이동
                Intent intent = new Intent(getApplicationContext(), resultScore.class);
                float time_count = time;
                //Log.d(TAG, "time_count: " + time_count);
                intent.putExtra("time_count", time_count);
                intent.putExtra("step_count", currentSteps);
                intent.putExtra("course_length", course_length);
                startActivity(intent);
                finish();
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning)
                    mPauseBtn.setText("일시정지");
                else
                    mPauseBtn.setText("시작");
            }

        });
    }

    //지도 추가
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
        measure.naverMap = naverMap;
//        naverMap.setLocationSource(locationSource);
//        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//
//        UiSettings uiSettings = naverMap.getUiSettings();
//        uiSettings.setLocationButtonEnabled(true); //현 위치

        float loc_latitude = Float.parseFloat(loc_la);
        float loc_longitude = Float.parseFloat(loc_long);
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(loc_latitude, loc_longitude), 16
        );
        naverMap.setCameraPosition(cameraPosition);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            time = hour*60 + min + sec/60;
            mTimeTextView.setText(result);
            //Log.d(TAG, "time: " + time);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}



