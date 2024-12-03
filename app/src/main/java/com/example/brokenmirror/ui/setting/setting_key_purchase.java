/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.27, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_key_purchase.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.R;

import java.util.Arrays;
import java.util.List;

public class setting_key_purchase extends AppCompatActivity {

    TextView layout_0_textView;
    TextView layout_1_textView;
    TextView layout_2_textView;
    TextView layout_3_textView;
    TextView layout_4_textView;
    TextView layout_5_textView;
    TextView popup_textView;
    TextView popup_NumberOfKey;

    View view_0;
    View view_1;
    View view_2;
    View view_3;
    View view_4;
    View view_5;
    ConstraintLayout layout_view_0;
    ConstraintLayout layout_view_1;
    ConstraintLayout layout_view_2;
    ConstraintLayout layout_view_3;
    ConstraintLayout layout_view_4;
    ConstraintLayout layout_view_5;
    Button purchase_button;
    Toast toast;
    int selectedIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_key_purchase);

        String error_message = getString(R.string.setting_key_purchase_block);

        toast = Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT);

        Button back_button = findViewById(R.id.setting_key_purchase_back_button);
        Button close_button = findViewById(R.id.setting_key_purchase_close_button);
        purchase_button = findViewById(R.id.purchase_button);

        view_0 = findViewById(R.id.view_0);
        view_1 = findViewById(R.id.view_1);
        view_2 = findViewById(R.id.view_2);
        view_3 = findViewById(R.id.view_3);
        view_4 = findViewById(R.id.view_4);
        view_5 = findViewById(R.id.view_5);

        layout_view_0 = findViewById(R.id.layout_0);
        layout_view_1 = findViewById(R.id.layout_1);
        layout_view_2 = findViewById(R.id.layout_2);
        layout_view_3 = findViewById(R.id.layout_3);
        layout_view_4 = findViewById(R.id.layout_4);
        layout_view_5 = findViewById(R.id.layout_5);

        layout_0_textView = findViewById(R.id.setting_key_purchase_num_layout_0);
        layout_1_textView = findViewById(R.id.setting_key_purchase_num_layout_1);
        layout_2_textView = findViewById(R.id.setting_key_purchase_num_layout_2);
        layout_3_textView = findViewById(R.id.setting_key_purchase_num_layout_3);
        layout_4_textView = findViewById(R.id.setting_key_purchase_num_layout_4);
        layout_5_textView = findViewById(R.id.setting_key_purchase_num_layout_5);


        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        // cancel_button : OnClickListener
        purchase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        });

        view_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout0_text = layout_0_textView.getText().toString();
//                showPopupDialog(layout0_text);
                setViewsSelectedState(layout_view_0);
            }
        });

        view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout1_text = layout_1_textView.getText().toString();
//                showPopupDialog(layout1_text);
                setViewsSelectedState(layout_view_1);
            }
        });

        view_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout2_text = layout_2_textView.getText().toString();
//                showPopupDialog(layout2_text);
                setViewsSelectedState(layout_view_2);
            }
        });

        view_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout3_text = layout_3_textView.getText().toString();
//                showPopupDialog(layout3_text);
                setViewsSelectedState(layout_view_3);
            }
        });

        view_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout4_text = layout_4_textView.getText().toString();
//                showPopupDialog(layout4_text);
                setViewsSelectedState(layout_view_4);
            }
        });

        view_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String layout5_text = layout_5_textView.getText().toString();
//                showPopupDialog(layout5_text);
                setViewsSelectedState(layout_view_5);
            }
        });
    }  // onCreate


    // Method(1) : Display a PopupDialog - First Dialog
    private void showPopupDialog(String selectedIndex) {
        // create dialog object
        Dialog popupDialog = new Dialog(setting_key_purchase.this);
        String selectedText ="";

        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_key_purchase_confirm);

        // set Text
        popup_textView = popupDialog.findViewById(R.id.popup_dialog_key_purchase_confirm_textView);

        switch (selectedIndex) {
            case "0":
                selectedText = layout_0_textView.getText().toString();
                break;
            case "1":
                selectedText = layout_1_textView.getText().toString();
                break;
            case "2":
                selectedText = layout_2_textView.getText().toString();
                break;
            case "3":
                selectedText = layout_3_textView.getText().toString();
                break;
            case "4":
                selectedText = layout_4_textView.getText().toString();
                break;
            case "5":
                selectedText = layout_5_textView.getText().toString();
                break;
        }

        popup_textView.setText(selectedText);

        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button done_button = popupDialog.findViewById(R.id.popup_dialog_key_purchase_doneBtn);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
                showSecondDialog(selectedIndex);
            }
        });

        Button cancel_button = popupDialog.findViewById(R.id.popup_dialog_key_purchase_cancelBtn);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }


    // Method(2) : Second Dialog
    private void showSecondDialog(String selectedIndex) {

        Dialog secondDialog = new Dialog(setting_key_purchase.this);

        secondDialog.setContentView(R.layout.popup_dialog_key_purchase);

        Window secondDialogWindow = secondDialog.getWindow();
        if (secondDialogWindow != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(secondDialogWindow.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            secondDialogWindow.setAttributes(layoutParams);
            secondDialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        popup_NumberOfKey = secondDialog.findViewById(R.id.popup_dialog_key_purchase_textView);

        String selectedItem = "";
        switch (selectedIndex) {
            case "0":
                selectedItem = layout_0_textView.getText().toString();
                break;
            case "1":
                selectedItem = layout_1_textView.getText().toString();
                break;
            case "2":
                selectedItem = layout_2_textView.getText().toString();
                break;
            case "3":
                selectedItem = layout_3_textView.getText().toString();
                break;
            case "4":
                selectedItem = layout_4_textView.getText().toString();
                break;
            case "5":
                selectedItem = layout_5_textView.getText().toString();
                break;
        }

        popup_NumberOfKey.setText(selectedItem);

        Button secondDialogButton = secondDialog.findViewById(R.id.popup_dialog_key_purchase_button);
        secondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondDialog.dismiss();
                finish();
            }
        });

        secondDialog.show();
    }


    // Method(3)
    private void setViewsSelectedState(View clickedView) {
        // 현재 선택 상태 확인
        boolean isSelected = clickedView.isSelected();
        List<View> views = Arrays.asList(layout_view_0, layout_view_1, layout_view_2, layout_view_3, layout_view_4, layout_view_5);
        boolean isAnyViewSelected = false; // true가 하나라도 있는지 여부

        for (int i = 0; i < views.size(); i++) {
            if (views.get(i) == clickedView) {
                selectedIndex = i;
                break;
            }
        }

        for (View view : views) {
            if (view == clickedView) {
                // 클릭된 뷰가 이미 선택되어 있는 경우 선택 해제
                if (isSelected) {
                    view.setSelected(false);
                    view.setBackgroundResource(R.drawable.bg_key_purchase);
                } else {
                    // 선택되지 않은 경우 선택 상태로 설정
                    view.setSelected(true);
                    view.setBackgroundResource(R.drawable.bg_selected);
                }
            } else {
                // 다른 뷰들은 선택 해제
                view.setSelected(false);
                view.setBackgroundResource(R.drawable.bg_key_purchase);
            }

            // 선택된 뷰가 있는지 확인
            if (view.isSelected()) {
                isAnyViewSelected = true;
            }
        }

        if (isAnyViewSelected) {
            purchase_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            purchase_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
            purchase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupDialog(String.valueOf(selectedIndex));
                }
            });
        } else {
            purchase_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
            purchase_button.setBackgroundResource(R.drawable.login_main_loginbtn);
            purchase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                }
            });
        }

    }

}