/*
__author__ = 'Song Chae Young'
__date__ = 'Dec.05, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_login.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.LoginInfoDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.LoginInfoApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_account_login extends AppCompatActivity {

    private final ArrayList<String> date_list = new ArrayList<String>();
    private final ArrayList<String> device_list = new ArrayList<>();
    private final ArrayList<String> ip_list = new ArrayList<>();
    private RecyclerView recyclerView;

    private final LoginInfoApi loginInfoApi = RetrofitService.getLoginInfoApi();

    private List<LoginInfoDto> login_info;

    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_login);

        // userSharedPref
        UserSharedPref user_pref = new UserSharedPref(this);
        UserDto user_info = user_pref.getUser();

        recyclerView = findViewById(R.id.setting_account_login_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button back_button = findViewById(R.id.setting_account_login_back_button);
        Button close_button = findViewById(R.id.close_button);

        // 로그인 내역 조회
        getLoginInfo(user_info.getId());

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //close_button
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set RecyclerView Adapter
        setting_account_login_adapter adapter = new setting_account_login_adapter(date_list, device_list, ip_list);
        recyclerView.setAdapter(adapter);
    }       // onCreate

    // getLoginInfo (로그인 내역 조회)
    public void getLoginInfo(String id) {
        loginInfoApi.getLoginInfo(id).enqueue(new Callback<List<LoginInfoDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoginInfoDto>> call, @NonNull Response<List<LoginInfoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    login_info = response.body();
                    for (LoginInfoDto info : login_info) {
                        date_list.add(dateformat.format(info.getCreatedAt()));
                        device_list.add(info.getDevice());
                        ip_list.add(info.getIpAddress());
                    }
                    // Set RecyclerView Adapter
                    setting_account_login_adapter adapter = new setting_account_login_adapter(date_list, device_list, ip_list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LoginInfoDto>> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }
}

