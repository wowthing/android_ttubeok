package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class searchresult extends AppCompatActivity {

    String TAG = "search_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_result);

        //코스 이름
        TextView tag = (TextView) findViewById(R.id.search_tag);
        TextView search_course1 = (TextView) findViewById(R.id.search_course1);
        TextView search_course2 = (TextView) findViewById(R.id.search_course2);
        TextView search_course3 = (TextView) findViewById(R.id.search_course3);
        TextView search_course4 = (TextView) findViewById(R.id.search_course4);
        TextView search_course5 = (TextView) findViewById(R.id.search_course5);
        TextView search_course6 = (TextView) findViewById(R.id.search_course6);
        TextView search_course7 = (TextView) findViewById(R.id.search_course7);

        //레이아웃
        LinearLayout coursev1 = (LinearLayout)findViewById(R.id.course_list1);
        LinearLayout coursev2 = (LinearLayout)findViewById(R.id.course_list2);
        LinearLayout coursev3 = (LinearLayout)findViewById(R.id.course_list3);
        LinearLayout coursev4 = (LinearLayout)findViewById(R.id.course_list4);
        LinearLayout coursev5 = (LinearLayout)findViewById(R.id.course_list5);
        LinearLayout coursev6 = (LinearLayout)findViewById(R.id.course_list6);
        LinearLayout coursev7 = (LinearLayout)findViewById(R.id.course_list7);

        //데이터를 담을 리스트
        List course_title = new ArrayList();
        List course_num = new ArrayList();

        //파이어스토어 연결
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference dbRef = db.collection("course");

        //파이어베이스 스토리지
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathRef = storageRef.child("course");

        //코스 이미지
        ImageView imgview1 = (ImageView) findViewById(R.id.searchcourse1);
        ImageView imgview2 = (ImageView) findViewById(R.id.searchcourse2);
        ImageView imgview3 = (ImageView) findViewById(R.id.searchcourse3);
        ImageView imgview4 = (ImageView) findViewById(R.id.searchcourse4);
        ImageView imgview5 = (ImageView) findViewById(R.id.searchcourse5);
        ImageView imgview6 = (ImageView) findViewById(R.id.searchcourse6);
        ImageView imgview7 = (ImageView) findViewById(R.id.searchcourse7);

        //태그 받아오기
        Intent secondIntent = getIntent();
        String searchtag = secondIntent.getStringExtra("searchtag");

        db.collection("course")
                .whereArrayContainsAny("tag", Arrays.asList(searchtag))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            tag.setText(searchtag); //검색 태그 설정

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                course_title.add(document.getString("name"));
                                course_num.add(document.getString("num"));
                            }

                            int i;
                            int length = course_title.size();
                            int size = 8 - length;
                            for (i=0; i<size; i++) { course_title.add("null"); } //검색 결과 없을 시 null

                            if (course_title.get(0).equals("null")) {
                                coursev1.setVisibility(View.GONE);
                            } else {
                                search_course1.setText((CharSequence) course_title.get(0));
                                String name1 = (String) course_num.get(0);
                                //코스 사진 불러오기
                                StorageReference img = storageRef.child("course/").child(course_num.get(0)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview1);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name1);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(1).equals("null")) {
                                coursev2.setVisibility(View.GONE);
                            } else {
                                search_course2.setText((CharSequence) course_title.get(1));
                                String name2 = (String) course_num.get(1);
                                StorageReference img = storageRef.child("course/").child(course_num.get(1)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview2);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name2);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(2).equals("null")) {
                                coursev3.setVisibility(View.GONE);
                            } else {
                                search_course3.setText((CharSequence) course_title.get(2));
                                String name3 = (String) course_num.get(2);
                                StorageReference img = storageRef.child("course/").child(course_num.get(2)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview3);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name3);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(3).equals("null")) {
                                coursev4.setVisibility(View.GONE);
                            } else {
                                search_course4.setText((CharSequence) course_title.get(3));
                                String name4 = (String) course_num.get(3);
                                StorageReference img = storageRef.child("course/").child(course_num.get(3)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview4);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name4);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(4).equals("null")) {
                                coursev5.setVisibility(View.GONE);
                            } else {
                                search_course5.setText((CharSequence) course_title.get(4));
                                String name5 = (String) course_num.get(4);
                                StorageReference img = storageRef.child("course/").child(course_num.get(4)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview5);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name5);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(5).equals("null")) {
                                coursev6.setVisibility(View.GONE);
                            } else {
                                search_course6.setText((CharSequence) course_title.get(5));
                                String name6 = (String) course_num.get(5);
                                StorageReference img = storageRef.child("course/").child(course_num.get(5)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview6);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name6);
                                        startActivity(course_info);
                                    }
                                });
                            }

                            if (course_title.get(6).equals("null")) {
                                coursev7.setVisibility(View.GONE);
                            } else {
                                search_course7.setText((CharSequence) course_title.get(6));
                                String name7 = (String) course_num.get(6);
                                StorageReference img = storageRef.child("course/").child(course_num.get(6)+".jpg");
                                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(searchresult.this).load(uri)
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                                                .into(imgview7);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                coursev7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent course_info = new Intent(searchresult.this, information.class);
                                        course_info.putExtra("get_title", name7);
                                        startActivity(course_info);
                                    }
                                });
                            }
                        }

                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}