package com.example.brokenmirror.ui.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class notification_main_adapter extends RecyclerView.Adapter<notification_main_adapter.ViewHolder> {
    private List<NotificationItem> data;
    private SimpleDateFormat sdf;

    public notification_main_adapter(List<NotificationItem> data) {
        this.data = data;  //
        sdf = new SimpleDateFormat("yyyy.MM.dd(EEE) HH:mm", new Locale("ko", "KR"));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        // Generate random data for each item
        for (NotificationItem item : data) {
            item.generateRandomTimestamp();
        }

        // Sort data in descending order by recent dates and refresh
        refreshData();
    }

    private void refreshData() {
        // Sort data in descending order by recent dates
        Collections.sort(data, new Comparator<NotificationItem>() {
            @Override
            public int compare(NotificationItem item1, NotificationItem item2) {
                return item2.getTimestamp().compareTo(item1.getTimestamp());
            }
        });
        // Notify the adapter that the data has been updated
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_main_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    public static class NotificationItem {
        private String text;
        private int logoResourceId;
        private Date timestamp;

        public NotificationItem(String text, int logoResourceId) {
            this.text = text;
            this.logoResourceId = logoResourceId;
            generateRandomTimestamp();
        }


        public String getText() {
            return text;
        }

        public int getLogoResourceId() {
            return logoResourceId;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        private void generateRandomTimestamp() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long startTimeMillis = calendar.getTimeInMillis();

            calendar.set(Calendar.YEAR, 2024);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            long endTimeMillis = calendar.getTimeInMillis();

            long randomTimeMillis = startTimeMillis + (long) (Math.random() * (endTimeMillis - startTimeMillis));

            this.timestamp = new Date(randomTimeMillis);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView img_logo;
        private View img_logo;
        private TextView img_text;
        private TextView img_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.notification_logo);
            img_text = itemView.findViewById(R.id.notification_main_adapter_text);
            img_time = itemView.findViewById(R.id.notification_main_adapter_time);
        }

        public void bindData(NotificationItem item) {
//            img_logo.setImageResource(item.getLogoResourceId());
            img_logo.setBackgroundResource(item.getLogoResourceId());
            img_text.setText(item.getText());
            img_time.setText(sdf.format(item.getTimestamp()));
        }
    }

        public void setData(List<NotificationItem> newData) {
            data = newData;
            refreshData();
        }

}