/*
__author__ = 'Song Chae Young'
__date__ = 'Feb.14, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_friend_block_adapter.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;

import java.util.ArrayList;

public class setting_friend_block_adapter extends RecyclerView.Adapter<setting_friend_block_adapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Pair<String, String>> combinedList;

    public setting_friend_block_adapter(Context context, ArrayList<Pair<String, String>> combinedList) {
        this.context = context;
        this.combinedList = combinedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_friend_block_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, String> currentItem = combinedList.get(position);

        String user_name = currentItem.first;
        String user_number = currentItem.second;
        String user_number_form = String.format("010-%s-%s",
                user_number.substring(3, 7), user_number.substring(7));

        holder.user_name_textView.setText(user_name);
        holder.user_number_textView.setText(user_number_form);
    }

    @Override
    public int getItemCount() {
        return combinedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView user_name_textView;
        private final TextView user_number_textView;

        public ViewHolder(View view) {
            super(view);
            Button unblock_button = view.findViewById(R.id.unblock_button);
            user_name_textView = view.findViewById(R.id.user_name_textView);
            user_number_textView = view.findViewById(R.id.user_number_textView);

            // Button
            // unblock_button : showPopupDialog
            unblock_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();        // get position
                    if (position != RecyclerView.NO_POSITION) {
                        Pair<String, String> currentItem = combinedList.get(position);
                        showPopupDialog(currentItem.first, position);       // set the userName to popup

                    }
                }
            });
        }
    }

    private void showPopupDialog(String user_name, int position) {
        // create dialog object
        Dialog popupDialog = new Dialog(context);

        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_block);

        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView pop_userName_textView = popupDialog.findViewById(R.id.popup_dialog_block_user_textView);
        TextView pop_notice_textView = popupDialog.findViewById(R.id.user_block);

        Button confirm_button = popupDialog.findViewById(R.id.popup_dialog_block_confirm_button);
        Button close_button = popupDialog.findViewById(R.id.popup_dialog_block_cancel_button);

        // setText
        pop_userName_textView.setText(user_name);
        pop_notice_textView.setText(R.string.user_profile_popup_unblock);

        // Button
        // confirm_button : dismiss
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<String, String> deletedItem = combinedList.get(position); // get the deleted item
                combinedList.remove(position);      // remove data
                notifyItemRemoved(position);

                popupDialog.dismiss();      // close popup
            }
        });

        // close_button : dismiss
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close popup
                popupDialog.dismiss();
            }
        });

        popupDialog.show();     // start
    }
}