/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.20, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'join_agreement.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.GlobalVariables;
import com.example.brokenmirror.R;
import com.example.brokenmirror.sharedpref.NotificationSharedPref;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.BuildConfig;

public class join_agreement extends AppCompatActivity {
    static boolean[] JOIN_AGREEMENT_LIST_STATE = GlobalVariables.JOIN_AGREEMENT_LIST_STATE;
    private int contextCode;
    private Intent intent;
    private Intent intent_login;
    private View[] views;
    private ActivityResultLauncher<Intent> permissionLauncher;

    // noti_pref
    private NotificationSharedPref noti_pref, sound_pref, vibe_pref, silence_pref, ch1_pref, ch2_pref, ch3_pref;
    private boolean noti_info, sound_info, vibe_info, silence_info, ch1_info, ch2_info, ch3_info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_agreement);

        intent = new Intent(join_agreement.this, join_agreement_detail.class);
        intent_login = new Intent(join_agreement.this, login_main.class);

        // Notification
        noti_pref = new NotificationSharedPref(this, "noti");
        sound_pref = new NotificationSharedPref(this, "sound");
        vibe_pref = new NotificationSharedPref(this, "vibe");
        silence_pref = new NotificationSharedPref(this, "silence");
        ch1_pref = new NotificationSharedPref(this, "ch1");
        ch2_pref = new NotificationSharedPref(this, "ch2");
        ch3_pref = new NotificationSharedPref(this, "ch3");

        views = new View[]{
                findViewById(R.id.view_0),
                findViewById(R.id.view_1),
                findViewById(R.id.view_2),
                findViewById(R.id.view_3),
                findViewById(R.id.view_4)
        };

        Button back_button = findViewById(R.id.back_button);
        Button close_button = findViewById(R.id.close_button);
        Button agree_button = findViewById(R.id.agree_button);

        ConstraintLayout list_0 = findViewById(R.id.list_0);
        ConstraintLayout list_1 = findViewById(R.id.list_1);
        ConstraintLayout list_2 = findViewById(R.id.list_2);
        ConstraintLayout list_3 = findViewById(R.id.list_3);
        ConstraintLayout list_4 = findViewById(R.id.list_4);

        // 권한 정보 상태 가져오기
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // 사용자가 알림을 허용했는지 확인
            if (NotificationManagerCompat.from(join_agreement.this).areNotificationsEnabled()) {
                // 권한이 허용됨
                JOIN_AGREEMENT_LIST_STATE[4] = true;
                noti_info = true;
                sound_info = true;
                vibe_info = true;
                silence_info = false;
                ch1_info = true;
                ch2_info = true;
                ch3_info = true;
            } else {
                // 권한이 비허용됨
                JOIN_AGREEMENT_LIST_STATE[4] = false;
                noti_info = false;
                sound_info = false;
                vibe_info = false;
                silence_info = true;
                ch1_info = false;
                ch2_info = false;
                ch3_info = false;
            }
            updateViewBackground();
        });

        // 이미 동의한 상태라면 true 및 체크표시 (1인 1계정이라면 필요한가?)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상일 경우 알림 권한 상태 확인
            if (NotificationManagerCompat.from(join_agreement.this).areNotificationsEnabled()) {
                JOIN_AGREEMENT_LIST_STATE[4] = true;
                updateViewBackground();
            }
        }

        // getIntent
        Intent get_intent = getIntent();
        if (get_intent != null) {
            int stateCode = get_intent.getIntExtra("stateCode", -1);
            if (stateCode == 100) {
                JOIN_AGREEMENT_LIST_STATE[0] = true;
            } else if (stateCode == 200) {
                JOIN_AGREEMENT_LIST_STATE[1] = true;
            } else if (stateCode == 300) {
                JOIN_AGREEMENT_LIST_STATE[2] = true;
            } else if (stateCode == 400) {
                JOIN_AGREEMENT_LIST_STATE[3] = true;
            } else if (stateCode == 500) {
                JOIN_AGREEMENT_LIST_STATE[4] = true;
            }
            updateViewBackground();
        }

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_login);
                finish();
            }
        });

        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_login);
                finish();
            }
        });

        agree_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JOIN_AGREEMENT_LIST_STATE[0] && JOIN_AGREEMENT_LIST_STATE[1] && JOIN_AGREEMENT_LIST_STATE[2] && JOIN_AGREEMENT_LIST_STATE[3]) {
                    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
                    // close all activities
                    for (ActivityManager.AppTask appTask : appTasks) {
                        appTask.finishAndRemoveTask();
                    }
                    noti_pref.putNoti(noti_info, "noti");
                    sound_pref.putNoti(sound_info, "sound");
                    vibe_pref.putNoti(vibe_info, "vibe");
                    silence_pref.putNoti(silence_info, "silence");
                    ch1_pref.putNoti(ch1_info, "ch1");
                    ch2_pref.putNoti(ch2_info, "ch2");
                    ch3_pref.putNoti(ch3_info, "ch3");
                    Intent intent1 = new Intent(join_agreement.this, join_member.class);
                    startActivity(intent1);
                    finish();
                } else {
                    for (int i = 0; i < JOIN_AGREEMENT_LIST_STATE.length - 1; i++) {
                        if (!JOIN_AGREEMENT_LIST_STATE[i]) {
                            contextCode = 100 + (i * 100);
                            intent.putExtra("contextCode", contextCode);
                            agreeButtonClick();
                            break;
                        }
                    }
                }
            }
        });

        list_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateCheck(0);
            }
        });

        list_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateCheck(1);
            }
        });

        list_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateCheck(2);
            }
        });

        list_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateCheck(3);
            }
        });

        list_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Android 13 이상일 경우 알림 권한 상태 확인
                    if (ContextCompat.checkSelfPermission(join_agreement.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        // 권한이 이미 허용된 경우에만 상태 변경
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        permissionLauncher.launch(intent);
                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        // 사용자가 한 번 거부 했을 경우 앱의 알림 세팅으로 이동해야함
                        // dialog창
                        new AlertDialog.Builder(join_agreement.this)
                                .setTitle(getResources().getString(R.string.join_agree_setting_title))
                                .setMessage(getResources().getString(R.string.join_agree_setting_message))
                                .setPositiveButton(getResources().getString(R.string.join_agree_setting_positive), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                        permissionLauncher.launch(intent);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.join_agree_setting_negative), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        // 권한이 허용되지 않은 경우 처리 (여기서 알림 권한 요청을 하지 않음)
                        // 권한 알림 표시
                        ActivityCompat.requestPermissions(join_agreement.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    }
                } else {
                    // Android 13 이하일 경우 상태를 그냥 토글 (권한 요청 필요 없음)
                    // 체크할경우 true, 아닐경우 false
                    toggleStateAndLog();
                    if (JOIN_AGREEMENT_LIST_STATE[4]) {
                        noti_info = true;
                        ch1_info = true;
                        ch2_info = true;
                        ch3_info = true;
                    } else {
                        noti_info = false;
                        ch1_info = false;
                        ch2_info = false;
                        ch3_info = false;
                    }
                }
            }
        });
    }   // onCreate

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // 권한 요청 결과 확인
            if (NotificationManagerCompat.from(join_agreement.this).areNotificationsEnabled()) {
                toggleStateAndLog();
            }
        }
    }

    // 상태 변경 및 로그 출력 함수
    private void toggleStateAndLog() {
        JOIN_AGREEMENT_LIST_STATE[4] = !JOIN_AGREEMENT_LIST_STATE[4];
        updateViewBackground();
    }

    private void agreeButtonClick() {
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void updateViewBackground() {
        for (int i = 0; i < JOIN_AGREEMENT_LIST_STATE.length; i++) {
            views[i].setBackgroundResource(JOIN_AGREEMENT_LIST_STATE[i] ? R.drawable.checkbox_activate : R.drawable.checkbox_deactivate);
        }
    }

    private void stateCheck(int num) {
        if (JOIN_AGREEMENT_LIST_STATE[num]) {
            JOIN_AGREEMENT_LIST_STATE[num] = false;
            updateViewBackground();
        } else {
            contextCode = (num + 1) * 100;
            intent.putExtra("contextCode", contextCode);
            agreeButtonClick();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(intent_login);
        // 기본 동작 (앱 종료)
        super.onBackPressed();
        finish();
    }
}   // join_agreement.java
