/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.08, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'login_find_id_result.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_find_id_result extends AppCompatActivity {
    private String DATE;
    private TextView id_textView;
    private TextView date_textVIew;
    private TextView title_textView;
    // UserApi
    UserApi userApi = RetrofitService.getUserApi();

    private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy. MM. dd");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_find_id_result);

        UserDto user = (UserDto) getIntent().getSerializableExtra("user");

        Button close_button = findViewById(R.id.find_id_result_close_btn);
        Button pw_button = findViewById(R.id.find_id_result_pw_btn);
        Button login_button = findViewById(R.id.find_id_result_login_btn);
        Button back_button = findViewById(R.id.back_button);

        id_textView = findViewById(R.id.user_id);
        date_textVIew = findViewById(R.id.user_date);
        title_textView = findViewById(R.id.title);

        findId(user);

        // Button
        // close_button
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_find_id_result.this, login_main.class);
                startActivity(intent);
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

        // pw_button
        pw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_find_id_result.this, login_find_pw.class);
                startActivity(intent);
                finish();
            }
        });

        // login_button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // ID masking
    public String maskId(String email) {
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        int usernameLength = username.length();
        if (usernameLength == 2 || usernameLength == 3) {
            username = username.substring(0, usernameLength - 1) + "*";
        } else if (usernameLength == 4 || usernameLength == 5) {
            username = username.substring(0, usernameLength - 2) + "**";
        } else if (usernameLength >= 6) {
            username = username.substring(0, 3) + "***";        // exceed 6 texts, masked only '***' (3)
        }
        return username + "@" + domain;
    }

    // findId (아이디 찾기)
    public void findId(UserDto user) {
        userApi.findId(user).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    id_textView.setText(maskId(response.body().getId()));
                    id_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    id_textView.setSelected(true);
                    DATE = dateformat.format(response.body().getCreatedAt());
                    date_textVIew.setText(DATE);
                } else {
                    id_textView.setText("-");
                    id_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    id_textView.setSelected(true);
                    date_textVIew.setText("-");
                    title_textView.setText("회원님의 정보와 일치하는 아이디가 존재하지 않습니다.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }
}