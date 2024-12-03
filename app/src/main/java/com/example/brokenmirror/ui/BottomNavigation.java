package com.example.brokenmirror.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.chat.chat_main;
import com.example.brokenmirror.ui.friend.friendList;
import com.example.brokenmirror.ui.notification.notification_main;
import com.example.brokenmirror.ui.setting.setting_main;

public class BottomNavigation {
    private Context context;
    private View rootView;

    public BottomNavigation(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    private void disableScreenTransitionAnimation() {
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    public void setupBottomNavigation() {
        //Friends Category
        rootView.findViewById(R.id.nav_friend_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(context instanceof friendList)) {
                    Intent intent = new Intent(context, friendList.class);
                    context.startActivity(intent);
                    disableScreenTransitionAnimation();
                    ((Activity) context).finish();
                }

//                if (!(context instanceof friend_list)) {
//                    Intent intent = new Intent(context, friend_list.class);
//                    context.startActivity(intent);
//                    disableScreenTransitionAnimation();
//                    ((Activity) context).finish();
//                }
            }
        });

        //Chat Category
        rootView.findViewById(R.id.nav_chat_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(context instanceof chat_main)) {
                    Intent intent = new Intent(context, chat_main.class);
                    context.startActivity(intent);
                    disableScreenTransitionAnimation();
                    ((Activity) context).finish();
                }
            }
        });

        //Notification Category
        rootView.findViewById(R.id.nav_noti_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(context instanceof notification_main)) {
                    Intent intent = new Intent(context, notification_main.class);
                    context.startActivity(intent);
                    disableScreenTransitionAnimation();
                    ((Activity) context).finish();
                }
            }
        });

        //Setting category
        rootView.findViewById(R.id.nav_set_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(context instanceof setting_main)) {
                    Intent intent = new Intent(context, setting_main.class);
                    context.startActivity(intent);
                    disableScreenTransitionAnimation();
                    ((Activity) context).finish();
                }
            }
        });
    }
}
