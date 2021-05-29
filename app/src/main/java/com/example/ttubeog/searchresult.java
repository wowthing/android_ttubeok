package com.example.ttubeog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class searchresult extends Activity {

    String TAG = "search_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_result);

        TextView tag = (TextView) findViewById(R.id.search_tag);
        TextView search_course1 = (TextView) findViewById(R.id.search_course1);
        TextView search_course2 = (TextView) findViewById(R.id.search_course2);
        TextView search_course3 = (TextView) findViewById(R.id.search_course3);
        TextView search_course4 = (TextView) findViewById(R.id.search_course4);
        TextView search_course5 = (TextView) findViewById(R.id.search_course5);

        LinearLayout coursev1 = (LinearLayout)findViewById(R.id.course_list1);
        LinearLayout coursev2 = (LinearLayout)findViewById(R.id.course_list2);
        LinearLayout coursev3 = (LinearLayout)findViewById(R.id.course_list3);
        LinearLayout coursev4 = (LinearLayout)findViewById(R.id.course_list4);
        LinearLayout coursev5 = (LinearLayout)findViewById(R.id.course_list5);

        List course_title = new ArrayList();

        //파이어스토어 연결
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference dbRef = db.collection("course");

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

                            //검색 태그 설정
                            tag.setText(searchtag);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                course_title.add(document.getString("contents"));
                            }

                            int i;
                            int length = course_title.size();
                            int size = 6 - length;
                            for (i=0; i<size; i++) { course_title.add("null"); }

                            if (course_title.get(0).equals("null")) {
                                coursev1.setVisibility(View.GONE);
                            } else {
                                search_course1.setText((CharSequence) course_title.get(0));
                            }

                            if (course_title.get(1).equals("null")) {
                                coursev2.setVisibility(View.GONE);
                            } else {
                                search_course2.setText((CharSequence) course_title.get(1));
                            }

                            if (course_title.get(2).equals("null")) {
                                coursev3.setVisibility(View.GONE);
                            } else {
                                search_course3.setText((CharSequence) course_title.get(2));
                            }

                            if (course_title.get(3).equals("null")) {
                                coursev4.setVisibility(View.GONE);
                            } else {
                                search_course4.setText((CharSequence) course_title.get(3));
                            }

                            if (course_title.get(4).equals("null")) {
                                coursev5.setVisibility(View.GONE);
                            } else {
                                search_course5.setText((CharSequence) course_title.get(4));
                            }

                        }

                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}