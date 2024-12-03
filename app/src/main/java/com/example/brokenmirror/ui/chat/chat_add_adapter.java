package com.example.brokenmirror.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.util.ArrayList;

public class chat_add_adapter extends RecyclerView.Adapter<chat_add_adapter.ViewHolder>{

    private ArrayList<String> user_name;
    private ArrayList<String> user_number;
    private ArrayList<Integer> user_key;
    private ArrayList<Boolean> isSelected;
    private OnItemSelectedListener onItemSelectedListener;

    public chat_add_adapter(ArrayList<String> user_name, ArrayList<String> user_number, ArrayList<Integer> user_key) {
        this.user_name = user_name;
        this.user_number = user_number;
        this.user_key = user_key;
        this.isSelected = new ArrayList<>(user_name.size());

        // first init
        for (int i = 0; i < user_name.size(); i++) {
            isSelected.add(false);
        }
    }

    public void setOnItemSelectedListener (OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_add_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String current_name = user_name.get(position);
        String current_number = user_number.get(position);
        int current_key = user_key.get(position);
        
        String formatted_number = String.format("010-%s-%s", current_number.substring(3, 7), current_number.substring(7));

        holder.name_textView.setText(current_name);
        holder.number_textView.setText(formatted_number);
        
        if (current_key == 3) {
            holder.round.setVisibility(View.GONE);
            holder.icon_layout.setVisibility(View.GONE);
        } else if (current_key == 2) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_2);
            holder.icon_layout.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_2);
        } else if (current_key == 1) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_1);
            holder.icon_layout.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_1);
        } else if (current_key == 0) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_0);
            holder.icon_layout.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_0);
        }

    }

    @Override
    public int getItemCount() {
        return user_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_textView;
        private TextView number_textView;
        private ConstraintLayout layout;
        private View check_view;
        private View round;
        private View icon;
        private ConstraintLayout icon_layout;

        public ViewHolder (View view) {
            super(view);
            name_textView = view.findViewById(R.id.user_name_textView);
            number_textView = view.findViewById(R.id.user_phone_textView);
            layout = view.findViewById(R.id.layout);
            check_view = view.findViewById(R.id.checkbox);
            round = view.findViewById(R.id.user_profile_round);
            icon = view.findViewById(R.id.key_icon);
            icon_layout = view.findViewById(R.id.view_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    // Toggle based on the current clicked state
                    isSelected.set(position, !isSelected.get(position));
                    onItemSelectedListener.onItemSelected(user_name.get(position), user_number.get(position), user_key.get(position));

                    // set background
                    if (isSelected.get(position)) {
                        layout.setBackgroundResource(R.drawable.bg_list_selected);
                        check_view.setBackgroundResource(R.drawable.checkbox_activate);
                    } else {
                        layout.setBackgroundResource(R.drawable.bg_transparent);
                        check_view.setBackgroundResource(R.drawable.checkbox_empty);
                    }
                }
            });
        }
    }

    // Method : Count selected items
    private int countItems() {
        int count = 0;
        for (boolean selected : isSelected) {
            if (selected) {
                count++;
            }
        }
        return count;
    }

    // callback interface
    public interface OnItemSelectedListener {
        void onItemSelected (String userName, String userNumber, int userKey);
    }

    public ArrayList<Boolean> getSelectedItems() {
        return isSelected;
    }


}