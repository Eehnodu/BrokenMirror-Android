/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.13, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'login_find_pw_result.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;

public class login_find_pw_result extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_find_pw_result);

        String email = getIntent().getStringExtra("email");

        TextView id_textView = findViewById(R.id.text_1);
        TextView text_0_textView = findViewById(R.id.text_0);

        Button close_button = findViewById(R.id.close_button);
        Button confirm_button = findViewById(R.id.confirm_button);

        // setText
        if(email != null) {
            id_textView.setText(email);
            id_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            id_textView.setSelected(true);
        }else{
            text_0_textView.setText("회원님의 정보와 일치하는 아이디가 존재하지 않습니다.");
            id_textView.setText("-");
            id_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            id_textView.setSelected(true);
        }
        // Button
        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // confirm_button : OnClickListener
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }
}
