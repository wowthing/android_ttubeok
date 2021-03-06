package com.example.ttubeog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class BottomFragment2 extends Fragment {

    String TAG = "BottomFragment2_log";

    String id;
    private FirebaseAuth auth;

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

        //?????? ?????? ?????? ?????????
        Button tag1 = (Button)rootview.findViewById(R.id.tag1);
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "????????? ?????? ??? ??????"; //?????? ??????
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag2 = (Button)rootview.findViewById(R.id.tag2);
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "?????? ????????? ??????";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag3 = (Button)rootview.findViewById(R.id.tag3);
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "?????? ????????? ???";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag4 = (Button)rootview.findViewById(R.id.tag4);
        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "?????? ?????? ??? ??????";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag5 = (Button)rootview.findViewById(R.id.tag5);
        tag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "????????? ?????? ????????? ??????";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        Button tag6 = (Button)rootview.findViewById(R.id.tag6);
        tag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchresult.class);
                String searchtag = "?????? ?????????";
                intent.putExtra("searchtag", searchtag);
                startActivity(intent);
            }
        });

        //????????? ??????
        auth = FirebaseAuth.getInstance();
        String user_id = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        TextView username = (TextView)rootview.findViewById(R.id.name);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        username.setText(name);
                    } else {
                    }
            } }
        });

        GetUserDataThread getUserDataThread = new GetUserDataThread();
        getUserDataThread.start();

        return rootview;
    }

    private class GetUserDataThread extends Thread {

        public void run(){
            auth = FirebaseAuth.getInstance();
            String user_id = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

            Preference preference = new Preference();
            get_preference = preference.preferenceTest(user_id);
            Log.d(TAG, "????????? ?????? ?????? (1): " + get_preference);
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
            Log.d(TAG, "????????? ?????? ???????????? (2): " + get_recommendations);
            recommendations = get_recommendations.split(",");
            Log.d(TAG, "????????? ?????? ???????????? (2)");

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

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            LinearLayout rec1 = (LinearLayout)getView().findViewById(R.id.rec_course1);
            LinearLayout rec2 = (LinearLayout)getView().findViewById(R.id.rec_course2);
            TextView course_1 = (TextView)getView().findViewById(R.id.recommendation_1_name);
            TextView course_2 = (TextView)getView().findViewById(R.id.recommendation_2_name);
            ImageView imgview1 = (ImageView)getView().findViewById(R.id.recommendation_1_img);
            ImageView imgview2 = (ImageView)getView().findViewById(R.id.recommendation_2_img);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            //?????? ?????? 1
            course_1.setText(recommendation_1_name);
            StorageReference img1 = storageRef.child("course/").child(recommendation_1+".jpg");
            img1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(BottomFragment2.this).load(uri)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                            .into(imgview1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

            rec1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), information.class);
                    String get_title = recommendation_1;
                    intent.putExtra("get_title", get_title);
                    startActivity(intent);
                }
            });

            //?????? ?????? 2
            course_2.setText(recommendation_2_name);
            StorageReference img2 = storageRef.child("course/").child(recommendation_2+".jpg");
            img2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(BottomFragment2.this).load(uri)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(45)))
                            .into(imgview2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

            rec2.setOnClickListener(new View.OnClickListener() {
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