package com.example.ttubeog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.lang.reflect.Array;


public class information extends Activity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;

    String TAG = "information_log";
    String content;
    GeoPoint location;
    Array tag;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_information);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

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
                                if (document.getId() == "test1") {
                                    location = (GeoPoint) document.get("location");
                                    tag = (Array) document.get("tag");
                                    time = (int) document.get("time");
                                    content = (String) document.get("content");
                                }
                            }
                        }
                        // 데이터를 가져오는 작업이 에러났을 때
                        else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });

        Log.d(TAG, time + content);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }
}
