package com.example.brokenmirror.ui.friend;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.ui.BottomNavigation;
import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.setting.setting_key;
import com.example.brokenmirror.ui.setting.user_profile_other;

import java.util.Collections;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class friend_list extends AppCompatActivity{

    private ArrayList<String> userNumbers = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>(Arrays.asList(        // test data
            "손흥민", "권흥민", "1흥민", "house", "김흥민", "2김흥민", "jellyfish", "김샬리송", "김사르", "3김베르너", "티모 베르너",
            "지오바니", "김날두", "wizard", "남케인", "남케인 .", "남 케인", "데스티니-우도기", "데스티니우도기", "도시락형제", "데안", "박매디슨", "스콧카슨", "아데르송", "👍아칸지", "음바페",
            "로셀소", "4이반", "이반", "정오바니", "모하메드엘네니", "조르지뉴", "탈까", "xylophone", "표지판", "쿨루셉스키", "토마스파티", "홀란드",
            "*개발", "!기획관리", "~솔루션", "#시스템", "@R&D", "apple", "baloon", "cake", "dog", "egg",
            "flamingo", "grape", "house", "icecream",  "kite", "lemon", "nose", "mouse", "pig",
            "orange", "rabbit", "queen", "zebra", "yo-wassup","violin", "umbrella",
            "tomato", "sun"
    ));

    private ArrayList<Integer> keyVal = new ArrayList<>(Arrays.asList(      // 0 : expired, 1 : waiting, 2: certified, 3 : none
            3, 1, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 2, 3, 3, 3,
            2, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2
    ));

    String search = "";

    // 로그인 후 유저의 정보
    String id;

    private Handler handler = new Handler();

    CustomComparator customComparator = new CustomComparator();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

        // 로그인 후 유저의 id
        id = getIntent().getStringExtra("id");

        // BottomNavigationBar Color
        findViewById(R.id.nav_friend_icon).setBackgroundResource(R.drawable.bottom_navigation_icon_friend_emph);
        ((TextView) findViewById(R.id.nav_friend_text)).setTextColor(ContextCompat.getColor(this, R.color.main));

        // Layout Components
        RecyclerView user_recyclerView = findViewById(R.id.friend_recyclerView);
        user_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView ver_user_recyclerView = findViewById(R.id.ver_friend_recyclerView);
        ver_user_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NestedScrollView nestScrollView = findViewById(R.id.nestedScrollView);

        TextView index_guide = findViewById(R.id.friend_indexer_guid_textView);
        TextView ver_user_textView = findViewById(R.id.ver_division_num_textView);
        TextView user_textView = findViewById(R.id.division_num_textView);

        View ver_fold_button = findViewById(R.id.friend_ver_fold_view);
        View fold_button = findViewById(R.id.friend_fold_view);

        Button search_button = findViewById(R.id.friend_search_button);

        ConstraintLayout index_layout = findViewById(R.id.friend_index);
        ConstraintLayout index_land_layout = findViewById(R.id.friend_land_index);

        ConstraintLayout ver_div_layout = findViewById(R.id.ver_friend_division);
        ConstraintLayout div_layout = findViewById(R.id.friend_division);
        ConstraintLayout key_notice_layout = findViewById(R.id.notice);

        key_notice_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(friend_list.this, setting_key.class);
                startActivity(intent);
            }
        });

        // ver_user_recyclerView : Filtered and Add data based on keyVal
        ArrayList<String> filteredNames = new ArrayList<>();
        ArrayList<String> filteredNumbers = new ArrayList<>();
        ArrayList<Integer> filteredKeyVal = new ArrayList<>();

        // combining filteredNames, filteredNumbers, filteredKeyVal Lists in one List
        ArrayList<ArrayList<Object>> combinedList_filter = new ArrayList<>();

        // combining userNames, userNumbers, keyVal Lists in one List
        ArrayList<ArrayList<Object>> combinedList = new ArrayList<>();

        // making user dummy data
        for (int i = 0; i <= userNames.size(); i++) {
            String userNumber = String.format("010%04d%04d", generateRandomNumber(), generateRandomNumber());

            userNumbers.add(userNumber);
        }

        // Sort : name data
