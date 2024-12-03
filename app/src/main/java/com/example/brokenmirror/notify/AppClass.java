package com.example.brokenmirror.notify;

import android.app.Application;

public class AppClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationService channel1 = new NotificationService(this);
        NotificationService channel2 = new NotificationService(this);
        NotificationService channel3 = new NotificationService(this);

        // channel_name : 설정 창에서 보이는 이름
        channel1.createNotificationChannel("channel1","채널1","channel1");
        channel2.createNotificationChannel("channel2","채널2","channel2");
        channel3.createNotificationChannel("channel3","채널3","channel3");
    }
}
