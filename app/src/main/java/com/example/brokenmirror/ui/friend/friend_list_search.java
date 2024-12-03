package com.example.brokenmirror.ui.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.setting.user_profile_other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class friend_list_search extends AppCompatActivity {

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

    private ArrayList<String> filteredNames = new ArrayList<>();
    private ArrayList<String> filteredNumbers = new ArrayList<>();
    private ArrayList<Integer> filteredKeyVals = new ArrayList<>();

    String search = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_search);

        EditText search_editText = findViewById(R.id.friend_search_editText);
        search_editText.requestFocus();

        Button clear_button = findViewById(R.id.friend_search_clear_button);
        Button back_button = findViewById(R.id.friend_search_back_button);

        RecyclerView result_recyclerView = findViewById(R.id.friend_search_recyclerView);
        result_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NestedScrollView nestedScrollView = findViewById(R.id.friend_search_nestedScrollView);

        TextView result_textView = findViewById(R.id.friend_search_result_textView);

        ConstraintLayout header_layout = findViewById(R.id.header);


        // making user dummy data
        for (int i = 0; i < userNames.size(); i++) {
            String userNumber = String.format("010%04d%04d", generateRandomNumber(), generateRandomNumber());

            userNumbers.add(userNumber);
        }

        // EditText
        // Focus : search_editText
        search_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {       // focus out
                    if (search.isEmpty()) {     // empty
                        search_editText.setBackgroundResource(R.drawable.btn_bottomsheet);
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search, 0, 0, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint));
                        clear_button.setOnClickListener(null);
                    } else {        // not empty
                        search_editText.setBackgroundResource(R.drawable.edittext_emph);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);        // display search_emph, clear_btn
                        clear_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                search_editText.setText("");
                                search_editText.clearFocus();
                            }
                        });
                    }
                } else {        // focus in
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);

                    if (search.isEmpty()) {     // empty
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, 0, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        clear_button.setOnClickListener(null);
                    } else {        // not empty
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        clear_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                search_editText.setText("");
                                search_editText.requestFocus();
                            }
                        });
                    }

                }
            }
        });

        // Change : search_editText
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString();

                // search_editText search func, Renewal
                filteredNames.clear();
                filteredNumbers.clear();
                filteredKeyVals.clear();

                result_textView.setVisibility(View.GONE);

                for (int j = 0; j < userNames.size(); j++) {
                    String name = userNames.get(j).toLowerCase();       // ignore upper, lower text when searching
                    String number = userNumbers.get(j);

                    if (name.contains(searchText) || number.contains(searchText)) {     // add search result item
                        filteredNames.add(userNames.get(j));
                        filteredNumbers.add(userNumbers.get(j));
                        filteredKeyVals.add(keyVal.get(j));
                    }
                }

                // display no result textView logic
                if (filteredNames.isEmpty()) {
                    result_textView.setVisibility(View.VISIBLE);
                    result_textView.setText(R.string.friend_search_no_result);
                } else {
                    result_textView.setVisibility(View.GONE);
                }

                // sort
                CustomComparator customComparator = new CustomComparator();
//                Collections.sort(filteredNames, customComparator);

                // result_recyclerView : new adapter for items refreshing
                friend_list_customAdapter filteredAdapter = new friend_list_customAdapter(filteredNames, filteredNumbers, filteredKeyVals, new friend_list_customAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
                result_recyclerView.setAdapter(filteredAdapter);
                filteredAdapter.notifyDataSetChanged();

                // Change : search_editText design logic
                if (searchText.isEmpty()) {       // empty
                    search_editText.requestFocus();
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, 0, 0);

                    result_textView.setVisibility(View.VISIBLE);
                    result_textView.setText(R.string.friend_search_init);
                    filteredAdapter.clear();

                } else {        // not empty
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);        // display search_emph, clear_btn

                    clear_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    // Clear focus
                    search_editText.clearFocus();

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(friend_list_search.this, friend_list.class);
                startActivity(intent);
                finish();
            }
        });

        // RecyclerView
        // result_recyclerView : OnTouchListener (clear focus)
        result_recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });

        // header_layout : OnClickListener (Clear Focus)
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        result_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                        Integer keyVal = filteredKeyVals.get(position);

                        Log.d("RecyclerView Click", "Position : " + position + ", UserName : " + userName +
                                ", UserNumber : " + userNumber + ", KeyVal : " + keyVal);

                        Intent intent = new Intent(friend_list_search.this, user_profile_other.class);
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

        // NestedScrollView
        // nestedScrollView : OnTouchListener (clear focus)
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });
    }       // onCreate

    // generating test phone number data (dummy data)
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(10000);
    }

    // Navigation - Back Button (Android System)
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(friend_list_search.this, friend_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}