package com.example.brokenmirror.ui.friend;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.util.ArrayList;

public class friend_list_customAdapter extends RecyclerView.Adapter<friend_list_customAdapter.ViewHolder> {

    private ArrayList<String> userNames;
    private ArrayList<String> userNumbers;
    private ArrayList<Integer> keyVal;
    private ItemClickListener mListener;


    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public friend_list_customAdapter(ArrayList<String> userNames, ArrayList<String> userNumbers, ArrayList<Integer> keyVal, ItemClickListener listener) {
        {
            this.userNames = userNames;
            this.userNumbers = userNumbers;
            this.keyVal = keyVal;
            this.mListener = listener;

        }
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_listview, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentUserName = userNames.get(position);
        String currentUserNumber = userNumbers.get(position);

        int currentKeyVal = keyVal.get(position);

        String formattedUserNumber = String.format("010-%s-%s",
                currentUserNumber.substring(3, 7), currentUserNumber.substring(7));

        holder.nameTextView.setText(currentUserName);
        holder.phoneTextView.setText(formattedUserNumber);

        if (currentKeyVal == 3) {
            holder.round.setVisibility(View.INVISIBLE);
            holder.icon.setVisibility(View.GONE);

        } else if (currentKeyVal == 2) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_2);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_2);

        } else if (currentKeyVal == 1) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_1);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_1);

        } else if (currentKeyVal == 0) {
            holder.round.setVisibility(View.VISIBLE);
            holder.round.setBackgroundResource(R.drawable.bg_round_key_0);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.icon_key_ver_0);
        }


    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView phoneTextView;
        private View round;
        private View icon;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.listView_user_name);
            phoneTextView = view.findViewById(R.id.listView_user_phone);
            round = view.findViewById(R.id.key_value_round);
            icon = view.findViewById(R.id.key_value_icon);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        if (mListener != null) {        // add
                            mListener.onItemClick(position);
                        }

                        String currentUserName = userNames.get(position);
                        String currentUserNumber = userNumbers.get(position);
                        Integer currentKeyVal = keyVal.get(position);

                        Log.d("RecyclerView Click", "Position : " + position + ", UserName : " + currentUserName +
                                ", UserNumber : " + currentUserNumber + ", KeyVal : " + currentKeyVal);

                    } else {
                        Log.d("RecyclerView Click", "NO POSITION");
                    }

                }
            });

        }
    }

    public void clear() {
        userNames.clear();
        userNumbers.clear();
        notifyDataSetChanged();     // data reload
    }


}