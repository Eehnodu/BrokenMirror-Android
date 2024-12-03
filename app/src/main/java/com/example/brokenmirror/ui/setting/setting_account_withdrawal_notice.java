/*
__author__ = 'Song Chae Young'
__date__ = 'Dec.04, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_withdrawal_notice.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.AutoSharedPref;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_account_withdrawal_notice extends AppCompatActivity {

    private final boolean[] clickStatusArray = new boolean[5];
    private final View[] checkViews = new View[5];
    private Button next_button;
    private Toast toast;
    private UserDto user_info;
    private UserSharedPref user_pref;
    private AutoSharedPref auto_pref;

    // UserApi
    UserApi userApi = RetrofitService.getUserApi();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_withdrawal_notice);
        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();
        // AutoLogin
        auto_pref = new AutoSharedPref(this);
        LinearLayout list_0_layout = findViewById(R.id.setting_account_withdrawal_notice_layout_0);
        LinearLayout list_1_layout = findViewById(R.id.setting_account_withdrawal_notice_layout_1);
        LinearLayout list_2_layout = findViewById(R.id.setting_account_withdrawal_notice_layout_2);
        LinearLayout list_3_layout = findViewById(R.id.setting_account_withdrawal_notice_layout_3);
        LinearLayout list_4_layout = findViewById(R.id.setting_account_withdrawal_notice_layout_4);

        next_button = findViewById(R.id.setting_account_withdrawal_notice_next_button);
        Button close_button = findViewById(R.id.setting_account_withdrawal_notice_close_button);

        checkViews[0] = findViewById(R.id.setting_account_withdrawal_notice_check_0);
        checkViews[1] = findViewById(R.id.setting_account_withdrawal_notice_check_1);
        checkViews[2] = findViewById(R.id.setting_account_withdrawal_notice_check_2);
        checkViews[3] = findViewById(R.id.setting_account_withdrawal_notice_check_3);
        checkViews[4] = findViewById(R.id.setting_account_withdrawal_notice_check_4);

        String notice_textView = getString(R.string.setting_account_withdrawal_notice_notice);
        toast = Toast.makeText(getApplicationContext(), notice_textView, Toast.LENGTH_SHORT);

        // LinearLayout
        // list_0_layout : OnClickListener
        list_0_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleListClick(0);
            }
        });

        // list_1_layout : OnClickListener
        list_1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleListClick(1);
            }
        });

        // list_2_layout : OnClickListener
        list_2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleListClick(2);
            }
        });

        // list_3_layout : OnClickListener
        list_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleListClick(3);
            }
        });

        // list_4_layout : OnClickListener
        list_4_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleListClick(4);
            }
        });

        // Button
        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }       // onCreate

    // toggle check status and update background for clicked item
    private void handleListClick(int index) {
        clickStatusArray[index] = !clickStatusArray[index];

        if (clickStatusArray[index]) {
            // Clicked
            checkViews[index].setBackgroundResource(R.drawable.checkbox_activate);
        } else {
            // UnClicked
            checkViews[index].setBackgroundResource(R.drawable.checkbox_empty);
        }

        // Check if all checkViews are clicked
        boolean allClicked = true;
        for (boolean status : clickStatusArray) {
            if (!status) {
                allClicked = false;
                break;
            }
        }

        // Add or remove OnClickListener for nextButton based on the status
        if (allClicked) {
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupDialog();
                }
            });
        } else {
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                }
            });
        }
    }

    // Display a PopupDialog
    private void showPopupDialog() {
        // create dialog object
        Dialog popupDialog = new Dialog(setting_account_withdrawal_notice.this);
        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_reconfirm);
        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Button
        // cancel_button : OnClickListener
        Button cancel_button = popupDialog.findViewById(R.id.popup_dialog_reconfirm_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
                finish();
            }
        });

        // confirm_button : onClickListener
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_reconfirm_confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
                userDelete(user_info.getId());
                Intent intent = new Intent(setting_account_withdrawal_notice.this, good_bye.class);
                startActivity(intent);
                finish();
            }
        });
        popupDialog.show();
    }

    // userDelete (회원탈퇴)
    public void userDelete(String id) {
        userApi.userDelete(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    user_pref.removeUser();
                    auto_pref.removeAuto();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }
}
