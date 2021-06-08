package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class information extends Activity {

    TextView course_name;
    TextView tag_1;
    TextView tag_2;
    TextView course_content;

    String TAG = "information_log";

    String content;
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

        //파이어베이스 스토리지
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathRef = storageRef.child("course");
        ImageView imgview = (ImageView) findViewById(R.id.image_view);
        
        if (pathRef == null) {
            Toast.makeText(information.this, "사진이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference img = storageRef.child("course/").child(get_name+".jpg");
            img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 뷰에 사진 띄우기 (06-08 코스6까지 테스트)
                    Glide.with(information.this).load(uri)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                            .into(imgview);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

        course_name = (TextView) findViewById(R.id.course_name);
        tag_1 = (TextView) findViewById(R.id.tag_1);
        tag_2 = (TextView) findViewById(R.id.tag_2);
        course_content = (TextView) findViewById(R.id.course_content);

        //파이어스토어
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
                        tag = (List<Map<String, String>>) document.get("tag");
                        //Log.d(TAG, "tag: " + tag.get(0));
                        time = document.getLong("time").intValue();
                        //Log.d(TAG, "time: " + time);
                        content = document.getString("contents");
                        //Log.d(TAG, "content: " + content);
                        String name = document.getString("name");

                        course_name.setText(name);
                        //Map 띄우기
                        tag_1.setText("# " + (CharSequence) tag.get(0));
                        tag_2.setText("# " + (CharSequence) tag.get(1));
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
}