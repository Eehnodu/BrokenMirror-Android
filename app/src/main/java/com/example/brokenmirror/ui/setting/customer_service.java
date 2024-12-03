/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.19, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'customer_service.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.brokenmirror.R;

public class customer_service extends AppCompatActivity {
    Intent emailIntent;
    Intent smsIntent;
    PackageManager pm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service);

        String homepage_url = "https://tiasolution.net/layout/basic/main.html";
        String email_address = "chaeyoung.song@tiasolution.net";
        String error_address = "chaeyoung.song@tiasolution.net";

//        String email_title = getResources().getString(R.string.customer_service_email_inquiry_title);
//        String email_context = getResources().getString(R.string.customer_service_email_inquiry_context);
//        String error_title = getResources().getString(R.string.customer_service_error_inquiry_title);
//        String error_context = getResources().getString(R.string.customer_service_error_inquiry_context);


        Button back_button = findViewById(R.id.back_button);
        Button close_button = findViewById(R.id.close_button);

        ConstraintLayout email_layout = findViewById(R.id.email_layout);
        ConstraintLayout error_layout = findViewById(R.id.error_layout);
        ConstraintLayout homepage_layout = findViewById(R.id.web_layout);

        TextView terms_textView = findViewById(R.id.terms_service_textView);
        TextView policy_textView = findViewById(R.id.privacy_policy_textView);
        TextView opensource_textView = findViewById(R.id.opensource_textView);
        TextView etc_textView = findViewById(R.id.etc_textView);

        pm = getPackageManager();

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
            public void onClick(View view) {
                finish();
            }
        });

        // Top Layout
        // email_layout : OnClickListener (이메일 보내는 거 앱에서 처리 해야할 듯)
        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendEmail(email_address, email_title, email_context);
                Intent intent = new Intent(customer_service.this, customer_service_email_inquiry.class);
                startActivity(intent);
                onPause();
            }
        });

        // error_layout : OnClickListener
        error_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendEmail(error_address, error_title, error_context);
                Intent intent = new Intent(customer_service.this, customer_service_error_inquiry.class);
                startActivity(intent);
                onPause();
            }
        });

        // homepage_layout : OnClickListener
        homepage_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(homepage_url);
                intent.setData(uri);
                startActivity(intent);

            }
        });

        // Bottom Layout
        // terms_textView : OnClickListener
        terms_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customer_service.this, customer_service_terms.class);
                startActivity(intent);
                onPause();
            }
        });

        // policy_textView : OnClickListener
        policy_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customer_service.this, customer_service_policy.class);
                startActivity(intent);
                onPause();
            }
        });

        // opensource_textView : OnClickListener
        opensource_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customer_service.this, customer_service_open.class);
                startActivity(intent);
                onPause();

            }
        });

        // etc_textView : OnClickListener
        etc_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }   // onCreate

//    private void sendEmail(String address, String title, String context) {
//        emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
//        emailIntent.putExtra(Intent.EXTRA_TEXT, context);
//
//        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
//
//        if (resolveInfoList != null && !resolveInfoList.isEmpty()) {
//            boolean foundEmailApp = false;
//            for (ResolveInfo resolveInfo : resolveInfoList) {
//                String packageName = resolveInfo.activityInfo.packageName;
//                if (packageName != null && (packageName.contains("android.email") || packageName.contains("com.google.android.gm"))) {
//                    // Found either the default Email app or Gmail app
//                    foundEmailApp = true;
//                    break;
//                }
//            }
//            if (foundEmailApp) {
//                // There's at least one app that can handle the intent, which includes Email app or Gmail app
//                startActivity(emailIntent);
//            } else {
//                smsIntent = new Intent(Intent.ACTION_SENDTO);
//                smsIntent.setData(Uri.parse("smsto:" + address));  // This ensures only SMS apps respond
//                smsIntent.putExtra("sms_body", title + "\n\n" + context);
//
//                // Verify that the intent will resolve to an activity
//                if (smsIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(smsIntent);
//                } else {
//                    // Handle case where no SMS app is available
//                    // display a toast or dialog to inform the user
//                }
//            }
//        }
//    }

}   // customer_service.java