//        Collections.sort(userNames, new CustomComparator());

        // RecyclerView
        // ver_user_recyclerView : Filtered and Add data based on keyVal
        // only key val 1, 2, 3
        for (int i = 0; i < keyVal.size(); i++) {
            if (keyVal.get(i) == 0 || keyVal.get(i) == 1 || keyVal.get(i) == 2) {
                filteredNames.add(userNames.get(i));
                filteredNumbers.add(userNumbers.get(i));
                filteredKeyVal.add(keyVal.get(i));
            }
        }

        // ver_user_recyclerView
        // combinedList_filter : Making filteredNames, filteredNumbers, filteredKeyVal in one List
        for (int i = 0; i < filteredNames.size(); i++) {
            ArrayList<Object> item = new ArrayList<>();
            item.add(filteredNames.get(i));
            item.add(filteredNumbers.get(i));
            item.add(filteredKeyVal.get(i));
            combinedList_filter.add(item);
        }

        // ver_user_recyclerView
        // filteredKeyVal : reverseOrder
        Collections.sort(combinedList_filter, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                int keyVal1 = (int) o1.get(2);
                int keyVal2 = (int) o2.get(2);

                return Integer.compare(keyVal2, keyVal1);
            }
        });

        // ver_user_recyclerView : Delete remaining data
        filteredNames.clear();
        filteredNumbers.clear();
        filteredKeyVal.clear();

        // ver_user_recyclerView : Separating data from combinedList_filter
        for (ArrayList<Object> item : combinedList_filter) {
            filteredNames.add((String) item.get(0));
            filteredNumbers.add((String) item.get(1));
            filteredKeyVal.add((int) item.get(2));
        }

        // ver_user_recyclerView : add Data (filtered name, num, keyVal)
        friend_list_customAdapter adapter1 = new friend_list_customAdapter(filteredNames, filteredNumbers, filteredKeyVal, new friend_list_customAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        ver_user_recyclerView.setAdapter(adapter1);

        // user_recyclerView : delete duplicated data
        // combinedList :  Making userNames, userNumbers, keyVal in one List
        for (int i = 0; i < userNames.size(); i++) {
            ArrayList<Object> item = new ArrayList<>();
            item.add(userNames.get(i));
            item.add(userNumbers.get(i));
            item.add(keyVal.get(i));
            combinedList.add(item);
        }

        // Assuming combinedList and combinedList_filter are already defined
        for (ArrayList<Object> item : combinedList_filter) {
            String filteredName = (String) item.get(0);
            for (int i = 0; i < combinedList.size(); i++) {
                ArrayList<Object> combinedItem = combinedList.get(i);
                String userName = (String) combinedItem.get(0);
                if (userName.equals(filteredName)) {
                    combinedList.remove(i);
                    i--; // Decrement i since we removed an item
                }
            }
        }

        // user_recyclerView : Delete remaining data
        userNames.clear();
        userNumbers.clear();
        keyVal.clear();

        // user_recyclerView : Separating data from combined_filter
        for (ArrayList<Object> item : combinedList) {
            userNames.add((String) item.get(0));
            userNumbers.add((String) item.get(1));
            keyVal.add((int) item.get(2));
        }

        // user_recyclerView : Adapter (send data to adapter)
        friend_list_customAdapter adapter = new friend_list_customAdapter(userNames, userNumbers, keyVal, new friend_list_customAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });        // connect with customAdapter


        user_recyclerView.setAdapter(adapter);

        // Section Indexer Hash Map (userNames)
