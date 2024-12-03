/*
__author__ = 'Song Chae Young'
__date__ = 'Jan.23, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'login_main.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.brokenmirror.GlobalVariables;
import com.example.brokenmirror.R;
import com.example.brokenmirror.data.LoginInfoDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.LoginInfoApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.AutoSharedPref;
import com.example.brokenmirror.sharedpref.UserSharedPref;
import com.example.brokenmirror.ui.chat.chat_main;
import com.example.brokenmirror.worker.FriendWorker;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_main extends AppCompatActivity {

    boolean[] JOIN_AGREEMENT_LIST_STATE = GlobalVariables.JOIN_AGREEMENT_LIST_STATE;    // imageviewStates reset
    private Button visible_button;
    private String pwInput;
    private EditText id_editText;
    private EditText pw_editText;
    View auto_login_button;

    Integer currentBackgroundResource;

    private UserDto user;
    private LoginInfoDto login_info;
    // UserApi
    private final UserApi userApi = RetrofitService.getUserApi();
    // LoginInfoApi
    private final LoginInfoApi loginInfoApi = RetrofitService.getLoginInfoApi();
    // userSharedPref
    private UserSharedPref user_pref;
    // AutoSharedPref
    private AutoSharedPref auto_pref;

    private Boolean auto_check;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // userSharedPref
        user_pref = new UserSharedPref(this);
        user = new UserDto();
        login_info = new LoginInfoDto();

        TextView find_id_textView = findViewById(R.id.login_main_id_textView);
        TextView find_pw_textView = findViewById(R.id.login_main_pw_textView);
        TextView join_textView = findViewById(R.id.login_main_join_textView);

        id_editText = findViewById(R.id.login_main_id_editText);
        pw_editText = findViewById(R.id.login_main_pw_editText);

        Button login_button = findViewById(R.id.login_main_login_button);
        visible_button = findViewById(R.id.login_main_visible_button);

        LinearLayout auto_login_layout = findViewById(R.id.auto_layout);
        ConstraintLayout full_screen_layout = findViewById(R.id.lenear_login_main);

        auto_login_button = findViewById(R.id.login_main_checkBox);

        pwInput = pw_editText.getText().toString();

        // AutoLogin
        auto_pref = new AutoSharedPref(this);

        // imageviewStates reset
        Arrays.fill(JOIN_AGREEMENT_LIST_STATE, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.basic));
        }

        // TextView
        // find_id : OnClickListener
        find_id_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_main.this, login_find_id.class);   // login_main.class -> login_find_id.class 전환
                startActivity(intent);
                onPause();
            }
        });

        // find_pw_textView : OnClickListener
        find_pw_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_main.this, login_find_pw.class);
                startActivity(intent);
                onPause();
            }
        });

        // join_textView : OnClickListener
        join_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_main.this, join_agreement.class);
                startActivity(intent);
                onPause();
            }
        });

        // Button
        // login_button : login_find_id.java -> friend_list.java
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setId(id_editText.getText().toString());
                user.setPw(pw_editText.getText().toString());
                // 로그인 내역 등록
                login_info.setId(id_editText.getText().toString());
                login_info.setDevice(Build.MODEL);
                login_info.setIpAddress(getLocalIpAddress());
                // 로그인
                userLogin(user, login_info);
            }
        });

        // visible_button : OnClickListener
        visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pw_editText.getText().toString().isEmpty()) {        // If pw_editText is empty, do noting (return)
                    return;
                }
                // 현재 비밀번호 입력란의 가시성 상태 가져오기
                boolean isPasswordVisible = pw_editText.getTransformationMethod() instanceof HideReturnsTransformationMethod;
                // toggle password visibility
                pw_editText.setTransformationMethod(
                        pw_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()
                                ? HideReturnsTransformationMethod.getInstance()
                                : PasswordTransformationMethod.getInstance()
                );
                // 버튼 배경 리소스 변경
                visible_button.setBackgroundResource(isPasswordVisible ? R.drawable.btn_invisible : R.drawable.btn_visible);
                // Set focus to the end of the text
                pw_editText.setSelection(pw_editText.getText().length());
            }
        });

        // EditText
        // pw_editText : FocusChangeListener
        pw_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
                    visible_button.setVisibility(View.GONE);
                } else {        // focus in
                    if (pwInput.isEmpty()) {
                        visible_button.setVisibility(View.GONE);
                    } else {
                        visible_button.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // pw_editText : Change
        pw_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwInput = charSequence.toString();
                if (pwInput.isEmpty()) {
                    visible_button.setVisibility(View.GONE);
                } else {
                    visible_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Layout
        // auto_login_layout : OnClickListener
        auto_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBackgroundResource = (Integer) auto_login_button.getTag();
                if (currentBackgroundResource == null || currentBackgroundResource.equals(R.drawable.square_checkbox_empty)) {
                    auto_login_button.setBackgroundResource(R.drawable.square_checkbox_activate);
                    auto_login_button.setTag(R.drawable.square_checkbox_activate);
                    auto_check = true;
                } else {
                    auto_login_button.setBackgroundResource(R.drawable.square_checkbox_empty);
                    auto_login_button.setTag(R.drawable.square_checkbox_empty);
                    auto_check = false;
                }
            }
        });

        // full_screen_layout : clearFocus
        full_screen_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                id_editText.clearFocus();
                pw_editText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });
        // auto_login (자동 로그인)
        autoLogin();
    }

    private boolean backButtonPressedOnce = false;
    private Handler handler = new Handler();
    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds

    @Override
    public void onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed(); // 두 번째 뒤로가기 버튼 눌림
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

    // userLogin (로그인)
    public void userLogin(UserDto user, LoginInfoDto login_info) {
        userApi.userLogin(user).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (user.getId().equals(response.body().getId()) && user.getPw().equals(response.body().getPw())) {
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(login_main.this, chat_main.class);
                        user_pref.putUser(response.body());
                        // auto-login
                        if (auto_check) {
                            if (!id_editText.getText().toString().isEmpty() && !pw_editText.getText().toString().isEmpty()) {
                                auto_pref.putAuto(true);
                            }
                        } else {
                            auto_pref.putAuto(false);
                        }
                        // 로그인 내역 등록
                        putLoginInfo(login_info);
                        // 로그인 성공 후 scheduleFriendUpdate 호출
                        scheduleFriendUpdate(user.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                    android.widget.Toast toast = new android.widget.Toast(getApplicationContext());
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.login_info_incorrect), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
                android.widget.Toast toast = new android.widget.Toast(getApplicationContext());
                toast = Toast.makeText(getApplicationContext(), getString(R.string.login_info_incorrect), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        });
    }

    // putLoginInfo (로그인 내역 등록)
    public void putLoginInfo(LoginInfoDto login_info) {
        loginInfoApi.putLoginInfo(login_info).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // auto_login (자동 로그인)
    public void autoLogin() {
        auto_check = auto_pref.getAuto();
        if (auto_check == null) {
            auto_check = false;
        } else {
            if (auto_check) {
                auto_login_button.setBackgroundResource(R.drawable.square_checkbox_activate);
                auto_login_button.setTag(R.drawable.square_checkbox_activate);
                id_editText.setText(user_pref.getUser().getId());
                pw_editText.setText(user_pref.getUser().getPw());
                // 로그인 내역 등록
                login_info.setId(id_editText.getText().toString());
                login_info.setDevice(Build.MODEL);
                login_info.setIpAddress(getLocalIpAddress());
                userLogin(user_pref.getUser(), login_info);
            }
        }
    }

    // ip address 가져오기
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumlpAddr = intf.getInetAddresses(); enumlpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumlpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            ;
        }
        return null;
    }

    // 메인 액티비티나 시작 지점에서 WorkManager 작업 예약
    public void scheduleFriendUpdate(String userId) {
        WorkManager workManager = WorkManager.getInstance(this);

        // 사용자 ID를 Worker에 전달
        Data data = new Data.Builder()
                .putString("ID", userId)
                .build();

        // 주기는 최소 15분
        PeriodicWorkRequest friendUpdateRequest = new PeriodicWorkRequest.Builder(FriendWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .build();

        workManager.enqueueUniquePeriodicWork(
                "FriendUpdate", // 작업의 고유 이름
                ExistingPeriodicWorkPolicy.KEEP, // 중복 실행 방지
                friendUpdateRequest
        );
    }
}