package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class information extends Activity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;
    TextView course_name;
    TextView tag_1;
    TextView tag_2;
    TextView course_content;

    String TAG = "information_log";

    String content;
    GeoPoint location;
    List<Map<String, String>> tag;
    Button btn;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_information);

        btn=findViewById(R.id.choose_button);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), measure.class);
                startActivity(intent);
            }
        });


        //BottomFragment에서 코스 이름 가져오기
        Intent secondIntent = getIntent();
        String get_name = secondIntent.getStringExtra("get_title");

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        course_name = (TextView) findViewById(R.id.course_name);
        tag_1 = (TextView) findViewById(R.id.tag_1);
        tag_2 = (TextView) findViewById(R.id.tag_2);
        course_content = (TextView) findViewById(R.id.course_content);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //DB 값 불러오기
        DocumentReference docRef = db.collection("course").document(get_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        location = document.getGeoPoint("location");
                        //Log.d(TAG, "location: " + location);
                        tag = (List<Map<String, String>>) document.get("tag");
                        //Log.d(TAG, "tag: " + tag.get(0));
                        time = document.getLong("time").intValue();
                        //Log.d(TAG, "time: " + time);
                        content = document.getString("contents");
                        //Log.d(TAG, "content: " + content);
                        String name = document.getString("name");

                        course_name.setText(name);
                        //Map 띄우기
                        tag_1.setText("#" + (CharSequence) tag.get(0));
                        tag_2.setText("#" + (CharSequence) tag.get(1));
                        course_content.setText(content);
                        course_content.setMovementMethod(new ScrollingMovementMethod());


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
    public void onMapReady(@NonNull NaverMap naverMap) {

    }



}