//        customComparator.generateSectionIndexes(userNames);
//        HashMap<String, Integer> sectionIndexes = customComparator.getSectionIndexes();

        // user_recyclerView : itemTouchListener (delete interceptTouchEvent touch event)
        user_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());


                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {
                    int position = rv.getChildAdapterPosition(childView);

                    if (position != RecyclerView.NO_POSITION) {
                        String userName = userNames.get(position);
                        String userNumber = userNumbers.get(position);
                        Integer keyValValue = keyVal.get(position);
                        Log.d("RecyclerView Click", "Position : " + position + ", UserName : " + userName +
                                ", UserNumber : " + userNumber + ", KeyVal : " + keyValValue);

                        Intent intent = new Intent(friend_list.this, user_profile_other.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userNumber", userNumber);
                        intent.putExtra("keyVal", keyValValue);

                        startActivity(intent);
                        onPause();

                    } else {
                        Log.d("RecyclerView Click", "NO POSITION");

                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // ver_user_recyclerView : itemTouchListener (delete interceptTouchEvent touch event)
        ver_user_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {
                    int position = rv.getChildAdapterPosition(childView);

                    if (position != RecyclerView.NO_POSITION) {
                        String userName = filteredNames.get(position);
                        String userNumber = filteredNumbers.get(position);
                        Integer keyVal = filteredKeyVal.get(position);

                        Log.d("RecyclerView Click", "Position : " + position + ", UserName : " + userName +
                                ", UserNumber : " + userNumber + ", KeyVal : " + keyVal);

                        Intent intent = new Intent(friend_list.this, user_profile_other.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userNumber", userNumber);
                        intent.putExtra("keyVal", keyVal);

                        startActivity(intent);
                        onPause();
                    }
                }

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // Nested ScrollView
        // nestScrollView : setOnScrollChangeListener
        nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (user_recyclerView.getHeight() != 0) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {  // Portrait
                        index_layout.setVisibility(View.VISIBLE);
                        index_land_layout.setVisibility(View.GONE);
                    } else {    // Landscape
                        index_land_layout.setVisibility(View.VISIBLE);
                        index_layout.setVisibility(View.GONE);
                    }
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                            fadeOut.setDuration(200);

                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {  // Portrait
                                index_layout.startAnimation(fadeOut);
                                index_layout.setVisibility(View.GONE);
                            } else {    // Landscape
                                index_land_layout.startAnimation(fadeOut);
                                index_land_layout.setVisibility(View.GONE);
                            }

                            index_guide.setVisibility(View.GONE);
                        }
                    }, 1000);

                }

            }
        });
//
//        // ConstraintLayout
//        // index_layout : OnTouchListener
//        index_layout.setOnTouchListener(new View.OnTouchListener() {
//            boolean isSliding = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                float y = event.getY();
//                int total_height = ver_user_recyclerView.getHeight() + div_layout.getHeight() + (int) (key_notice_layout.getHeight() * 2.3);
//
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:       // touch start
//                        index_guide.setVisibility(View.VISIBLE);
//                        isSliding = true;       // sliding check
//
//                        String section = calculateSection(y);
//                        search = section;
//                        index_guide.setText(search);        // index_guide
//
//                        if (sectionIndexes.containsKey(search)) {
//                            Integer value = sectionIndexes.get(search);
//                            Log.d("test", "search : " + search + " Value : " + value);
//
//                            if (value >= 0 && value < userNames.size()) {
//                                String userName = userNames.get(value);
//                                Log.d("Selected UserName", userName);
//
//                                View targetView = user_recyclerView.getLayoutManager().findViewByPosition(value);
//                                int item_height = targetView.getTop();
//                                nestScrollView.scrollTo(0, total_height + item_height);
//
//                            }
//
//                        }
//
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:       // touching(sliding)
//                        if (isSliding) {
//                            index_layout.setVisibility(View.VISIBLE);
//                            index_guide.setVisibility(View.VISIBLE);
//                            String sectionMove = calculateSection(y);       // cal to SectionIndexer height,and positioning to sectionLetter
//                            search = sectionMove;
//                            index_guide.setText(search);        // index_guide
//                        }
//
//                        if (sectionIndexes.containsKey(search)) {
//                            Integer value = sectionIndexes.get(search);
//                            Log.d("test", "search : " + search + " Value : " + value);
//
//                            if (value >= 0 && value < userNames.size()) {
//                                String userName = userNames.get(value);
//                                Log.d("Selected UserName", userName);
//
//                                View targetView = user_recyclerView.getLayoutManager().findViewByPosition(value);
//                                int item_height = targetView.getTop();
//                                nestScrollView.scrollTo(0, total_height + item_height);
//
//                            }
//                        }
//
//                        break;
//
//                    case MotionEvent.ACTION_UP:     // touch end
//                        isSliding = false;      // sliding check
//                        handler.removeCallbacksAndMessages(null);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {     // EFFECT
//                                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);        // opacity == (start, end)
//                                fadeOut.setDuration(200);       // animation duration
//
//                                index_layout.startAnimation(fadeOut);
//                                index_guide.startAnimation(fadeOut);
//                                index_layout.setVisibility(View.GONE);
//                                index_guide.setVisibility(View.GONE);
//                            }
//                        }, 1000);       // sleep in 1sec
//
//                        break;
//                }
//
//                return true;
//            }
//        });

