/*
__author__ = 'Song Chae Young'
__date__ = 'Dec.05, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_key_adapter.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.util.ArrayList;

public class setting_key_adapter extends RecyclerView.Adapter<setting_key_adapter.ViewHolder> {

    private ArrayList<String> date;
    private ArrayList<String> user;
    private ArrayList<String> device;
    private ArrayList<Integer> key;

    public setting_key_adapter(ArrayList<String> date, ArrayList<String> user, ArrayList<String> device, ArrayList<Integer> key) {
        this.date = date;
        this.user = user;
        this.device = device;
        this.key = key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_key_adapter, parent, false);

//        Log.d("test", "date : " + date);
//        Log.d("test", "user : " + user);
//        Log.d("test", "device : " + device);
//        Log.d("test", "key : " + key);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentDate = date.get(position);
        String currentUser = user.get(position);
        String currentDevice = device.get(position);
        Integer currentKey = key.get(position);

        holder.date_textView.setText(currentDate);
        holder.user_textView.setText(currentUser);
        holder.device_textView.setText(currentDevice);
        holder.key_textView.setText(String.valueOf(currentKey));

    }

    @Override
    public int getItemCount() {     // Number of items to display
        return date.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date_textView;
        private final TextView user_textView;
        private final TextView device_textView;
        private final TextView key_textView;

        public ViewHolder(View view) {
            super(view);
            date_textView = view.findViewById(R.id.key_adapter_date_textView);
            user_textView = view.findViewById(R.id.key_adapter_user_textView);
            device_textView = view.findViewById(R.id.key_adapter_device_textView);
            key_textView = view.findViewById(R.id.key_adapter_key_textView);
        }
    }

    public void clear() {
        date.clear();
        user.clear();
        device.clear();
        key.clear();
        notifyDataSetChanged();     // data reload
    }
}

