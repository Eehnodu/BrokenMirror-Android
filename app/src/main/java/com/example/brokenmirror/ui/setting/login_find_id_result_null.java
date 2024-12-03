/*
__author__ = 'Song Chae Young'
__date__ = 'Oct.14, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'login_find_id_result_null.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;

public class login_find_id_result_null extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_find_id_result_null);

        // textView
        // find_id
        TextView find_id = findViewById(R.id.find_id_result_null_id);
        find_id.setPaintFlags(find_id.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button join_btn = findViewById(R.id.find_id_result_null_join_btn);
        Button close_btn = findViewById(R.id.find_id_result_null_close);
        Button confirm_btn = findViewById(R.id.find_id_result_null_confirm_btn);
        Button back_button = findViewById(R.id.back_button);

        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_find_id_result_null.this, login_find_id.class);
                startActivity(intent);
                finish();
            }
        });

        // Button
        // btn_close
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // confirm_btn
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_find_id_result_null.this, join_agreement.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
