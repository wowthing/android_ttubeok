package com.example.ttubeog;

import android.app.Activity;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class resultScore extends Activity {

    String TAG = "resultScore_log";

    private RatingBar ratingBar;
    float current_rating;
    int rating_count = 0; //별점 개수
    float rating_total = 0; //별점 누적 기록
    float rating_avg = 0; //별점 평균

    //코스 측정화면에서 get_name으로 코스 이름 받아오기
    String get_name = "test1";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_resultscore);

        //코스 측정화면에서 시간 카운트 받아온 후 DB에 저장
        //코스 측정화면에서 그린 이동 기록 받아온 후 DB에 저장 https://dvlv.tistory.com/35

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new Listener());
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
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //별점 누적 계산
        rating_count = rating_count + 1;
        rating_total = rating_total + current_rating;
        rating_avg = rating_total / rating_count;

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
    }
}
