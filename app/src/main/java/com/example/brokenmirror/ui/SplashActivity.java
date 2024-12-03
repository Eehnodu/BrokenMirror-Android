/*
__author__ = 'Song Chae Young'
__date__ = 'Oct.10, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'SplashActivity.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Production'
*/
package com.example.brokenmirror.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.MainActivity;
import com.example.brokenmirror.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView splashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.basic));
        }

        splashLogo = findViewById(R.id.splash_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashLogo.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1200);
            }
        }, 100);
    }
}
