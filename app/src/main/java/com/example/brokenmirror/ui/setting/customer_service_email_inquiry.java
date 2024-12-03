/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.20, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'customer_service_email_inquiry.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.brokenmirror.R;

public class customer_service_email_inquiry extends AppCompatActivity {

    private String title_input = "";
    private String context_input = "";
    private String index;
    private String no_app;
    private String select_email;
    private String email_contact;
    private String id;
    private String name;
    private String context;
    private final String user_id = "user_id";
    private EditText title_editText;
    private EditText context_editText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_email_inquiry);

        Button close_button = findViewById(R.id.close_button);
        Button back_button = findViewById(R.id.back_button);
        Button inquiry_button = findViewById(R.id.inquiry_button);

        title_editText = findViewById(R.id.title_editText);
        context_editText = findViewById(R.id.context_editText);

        ConstraintLayout header_layout = findViewById(R.id.header);
        ConstraintLayout context_layout = findViewById(R.id.context_layout);

        index = getResources().getString(R.string.customer_service_email_inquiry_index);
        no_app = getResources().getString(R.string.customer_service_email_inquiry_no_app);
        select_email = getResources().getString(R.string.customer_service_email_inquiry_select_email);
        email_contact = getResources().getString(R.string.customer_service_email_inquiry_contact);
        id = getResources().getString(R.string.customer_service_email_inquiry_id);
        name = getResources().getString(R.string.customer_service_email_inquiry_name);
        context = getResources().getString(R.string.customer_service_email_inquiry_hint_2);

        // Button
        // close_button : finish
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // back_button : finish
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // inquiry_button : sendEmail / showAlertPopupDialog
        inquiry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title_input.isEmpty() && !context_input.isEmpty()) {
                    sendEmail();
                } else {
                    showAlertPopupDialog();
                }
            }
        });

        // header_layout : clearFocus
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus();
            }
        });

        // context_layout : clearFocus
        context_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus();
            }
        });

//        title_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            inquiry_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
//
//                } else {        // focus in
//                    inquiry_button.setVisibility(View.GONE);
//
//                }
//            }
//        });

//        context_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {       // focus out
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            inquiry_button.setVisibility(View.VISIBLE);
//                        }
//                    }, 150);
//
//                } else {        // focus in
//                    inquiry_button.setVisibility(View.GONE);
//
//                }
//            }
//        });

        // EditText
        // title_editText : TextChange
        title_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title_input = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // context_editText : TextChange
        context_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                context_input = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }   // onCreate

    // Send an Email via Email Application
    private void sendEmail() {
        String email_to = "chaeyoung.song@tiasolution.net";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, index);
        emailIntent.putExtra(Intent.EXTRA_TEXT, id + " " + user_id + "\n" +
                name + " \n" +
                email_contact + " " + title_input + "\n" +
                context + " : \n" + context_input);

        try {
            startActivity(emailIntent);
            Toast toast = Toast.makeText(customer_service_email_inquiry.this, select_email, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast toast = Toast.makeText(customer_service_email_inquiry.this, no_app, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }

    }

    // Information Missing Notification Popup
    private void showAlertPopupDialog() {
        Dialog popupDialog = new Dialog(customer_service_email_inquiry.this);
        popupDialog.setContentView(R.layout.popup_dialog_notice_one_button);

        TextView context_textView = popupDialog.findViewById(R.id.context_textView);
        Button confirm_button = popupDialog.findViewById(R.id.confirm_button);
        context_textView.setText(getResources().getText(R.string.popup_email_inquiry));

        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }

    // Clear EditText Focus
    private void clearFocus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        title_editText.clearFocus();
        context_editText.clearFocus();
    }
}   // customer_service_email_inquiry.java
