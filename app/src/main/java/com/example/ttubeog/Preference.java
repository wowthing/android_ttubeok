package com.example.ttubeog;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Preference {

    String preference = null;
    String rec_1;
    String rec_2;
    String name_1;
    String name_2;

    String TAG = "Preference_log";

    public String preferenceTest(String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String animal = (String) document.get("animal");
                        String time = (String) document.get("time");
                        String length = (String) document.get("length");
                        String place = (String) document.get("place");
                        String purpose = (String) document.get("purpose");

                        preference = animal + time + length + place + purpose;
                        Log.d(TAG, "preference 선호도 조사 결과 시간: " + preference);
                    } else {
                        Log.d(TAG, "선호도 조사 결과 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return preference;
    }

    public String recomTest(String recommendation_1){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db.collection("user_base").document(recommendation_1);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        rec_1 = (String) document.get("rec_1");
                    } else {
                        Log.d(TAG, "추천 코스1 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rec_1;
    }

    public String recom2Test(String recommendation_2){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db.collection("user_base").document(recommendation_2);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        rec_2 = (String) document.get("rec_2");
                    } else {
                        Log.d(TAG, "추천 코스2 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rec_2;
    }

    public String recomName(String recommendation_1){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db.collection("user_base").document(recommendation_1);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name_1 = (String) document.get("name_1");
                    } else {
                        Log.d(TAG, "추천 코스1 이름 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return name_1;
    }

    public String recom2Name(String recommendation_2){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db.collection("user_base").document(recommendation_2);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name_2 = (String) document.get("name_2");
                    } else {
                        Log.d(TAG, "추천 코스2 이름 불러오기 실패");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return name_2;
    }
}
