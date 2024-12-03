/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.07, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_pw.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
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
import androidx.core.widget.NestedScrollView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_account_pw extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(setting_account_pw.class);
    private final String pwPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private String notice_0;
    private String notice_1;
    private String notice_2;
    private String notice_3;
    private String currentInput;
    private String newInput;
    private String newConfirmInput;
    private EditText current_editText;
    private EditText new_editText;
    private EditText new_confirm_editText;
    private TextView condition_0;
    private TextView condition_1;
    private TextView condition_2;
    private Button current_visible_button;
    private Button new_visible_button;
    private Button new_conf_visible_button;

    private UserDto user_info;
    private boolean check_current_pw = false;

    // UserApi
    UserApi userApi = RetrofitService.getUserApi();

    UserSharedPref user_pref;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_pw);

        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        Button back_button = findViewById(R.id.setting_account_pw_back_button);
        Button close_button = findViewById(R.id.close_button);
        Button next_change_button = findViewById(R.id.setting_account_pw_next_change_button);
        Button change_pw_button = findViewById(R.id.setting_account_pw_change_button);
        current_visible_button = findViewById(R.id.setting_account_pw_current_button);
        new_visible_button = findViewById(R.id.setting_account_pw_new_button);
        new_conf_visible_button = findViewById(R.id.setting_account_new_pw_confirm_button);

        current_editText = findViewById(R.id.setting_account_pw_current_editText);
        new_editText = findViewById(R.id.setting_account_pw_new_editText);
        new_confirm_editText = findViewById(R.id.setting_account_pw_current_conf_editText);

        TextView find_pw_textView = findViewById(R.id.setting_account_pw_find_pw_textView);
        condition_0 = findViewById(R.id.setting_account_pw_condition_0);
        condition_1 = findViewById(R.id.setting_account_pw_condition_1);
        condition_2 = findViewById(R.id.setting_account_pw_condition_2);

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        ConstraintLayout main_layout = findViewById(R.id.setting_account_main_layout);

        notice_0 = getResources().getString(R.string.setting_account_pw_notice_0);
        notice_1 = getResources().getString(R.string.setting_account_pw_notice_1);
        notice_2 = getResources().getString(R.string.setting_account_pw_notice_2);
        notice_3 = getResources().getString(R.string.setting_account_pw_same);

        currentInput = current_editText.getText().toString();
        newInput = new_editText.getText().toString();
        newConfirmInput = new_confirm_editText.getText().toString();

        condition_0.setSelected(true);
        condition_0.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        condition_1.setSelected(true);
        condition_1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        condition_2.setSelected(true);
        condition_2.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        // TextView
        // find_pw_textView
        find_pw_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_account_pw.this, login_find_pw.class);
                startActivity(intent);
                onPause();
            }
        });

        // Button
        // back_button = OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //close_button
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // next_change_button : OnClickListener
        next_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // current_visible_button : OnClickListener (delete pw mask)
        current_visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_editText.getText().toString().isEmpty()) {
                    return;
                }
                int currentVisibility = current_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance() ? View.GONE : View.VISIBLE;
                // toggle pw visibility
                current_editText.setTransformationMethod(
                        current_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()
                                ? HideReturnsTransformationMethod.getInstance()
                                : PasswordTransformationMethod.getInstance()
                );
                // Change Button Background Resource
                current_visible_button.setBackgroundResource(
                        currentVisibility == View.VISIBLE ? R.drawable.btn_visible : R.drawable.btn_invisible
                );
                // Set focus to the end of the text
                current_editText.setSelection(current_editText.getText().length());
            }
        });

        // new_visible_button : OnClickListener (delete pw mask)
        new_visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_editText.getText().toString().isEmpty()) {
                    return;
                }
                int currentVisibility = new_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance() ? View.GONE : View.VISIBLE;
                // toggle pw visibility
                new_editText.setTransformationMethod(
                        new_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()
                                ? HideReturnsTransformationMethod.getInstance()
                                : PasswordTransformationMethod.getInstance()
                );
                // Change Button Background Resource
                new_visible_button.setBackgroundResource(
                        currentVisibility == View.VISIBLE ? R.drawable.btn_visible : R.drawable.btn_invisible
                );
                // Set focus to the end of the text
                new_editText.setSelection(new_editText.getText().length());
            }
        });

        // new_conf_visible_button : OnClickListener (delete pw mask)
        new_conf_visible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_confirm_editText.getText().toString().isEmpty()) {
                    return;
                }
                int currentVisibility = new_confirm_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance() ? View.GONE : View.VISIBLE;
                // toggle pw visibility
                new_confirm_editText.setTransformationMethod(
                        new_confirm_editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()
                                ? HideReturnsTransformationMethod.getInstance()
                                : PasswordTransformationMethod.getInstance()
                );
                // Change Button Background Resource
                new_conf_visible_button.setBackgroundResource(
                        currentVisibility == View.VISIBLE ? R.drawable.btn_visible : R.drawable.btn_invisible
                );
                // Set focus to the end of the text
                new_confirm_editText.setSelection(new_confirm_editText.getText().length());
            }
        });

        // change_pw_button
        change_pw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_editText.getText().toString().isEmpty() || condition_0.getVisibility() == View.VISIBLE) {
                    showToast(notice_0);
                    return;
                }
                if (new_editText.getText().toString().isEmpty() || condition_1.getVisibility() == View.VISIBLE) {
                    showToast(notice_1);
                    return;
                }
                if (new_confirm_editText.getText().toString().isEmpty() || condition_2.getVisibility() == View.VISIBLE) {
                    showToast(notice_2);
                    return;
                } else {
                    newPw(user_info.getId(), newConfirmInput);
                }
            }
        });

        // EditText
        // current_editText : FocusChangeListener
        current_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            button_layout.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    current_visible_button.setVisibility(View.GONE);
                    if (currentInput.isEmpty()) {      // focus out : NO INPUT
                        current_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        condition_0.setVisibility(View.GONE);
                    } else {
                        current_visible_button.setVisibility(View.VISIBLE);
                        if (check_current_pw) {      // 비밀번호가 일치할 때
                            current_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0.setVisibility(View.GONE);
                        } else {        // 비밀번호가 불일치할 때
                            current_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    current_visible_button.setVisibility(View.GONE);
                    if (currentInput.isEmpty()) {      // focus in : NO INPUT
                        current_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_0.setVisibility(View.GONE);
                    } else {
                        current_visible_button.setVisibility(View.VISIBLE);
                        if (check_current_pw) {
                            // focus in : 비밀번호 조건 충족
                            current_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0.setVisibility(View.GONE);
                        } else {        // focus in : 비밀번호 조건 불충족
                            current_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // new_editText : FocusChangeListener
        new_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            button_layout.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    // Default
                    new_visible_button.setVisibility(View.GONE);
                    if (newInput.isEmpty()) {       // focus in : Empty
                        new_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        condition_1.setVisibility(View.GONE);
                    } else {
                        new_visible_button.setVisibility(View.VISIBLE);
                        if (newInput.matches(pwPattern)) {      // focus out : matched pwPattern
                            if (newInput.matches(currentInput)) {        // focus out : matched currentInput
                                new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                condition_1.setVisibility(View.VISIBLE);
                                condition_1.setText(notice_3);
                            } else {        // focus out : not matched currentInput     =>TRUE
                                new_editText.setBackgroundResource(R.drawable.login_main_edittext);
                                condition_1.setVisibility(View.GONE);
                            }
                        } else {        // focus out : not matched pwPattern
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_1.setVisibility(View.VISIBLE);
                            condition_1.setText(R.string.join_member_pw_input);
                        }
                    }
                } else {        // focus in
                    // Default
                    new_visible_button.setVisibility(View.GONE);
                    if (newInput.isEmpty()) {       // focus in : Empty
                        new_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_1.setVisibility(View.GONE);
                    } else {
                        new_visible_button.setVisibility(View.VISIBLE);
                        if (newInput.matches(pwPattern)) {
                            if (newInput.matches(currentInput)) {       // focus in : matched currentInput
                                new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                condition_1.setVisibility(View.VISIBLE);
                                condition_1.setText(notice_3);
                            } else {        // focus in : not matched currentInput      =>TRUE
                                new_editText.setBackgroundResource(R.drawable.textfield_activate);
                                condition_1.setVisibility(View.GONE);
                            }
                        } else {        // focus in : not matched paPattern
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_1.setVisibility(View.VISIBLE);
                            condition_1.setText(R.string.join_member_pw_input);
                        }
                    }
                }
            }
        });

        // new_confirm_editText : FocusChangeListener
        new_confirm_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {  // focus out
                    new_conf_visible_button.setVisibility(View.GONE);
                    if (newConfirmInput.isEmpty()) {
                        new_confirm_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        condition_2.setVisibility(View.GONE);
                    } else {
                        new_conf_visible_button.setVisibility(View.VISIBLE);
                        if (newConfirmInput.equals(newInput)) { // newInput과 일치하는지 확인
                            if (newInput.equals(currentInput)) { // currentInput과 일치하는지 확인
                                new_confirm_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                condition_2.setVisibility(View.VISIBLE);
                                condition_2.setText(notice_3);
                            } else {
                                new_confirm_editText.setBackgroundResource(R.drawable.textfield_activate);
                                condition_2.setVisibility(View.GONE);
                            }
                        } else {
                            new_confirm_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_2.setVisibility(View.VISIBLE);
                            condition_2.setText(notice_2);
                        }
                    }
                } else {  // focus in
                    new_conf_visible_button.setVisibility(View.GONE);
                    if (newConfirmInput.isEmpty()) {
                        new_confirm_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_2.setVisibility(View.GONE);
                    } else {
                        new_conf_visible_button.setVisibility(View.VISIBLE);
                        if (newConfirmInput.equals(newInput)) {
                            new_confirm_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_2.setVisibility(View.GONE);
                        } else {
                            new_confirm_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_2.setVisibility(View.VISIBLE);
                            condition_2.setText(notice_2);
                        }
                    }
                }
            }
        });

        // current_editText : TextChangeListener
        current_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentInput = charSequence.toString();
                currentPw(user_info.getId());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // new_editText : TextChangeListener
        new_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newInput = charSequence.toString();
                textChangeConditions(new_editText, condition_1, newInput, new_visible_button);
                if (!currentInput.isEmpty()) {
                    if (newInput.matches(pwPattern)) {
                        if (newInput.equals(currentInput)) {
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_1.setVisibility(View.VISIBLE);
                            condition_1.setText(notice_3);
                        } else {
                            new_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_1.setVisibility(View.GONE);
                        }
                    } else {
                        new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        condition_1.setVisibility(View.VISIBLE);
                        condition_1.setText(R.string.join_member_pw_input);
                    }
                }
                NewConfPWCheckCurrentPWMatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // new_confirm_editText : TextChangeListener
        new_confirm_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newConfirmInput = charSequence.toString();
                textChangeConditions(new_confirm_editText, condition_2, newConfirmInput, new_conf_visible_button);
                if (!newConfirmInput.isEmpty()) {
                    if (newConfirmInput.equals(newInput)) {
                        new_confirm_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_2.setVisibility(View.GONE);
                    } else {
                        new_confirm_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        condition_2.setVisibility(View.VISIBLE);
                        condition_2.setText(notice_2);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // LinearLayout
        // main_layout : OnTouchListener (clear editText focus)
        main_layout.setOnTouchListener((view, motionEvent) -> {
            current_editText.clearFocus();
            new_editText.clearFocus();
            new_confirm_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        // NestedScrollView
        // nestedScrollView : OnTouchListener
        nestedScrollView.setOnTouchListener((view, motionEvent) -> {
            current_editText.clearFocus();
            new_editText.clearFocus();
            new_confirm_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });
    }       // onCrate

    // Display a PopupDialog
    private void showPopupDialog() {
        // create dialog object
        Dialog popupDialog = new Dialog(setting_account_pw.this);
        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog);
        // setText
        TextView textView = popupDialog.findViewById(R.id.popup_dialog_textView);
        textView.setText(R.string.setting_account_pw_popup);
        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Button : confirm_button (OnClickListener)
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();      // close dialog
                finish();
            }
        });
        popupDialog.show();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    private void NewConfPWCheckCurrentPWMatch() {
        if (newConfirmInput.isEmpty()) {        // new_confirm_editText is empty
            new_confirm_editText.setBackgroundResource(R.drawable.login_main_edittext);
            condition_2.setVisibility(View.GONE);
        } else {
            if (newConfirmInput.equals(newInput)) {     // same as newPW
                new_confirm_editText.setBackgroundResource(R.drawable.login_main_edittext);
                condition_2.setVisibility(View.GONE);
            } else {        // not same as newPW
                new_confirm_editText.setBackgroundResource(R.drawable.textfield_invalid);
                condition_2.setText(R.string.join_member_pw_incorrect);
                condition_2.setVisibility(View.VISIBLE);
            }
        }
    }

    // TextChange Input Conditions
    private void textChangeConditions(EditText editText, TextView condition, String input, Button button) {
        button.setVisibility(View.VISIBLE);
        if (input.isEmpty()) {   // NO INPUT
            editText.setBackgroundResource(R.drawable.textfield_activate);
            condition.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        } else {
            if (input.matches(pwPattern)) {     // satisfy pw conditions
                editText.setBackgroundResource(R.drawable.textfield_activate);
                condition.setVisibility(View.GONE);
            } else {    // unsatisfied pw conditions
                editText.setBackgroundResource(R.drawable.textfield_invalid);
                condition.setVisibility(View.VISIBLE);
            }
        }
    }

    // currentPw (현재 비밀번호 확인)
    public void currentPw(String id) {
        userApi.currentPw(id).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!currentInput.isEmpty()) {
                        if (currentInput.equals(response.body().getPw())) {
                            check_current_pw = true;
                            current_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0.setVisibility(View.GONE);
                        } else {
                            check_current_pw = false;
                            current_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0.setVisibility(View.VISIBLE);
                            condition_0.setText(notice_0);
                        }
                    } else {
                        current_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_0.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // newPw (비밀번호 변경)
    public void newPw(String id, String pw) {
        userApi.newPw(id, pw).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user_info.setPw(newConfirmInput);
                    user_pref.putUser(user_info);
                    // Show Popup Dialog
                    showPopupDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }
}       // setting_account_pw.class