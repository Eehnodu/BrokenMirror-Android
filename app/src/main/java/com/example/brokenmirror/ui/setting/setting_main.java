/*
__author__ = 'Song Chae Young, Jung Chae Lin'
__date__ = 'Feb.06, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_main.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.brokenmirror.bitmap.BitmapConverter;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.notify.NotificationService;
import com.example.brokenmirror.sharedpref.NotificationSharedPref;
import com.example.brokenmirror.sharedpref.UserSharedPref;
import com.example.brokenmirror.ui.BottomNavigation;
import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.notification.notification_main;

public class setting_main extends AppCompatActivity {
    //    private String service_url = "https://www.naver.com";
    private final String service_url = "https://tiasolution.net/layout/basic/main.html";
    private final String notice_url = "https://tiasolution.net/board/free/list.html";

    private Toast toast;
    private TextView userNameTextView;
    private TextView phoneNumTextView;
    private ImageView userImageView;

    private UserDto user_info;
    private UserSharedPref user_pref;

    private final BitmapConverter converter = new BitmapConverter();

    // 알림 테스트용
    private final NotificationService channel = new NotificationService(this);
    private NotificationSharedPref noti_pref;
    boolean noti_info, sound_info, vibe_info, silence_info, ch1_info, ch2_info, ch3_info;

    // 진동 및 소리 설정
    private Vibrator vibrator;
    private Ringtone rt;
    private AudioManager audioManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        Button edit_profile_button = findViewById(R.id.setting_edit_profile);

        ConstraintLayout add_friend_layout = findViewById(R.id.setting_add_friend_layout);
        ConstraintLayout key_layout = findViewById(R.id.setting_key_layout);
        ConstraintLayout service_layout = findViewById(R.id.setting_service_layout);
        ConstraintLayout notification_layout = findViewById(R.id.setting_notification_layout);

        TextView opt_account = findViewById(R.id.second_account);
        TextView opt_notice = findViewById(R.id.second_notice);
        TextView opt_manage_friend = findViewById(R.id.second_friend);
        TextView opt_info = findViewById(R.id.second_info);
        TextView opt_test_0 = findViewById(R.id.second_test_0);
        TextView opt_test_1 = findViewById(R.id.second_test_1);
        TextView androidTest = findViewById(R.id.android_test);
        userNameTextView = findViewById(R.id.user_name);
        userImageView = findViewById(R.id.user_profile);
        phoneNumTextView = findViewById(R.id.number);

        String add_friend_toast = getResources().getString(R.string.setting_main_preparing);

        // 알림 테스트용
        TextView notificationTest1 = findViewById(R.id.notification_test1);
        TextView notificationTest2 = findViewById(R.id.notification_test2);
        TextView notificationTest3 = findViewById(R.id.notification_test3);
        // ch_pref
        noti_pref = new NotificationSharedPref(this, "notification_settings");
        noti_info = getOrDefault(noti_pref, "noti", NotificationManagerCompat.from(setting_main.this).areNotificationsEnabled());
        sound_info = getOrDefault(noti_pref, "sound", true);
        vibe_info = getOrDefault(noti_pref, "vibe", true);
        silence_info = getOrDefault(noti_pref, "silence", false);
        ch1_info = getOrDefault(noti_pref, "ch1", true);
        ch2_info = getOrDefault(noti_pref, "ch2", true);
        ch3_info = getOrDefault(noti_pref, "ch3", true);
        // 진동 및 소리 설정
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri noti_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        rt = RingtoneManager.getRingtone(getApplicationContext(), noti_sound);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 알림 테ㅡ트용

        // 로그인 한 회원 조회
        userNameTextView.setText(user_info.getUserName());
//        phoneNumTextView.setText(user_info.getPhoneNum());
        transNumber(user_info.getPhoneNum());

        // BottomNavigationBar Color
        findViewById(R.id.nav_set_icon).setBackgroundResource(R.drawable.bottom_navigation_icon_setting_emph);
        ((TextView) findViewById(R.id.nav_set_text)).setTextColor(ContextCompat.getColor(this, R.color.main));

        if (user_info != null) {
//            bitmap = converter.StringToBitmap(user_info.getProfileImg());
            loadGlideImage(user_info.getProfileImg(), userImageView);
        }

        // Button
        // edit_profile_button : OnClickListener (setting_main.java -> friend_list.java)
        // 프로필 편집 버튼 클릭 시
        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, user_profile_me.class);
//                launcher.launch(intent);
                startActivity(intent);
                onPause();
            }
        });

        // FrameLayout
        // add_friend_layout : OnClickListener
        add_friend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast = Toast.makeText(getApplicationContext(), add_friend_toast, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        });

        // key_layout : OnClickListener
        key_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_key.class);
                startActivity(intent);
                onPause();
            }
        });

        // service_layout : OnClickListener
        service_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, customer_service.class);
                startActivity(intent);
                onPause();
            }
        });

        // notification_layout : OnClickListener
        notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, announcement.class);
                startActivity(intent);
                onPause();
            }
        });

        // LinearLayout (setting options)
        // opt_account : OnClickListener
        opt_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_account.class);
                startActivity(intent);
                onPause();
            }
        });

        // opt_notice : OnClickListener
        opt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_notice.class);
                startActivity(intent);
                onPause();
            }
        });

        // opt_manage_friend : OnClickListener
        opt_manage_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_friend.class);
                startActivity(intent);
                onPause();
            }
        });

        // opt_info : OnClickListener (setting_main.java -> setting_app_info.java)
        opt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_app_info.class);
                startActivity(intent);
                onPause();
            }
        });

        // opt_test_0 : OnClickListener
        opt_test_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_etc1.class);
                startActivity(intent);
                onPause();
            }
        });

        // opt_test_1 : OnClickListener
        opt_test_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, setting_etc2.class);
                startActivity(intent);
                onPause();
            }
        });

        androidTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_main.this, com.example.brokenmirror.androidTest.class);
                startActivity(intent);
            }
        });

        // 알림 테스트용
        notificationTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noti_info) {
                    if (ch1_info) {
                        soundCheck();
                        channel.sendNotification("channel1", "channel1 제목", "channel1 내용", R.drawable.icon_key_1, 1, "etc", notification_main.class, R.id.notification_submenu_img);
                    }
                }
            }
        });
        notificationTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noti_info) {
                    if (ch2_info) {
                        soundCheck();
                        channel.sendNotification("channel2", "channel2 제목", "channel2 내용", R.drawable.icon_key_2, 2, "etc", notification_main.class, R.id.notification_submenu_add_friends);
                    }
                }
            }
        });
        notificationTest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noti_info) {
                    if (ch3_info) {
                        soundCheck();
                        channel.sendNotification("channel3", "channel3 제목", "channel3 내용", R.drawable.icon_key_3, 3, "etc", notification_main.class, R.id.notification_submenu_buy_key);
                    }
                }
            }
        });
        //bottomNavigationBar
        BottomNavigation bottomNavigation = new BottomNavigation(this, findViewById(R.id.setting_layout));
        bottomNavigation.setupBottomNavigation();
    }  //onCreate

    private boolean backButtonPressedOnce = false;
    private Handler handler = new Handler();
    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds

    @Override
    public void onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed(); // 두 번째 뒤로가기 버튼 눌림
            Log.d("ghkrdls", "Activity destroyed");
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

    // Glide (이미지 로드)
    public void loadGlideImage(String imageUrl, ImageView profileImage) {
        Glide.with(this)
                .load(imageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(profileImage);
    }

    // 알림권한 변경 결과를 받기 위해 resume 사용
    // 후에 필요한 곳에 옮겨다 사용
    @Override
    protected void onResume() {
        super.onResume();
        sound_info = getOrDefault(noti_pref, "sound", true);
        vibe_info = getOrDefault(noti_pref, "vibe", true);
        silence_info = getOrDefault(noti_pref, "silence", false);
        noti_info = getOrDefault(noti_pref, "noti", true);
        ch1_info = getOrDefault(noti_pref, "ch1", true);
        ch2_info = getOrDefault(noti_pref, "ch2", true);
        ch3_info = getOrDefault(noti_pref, "ch3", true);
        user_info = user_pref.getUser();
        userNameTextView.setText(user_info.getUserName());
        transNumber(user_info.getPhoneNum());
        loadGlideImage(user_info.getProfileImg(),userImageView);
    }

    // NotificationService Default 확인
    private boolean getOrDefault(NotificationSharedPref pref, String key, boolean def) {
        if (pref.getNoti(key) == null) {
            pref.putNoti(def, key);
            return def;
        } else {
            return pref.getNoti(key);
        }
    }

    // soundCheck 소리 및 진동 확인
    private void soundCheck() {
        if (!(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT))
            if (sound_info && vibe_info) {
                vibrator.vibrate(1000); // 1초간 진동
                rt.play();
            } else if (sound_info) {
                rt.play();
            } else if (vibe_info) {
                vibrator.vibrate(1000);
            }
    }

    public void transNumber(String userNumber) {
        if (userNumber != null) {
            if (userNumber.length() == 11) {
                String formattedNumber = userNumber.substring(0, 3) + "-" + userNumber.substring(3, 7) + "-" + userNumber.substring(7);
                phoneNumTextView.setText(formattedNumber);
            } else {
                phoneNumTextView.setText(userNumber);
            }
        }
    }
}
