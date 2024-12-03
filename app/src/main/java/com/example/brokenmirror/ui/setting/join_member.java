/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.07, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'join_member.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.bitmap.BitmapConverter;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class join_member extends AppCompatActivity {
    private boolean[] req = new boolean[8];
    private Toast toast;
    private Button email_num_button;
    private Button join_button;
    private Button email_list_button;
    private Button name_button;
    private Button male_button;
    private Button female_button;
    private EditText emailNum_editText;
    private EditText email_editText;
    private EditText pw_editText;
    private EditText pw_check_editText;
    private EditText name_editText;
    private EditText birth_editText;
    private EditText phone_editText;
    private TextView name_condition;
    private TextView birth_condition;
    private TextView phone_condition;
    private TextView email_condition;
    private TextView pw_condition;
    private TextView pw_conf_condition;
    private final String idPattern = "[a-zA-Z0-9._-]+";
    private final String emailNumPattern = "\\d+";
    private final String pwPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private final String namePattern = "[가-힣]*";
    private final String birthPattern = "\\d{8}";
    private final String phonePattern = "\\d{11}";
    private String inputEmail;
    private String inputEmailNum;
    private String inputPW;
    private String inputPwCheck;
    private String inputName;
    private String inputBirth;
    private String inputPhone;
    private String inputNameValue;
    private String buttonText;
    private String email_num = "12345";
    private String email;
//    private String profileImg;

    private ConstraintLayout constraintLayout;
    private NestedScrollView nestedScrollView;

    private UserApi userApi = RetrofitService.getUserApi();

