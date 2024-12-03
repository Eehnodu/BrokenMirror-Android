package com.example.brokenmirror.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.friend.KoreanExtraction;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

public class chat_add extends AppCompatActivity implements chat_add_adapter.OnItemSelectedListener {

    private ArrayList<String> number_list = new ArrayList<>();
    private ArrayList<String> name_list = new ArrayList<>(Arrays.asList(        // test data
            "손흥민", "권흥민", "1흥민", "house", "김흥민", "2김흥민", "jellyfish", "김샬리송", "김사르", "3김베르너", "티모 베르너",
            "지오바니", "김날두", "wizard", "남케인", "남케인 .", "남 케인", "데스티니-우도기", "데스티니우도기", "도시락형제", "데안", "박매디슨", "스콧카슨", "아데르송", "👍아칸지", "음바페",
            "로셀소", "4이반", "이반", "정오바니", "모하메드엘네니", "조르지뉴", "탈까", "xylophone", "표지판", "쿨루셉스키", "토마스파티", "홀란드",
            "*개발", "!기획관리", "~솔루션", "#시스템", "@R&D", "appleappleappleappleappleappleappleappleappleappleappleapple", "baloon", "cake", "dog", "egg",
            "flamingo", "grape", "house", "icecream",  "kite", "lemon", "nose", "mouse", "pig",
            "orange", "rabbit", "queen", "zebra", "yo-wassup","violin", "umbrella",
            "tomato", "sun"
    ));
    private ArrayList<Integer> key_list = new ArrayList<>(Arrays.asList(      // 0 : expired, 1 : waiting, 2 : certified, 3 : none
            3, 1, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 2, 3, 3, 3,
            2, 3, 3, 3, 1, 3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2
    ));
    private ArrayList<ArrayList<Object>> combined_list = new ArrayList<>();
    private ArrayList<String> search_name = new ArrayList<>();
    private ArrayList<String> search_number = new ArrayList<>();
    private ArrayList<Integer> search_key = new ArrayList<>();
    private ArrayList<ArrayList<Object>> search_combined_list = new ArrayList<>();
    private ArrayList<UserInfo> selectedUsers = new ArrayList<>();
    Button next_button;
    EditText search_editText;

    String search = "";
    private String selectedFriendId;
    private String friendName;
    private String friendNumber;
    private int friendKey;

    chat_add_adapter adapter;
    chat_add_adapter search_adapter;


    class UserInfo {
        private String userName;
        private String userNumber;
        private int userKey;

        public UserInfo(String userName, String userNumber, int userKey) {
            this.userName = userName;
            this.userNumber = userNumber;
            this.userKey = userKey;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserNumber() {
            return userNumber;
        }

        public int getUserKey() {
            return userKey;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            UserInfo otherUser = (UserInfo) obj;

            // check if the contents are the same
            return userName.equals(otherUser.userName)
                    && userNumber.equals(otherUser.userNumber)
                    && userKey == otherUser.userKey;
        }

        @Override
        public int hashCode() {     // Minimizing collisions in the hash function using 31
            int result = userName.hashCode();
            result = 31 * result + userNumber.hashCode();
            result = 31 * result + Integer.hashCode(userKey);
            return result;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_add);

        ConstraintLayout header_layout = findViewById(R.id.header);
        NestedScrollView nestedScrollView = findViewById(R.id.chat_add_nestedScrollView);
        Button close_button = findViewById(R.id.close_button);
        Button clear_button = findViewById(R.id.clear_button);
        next_button = findViewById(R.id.next_button);
        search_editText = findViewById(R.id.chat_add_search_editText);

        RecyclerView recyclerView = findViewById(R.id.friend_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Generate & Set Data
        // Generate Phone Num
        for (int i = 0; i <= name_list.size(); i++) {
            String userNumber = String.format("010%04d%04d", generateRandomNumber(), generateRandomNumber());
            number_list.add(userNumber);
        }

        // Making normal data in one
        for (int i = 0; i < name_list.size(); i++) {
            ArrayList<Object> item = new ArrayList<>();
            item.add(name_list.get(i));
            item.add(number_list.get(i));
            item.add(key_list.get(i));
            combined_list.add(item);
        }

        combined_list.sort(new NewComparator());

        // init
        name_list.clear();
        number_list.clear();
        key_list.clear();

        // Separating data from combined_list
        for (ArrayList<Object> item : combined_list) {
            name_list.add((String) item.get(0));
            number_list.add((String) item.get(1));
            key_list.add((Integer) item.get(2));
        }


        // Set Adapter : recyclerView
        adapter = new chat_add_adapter(name_list, number_list, key_list);
        adapter.setOnItemSelectedListener(this);
        recyclerView.setAdapter(adapter);

        // Button
        // close_button : finish
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 하단 다음 버튼
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1:1 대화
                if (selectedUsers.size() > 1) {
                    showToast(chat_add.this, getString(R.string.chat_add_current_service));
                } else if (selectedUsers.size() < 1) {
                    showToast(chat_add.this, getString(R.string.chat_add_no_choice));
                } else {
                    UserInfo selectedUser = selectedUsers.get(0);
                    friendName = selectedUser.getUserName();    // 첫 번째 데이터를 user_name에 저장
                    friendNumber = selectedUser.getUserNumber(); // 두 번째 데이터를 user_number에 저장
                    friendKey = selectedUser.getUserKey();       // 세 번째 데이터를 user_key에 저장

                    Intent intent = new Intent(chat_add.this, ChatRoomActivity.class);
                    intent.putExtra("friend's id", selectedFriendId);
                    intent.putExtra("friend's name", friendName);
                    intent.putExtra("friend's number", friendNumber);
                    intent.putExtra("friend's key", friendKey);
                    startActivity(intent);
                    finish();
                }
            }

        });


