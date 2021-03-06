package com.example.ttubeog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Join extends AppCompatActivity {

    String TAG = "Join_log";

    String purpose;
    String animal;
    String time;
    String length;
    String place;

    int zero = 0;
    boolean false_ = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        TextView txt_id_check = (TextView) findViewById(R.id.txt_id_check);
        txt_id_check.setVisibility(View.GONE);

        RadioGroup purpose_group = (RadioGroup) findViewById(R.id.purpose_group);
        purpose_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton select = (RadioButton)findViewById(checkedId);
                purpose = (String) select.getText();
            }
        });

        RadioGroup animal_group = (RadioGroup) findViewById(R.id.animal_group);
        animal_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton select = (RadioButton)findViewById(checkedId);
                animal = (String) select.getText();
            }
        });

        RadioGroup time_group = (RadioGroup) findViewById(R.id.time_group);
        time_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton select = (RadioButton)findViewById(checkedId);
                time = (String) select.getText();
            }
        });

        RadioGroup length_group = (RadioGroup) findViewById(R.id.length_group);
        length_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton select = (RadioButton)findViewById(checkedId);
                length = (String) select.getText();
            }
        });

        RadioGroup place_group = (RadioGroup) findViewById(R.id.place_group);
        place_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton select = (RadioButton)findViewById(checkedId);
                place = (String) select.getText();
            }
        });

        EditText join_id = (EditText) findViewById(R.id.join_id);
        Button btn_id_check = (Button) findViewById(R.id.btn_id_check);
        EditText join_pw = (EditText) findViewById(R.id.join_pw);
        EditText join_pw_check = (EditText) findViewById(R.id.join_pw_check);
        TextView pw_check_msg = (TextView) findViewById(R.id.pw_check_msg);
        EditText join_name = (EditText) findViewById(R.id.join_name);
        Button btn_join = (Button) findViewById(R.id.btn_join);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = join_id.getText().toString().trim();
                String pw = join_pw.getText().toString().trim();
                String name = join_name.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(Join.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    Map<String, Object> join_data = new HashMap<>();
                                    join_data.put("id", id);
                                    join_data.put("pw", pw);
                                    join_data.put("name", name);
                                    join_data.put("course_count", zero);
                                    join_data.put("course_length", zero);
                                    join_data.put("step_count", zero);
                                    join_data.put("time_count", zero);
                                    join_data.put("dayMon", false_);
                                    join_data.put("dayTue", false_);
                                    join_data.put("dayWen", false_);
                                    join_data.put("dayThu", false_);
                                    join_data.put("dayFri", false_);
                                    join_data.put("daySat", false_);
                                    join_data.put("daySun", false_);

                                    if(animal.equals("???")){
                                        animal = "0";
                                        join_data.put("animal", animal);
                                    }else {
                                        animal = "1";
                                        join_data.put("animal", animal);
                                    }

                                    if(time.equals("???")){
                                        time = "0";
                                        join_data.put("time", time);
                                    }else {
                                        time = "1";
                                        join_data.put("time", time);
                                    }

                                    if(length.equals("?????? ??????")){
                                        length = "0";
                                        join_data.put("length", length);
                                    }else {
                                        length = "1";
                                        join_data.put("length", length);
                                    }

                                    if(place.equals("???")){
                                        place = "0";
                                        join_data.put("place", place);
                                    }else {
                                        place = "1";
                                        join_data.put("place", place);
                                    }

                                    if(purpose.equals("??????")){
                                        purpose = "1";
                                        join_data.put("purpose", purpose);
                                    }else {
                                        purpose = "0";
                                        join_data.put("purpose", purpose);
                                    }

                                    db.collection("user").document(id)
                                            .set(join_data)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });

                                    Intent intent = new Intent(Join.this, AppStart.class);
                                    Toast.makeText(Join.this, "??????????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Join.this, "????????? ??????????????????. ?????? ??????????????????.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
            }
        });

    }

    public void btn_id_check(View v) {

        EditText join_id = (EditText) findViewById(R.id.join_id);
        TextView txt_id_check = (TextView) findViewById(R.id.txt_id_check);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference productRef = db.collection("user");
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //????????? ??????????????? ????????????
                if (task.isSuccessful()) {
                    //????????? ????????? ?????? ?????? ????????? ????????????.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, "DocumentSnapshot id: " + document.getId() + " DocumentSnapshot data: " + document.getData());
                        String db_id = document.getId().toString();
                        String input_id = join_id.getText().toString();
                        //Log.d(TAG, "db_id: " +db_id);
                        //Log.d(TAG, "input_id: " +input_id);
                        if (input_id.equals(db_id)) {
                            txt_id_check.setVisibility(View.VISIBLE);
                            //Log.d(TAG, "SET VISIBLE");
                        } else {
                            txt_id_check.setVisibility(View.GONE);
                            //Log.d(TAG, "NOT EQUAL");
                        }
                    }
                    //????????? ?????????
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        });
    }
}