//        // NEW
//        // ConstraintLayout
//        // index_land_layout : OnTouchListener
//        index_land_layout.setOnTouchListener(new View.OnTouchListener() {
//            boolean isSliding = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                float y = event.getY();
//                int total_height = ver_user_recyclerView.getHeight() + div_layout.getHeight() + (int) (key_notice_layout.getHeight() * 2.3);
//
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:       // touch start
//                        index_guide.setVisibility(View.VISIBLE);
//                        isSliding = true;       // sliding check
//
//                        String section = calculateLandSection(y);
//                        search = section;
//                        index_guide.setText(search);        // index_guide
//
//                        if (sectionIndexes.containsKey(search)) {
//                            Integer value = sectionIndexes.get(search);
//                            Log.d("test", "search : " + search + " Value : " + value);
//
//                            if (value >= 0 && value < userNames.size()) {
//                                String userName = userNames.get(value);
//                                Log.d("Selected UserName", userName);
//
//                                View targetView = user_recyclerView.getLayoutManager().findViewByPosition(value);
//                                int item_height = targetView.getTop();
//                                nestScrollView.scrollTo(0, total_height + item_height);
//
//                            }
//
//                        }
//
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:       // touching(sliding)
//                        if (isSliding) {
//                            index_land_layout.setVisibility(View.VISIBLE);
//                            index_guide.setVisibility(View.VISIBLE);
//                            String sectionMove = calculateLandSection(y);       // cal to SectionIndexer height,and positioning to sectionLetter
//                            search = sectionMove;
//                            index_guide.setText(search);        // index_guide
//                        }
//
//                        if (sectionIndexes.containsKey(search)) {
//                            Integer value = sectionIndexes.get(search);
//                            Log.d("test", "search : " + search + " Value : " + value);
//
//                            if (value >= 0 && value < userNames.size()) {
//                                String userName = userNames.get(value);
//                                Log.d("Selected UserName", userName);
//
//                                View targetView = user_recyclerView.getLayoutManager().findViewByPosition(value);
//                                int item_height = targetView.getTop();
//                                nestScrollView.scrollTo(0, total_height + item_height);
//
//                            }
//                        }
//
//                        break;
//
//                    case MotionEvent.ACTION_UP:     // touch end
//                        isSliding = false;      // sliding check
//                        handler.removeCallbacksAndMessages(null);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {     // EFFECT
//                                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);        // opacity == (start, end)
//                                fadeOut.setDuration(200);       // animation duration
//
//                                index_land_layout.startAnimation(fadeOut);
//                                index_guide.startAnimation(fadeOut);
//                                index_land_layout.setVisibility(View.GONE);
//                                index_guide.setVisibility(View.GONE);
//                            }
//                        }, 1000);       // sleep in 1sec
//
//                        break;
//                }
//
//                return true;
//            }
//        });

        // search_button : OnClickListener
