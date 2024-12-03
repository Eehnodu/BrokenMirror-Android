/*
__author__ = 'Song Chae Young'
__date__ = 'Nov.01, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'first_access.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.ui.setting.service_guide;

public class first_access extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_access);

        Button next_button = findViewById(R.id.next_button);

        // Button
        // next_button : OnClickListener
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(first_access.this, service_guide.class);
                startActivity(intent);
                finish();
            }
        });

    }   // onCreate

    private boolean backButtonPressedOnce = false;
    private Handler handler = new Handler();
    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds
    @Override
    public void onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed(); // 두 번째 뒤로가기 버튼 눌림
            Log.d("ghkrdls", "Activity destroyed");
            handler.removeCallbacksAndMessages(null); // 핸들러 메시지 제거
        } else {
            if (isTaskRoot()) {
                backButtonPressedOnce = true; // 첫 번째 뒤로가기 버튼 눌림
                Toast.makeText(this, getResources().getText(R.string.app_exit), Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backButtonPressedOnce = false; // 2초가 지나면 초기화
                    }
                }, DOUBLE_BACK_PRESS_INTERVAL);
            } else {
                super.onBackPressed(); // 루트가 아닐 때
            }
        }
    }

}   // first_access.class
