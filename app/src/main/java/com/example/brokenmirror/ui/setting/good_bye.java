/*
__author__ = 'Song Chae Young'
__date__ = 'Nov.28, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'good_bye.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.R;

import java.util.List;

public class good_bye extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_bye);

        Button confirm_button = findViewById(R.id.good_bye_confirm_button);

        // Set statusBar color == basic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.basic));
        }

        // Button
        // confirm_button : OnClickListener
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();

                // close all activities
                for (ActivityManager.AppTask appTask : appTasks) {
                    appTask.finishAndRemoveTask();
                }
                // switch to the 'login_main' activity
                Intent intent = new Intent(good_bye.this, login_main.class);
                startActivity(intent);
                finish();

            }
        });
    }       // onCreate
}       // good_bye.java
