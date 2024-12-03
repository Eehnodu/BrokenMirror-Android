/*
__author__ = 'Song Chae Young'
__date__ = 'Nov.7, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'user_profile_other.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.brokenmirror.R;
import com.example.brokenmirror.bitmap.BitmapConverter;
import com.example.brokenmirror.data.FriendRoomDto;
import com.example.brokenmirror.sharedpref.FriendInfoPref;
import com.example.brokenmirror.ui.chat.ChatRoomActivity;

public class user_profile_other extends AppCompatActivity{

    private TextView popup_textView;
    private LinearLayout unblock_layout;
    private LinearLayout chat_layout;
    private LinearLayout block_layout;
    private ConstraintLayout contextLayout;

    String userName;
    String userNumber;
    int keyVal;

    private Bitmap bitmap;
    private final BitmapConverter converter = new BitmapConverter();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_other);

        FriendInfoPref friendInfoPref = new FriendInfoPref(this);
        FriendRoomDto friend_info = friendInfoPref.getFriendInfo();

        userName = friend_info.getUserName();
        userNumber = friend_info.getPhoneNum();
        keyVal = friend_info.getKey();
        bitmap = converter.StringToBitmap(friend_info.getProfileImg());

        Button close_button = findViewById(R.id.profile_other_close_button);

        TextView name_textView = findViewById(R.id.profile_other_name_textView);
        TextView number_textView = findViewById(R.id.profile_other_number_textView);
        ImageView prifle_imageView = findViewById(R.id.profile_other_main_imageView);

        View key_round_imageView = findViewById(R.id.profile_other_key_round_imageView);
        View key_icon_imageView = findViewById(R.id.profile_other_key_icon_imageView);

        chat_layout = findViewById(R.id.profile_other_chat_layout);
        block_layout = findViewById(R.id.profile_other_block_layout);
        unblock_layout = findViewById(R.id.profile_other_unblock_layout);
        contextLayout = findViewById(R.id.context_layout);

        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.test_not_implemented), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);

        loadGlideImage(friend_info.getProfileImg(), prifle_imageView);

        // Button
        // close_button : onClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendInfoPref.clearFriendInfo();
                finish();
            }
        });

        // TextView
        // name_textView : setText
        name_textView.setText(userName);

        // number_textview : change userNumber Format (010-0000-0000) & setText
        // DB 수정하면 코드 수정해야함!@!@!@!@!@
        if (userNumber != null) {
            if (userNumber.length() == 11) {
                String formattedNumber = userNumber.substring(0, 3) + "-" + userNumber.substring(3, 7) + "-" + userNumber.substring(7);
                number_textView.setText(formattedNumber);
            } else {
                number_textView.setText(userNumber);
            }
        } else {
            number_textView.setText(getString(R.string.test_phone_num2));
        }


        // ImageView
        // key_round_imageView : visual representation of keyValue
        if (keyVal == 0) {
            key_round_imageView.setBackgroundResource(R.drawable.bg_round_key_0);
            key_icon_imageView.setVisibility(View.VISIBLE);
            key_icon_imageView.setBackgroundResource(R.drawable.icon_key_ver_0);
        } else if (keyVal == 1) {
            key_round_imageView.setBackgroundResource(R.drawable.bg_round_key_1);
            key_icon_imageView.setVisibility(View.VISIBLE);
            key_icon_imageView.setBackgroundResource(R.drawable.icon_key_ver_1);
        } else if (keyVal == 2) {
            key_round_imageView.setBackgroundResource(R.drawable.bg_round_key_2);
            key_icon_imageView.setVisibility(View.VISIBLE);
            key_icon_imageView.setBackgroundResource(R.drawable.icon_key_ver_2);
        } else if (keyVal == 3) {
            key_round_imageView.setVisibility(View.GONE);
            key_icon_imageView.setVisibility(View.INVISIBLE);

        }

        // LinearLayout
        // block_layout : OnClickListener
        block_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBlockPopupDialog(userName);
            }
        });

        // button_layout : OnClickListener
        unblock_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnBlockPopupDialog(userName);
            }
        });

        // chat_layout : OnClickListener
        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatRoom_intent = new Intent(user_profile_other.this, ChatRoomActivity.class);
                chatRoom_intent.putExtra("name", userName);
                chatRoom_intent.putExtra("key", keyVal);
                startActivity(chatRoom_intent);
                finish();


//                toast.show();
            }
        });

    }       // onCreate

    // Display a BlockPopupDialog
    private void showBlockPopupDialog(String text) {
        // create dialog object
        Dialog popupDialog = new Dialog(user_profile_other.this);

        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_block);

        // set Text
        popup_textView = popupDialog.findViewById(R.id.popup_dialog_block_user_textView);
        popup_textView.setText(text);

        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Button : cancel_button (OnClickListener)
        Button cancel_button = popupDialog.findViewById(R.id.popup_dialog_block_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });

        // Button : confirm_button (OnClickListener)
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_block_confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chat_layout.setVisibility(View.GONE);
                block_layout.setVisibility(View.GONE);
                unblock_layout.setVisibility(View.VISIBLE);

                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }

    // Display an UnBlockPopupDialog
    private void showUnBlockPopupDialog(String text) {
        // create dialog object
        Dialog popupDialog = new Dialog(user_profile_other.this);

        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_unblock);

        // set Text
        popup_textView = popupDialog.findViewById(R.id.popup_dialog_block_user_textView);
        popup_textView.setText(text);

        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Button : cancel_button (OnClickListener)
        Button cancel_button = popupDialog.findViewById(R.id.popup_dialog_block_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });

        // Button : confirm_button (OnClickListener)
        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_block_confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unblock_layout.setVisibility(View.GONE);
                chat_layout.setVisibility(View.VISIBLE);
                block_layout.setVisibility(View.VISIBLE);

                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }

    // Glide (이미지 로드)
    public void loadGlideImage(String imageUrl, ImageView profileImage) {
        Glide.with(this)
                .load(imageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(profileImage);
    }

}