        // EditText
        // search_editText : detect ENTER
        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_NULL) {
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

        // search_editText : focus
        search_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            next_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (search.isEmpty()) {     // empty
                        search_editText.setBackgroundResource(R.drawable.btn_bottomsheet);
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search, 0, 0, 0);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint));
                        clear_button.setVisibility(View.GONE);
                    } else {        // not empty
                        search_editText.setBackgroundResource(R.drawable.edittext_emph);
                        search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                        search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);
                        clear_button.setVisibility(View.VISIBLE);
                        clear_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                search_editText.setText("");
                                search_editText.clearFocus();
                                selectedUsers.clear();
                                next_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                                next_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                            }
                        });
                    }
                } else {        // focus in
//                    next_button.setVisibility(View.GONE);
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
                                selectedUsers.clear();
                                next_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                                next_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                            }
                        });
                    }
                }

            }
        });

        // search_editText : change
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString();

                // init
                search_name.clear();
                search_number.clear();
                search_key.clear();
                search_combined_list.clear();

                for (int j = 0; j < name_list.size(); j++) {
                    String name = name_list.get(j).toLowerCase();       // ignore upper, lower text when searching
                    String number = number_list.get(j);

                    if (name.contains(searchText) || number.contains(searchText)) {     // add search result item
                        search_name.add(name_list.get(j));
                        search_number.add(number_list.get(j));
                        search_key.add(key_list.get(j));
                    }
                }

                // making search data in one
                for (int j = 0; j < search_name.size(); j++) {
                    ArrayList<Object> item = new ArrayList<>();
                    item.add(search_name.get(j));
                    item.add(search_number.get(j));
                    item.add(search_key.get(j));
                    search_combined_list.add(item);
                }

                // sort
                search_combined_list.sort(new NewComparator());

                search_name.clear();
                search_number.clear();
                search_key.clear();

                // separating data from search_combined_list
                for (ArrayList<Object> item : search_combined_list) {
                    search_name.add((String) item.get(0));
                    search_number.add((String) item.get(1));
                    search_key.add((Integer) item.get(2));
                }


                // set search result adapter
                search_adapter = new chat_add_adapter(search_name, search_number, search_key);
                recyclerView.setAdapter(search_adapter);
                search_adapter.setOnItemSelectedListener(chat_add.this);
                search_adapter.notifyDataSetChanged();


                // design
                if (searchText.isEmpty()) {     // empty
                    search_editText.requestFocus();
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, 0, 0);
                } else {        // not empty
                    search_editText.setBackgroundResource(R.drawable.edittext_emph);
                    search_editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                    search_editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search_emph, 0, R.drawable.close_btn_edittext, 0);
                    clear_button.setVisibility(View.VISIBLE);
                    clear_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search_editText.setText("");
                            selectedUsers.clear();
                            next_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                            next_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));

                        }
                    });
                }
                search = searchText;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // NestedScrollView
        // nestedScrollView : clear focus
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                search_editText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });

        // header_layout : Clear Focus
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_editText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

    }       // onCreate



    // generating test phone number data (dummy data)
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(10000);
    }

    public void onItemSelected(String userName, String userNumber, int userKey) {
        // Create an instance of UserInformation
        UserInfo userInfo = new UserInfo(userName, userNumber, userKey);

        search_editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_editText.getWindowToken(), 0);

        // Check if the UserInformation instance is already in the list
        if (!selectedUsers.contains(userInfo)) {
            // If not, add it to the list
            selectedUsers.add(userInfo);
        } else {
            // If already in the list, remove it
            selectedUsers.remove(userInfo);
        }

        // set next_button
        if (selectedUsers.size() == 1) {
            next_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
            next_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else {
            next_button.setBackgroundResource(R.drawable.login_main_loginbtn);
            next_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        }

        friendName = userName;
        friendNumber = userNumber;
        friendKey = userKey;
    }

    private void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

}     // chat_add.java



// sorting text kinds & mapping korean initial sound, first letter with key values
class NewComparator implements Comparator<ArrayList<Object>> {
    private Collator collator = Collator.getInstance(Locale.KOREAN);

    @Override
    public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
        String str1 = (String) o1.get(0);       // extract name_list
        String str2 = (String) o2.get(0);       // extract name_list

        int category1 = getCategory(str1);
        int category2 = getCategory(str2);

        if (category1 != category2) {
            return category1 - category2;
        }

        return collator.compare(str1, str2);
    }

    int getCategory(String str) {
        char firstChar = KoreanExtraction.extractInitial(str.charAt(0)); // extract korean initial sound

        if (Character.getType(firstChar) == Character.OTHER_LETTER) { // sequence sorting : kor -> eng -> num -> etc
            return 0; // etc (special character)
        } else if (Character.getType(firstChar) == Character.LOWERCASE_LETTER) {
            return 1; // eng
        } else if (Character.isDigit(firstChar)) {
            return 2; // num
        } else {
            return 3; // kor
        }
    }
}