//    private BitmapConverter converter = new BitmapConverter();

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_member);

        int colorMain = ContextCompat.getColor(join_member.this, R.color.main);
        int colorDefault = ContextCompat.getColor(join_member.this, R.color.hint);

        final Button[] lastClickedButton = {null};

        constraintLayout = findViewById(R.id.layout_join_member);
        nestedScrollView = findViewById(R.id.nestedView_join_member);

        Button close_button = findViewById(R.id.join_member_close_button);
        name_button = findViewById(R.id.join_member_name_button);
        male_button = findViewById(R.id.join_member_birth_male_button);
        female_button = findViewById(R.id.join_member_birth_female_button);
        Button back_button = findViewById(R.id.back_button);
        email_num_button = findViewById(R.id.join_member_emailNum_button);
        join_button = findViewById(R.id.join_member_join_button);
        email_list_button = findViewById(R.id.join_member_email_list_button);

        name_editText = findViewById(R.id.join_member_name_editText);
        birth_editText = findViewById(R.id.join_member_birth_editText);
        phone_editText = findViewById(R.id.join_member_phone_editText);
        emailNum_editText = findViewById(R.id.join_member_emailNum_editText);
        email_editText = findViewById(R.id.join_member_email_editText);
        pw_editText = findViewById(R.id.join_member_pw_editText);
        pw_check_editText = findViewById(R.id.join_member_pw_check_editText);

        name_condition = findViewById(R.id.join_member_name_condition_textView);
        birth_condition = findViewById(R.id.join_member_birth_condition_textView);
        phone_condition = findViewById(R.id.join_member_phone_condition_textView);
        email_condition = findViewById(R.id.join_member_email_condition_textView);
        pw_condition = findViewById(R.id.join_member_pw_condition_textView);
        pw_conf_condition = findViewById(R.id.join_member_pw_check_condition_textView);

        inputEmail = email_editText.getText().toString();
        inputEmailNum = emailNum_editText.getText().toString();
        inputPW = pw_editText.getText().toString();
        inputPwCheck = pw_check_editText.getText().toString();
        inputName = name_editText.getText().toString();
        inputBirth = birth_editText.getText().toString();
        inputPhone = phone_editText.getText().toString();

        inputNameValue = inputName;

        email_condition.setSelected(true);
        email_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        pw_condition.setSelected(true);
        pw_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        pw_conf_condition.setSelected(true);
        pw_conf_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name_condition.setSelected(true);
        name_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        birth_condition.setSelected(true);
        birth_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        phone_condition.setSelected(true);
        phone_condition.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        email_list_button.setSelected(true);
        email_list_button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        email_num_button.setSelected(true);
        email_num_button.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        // Button
        // close_button
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
                // close all activities
                for (ActivityManager.AppTask appTask : appTasks) {
                    appTask.finishAndRemoveTask();
                }
                // switch to the 'login_main' activity
                Intent intent = new Intent(join_member.this, login_main.class);
                startActivity(intent);
                finish();
            }
        });

        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
                // close all activities
                for (ActivityManager.AppTask appTask : appTasks) {
                    appTask.finishAndRemoveTask();
                }
                // switch to the 'login_main' activity
                Intent intent = new Intent(join_member.this, login_main.class);
                startActivity(intent);
                finish();
            }
        });

        // email_list_button
        email_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetEmailDialog bottomSheetDialogFragment = new BottomSheetEmailDialog();
                // BottomSheetEmailDialog -> join_member
                bottomSheetDialogFragment.setTextButtonClickListener(new BottomSheetEmailDialog.TextButtonClickListener() {
                    @Override
                    public void onTextButtonClick(String buttonText) {
                        email_list_button.setText(buttonText);      // when changing email, set editText == ""
                        email_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        if (!(inputEmail.isEmpty())) {
                            emailTextChange(inputEmail);
                        }
                    }
                });

                // join_member -> BottomSheetEmailDialog
                buttonText = email_list_button.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("buttonText", buttonText);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheetDialogFragment");
            }
        });

        // email_num_button
        email_num_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = email_editText.getText().toString();
                String current_email = email_list_button.getText().toString();
                if (input.isEmpty() || email_condition.getVisibility() == View.VISIBLE) {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_email_warning), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                } else if (email_condition.getVisibility() == View.GONE) {
                    if (current_email.equals(getString(R.string.find_id_email_list_0))) {
                        email = input;
                    } else {
                        email = input + current_email;
                    }
                    // certifyMail (인증번호 메일 발송)
                    email_num = certifyMail(email);
                    showPopupDialog_first(email);
                }
            }
        });

        // name_button, bottomSheetDialog
        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetNationDialog bottomSheetDialogFragment = new BottomSheetNationDialog();
                // BottomSheetNationDialog -> join_member
                bottomSheetDialogFragment.setTextButtonClickListener(new BottomSheetNationDialog.TextButtonClickListener() {
                    @Override
                    public void onTextButtonClick(String buttonText) {
                        name_button.setText(buttonText);        // import selected nationality text from bottomSheetNameDialog
                    }
                });
                // login_find_id -> BottomSheetDialog
                // showing selectd button and change button color in bottomSheetDialog
                String buttonText = name_button.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("buttonText", buttonText);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheetDialogFragment");
            }
        });

        // male_button
        male_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClickedButton[0] != male_button) {
                    if (lastClickedButton[0] != null) {
                        lastClickedButton[0].setBackgroundResource(R.drawable.login_main_edittext);
                        lastClickedButton[0].setTextColor(colorDefault);
                    }
                    male_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                    male_button.setTextColor(colorMain);
                    lastClickedButton[0] = male_button;
                }
                req[6] = true;
                updateJoinButton(req);
            }
        });

        // female_button
        female_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClickedButton[0] != female_button) {
                    if (lastClickedButton[0] != null) {
                        lastClickedButton[0].setBackgroundResource(R.drawable.login_main_edittext);
                        lastClickedButton[0].setTextColor(colorDefault);
                    }
                    female_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                    female_button.setTextColor(colorMain);
                    lastClickedButton[0] = female_button;
                }
                req[6] = true;
                updateJoinButton(req);
            }
        });

        // join_button
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allFalse = true;
                // req 배열이 모두 false 인지 확인
                for (boolean b : req) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    Toast.makeText(getApplicationContext(), getString(R.string.find_pw_data_input_warning), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // editText
        // editText Focus
        // Focus : email_editText
        email_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputEmail.isEmpty()) {     // focus out : 공백일 때
                        email_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        email_condition.setVisibility(View.GONE);
                    } else {
                        if (email_list_button.getText().toString().equals(getString(R.string.find_id_email_list_0))) {     // focus out : 직접 입력일 때
                            if (Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {      //  focus out : 직접 입력 - 이메일 형식이 맞을 경우
                                email_editText.setBackgroundResource(R.drawable.login_main_edittext);
                                email_condition.setVisibility(View.GONE);
                            } else {         // focus out : 직접 입력 - 공백이 아닌, 조건에 부합하지 않을 때
                                email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                email_condition.setVisibility(View.VISIBLE);
                            }
                        } else {      // focus out : 직접 입력이 아닐 때 (이메일 4종 조건)
                            if (inputEmail.matches(idPattern)) {        // focus out : 이메일 선택 - 아이디 조건에 부합할 때
                                email_editText.setBackgroundResource(R.drawable.login_main_edittext);
                                email_condition.setVisibility(View.GONE);
                            } else {        // focus out : 이메일 선택 - 아이디 조건에 부합하지 않고, 공백도 아닐 때
                                email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                email_condition.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {      // focus in 일 때
                    if (inputEmail.isEmpty()) {        // focus in : 공백일 때
                        email_editText.setBackgroundResource(R.drawable.textfield_activate);
                        email_condition.setVisibility(View.GONE);
                    } else {
                        if (email_list_button.getText().toString().equals(getString(R.string.find_id_email_list_0))) {     // focus in : 직접 입력일 때
                            if (Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {      //  focus in : 직접 입력 - 이메일 형식이 맞을 경우
                                email_editText.setBackgroundResource(R.drawable.textfield_activate);
                                email_condition.setVisibility(View.GONE);
                            } else {        // focus in : 직접 입력 - 이메일 형식이 맞지 않은 경우 (공백 제외)
                                email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                email_condition.setVisibility(View.VISIBLE);
                            }
                        } else {        // focus in : 이메일 4종 일 때
                            if (inputEmail.matches(idPattern)) {       // focus in : 이메일 4종 - 조건에 부합할 때
                                email_editText.setBackgroundResource(R.drawable.textfield_activate);
                                email_condition.setVisibility(View.GONE);
                            } else {        // focus in : 이메일 4종 - 조건에 부합 하지 않고 공백도 아닐 때
                                email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                email_condition.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

        // Focus : emailNum_editText
        emailNum_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputEmailNum.isEmpty()) {      // focus out : 공백일 때
                        emailNum_editText.setBackgroundResource(R.drawable.login_main_edittext);
                    } else {
                        if (inputEmailNum.matches(emailNumPattern)) {        // focus out : 인증 번호 조건 충족
                            emailNum_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        } else {        // focus out : 인증 번호 조건 미충족
                            emailNum_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        }
                    }
                } else {        // focus in
                    if (inputEmailNum.isEmpty()) {      // focus in : 공백일 때
                        emailNum_editText.setBackgroundResource(R.drawable.textfield_activate);
                    } else {
                        if (inputEmailNum.matches(emailNumPattern)) {        // focus in : 인증 번호 조건 충족
                            emailNum_editText.setBackgroundResource(R.drawable.textfield_activate);
                        } else {        // focus in : 인증 번호 조건 미충족
                            emailNum_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        }
                    }
                }
            }
        });

        // Focus : pw_editText
        pw_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputPW.isEmpty()) {        // focus out : 공백일 때
                        pw_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        pw_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPW.matches(pwPattern)) {       // focus out : 비밀번호 입력 조건 부합
                            pw_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            pw_condition.setVisibility(View.GONE);
                        } else {        // focus out : 비밀번호 입력 조건 미부합
                            pw_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            pw_condition.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    if (inputPW.isEmpty()) {        // focus in : 공백일 때
                        pw_editText.setBackgroundResource(R.drawable.textfield_activate);
                        pw_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPW.matches(pwPattern)) {       // focus in : 비밀번호 입력 조건 부합
                            pw_editText.setBackgroundResource(R.drawable.textfield_activate);
                            pw_condition.setVisibility(View.GONE);
                        } else {        // focus in : 비밀번호 조건 미부합
                            pw_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            pw_condition.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // Focus : pw_check_editText
        pw_check_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputPwCheck.isEmpty()) {        // focus out : 공백일 때
                        pw_check_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        pw_conf_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPwCheck.equals(inputPW)) {        // focus out : 비밀번화 확인 조건 부합
                            pw_check_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            pw_conf_condition.setVisibility(View.GONE);
                        } else {        // focus out : 비밀번호 확인 입력 조건 미부합
                            pw_check_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            pw_conf_condition.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    if (inputPwCheck.isEmpty()) {
                        pw_check_editText.setBackgroundResource(R.drawable.textfield_activate);
                        pw_conf_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPwCheck.equals(inputPW)) {        // focus in : 비밀번호 입력 조건 부합
                            pw_check_editText.setBackgroundResource(R.drawable.textfield_activate);
                            pw_conf_condition.setVisibility(View.GONE);
                        } else {        // focus in : 비밀번호 조건 미부합
                            pw_check_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            pw_conf_condition.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // Focus : name_editText
        name_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputName.isEmpty()) {      // focus out : 공백일 때
                        name_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        name_condition.setVisibility(View.GONE);
                    } else {
                        if (inputName.matches(namePattern)) {       // focus out : 이름 입력 조건 부합
                            name_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            name_condition.setVisibility(View.GONE);
                        } else {        // focus out : 이름 입력 조건 미부합
                            name_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            name_condition.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    if (inputName.isEmpty()) {      // focus in : 공백일 때
                        name_editText.setBackgroundResource(R.drawable.textfield_activate);
                        name_condition.setVisibility(View.GONE);
                    } else {
                        if (inputName.matches(namePattern)) {       // focus in : 이름 조건 부합
                            name_editText.setBackgroundResource(R.drawable.textfield_activate);
                            name_condition.setVisibility(View.GONE);
                        } else {        // focus in : 이름 조건 미부합
                            name_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            name_condition.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // Focus : birth_editText
        birth_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {        // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputBirth.isEmpty()) {     // focus out : 공백일 때
                        birth_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        birth_condition.setVisibility(View.GONE);
                    } else {
                        if (inputBirth.matches(birthPattern)) {     // focus out : 생일 입력 조건 부합
                            birth_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            birth_condition.setVisibility(View.GONE);
                        } else {        // focus out : 생일 입력 조건 미부합
                            birth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            birth_condition.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    if (inputBirth.isEmpty()) {       // focus in : 공백일 때
                        birth_editText.setBackgroundResource(R.drawable.textfield_activate);
                        birth_condition.setVisibility(View.GONE);
                    } else {
                        if (inputBirth.matches(birthPattern)) {       // focus in : 생일 입력 조건 부합
                            birth_editText.setBackgroundResource(R.drawable.textfield_activate);
                            birth_condition.setVisibility(View.GONE);
                        } else {        // focus in : 생일 입력 조건 미부합
                            birth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            birth_condition.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // Focus : phone_editText
        phone_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {        // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            join_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (inputPhone.isEmpty()) {     // focus in : 공백일 때
                        phone_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        phone_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPhone.matches(phonePattern)) {        // focus out : 번호 입력 조건 충족
                            phone_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            phone_condition.setVisibility(View.GONE);
                        } else {        // focus out : 번호 입력 조건 미충족
                            phone_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            phone_condition.setVisibility(View.VISIBLE);
                        }
                    }
                } else {      // focus in
                    if (inputPhone.isEmpty()) {     // focus in : 공백일 때
                        phone_editText.setBackgroundResource(R.drawable.textfield_activate);
                        phone_condition.setVisibility(View.GONE);
                    } else {
                        if (inputPhone.matches(phonePattern)) {        // focus in : 번호 입력 조건 충족
                            phone_editText.setBackgroundResource(R.drawable.textfield_activate);
                            phone_condition.setVisibility(View.GONE);
                        } else {        // focus in : 번호 입력 조건 불충족
                            phone_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            phone_condition.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // editText Change
        // Change : email_editText
        email_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputEmail = charSequence.toString();
                emailTextChange(inputEmail);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : emailNum_editText
        emailNum_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputEmailNum = charSequence.toString();

                if (inputEmailNum.isEmpty()) {      // 공백일 시
                    emailNum_editText.setBackgroundResource(R.drawable.textfield_activate);
                    req[1] = false;
                } else {
                    if (inputEmailNum.matches(emailNumPattern)) {        // 인증 번호 조건 충족
                        emailNum_editText.setBackgroundResource(R.drawable.textfield_activate);
                        req[1] = true;
                    } else {        // 인증 번호 조건 불충족
                        emailNum_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        req[1] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : pw_editText
        pw_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPW = charSequence.toString();

                if (inputPW.isEmpty()) {        // 공백일 시
                    pw_editText.setBackgroundResource(R.drawable.textfield_activate);
                    pw_condition.setVisibility(View.GONE);
                    req[2] = false;
                } else {
                    if (inputPW.matches(pwPattern)) {       //  비밀번호 조건 충족
                        pw_editText.setBackgroundResource(R.drawable.textfield_activate);
                        pw_condition.setVisibility(View.GONE);
                        req[2] = true;
                        if (inputPW.matches(inputPwCheck) || inputPwCheck.isEmpty()) {
                            pw_check_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            pw_conf_condition.setVisibility(View.GONE);
                            req[3] = true;
                        } else {
                            pw_check_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            pw_conf_condition.setVisibility(View.VISIBLE);
                            req[3] = false;
                        }
                    } else {        // 비밀번호 조건 불충족
                        pw_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        pw_condition.setVisibility(View.VISIBLE);
                        req[2] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : pw_check_editText
        pw_check_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPwCheck = charSequence.toString();

                if (inputPwCheck.isEmpty()) {       // 공백일 시
                    pw_check_editText.setBackgroundResource(R.drawable.textfield_activate);
                    pw_conf_condition.setVisibility(View.GONE);
                    req[3] = false;
                } else {
                    if (inputPwCheck.equals(inputPW)) {        // 비밀번호 확인 조건 충족
                        pw_check_editText.setBackgroundResource(R.drawable.textfield_activate);
                        pw_conf_condition.setVisibility(View.GONE);
                        req[3] = true;
                    } else {        // 비밀번호 확인 조건 불충족
                        pw_check_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        pw_conf_condition.setVisibility(View.VISIBLE);
                        req[3] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : name_editText
        name_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputName = charSequence.toString();
                inputNameValue = inputName;      // add

                if (inputName.isEmpty()) {      // 공백일 시
                    name_editText.setBackgroundResource(R.drawable.textfield_activate);
                    name_condition.setVisibility(View.GONE);
                    req[4] = false;
                } else {
                    if (inputName.matches(namePattern)) {       // 이름 조건 충족
                        name_editText.setBackgroundResource(R.drawable.textfield_activate);
                        name_condition.setVisibility(View.GONE);
                        req[4] = true;
                    } else {        // 이름 조건 불충족
                        name_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        name_condition.setVisibility(View.VISIBLE);
                        req[4] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : birth_editText
        birth_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputBirth = charSequence.toString();

                if (inputBirth.isEmpty()) {     // 공백일 시
                    birth_editText.setBackgroundResource(R.drawable.textfield_activate);
                    birth_condition.setVisibility(View.GONE);
                    req[5] = false;
                } else {
                    if (inputBirth.matches(birthPattern)) {     // 생일 입력 조건 충족
                        birth_editText.setBackgroundResource(R.drawable.textfield_activate);
                        birth_condition.setVisibility(View.GONE);
                        req[5] = true;
                    } else {        // 생일 입력 조건 불충족
                        birth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        birth_condition.setVisibility(View.VISIBLE);
                        req[5] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Change : phone_editText
        phone_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPhone = charSequence.toString();

                if (inputPhone.isEmpty()) {     // 공백일 시
                    phone_editText.setBackgroundResource(R.drawable.textfield_activate);
                    phone_condition.setVisibility(View.GONE);
                    req[7] = false;
                } else {
                    if (inputPhone.matches(phonePattern)) {        // 번호 입력 조건 충족
                        phone_editText.setBackgroundResource(R.drawable.textfield_activate);
                        phone_condition.setVisibility(View.GONE);
                        req[7] = true;
                    } else {        // 번호 입력 조건 불충족
                        phone_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        phone_condition.setVisibility(View.VISIBLE);
                        req[7] = false;
                    }
                }
                updateJoinButton(req);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Layout
        // clear focus (When if you Auto Focusing at first editText, add focusableInTouchMode, focusable == true in topLayout
        constraintLayout.setOnTouchListener((view, motionEvent) -> {
            email_editText.clearFocus();
            emailNum_editText.clearFocus();
            pw_editText.clearFocus();
            pw_check_editText.clearFocus();
            name_editText.clearFocus();
            birth_editText.clearFocus();
            phone_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        // ScrollView
        // clear focus (When if you AUto Focusing at first editText, add focusableInTouchMode, focusable == true in topLayout
        nestedScrollView.setOnTouchListener((view, motionEvent) -> {
            email_editText.clearFocus();
            emailNum_editText.clearFocus();
            pw_editText.clearFocus();
            pw_check_editText.clearFocus();
            name_editText.clearFocus();
            birth_editText.clearFocus();
            phone_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });
    }   // onCreate

    // Update join_button (next_button)
    public void updateJoinButton(boolean[] req) {
        // include false
        for (int i = 0; i < req.length; i++) {
            if (!req[i]) {
                join_button.setBackgroundResource(R.drawable.login_main_loginbtn);
                join_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                join_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.widget.Toast toast = new android.widget.Toast(getApplicationContext());
                        if (!req[0]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_email_warning), Toast.LENGTH_SHORT);
                        } else if (!req[1]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_email_num_warning), Toast.LENGTH_SHORT);
                        } else if (!req[2]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.join_member_pw_warning), Toast.LENGTH_SHORT);
                        } else if (!req[3]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.join_member_pw_num_warning), Toast.LENGTH_SHORT);
                        } else if (!req[4]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_name_warning), Toast.LENGTH_SHORT);
                        } else if (!req[5]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_birth_warning), Toast.LENGTH_SHORT);
                        } else if (!req[6]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_gender_warning), Toast.LENGTH_SHORT);
                        } else if (!req[7]) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.find_pw_phone_warning), Toast.LENGTH_SHORT);
                        }
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                        toast.show();
                    }
                });
                return;
            }
        }
        // all True
        join_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        join_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        // change layout & activity join_member -> join_member_success
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_email_num = emailNum_editText.getText().toString();
                if (current_email_num.equals(email_num)) {
                    // 회원가입 시 기본 프로필 이미지로 설정
//                    Bitmap bitmap = bitmap = getBitmapFromVectorDrawable(join_member.this, R.drawable.default_profile);
//                    profileImg = converter.BitmapToString(bitmap);
                    String profileUrl = "http://10.0.2.2:8081/profile/profile.jpeg";
                    userJoin(new UserDto(email, inputPW, inputName, inputBirth, inputPhone, profileUrl)); // 회원가입
                } else {
                    showPopupDialog_def();
                }
            }
        });
    }

    // userJoin (회원가입)
    public void userJoin(UserDto user) {
        userApi.userJoin(user).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(join_member.this, join_member_success.class);
                    intent.putExtra("email", email);
                    intent.putExtra("name", inputNameValue);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // certifyMail (인증번호 메일 발송)
    public String certifyMail(String id) {
        userApi.certifyMail(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    email_num = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
        return email_num;
    }

    private void showPopupDialog_first(String email) {
        Dialog popupDialog = new Dialog(join_member.this);
        popupDialog.setContentView(R.layout.popup_dialog_email_certi);
        // Background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        TextView email_textView = popupDialog.findViewById(R.id.email_textView);
        Button confirm_button = popupDialog.findViewById(R.id.confirm_button);
        email_textView.setText(email);
        email_textView.setSelected(true);
        email_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        // Button : OnClickListener
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
                email_num_button.setText(getString(R.string.find_id_email_re_request));
            }
        });
        popupDialog.show();
    }

    private void showPopupDialog_def() {
        Dialog popupDialog = new Dialog(join_member.this);
        popupDialog.setContentView(R.layout.popup_dialog);
        // Background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        TextView content_textView = popupDialog.findViewById(R.id.popup_dialog_textView);
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_confirm_button);
        content_textView.setText(getString(R.string.popup_dialog_wrong));
        // Button : OnClickListener
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });
        popupDialog.show();
    }

    private void emailTextChange(String input) {
        if (input.isEmpty()) {     // 공백일 시
            email_editText.setBackgroundResource(R.drawable.textfield_activate);
            email_condition.setVisibility(View.GONE);
            req[0] = false;
        } else {
            if (email_list_button.getText().toString().equals(getString(R.string.find_id_email_list_0))) {      // 직접 입력
                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {     // 직접 입력 입력 조건 충족
                    email_editText.setBackgroundResource(R.drawable.textfield_activate);
                    email_condition.setVisibility(View.GONE);
                    req[0] = true;
                } else {        // 직접 입력 조건 미충족
                    email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                    email_condition.setVisibility(View.VISIBLE);
                    req[0] = false;
                }
            } else {        // 이메일 4종
                if (input.matches(idPattern)) {        // 이메일 4종 조건 충족
                    email_editText.setBackgroundResource(R.drawable.textfield_activate);
                    email_condition.setVisibility(View.GONE);
                    req[0] = true;
                } else {        // 이메일 4종 조건 미충족
                    email_editText.setBackgroundResource(R.drawable.textfield_invalid);
                    email_condition.setVisibility(View.VISIBLE);
                    req[0] = false;
                }
            }
        }
        updateJoinButton(req);
    }
//
//    // VectorDrawable -> Bitmap
//    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
//        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
//        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(drawable).getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//        return bitmap;
//    }
}   // join_member.java
