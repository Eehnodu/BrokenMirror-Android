/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.20, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_notice.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.R;
import com.example.brokenmirror.sharedpref.NotificationSharedPref;

public class setting_notice extends AppCompatActivity {
    private boolean opt_0;
    private SwitchCompat opt_0_switch, opt_1_switch, opt_2_switch, opt_3_switch, channel_1_switch, channel_2_switch, channel_3_switch;
    private ActivityResultLauncher<Intent> permissionLauncher;
    // noti_pref
    private NotificationSharedPref noti_pref;
    boolean noti_info, sound_info, vibe_info, silence_info, ch1_info, ch2_info, ch3_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notice);

        ConstraintLayout opt_0_layout = findViewById(R.id.setting_notice_opt_0_layout);
        ConstraintLayout opt_1_layout = findViewById(R.id.setting_notice_opt_1_layout);
        ConstraintLayout opt_2_layout = findViewById(R.id.setting_notice_opt_2_layout);
        ConstraintLayout opt_3_layout = findViewById(R.id.setting_notice_opt_3_layout);
        ConstraintLayout channel_1_layout = findViewById(R.id.setting_notice_channel_1_layout);
        ConstraintLayout channel_2_layout = findViewById(R.id.setting_notice_channel_2_layout);
        ConstraintLayout channel_3_layout = findViewById(R.id.setting_notice_channel_3_layout);

        opt_0_switch = findViewById(R.id.setting_notice_switch_0);
        opt_1_switch = findViewById(R.id.setting_notice_switch_1);
        opt_2_switch = findViewById(R.id.setting_notice_switch_2);
        opt_3_switch = findViewById(R.id.setting_notice_switch_3);
        channel_1_switch = findViewById(R.id.setting_notice_channel_switch_1);
        channel_2_switch = findViewById(R.id.setting_notice_channel_switch_2);
        channel_3_switch = findViewById(R.id.setting_notice_channel_switch_3);

        Button back_button = findViewById(R.id.setting_notice_back_button);
        Button close_button = findViewById(R.id.close_button);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(150);

        // ch_pref
        noti_pref = new NotificationSharedPref(this, "notification_settings");
        noti_info = noti_pref.getNoti("noti");
        sound_info = noti_pref.getNoti("sound");
        vibe_info = noti_pref.getNoti("vibe");
        silence_info = noti_pref.getNoti("silence");
        ch1_info = noti_pref.getNoti("ch1");
        ch2_info = noti_pref.getNoti("ch2");
        ch3_info = noti_pref.getNoti("ch3");

        // 권한 정보 상태 가져오기
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (NotificationManagerCompat.from(setting_notice.this).areNotificationsEnabled()) {
                // 권한이 허용됨
                opt_0 = true;
            } else {
                // 권한이 비허용됨
                opt_0 = false;
            }
            opt_0_switch.performClick();
        });

        // 이미 동의한 상태라면 true 및 체크표시
        opt_0 = noti_info;
        // post()를 사용하여 UI 스레드가 완전히 준비된 후에 클릭 동작을 실행
        opt_0_switch.post(new Runnable() {
            @Override
            public void run() {
                opt_0_switch.performClick();
            }
        });

        // FrameLayout
        // opt_0_layout : OnClickListener
        opt_0_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Android 13 이상일 경우 알림 권한 상태 확인
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(setting_notice.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        // 권한이 이미 허용된 경우에만 상태 변경
                        // dialog창
                        new AlertDialog.Builder(setting_notice.this)
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
                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        permissionLauncher.launch(intent);
                    } else {
                        // 권한이 허용되지 않은 경우 처리 (여기서 알림 권한 요청을 하지 않음)
                        // 권한 알림 표시
                        ActivityCompat.requestPermissions(setting_notice.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    }
                } else {
                    opt_0 = !opt_0;
                    opt_0_switch.performClick();
                }
            }
        });

        // opt_1_layout : OnClickListener
        opt_1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opt_1_switch.performClick();
            }
        });

        // opt_2_layout : OnClickListener
        opt_2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opt_2_switch.performClick();
            }
        });

        // opt_3_layout : OnClickListener
        opt_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opt_3_switch.performClick();
            }
        });

        // channel_1_layout : OnClickListener
        channel_1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channel_1_switch.performClick();
            }
        });

        // channel_2_layout : OnClickListener
        channel_2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channel_2_switch.performClick();
            }
        });

        // channel_3_layout : OnClickListener
        channel_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channel_3_switch.performClick();
            }
        });

        // Switch
        // opt_0_switch : OnCheckedChangeListener
        opt_0_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!opt_0) {
                opt_0_switch.setChecked(opt_0);
                opt_1_layout.setVisibility(View.GONE);
                opt_2_layout.setVisibility(View.GONE);
                opt_3_layout.setVisibility(View.GONE);
                channel_1_layout.setVisibility(View.GONE);
                channel_2_layout.setVisibility(View.GONE);
                channel_3_layout.setVisibility(View.GONE);
            } else {
                opt_0_switch.setChecked(opt_0);
                opt_1_layout.setVisibility(View.VISIBLE);
                opt_2_layout.setVisibility(View.VISIBLE);
                opt_3_layout.setVisibility(View.VISIBLE);
                channel_1_layout.setVisibility(View.VISIBLE);
                channel_2_layout.setVisibility(View.VISIBLE);
                channel_3_layout.setVisibility(View.VISIBLE);
            }
            noti_pref.putNoti(opt_0, "noti");
        });

        // opt_1_switch : OnCheckedChangeListener
        opt_1_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                silence_info = false;
                sound_info = true;
            } else {
                sound_info = false;
                if (!vibe_info) {
                    silence_info = true;
                }
            }
            noti_pref.putNoti(sound_info, "sound");
            noti_pref.putNoti(vibe_info, "vibe");
            noti_pref.putNoti(silence_info, "silence");
            opt_3_switch.setChecked(silence_info);
        });

        // opt_2_switch : OnCheckedChangeListener
        opt_2_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                silence_info = false;
                vibe_info = true;
            } else {
                vibe_info = false;
                if (!sound_info) {
                    silence_info = true;
                }
            }
            noti_pref.putNoti(sound_info, "sound");
            noti_pref.putNoti(vibe_info, "vibe");
            noti_pref.putNoti(silence_info, "silence");
            opt_3_switch.setChecked(silence_info);
        });

        // opt_2_switch : OnCheckedChangeListener
        opt_3_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sound_info = false;
                vibe_info = false;
                silence_info = true;
            } else {
                sound_info = true;
                vibe_info = true;
                silence_info = false;
            }
            noti_pref.putNoti(sound_info, "sound");
            noti_pref.putNoti(vibe_info, "vibe");
            noti_pref.putNoti(silence_info, "silence");
            opt_1_switch.setChecked(sound_info);
            opt_2_switch.setChecked(vibe_info);
        });

        // channel_1_switch : OnCheckedChangeListener
        channel_1_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch1_info = isChecked;
            noti_pref.putNoti(ch1_info, "ch1");
        });

        // channel_2_switch : OnCheckedChangeListener
        channel_2_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch2_info = isChecked;
            noti_pref.putNoti(ch2_info, "ch2");
        });

        // channel_3_switch : OnCheckedChangeListener
        channel_3_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch3_info = isChecked;
            noti_pref.putNoti(ch3_info, "ch3");
        });

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

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // 권한 요청 결과 확인
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 상태 변경
                opt_0 = true;
            }
            opt_0_switch.performClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 알림 권한 상태 다시 확인
        opt_0_switch.performClick();
        noti_info = noti_pref.getNoti("noti");
        opt_1_switch.setChecked(sound_info);
        opt_2_switch.setChecked(vibe_info);
        opt_3_switch.setChecked(silence_info);
        channel_1_switch.setChecked(ch1_info);
        channel_2_switch.setChecked(ch2_info);
        channel_3_switch.setChecked(ch3_info);
    }
}