//        search_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(friend_list.this, friend_list_search.class);
//                startActivity(intent);
//                onPause();
//
//            }
//        });

        // Textview
        // ver_user_textView
        ver_user_textView.setText(String.valueOf(filteredNames.size()));

        // user_textView
        user_textView.setText(String.valueOf(userNames.size()));

        // Layout
        // ver_div_layout : OnClickListener (list fold func)
        ver_div_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ver_user_recyclerView.getVisibility() == View.GONE) {
                    ver_user_recyclerView.setVisibility(View.VISIBLE);
                    ver_fold_button.setBackgroundResource(R.drawable.btn_up_arrow);
                } else {
                    ver_user_recyclerView.setVisibility(View.GONE);
                    ver_fold_button.setBackgroundResource(R.drawable.btn_down_arrow);
                }
            }
        });

        // div_layout : OnClickListener (list fold func)
        div_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_recyclerView.getVisibility() == View.GONE) {
                    user_recyclerView.setVisibility(View.VISIBLE);
                    fold_button.setBackgroundResource(R.drawable.btn_up_arrow);
                } else {
                    user_recyclerView.setVisibility(View.GONE);
                    fold_button.setBackgroundResource(R.drawable.btn_down_arrow);
                }
            }
        });

        //bottomNavigationBar
        BottomNavigation bottomNavigation = new BottomNavigation(this, findViewById(R.id.friend_layout_parent));
        bottomNavigation.setupBottomNavigation();

    }  // onCreate

    // generating test phone number data (dummy data)
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(10000);
    }

    // SectionIndexer(PORTRAIT) height calculation
    private String calculateSection(float y){
        // height of index_layout
        int indexHeight = findViewById(R.id.friend_index).getHeight();

        // height of 1 section
        int sectionHeight = indexHeight / 19;

        // transform y axis to section
        int section = (int) (y / sectionHeight);

        // 'ㄱ'위로 스크롤 시 '#'으로의 이동 방지 코드
        section = Math.max(section, 0);
        section = Math.min(section, 18);

        // 계산된 섹션 값을 문자로 변환합니다. - 삭제해도 됨
        //char sectionChar = (char) ('ㄱ' + section - 1); - 삭제해도 됨

        // transform section to sectionLetter
        String sectionLetter;
        switch (section) {
            case 0:
                sectionLetter = getString(R.string.friend_section_0);
                break;
            case 1:
                sectionLetter = getString(R.string.friend_section_1);
                break;
            case 2:
                sectionLetter = getString(R.string.friend_section_2);
                break;
            case 3:
                sectionLetter = getString(R.string.friend_section_3);
                break;
            case 4:
                sectionLetter = getString(R.string.friend_section_4);
                break;
            case 5:
                sectionLetter = getString(R.string.friend_section_5);
                break;
            case 6:
                sectionLetter = getString(R.string.friend_section_6);
                break;
            case 7:
                sectionLetter = getString(R.string.friend_section_7);
                break;
            case 8:
                sectionLetter = getString(R.string.friend_section_8);
                break;
            case 9:
                sectionLetter = getString(R.string.friend_section_9);
                break;
            case 10:
                sectionLetter = getString(R.string.friend_section_10);
                break;
            case 11:
                sectionLetter = getString(R.string.friend_section_11);
                break;
            case 12:
                sectionLetter = getString(R.string.friend_section_12);
                break;
            case 13:
                sectionLetter = getString(R.string.friend_section_13);
                break;
            case 14:
                sectionLetter = getString(R.string.friend_section_14);
                break;
            case 15:
                sectionLetter = getString(R.string.friend_section_15);
                break;
            case 16:
                sectionLetter = getString(R.string.friend_section_16);
                break;
            case 17:
                sectionLetter = getString(R.string.friend_section_17);
                break;
            default:
                sectionLetter = getString(R.string.friend_section_18);
                break;
        }


        return sectionLetter;
    }

    // NEW
    // SectionIndexer(LANDSCAPE) height calculation
    private String calculateLandSection(float y){
        // height of index_layout
        int indexHeight = findViewById(R.id.friend_land_index).getHeight();

        // height of 1 section
        int sectionHeight = indexHeight / 8;

        // transform y axis to section
        int section = (int) (y / sectionHeight);

        // 'ㄱ'위로 스크롤 시 '#'으로의 이동 방지 코드
        section = Math.max(section, 0);
        section = Math.min(section, 7);

        // transform section to sectionLetter
        String sectionLetter;
        switch (section) {
            case 0:
                sectionLetter = getString(R.string.friend_section_0);
                break;
            case 1:
                sectionLetter = getString(R.string.friend_section_2);
                break;
            case 2:
                sectionLetter = getString(R.string.friend_section_4);
                break;
            case 3:
                sectionLetter = getString(R.string.friend_section_6);
                break;
            case 4:
                sectionLetter = getString(R.string.friend_section_8);
                break;
            case 5:
                sectionLetter = getString(R.string.friend_section_10);
                break;
            case 6:
                sectionLetter = getString(R.string.friend_section_12);
                break;
            default:
                sectionLetter = getString(R.string.friend_section_14);
                break;
        }

        return sectionLetter;
    }

    private boolean backButtonPressedOnce = false;
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

}       // public class friend_list

