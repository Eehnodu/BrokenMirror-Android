package com.example.brokenmirror.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.ui.BottomNavigation;
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

public class chat_main extends AppCompatActivity {

    private final ArrayList<String> name_list = new ArrayList<>(Arrays.asList("house", "jellyfish", "wizard", "xylophone", "*개발", "!기획관리", "~솔루션", "#시스템", "@R&D", "송지광", "balloon", "cake", "dog", "egg", "flamingo", "1grape", "house", "icecream", "kite", "2lemon", "nose", "mouse", "pig", "orange", "rabbit", "queen", "zebra", "yo-wassup", "violin", "umbrella", "tomato", "sun"));
    private final ArrayList<String> message_list = new ArrayList<>();
    private final ArrayList<String> date_list = new ArrayList<>();
//    private final ArrayList<DataItem> combined_list = new ArrayList<>();

    private final ArrayList<String> search_name_list = new ArrayList<>();
    private final ArrayList<String> search_message_list = new ArrayList<>();
    private final ArrayList<String> search_date_list = new ArrayList<>();
//    private final ArrayList<DataItem> search_combined_list = new ArrayList<>();

    private int backPressCount = 0;

    String search = "";

    // 로그인 후 유저의 정보
    String id;

//    // UserSharedPref
//    private UserSharedPref user_pref;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);

//        // UserSharedPref
//        user_pref = new UserSharedPref(this);
//        user = user_pref.getUser();

        RecyclerView recyclerView = findViewById(R.id.chat_main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText search_editText = findViewById(R.id.chat_main_editText);

        Button clear_button = findViewById(R.id.chat_main_clear_button);
        Button add_button = findViewById(R.id.chat_main_add_button);

        TextView result_textView = findViewById(R.id.chat_main_result_textView);

        NestedScrollView nestedScrollView = findViewById(R.id.chat_main_nestedScrollView);

        ConstraintLayout header_layout = findViewById(R.id.header);
        ConstraintLayout navigation = findViewById(R.id.navigation);

//        // generate Random Data
//        generateRandomData(name_list.size());
//
//        // sort
//        Collections.sort(combined_list, new DateComparator());
//
//        // clear name_list
//        name_list.clear();
//
//        // split data
//        for (DataItem item : combined_list) {
//            name_list.add(item.name);
//            message_list.add(item.message);
//            date_list.add(item.date);
//        }

        //  SharedPreferences에서 userId 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // set adapter
//        chat_main_adapter adapter = new chat_main_adapter(name_list, message_list, date_list);
//        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                search_editText.clearFocus();
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // EditText
        // search_editText : focus
        search_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigation.setVisibility(View.VISIBLE);
                        }
                    }, 150);
                    if (search.isEmpty()) {
                        search_editText.setBackgroundResource(R.drawable.btn_bottomsheet);
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search, 0, 0, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint));
                        clear_button.setVisibility(View.GONE);
                    } else {    // not empty
                        search_editText.setBackgroundResource(R.drawable.edittext_emph);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);      // display search_emph, clear_butotn
                        clear_button.setVisibility(View.VISIBLE);
                        clear_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                search_editText.setText("");
                                search_editText.clearFocus();
                            }
                        });
                    }
                } else {        // focus in
                    navigation.setVisibility(View.GONE);
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    if (search.isEmpty()) {     // empty
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, 0, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        clear_button.setVisibility(View.GONE);
                    } else {        // not empty
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        clear_button.setVisibility(View.VISIBLE);
                        clear_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                search_editText.setText("");
                                search_editText.requestFocus();
                            }
                        });
                    }
                }

            }
        });


        // search_editText : Change
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString();

                // search_editText search func, Renewal
                search_name_list.clear();
                search_message_list.clear();
                search_date_list.clear();

                result_textView.setVisibility(View.INVISIBLE);

                for (int j = 0; j < name_list.size(); j++) {
                    String name = name_list.get(j).toLowerCase();       // ignore upper, lower text when searching

                    if (name.contains(searchText)) {        // add search result item
                        search_name_list.add(name_list.get(j));
                        search_message_list.add(message_list.get(j));
                        search_date_list.add(date_list.get(j));

//                        for (DataItem item : search_combined_list) {
//                            search_name_list.add(item.name);
//                            search_message_list.add(item.message);
//                            search_date_list.add(item.date);
//                        }
                    }
                }

                // display no result textView
                if (search_name_list.isEmpty()) {
                    result_textView.setVisibility(View.VISIBLE);
                    result_textView.setText(R.string.friend_search_no_result);
                } else {
                    result_textView.setVisibility(View.INVISIBLE);
                }

                // Sort
//                Collections.sort(search_combined_list, new DateComparator());

                // set result adapter : item refreshing
