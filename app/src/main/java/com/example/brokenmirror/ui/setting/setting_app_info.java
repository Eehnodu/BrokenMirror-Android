/*
__author__ = 'Song Chae Young'
__date__ = 'Nov.9, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_app_info.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;

public class setting_app_info extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_app_info);

        Button back_button = findViewById(R.id.setting_app_info_back_button);

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