//// extraction of korean initial sound
//class KoreanExtraction {
//    private static final char HANGUL_BEGIN_UNICODE = 44032;
//    private static final char HANGUL_BASE_UNIT = 588;
//    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
//            'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
//
//    private static boolean isInitialSound(char searchar){
//        for(char c:INITIAL_SOUND){
//            if(c == searchar){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static char getInitialSound(char c) {
//        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
//        int index = hanBegin / HANGUL_BASE_UNIT;
//        return INITIAL_SOUND[index];
//    }
//
//    private static boolean isHangul(char c) {
//        return HANGUL_BEGIN_UNICODE <= c && c <= (HANGUL_BEGIN_UNICODE + 11171);
//    }
//
//    public static char extractInitial(char c) {
//        if (isHangul(c)) {
//            return getInitialSound(c);
//        } else {
//            return c;
//        }
//    }
//}
//
//// sorting text kinds & mapping korean initial sound, first letter with key values
//class CustomComparator implements Comparator<String> {
//    private Collator collator = Collator.getInstance(Locale.KOREAN);
//    private HashMap<String, Integer> sectionIndexes = new HashMap<>();
//    public HashMap<String, Integer> getSectionIndexes() {
//        return sectionIndexes;
//    }
//
//    @Override
//    public int compare(String o1, String o2) {
//        int category1 = getCategory(o1);
//        int category2 = getCategory(o2);
//
//        if (category1 != category2) {
//            return category1 - category2;
//        }
//
//        return collator.compare(o1, o2);
//    }
//
//    int getCategory(String str) {
//        char firstChar = KoreanExtraction.extractInitial(str.charAt(0));        // extract korean initial sound
//
//        if (Character.getType(firstChar) == Character.OTHER_LETTER) {       // sequence sorting : kor -> eng -> num -> etc
//            return 0; // etc (special character)
//        } else if (Character.getType(firstChar) == Character.LOWERCASE_LETTER) {
//            return 1; // eng
//        } else if (Character.isDigit(firstChar)) {
//            return 2; // num
//        } else {
//            return 3; // kor
//        }
//    }
//
//    // put the key(firstchar), the key value(int) in the HashMap of sectionIndex
//    public void generateSectionIndexes(List<String> data) {
//        String currentSection = "";
//        for (int i = 0; i < data.size(); i++) {
//            String item = data.get(i);
//            String firstChar = String.valueOf(KoreanExtraction.extractInitial(item.charAt(0))).toUpperCase(Locale.getDefault());        // extract korean initial sound
//
//            if (!currentSection.equals(firstChar)) {
//                sectionIndexes.put(firstChar, i);
//                currentSection = firstChar;
//            }
//        }
//    }
//}
