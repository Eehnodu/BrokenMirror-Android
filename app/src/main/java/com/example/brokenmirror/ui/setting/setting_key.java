/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.14, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_key.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

// DB date format : YYYY-MM-DD HH:MM:SS (DATETIME)
// SERVER : YYYY-MM-DD(EE) HH:MM

package com.example.brokenmirror.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class setting_key extends AppCompatActivity {

    private final ArrayList<DataItem> combinedList = new ArrayList<>();
    private final ArrayList<String> user_list = new ArrayList<>(Arrays.asList(
            "house", "jellyfish", "wizard", "xylophone", "*개발", "!기획관리", "~솔루션", "#시스템",
            "@R&D", "apple", "balloon", "cake", "dog","egg", "flamingo", "grape", "house", "icecream",
            "kite", "lemon"
    ));

    private final ArrayList<String> date_list = new ArrayList<>();
    private final ArrayList<String> user_list_result = new ArrayList<>();
    private final ArrayList<String> device_list_result = new ArrayList<>();
    private final ArrayList<Integer> key_list_result = new ArrayList<>();

    private View fold_button;
    private RecyclerView key_use_recyclerView;
    private ConstraintLayout key_use_layout;
    private NestedScrollView key_nestedScrollView;

    private final int item_height = 151;

    private boolean fold_check = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_key);

        Button back_button = findViewById(R.id.setting_key_back_button);
        fold_button = findViewById(R.id.setting_key_fold_button);

        key_use_recyclerView = findViewById(R.id.setting_key_key_use_recyclerView);
        key_use_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        key_use_layout = findViewById(R.id.setting_key_use_key_layout);

        key_nestedScrollView = findViewById(R.id.key_setting_nestedScrollView);

        TextView issued_textView = findViewById(R.id.setting_key_issued_textView);
        TextView key_date_textView = findViewById(R.id.text_17);

        // 랜덤 데이터 생성
        generateRandomData(20);

        // combinedList를 날짜를 기준으로 정렬
        Collections.sort(combinedList, new DateComparator());


        // combinedList를 date_list, user_list, device_list, key_list로 분할
        for (DataItem item : combinedList) {
            date_list.add(item.date);
            user_list_result.add(item.user);
            device_list_result.add(item.device);
            key_list_result.add(item.key);
        }

        // Set RecyclerView Adapter
        setting_key_adapter use_adapter = new setting_key_adapter(date_list, user_list_result, device_list_result, key_list_result);
        key_use_recyclerView.setAdapter(use_adapter);

        key_date_textView.setSelected(true);
        key_date_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        ViewGroup.LayoutParams params = key_use_recyclerView.getLayoutParams();
        params.height = item_height * 4 - 21;
        key_use_recyclerView.setLayoutParams(params);

//        Log.d("Test", "date_list: " + date_list.size());
//        Log.d("Test", "user_list_result: " + user_list_result.size());
//        Log.d("Test", "device_list_result: " + device_list_result.size());
//        Log.d("Test", "key_list_result: " + key_list_result.size());

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        // LinearLayout
        // key_use_layout : Fold (OnClickListener)
        key_use_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "fold_check : " + fold_check);
                if (fold_check == true) {
                    fold_button.setBackgroundResource(R.drawable.btn_up_arrow);
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    key_use_recyclerView.setLayoutParams(params);
                    key_nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            key_nestedScrollView.smoothScrollTo(0, key_use_layout.getTop());
                        }
                    });

                    fold_check = false;
                } else {
                    fold_button.setBackgroundResource(R.drawable.btn_down_arrow);
                    params.height = item_height * 4 - 21;
                    key_use_recyclerView.setLayoutParams(params);
                    key_nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            key_nestedScrollView.smoothScrollTo(0, key_use_layout.getBottom() - key_nestedScrollView.getHeight());
                        }
                    });

                    fold_check = true;
                }

            }
        });

        // issued_textView : OnClickListener
        issued_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_key.this, setting_key_purchase.class);
                startActivity(intent);
            }
        });
    } // onCreate

    private void generateRandomData(int count) {
        for (int i = 0; i < count; i++) {
            String randomUser = user_list.get(i % user_list.size());
            String device = "SM-S916N";
            int randomKey = i;
            DataItem item = new DataItem(randomUser, device, randomKey);
            combinedList.add(item);
        }
    }

    private class DataItem {
        String date;
        String user;
        String device;
        int key;

        DataItem(String user, String device, int key) {
            this.user = user;
            this.device = device;
            this.key = key;
            this.date = generateRandomDate();
        }

        private String generateRandomDate() {
            // 현재 날짜 가져오기
            Calendar calendar = Calendar.getInstance();

            // 랜덤한 날짜 생성
            Random random = new Random();
            int daysToAdd = random.nextInt(365);        // 최대 1년(365일)까지의 날짜 범위로 설정
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);

            // 랜덤한 시간 생성 (0시부터 23시까지)
            int randomHour = random.nextInt(24);
            calendar.set(Calendar.HOUR_OF_DAY, randomHour);

            // 랜덤한 분 생성 (0분부터 59분까지)
            int randomMinute = random.nextInt(60);
            calendar.set(Calendar.MINUTE, randomMinute);

            // 형식 지정
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

            // 날짜를 문자열로 변환하여 반환
            return sdf.format(calendar.getTime());
        }
    }

    // Sort : Date
    private class DateComparator implements Comparator<DataItem> {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        @Override
        public int compare(DataItem o1, DataItem o2) {
            try {
                Date date1 = sdf.parse(o1.date);
                Date date2 = sdf.parse(o2.date);

                // 날짜를 비교하여 오름차순으로 정렬
                return date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
