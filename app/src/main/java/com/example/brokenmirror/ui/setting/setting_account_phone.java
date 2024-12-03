/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.08, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_phone.java'
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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_account_phone extends AppCompatActivity {
    private String phoneNumber = "01024453467";
    private final String phonePattern = "\\d{11}";
    private final String authNumPattern = "\\d+";
    private String notice_0;
    private String notice_1;
    private String sendNumNewPhone;
    private String completeNewPhone;
    private String newPhoneInput;
    private String numInput;
    private String num = "12345";
    private String wrong_num;

    private EditText new_editText;
    private TextView condition_0_textView;
    private TextView condition_1_textView;

    UserDto user_info;

    private boolean check_current_phone_num = false;
    private UserSharedPref user_pref;

    // UserApi
    UserApi userApi = RetrofitService.getUserApi();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_phone);

        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        new_editText = findViewById(R.id.setting_account_phone_new_textView);
        EditText auth_editText = findViewById(R.id.setting_account_phone_auth_num_editText);

        condition_0_textView = findViewById(R.id.setting_account_phone_condition_0_textView);
        condition_1_textView = findViewById(R.id.setting_account_phone_condition_1_textView);

        ConstraintLayout parent_layout = findViewById(R.id.setting_account_phone_parent_layout);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);

        Button change_phone_button = findViewById(R.id.setting_account_phone_change_button);
        Button next_change_button = findViewById(R.id.setting_account_phone_next_change_button);
        Button back_button = findViewById(R.id.setting_account_phone_back_button);
        Button close_button = findViewById(R.id.close_button);
        Button send_button = findViewById(R.id.certified_button);

        notice_0 = getResources().getString(R.string.setting_account_phone_warning_0);
        notice_1 = getResources().getString(R.string.setting_account_phone_warning_1);
        sendNumNewPhone = getString(R.string.popup_send_to_new_phone);
        completeNewPhone = getString(R.string.setting_account_phone_popup);
        newPhoneInput = new_editText.getText().toString();
        numInput = auth_editText.getText().toString();
        wrong_num = getString(R.string.popup_dialog_wrong);

        condition_0_textView.setSelected(true);
        condition_0_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        condition_1_textView.setSelected(true);
        condition_1_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        // number_textview : change userNumber Format (010-0000-0000) & setText
        if (phoneNumber.length() == 11) {
            String formattedNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
            new_editText.setHint(formattedNumber);
        }

        // TextView
        // new_editText : OnFocusChangeListener
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
                    if (newPhoneInput.isEmpty()) { // focus out : NO INPUT
                        new_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        condition_0_textView.setVisibility(View.GONE);
                    } else {
                        if (newPhoneInput.matches(phonePattern)) {     // focus out : satisfy phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            condition_0_textView.setVisibility(View.GONE);
                            if (check_current_phone_num) {       // same as current_phoneNum
                                new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                condition_0_textView.setText(R.string.setting_account_phone_same);
                                condition_0_textView.setVisibility(View.VISIBLE);
                            }
                        } else {        // focus out : unsatisfied phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0_textView.setText(R.string.find_id_phone_condition);
                            condition_0_textView.setVisibility(View.VISIBLE);
                        }
                    }
                } else { // focus in
                    if (newPhoneInput.isEmpty()) {     // focus in : NO INPUT
                        new_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_0_textView.setVisibility(View.GONE);
                    } else {
                        if (newPhoneInput.matches(phonePattern)) {     // focus in : satisfy phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0_textView.setVisibility(View.GONE);
                            if (check_current_phone_num) {       // same as current phoneNum
                                new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                                condition_0_textView.setText(R.string.setting_account_phone_same);
                                condition_0_textView.setVisibility(View.VISIBLE);
                            }
                        } else {        // focus in : unsatisfied phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0_textView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        // auth_editText : OnFocusChangeListener
        auth_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            button_layout.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
                    if (numInput.isEmpty()) {
                        auth_editText.setBackgroundResource(R.drawable.login_main_edittext);
                    } else {
                        if (numInput.matches(authNumPattern)) {
                            auth_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        } else {
                            auth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        }
                    }
                } else {        // focus in
                    if (numInput.isEmpty()) {
                        auth_editText.setBackgroundResource(R.drawable.textfield_activate);
                    } else {
                        if (numInput.matches(authNumPattern)) {
                            auth_editText.setBackgroundResource(R.drawable.textfield_activate);
                        } else {
                            auth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        }
                    }
                }
            }
        });

        // new_editText : TextChangedListener
        new_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newPhoneInput = charSequence.toString();
                if (newPhoneInput.isEmpty()) {     // NO INPUT
                    new_editText.setBackgroundResource(R.drawable.textfield_activate);
                    condition_0_textView.setVisibility(View.GONE);
                } else {
                    currentPhoneNum(user_info.getId());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // auth_editText : TextChangedListener
        auth_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numInput = charSequence.toString();

                if (numInput.isEmpty()) {        // NO INPUT
                    auth_editText.setBackgroundResource(R.drawable.textfield_activate);
                } else {
                    if (numInput.matches(authNumPattern)) {      // satisfy authNum conditions
                        auth_editText.setBackgroundResource(R.drawable.textfield_activate);
                    } else {        // unsatisfied authNum conditions
                        auth_editText.setBackgroundResource(R.drawable.textfield_invalid);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // LinearLayout
        // main_layout : OnTouchListener (clear editText focus)
        parent_layout.setOnTouchListener((view, motionEvent) -> {
            new_editText.clearFocus();
            auth_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        // NestedScrollView
        // nestedScrollView : OnTouchListener (clear Focus)
        nestedScrollView.setOnTouchListener((view, motionEvent) -> {
            new_editText.clearFocus();
            auth_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        // Button
        // change_phone_button
        change_phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPhoneInput.isEmpty()) {
                    showToast(notice_0);
                    return;
                }
                if (condition_0_textView.getVisibility() == View.VISIBLE) {
                    showToast(notice_0);
                    return;
                }
                if (numInput.isEmpty()) {
                    showToast(notice_1);
                    return;
                }
                if (condition_1_textView.getVisibility() == View.VISIBLE) {
                    showToast(notice_1);
                    return;
                }
                if (numInput.matches(num)) {
                    newPhoneNum(user_info.getId(), newPhoneInput);
                }
            }
        });

        // back_button : OnClickListener
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

        // send_button : OnClickListener
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPhoneInput.isEmpty()) {
                    showToast(notice_0);
                    return;
                }
                if (condition_0_textView.getVisibility() == View.VISIBLE) {
                    showToast(notice_0);
                    return;
                }
                showPopupDialog(sendNumNewPhone, false);
                send_button.setText(R.string.find_id_email_re_request);
            }
        });
    }       // onCreate

    // Display a PopupDialog
    private void showPopupDialog(String message, Boolean close) {
        // create dialog objcect
        Dialog popupDialog = new Dialog(setting_account_phone.this);
        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog);
        // setText
        TextView textView = popupDialog.findViewById(R.id.popup_dialog_textView);
        textView.setText(message);
        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Button : confirm_button (onClickListener)
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (close.equals(true)) {
                    popupDialog.dismiss();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    popupDialog.dismiss();
                }
            }
        });
        // start
        popupDialog.show();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    // currentPhoneNum (현재 전화번호 확인)
    public void currentPhoneNum(String id) {
        userApi.currentPhoneNum(id).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (newPhoneInput.equals(response.body().getPhoneNum())) {
                        check_current_phone_num = true;
                        new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                        condition_0_textView.setText(R.string.setting_account_phone_same);
                        condition_0_textView.setVisibility(View.VISIBLE);
                    } else {
                        check_current_phone_num = false;
                        if (newPhoneInput.matches(phonePattern)) {     // satisfy phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0_textView.setVisibility(View.GONE);
                        } else {        // unsatisfied phoneNum conditions
                            new_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0_textView.setText(R.string.find_id_phone_condition);
                            condition_0_textView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // newPhoenNume (전화번호 변경)
    public void newPhoneNum(String id, String phoneNum) {
        userApi.newPhoneNum(id, phoneNum).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user_info.setPhoneNum(newPhoneInput);
                    user_pref.putUser(user_info);
                    showPopupDialog(completeNewPhone, true);
                } else {
                    showPopupDialog(wrong_num, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

}       // setting_account_phone.java
