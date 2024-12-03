package com.example.brokenmirror.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.brokenmirror.R;
import com.example.brokenmirror.bitmap.BitmapConverter;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.sharedpref.UserSharedPref;

public class user_profile_me extends AppCompatActivity {
    private ImageView myProfileImg;
    private TextView myNameText;
    private TextView myPhoneNumberText;
    private UserDto user_info;
    private UserSharedPref user_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_me);
        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        Button closeMyProfileBtn = findViewById(R.id.close_myProfile_btn);
        myProfileImg = findViewById(R.id.my_profile_img);
        myNameText = findViewById(R.id.my_name_text);
        myPhoneNumberText = findViewById(R.id.my_phoneNumber_text);
        Button editProfileBtn = findViewById(R.id.edit_profile_btn);

        myNameText.setText(user_info.getUserName());
        transNumber(user_info.getPhoneNum());

        if (user_info != null) {
            loadGlideImage(user_info.getProfileImg(), myProfileImg);
        }

        // 1. X버튼
        closeMyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        // 2. 프로필 편집 버튼
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile_me.this, user_profile_me_edit.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }  // onCreate

    // Glide (이미지 로드)
    public void loadGlideImage(String imageUrl, ImageView profileImage) {
        Glide.with(this)
                .load(imageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(profileImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_info = user_pref.getUser();
        if (user_info != null) {
            myNameText.setText(user_info.getUserName());
            loadGlideImage(user_info.getProfileImg(), myProfileImg);
        }
    }

    public void transNumber(String userNumber) {
        if (userNumber != null) {
            if (userNumber.length() == 11) {
                String formattedNumber = userNumber.substring(0, 3) + "-" + userNumber.substring(3, 7) + "-" + userNumber.substring(7);
                myPhoneNumberText.setText(formattedNumber);
            } else {
                myPhoneNumberText.setText(userNumber);
            }
        }
    }
}