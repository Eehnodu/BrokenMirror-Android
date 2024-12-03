/*
__author__ = 'Song Chae Young'
__date__ = 'Dec.05, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_account_login_adapter.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.util.ArrayList;

public class setting_account_login_adapter extends RecyclerView.Adapter<setting_account_login_adapter.ViewHolder> {
    private final ArrayList<String> createdAt;
    private final ArrayList<String> device;
    private final ArrayList<String> ip;

    public setting_account_login_adapter(ArrayList<String> createdAt, ArrayList<String> device, ArrayList<String> ip) {
        this.createdAt = createdAt;
        this.device = device;
        this.ip = ip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_account_login_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String current_device = device.get(position);
        String current_date = createdAt.get(position);
        String current_ip = ip.get(position);

        holder.date_textView.setText(current_date);
        holder.device_textView.setText(current_device);
        holder.ip_textView.setText(current_ip);
    }

    @Override
    public int getItemCount() {
        return device.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date_textView;
        private final TextView device_textView;
        private final TextView ip_textView;

        public ViewHolder(View view) {
            super(view);
            date_textView = view.findViewById(R.id.login_adapter_date_textView);
            device_textView = view.findViewById(R.id.login_adapter_device_textView);
            ip_textView = view.findViewById(R.id.login_adapter_ip_textView);

        }
    }
}