//                chat_main_adapter adapter1 = new chat_main_adapter(search_name_list, search_message_list, search_date_list);
//                recyclerView.setAdapter(adapter1);
//                adapter1.notifyDataSetChanged();

                // design logic
                if (searchText.isEmpty()) {     // empty
                    search_editText.requestFocus();
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, 0, 0);

//                    recyclerView.setAdapter(adapter);

                } else {        // not empty
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);      // display search_emph, clear_button
                    clear_button.setVisibility(View.VISIBLE);

                    clear_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search_editText.setText("");
                        }
                    });
                }
                search = searchText;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // search_editText : detect ENTER
        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_NULL) {
                    // clear focus
                    search_editText.clearFocus();

                    // hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });


        // Button
        // add_button : intent (chat_main -> chat_add)
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_main.this, chat_add.class);
                startActivity(intent);
                onPause();
            }
        });

        // No Chat History
        if (name_list.size() == 0 || message_list.size() == 0 || date_list.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            result_textView.setVisibility(View.VISIBLE);
            result_textView.setText(R.string.chat_main_no_history);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            result_textView.setVisibility(View.INVISIBLE);
        }


        // NestedScrollView
        // nestedScrollView : focus out
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        // RecyclerView
        // recyclerView : focus out
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        // header_layout : focus out
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        // BottomNavigationBar Color
        findViewById(R.id.nav_chat_icon).setBackgroundResource(R.drawable.bottom_navigation_icon_chat_emph);
        ((TextView) findViewById(R.id.nav_chat_text)).setTextColor(ContextCompat.getColor(this, R.color.main));

        //bottomNavigationBar
        BottomNavigation bottomNavigation = new BottomNavigation(this, findViewById(R.id.chat_layout));
        bottomNavigation.setupBottomNavigation();

    }   // onCreate



//    private void generateRandomData(int count) {
//        for (int i = 0; i < count; i++) {
//            String message = "사진을 수신 받았습니다.";
//            DataItem item = new DataItem(name_list.get(i), message);
//            combined_list.add(item);
//        }
//    }
//
//
//    // generate dummy data
//    private static class DataItem {
//        String name;
//        String date;
//        String message;
//
//        DataItem(String name, String message) {
//            this.name = name;
//            this.date = generateRandomDate();
//            this.message = message;
//        }
//
//        private String generateRandomDate() {
//            Calendar calendar = Calendar.getInstance(); // get current date
//
//            // generate random date
//            Random random = new Random();
//            int daysToAdd = random.nextInt(365);
//            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
//
//            // generate random hour
//            int randomHour = random.nextInt(24);
//            calendar.set(Calendar.HOUR_OF_DAY, randomHour);
//
//            // generate random min
//            int randomMinute = random.nextInt(60);
//            calendar.set(Calendar.MINUTE, randomMinute);
//
//            // set formation
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
//
//            return sdf.format(calendar.getTime());
//        }
//    }
//
//
//    // Sort: Date
//    private static class DateComparator implements Comparator<DataItem> {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
//
//        @Override
//        public int compare(DataItem o1, DataItem o2) {
//            try {
//                Date date1 = sdf.parse(o1.date);
//                Date date2 = sdf.parse(o2.date);
//
//                // 날짜를 비교하여 오름차순으로 정렬
//                assert date2 != null;
//                return date2.compareTo(date1);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                return 0;
//            }
//        }
//    }


//    @Override
//    public void onBackPressed() {
//        BackPressedHandler backPressedHandler = new BackPressedHandler(this);
//        if (backPressedHandler.onBackPressed()) {
//            super.onBackPressed();
//            finish();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("ghkrdls", "Activity destroyed");
//        if(isTaskRoot()){
//            Log.d("ghkrdls", "finish");
//            Toast toast = Toast.makeText(this, getResources().getText(R.string.app_exit), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
//            toast.show();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("ghkrdls", "Activity destroyed");
//        if(isTaskRoot()){
//            BackPressedHandler backPressedHandler = new BackPressedHandler(this);
//            backPressedHandler.onBackPressed();
//        }
//    }
//
//    private boolean backButtonPressedOnce = false;
//    private Handler handler = new Handler();
//    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds
//        @Override
//        public void onBackPressed() {
//            if (backButtonPressedOnce) {
//                super.onBackPressed(); // 두 번째 뒤로가기 버튼 눌림
//                Log.d("ghkrdls", "Activity destroyed");
//                handler.removeCallbacksAndMessages(null); // 핸들러 메시지 제거
//            } else {
//                if (isTaskRoot()) {
//                    backButtonPressedOnce = true; // 첫 번째 뒤로가기 버튼 눌림
//                    Toast.makeText(this, getResources().getText(R.string.app_exit), Toast.LENGTH_SHORT).show();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            backButtonPressedOnce = false; // 2초가 지나면 초기화
//                        }
//                    }, DOUBLE_BACK_PRESS_INTERVAL);
//                } else {
//                    super.onBackPressed(); // 루트가 아닐 때
//                }
//            }
//        }
}
