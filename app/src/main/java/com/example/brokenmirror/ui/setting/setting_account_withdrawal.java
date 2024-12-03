/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.06, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_withdrawal.java'
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
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_account_withdrawal extends AppCompatActivity {
    private String currentInput;
    private String notice_0;
    private Button visible_button;
    EditText current_editText;
    TextView condition_0;

    UserDto user_info;
    // UserApi
    UserApi userApi = RetrofitService.getUserApi();

    private boolean check_current_pw = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_withdrawal);

        // userSharedPref
        UserSharedPref user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        current_editText = findViewById(R.id.setting_account_withdrawal_editText);

        visible_button = findViewById(R.id.setting_account_withdrawal_visible_button);
        Button next_button = findViewById(R.id.setting_account_withdrawal_next_button);
        Button close_button = findViewById(R.id.setting_account_withdrawal_close_button);

        ConstraintLayout parent_layout = findViewById(R.id.setting_account_withdrawal_parent_layout);
        ConstraintLayout context_layout = findViewById(R.id.full);

        condition_0 = findViewById(R.id.setting_account_withdrawal_condition_textView);
        TextView find_pw_textView = findViewById(R.id.setting_account_withdrawal_find_pw_textView);

        currentInput = current_editText.getText().toString();
        notice_0 = getResources().getString(R.string.setting_account_pw_notice_0);

        // EditText
        // current_editText : OnFocusChangeListener
        current_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {       // focus out
                    visible_button.setVisibility(View.GONE);
                    if (currentInput.isEmpty()) {        // focus out : NO INPUT
                        current_editText.setBackgroundResource(R.drawable.login_main_edittext);
                        condition_0.setVisibility(View.GONE);
                    } else {
                        visible_button.setVisibility(View.VISIBLE);
                        if (check_current_pw) {      // focus out : satisfy PW conditions
                            current_editText.setBackgroundResource(R.drawable.login_main_edittext);
                            condition_0.setVisibility(View.GONE);
                        } else {        // focus out : unsatisfied PW conditions
                            current_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0.setVisibility(View.VISIBLE);
                        }
                    }
                } else {        // focus in
                    visible_button.setVisibility(View.VISIBLE);
                    if (currentInput.isEmpty()) {        // focus in : NO INPUT
                        current_editText.setBackgroundResource(R.drawable.textfield_activate);
                        condition_0.setVisibility(View.GONE);
                    } else {
                        if (check_current_pw) {      // focus in : satisfy PW conditions
                            current_editText.setBackgroundResource(R.drawable.textfield_activate);
                            condition_0.setVisibility(View.GONE);
                        } else {        // focus in : unsatisfied PW conditions
                            current_editText.setBackgroundResource(R.drawable.textfield_invalid);
                            condition_0.setVisibility(View.VISIBLE);
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
                currentInput = current_editText.getText().toString();
                currentPw(user_info.getId());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Button
        // visible_button : OnClickListener
        visible_button.setOnClickListener(new View.OnClickListener() {
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
                if (currentVisibility == View.VISIBLE) {
                    visible_button.setBackgroundResource(R.drawable.btn_visible);
                } else {
                    visible_button.setBackgroundResource(R.drawable.btn_invisible);
                }
                // Set focus to the end of the text
                current_editText.setSelection(current_editText.getText().length());
            }
        });

        // next_button : OnClickListener
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_current_pw) {
                    showPopupDialog();
                } else {
                    Toast toast;
                    toast = Toast.makeText(getApplicationContext(), notice_0, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                }
            }
        });

        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // LinearLayout
        // find_pw_textView : OnClickListener
        find_pw_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_account_withdrawal.this, login_find_pw.class);
                startActivity(intent);
                onPause();
            }
        });

        // LinearLayout
        // parent_layout : OnTouchListener
        parent_layout.setOnTouchListener((view, motionEvent) -> {
            current_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        context_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_editText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }       // onCreate

    // Display a PopupDialog
    private void showPopupDialog() {
        // create dialog object
        Dialog popupDialog = new Dialog(setting_account_withdrawal.this);
        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog);
        // setText
        TextView textView = popupDialog.findViewById(R.id.popup_dialog_textView);
        textView.setText(R.string.setting_account_withdrawal_popup);
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
                Intent intent = new Intent(setting_account_withdrawal.this, setting_account_withdrawal_notice.class);
                startActivity(intent);
                finish();
            }
        });
        popupDialog.show();     // start
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
}
