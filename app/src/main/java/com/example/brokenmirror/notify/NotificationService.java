package com.example.brokenmirror.notify;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalTime;

public class NotificationService {
    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
    }

    // 알림 채널 생성
    public void createNotificationChannel(String CHANNEL_ID, CharSequence channel_name, String channel_description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            channel.setDescription(channel_description);
            channel.setSound(null, null); // 알림 소리 X

            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                // API 26 미만에서는 NotificationChannel 없이 알림을 처리
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // 필요한 추가 설정이 있으면 여기에서 처리 (예: 기본 알림 중요도 설정)
                // 알림 생성 시 알림 매니저를 통해 알림을 직접 관리
            }
        }
    }

    // 알림 객체 생성
    public NotificationCompat.Builder createNotification(String CHANNEL_ID, String title, String text, int icon, String group_id, Class<?> cls, int menu) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이미 열려 있는 액티비티가 있으면 그 위에 쌓이지 않도록
        intent.putExtra("menu", menu);
        // 문자 등 답장을 보내야하는 거라면 pendingIntent값을 다르게 해야함, PadingIntent.FLAG_UPDATE_CURRENT? 혹은 REQUESTCODE를 변경
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getActivity(context, LocalTime.now().hashCode(), intent, PendingIntent.FLAG_IMMUTABLE);
        }

        // 백그라운드 작업은 BroadcastReceiver??를 사용??
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(text)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(group_id)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // 우선순위 높게 설정
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);  // 잠금화면에도 표시
    }

    // 그룹 알림
    public NotificationCompat.Builder summaryNotification(String CHANNEL_ID, String group_id, int icon) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setGroup(group_id)
                .setGroupSummary(true);
    }

    // 알림 표시
    public void sendNotification(String CHANNEL_ID, String title, String text, int icon, int notificationId, String group_id, Class<?> cls, int menu) {
        NotificationCompat.Builder builder = createNotification(CHANNEL_ID, title, text, icon, group_id, cls, menu);
        NotificationCompat.Builder summary = summaryNotification(CHANNEL_ID, group_id, icon);
        // NotificationManagerCompat를 통해 알림 전송
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        // 알림 전송
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(notificationId, builder.build());
        notificationManagerCompat.notify(0, summary.build());
    }
}
