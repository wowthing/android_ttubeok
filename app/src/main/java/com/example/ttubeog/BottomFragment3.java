package com.example.ttubeog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BottomFragment3 extends Fragment {

    String TAG = "BottomFragment3_log";

    //사용자 유저 정보 가져오는 것 필요
    String get_name = "test_1";

    String course_length;
    String step_count;
    String count;
    boolean today[]=new boolean[8];
    ImageView imagebtn[]=new ImageView[8];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bottom3, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //DB에서 값 불러오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(get_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        step_count = document.getLong("step_count").toString();
                        course_length = document.getLong("course_length").toString();
                        count = document.getLong("course_count").toString();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        //일요일되면 주간 운동날짜 초기화
                        String get_user = "test_1";
                        Map<String, Object> user_data = new HashMap<>();
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        String getTime = dateFormat.format(date);
                        Calendar cal=Calendar.getInstance();
                        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
                        if(dayOfWeek==1||getTime=="23:59:59"){
                            user_data.put("daySun",false);
                            user_data.put("dayMon",false);
                            user_data.put("dayTue",false);
                            user_data.put("dayWen",false);
                            user_data.put("dayThu",false);
                            user_data.put("dayFri",false);
                            user_data.put("daySat",false);
                        }
                        db.collection("user").document(get_user)
                                .set(user_data, SetOptions.merge())
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });

                        today[1]=document.getBoolean("dayMon");
                        today[2]=document.getBoolean("dayTue");
                        today[3]=document.getBoolean("dayWen");
                        today[4]=document.getBoolean("dayThu");
                        today[5]=document.getBoolean("dayFri");
                        today[6]=document.getBoolean("daySat");
                        today[7]=document.getBoolean("daySun");


                        //걸음 수 및 이동 거리
                        TextView step_count_view = (TextView)view.findViewById(R.id.step_count_view);
                        step_count_view.setText(step_count);
                        Log.d(TAG, "step_count: " + step_count);

                        TextView course_length_view = (TextView)view.findViewById(R.id.course_length_view);
                        course_length_view.setText(course_length);

                        TextView course_count = (TextView)view.findViewById(R.id.course_count);
                        course_count.setText(count);

                        imagebtn[1] = (ImageView)view.findViewById(R.id.day_1);
                        imagebtn[2]=  (ImageView)view.findViewById(R.id.day_2);
                        imagebtn[3] = (ImageView)view.findViewById(R.id.day_3);
                        imagebtn[4] = (ImageView)view.findViewById(R.id.day_4);
                        imagebtn[5] = (ImageView)view.findViewById(R.id.day_5);
                        imagebtn[6] = (ImageView)view.findViewById(R.id.day_6);
                        imagebtn[7] = (ImageView)view.findViewById(R.id.day_7);


                        for(int i=1;i<8;i++){
                            if(today[i])
                                imagebtn[i].setVisibility(View.VISIBLE);
                        }

                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
