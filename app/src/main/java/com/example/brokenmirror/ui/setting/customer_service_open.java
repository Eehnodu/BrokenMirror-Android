/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.26, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'customer_service_open.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;

public class customer_service_open extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_open);

        Button back_button = findViewById(R.id.back_button);
        Button close_button = findViewById(R.id.close_button);

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
