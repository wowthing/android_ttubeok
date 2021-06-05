package com.example.ttubeog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class measure extends AppCompatActivity {
    private Button mStartBtn, mStopBtn, mPauseBtn;
    private TextView mTimeTextView;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_measure);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#4ea1d3"));
        }

        //시작,일시정지,종료 버튼 만들기
        mStartBtn = (Button) findViewById(R.id.start_button);
        mStopBtn = (Button) findViewById(R.id.stop_button);
        mPauseBtn = (Button) findViewById(R.id.pause_button);
        //동작되는 타이머를 표시할 텍스트 뷰
        mTimeTextView = (TextView) findViewById(R.id.timeView);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mPauseBtn.setVisibility(v.VISIBLE);
                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeThread.interrupt();
                //종료시 resultScore로 이동
                Intent intent = new Intent(getApplicationContext(), resultScore.class);
                startActivity(intent);

            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning)
                    mPauseBtn.setText("일시정지");
                else
                    mPauseBtn.setText("시작");
            }

        });
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            mTimeTextView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}



