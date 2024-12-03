package com.example.brokenmirror.ui.friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.brokenmirror.R;
import com.example.brokenmirror.data.FriendRoomDto;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private List<FriendRoomDto> friendList;

    // 생성자
    public FriendAdapter(List<FriendRoomDto> friendList) {
        this.friendList = friendList;
    }

    // onCreateViewHolder()는 아이템뷰 생성
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_listview, parent, false);
        return new FriendViewHolder(view);
    }

    // onBindViewHolder()는 생성된 뷰에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friendList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setFriends(List<FriendRoomDto> friendList) {
        this.friendList = friendList;
        notifyItemRangeChanged(0, friendList.size());
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        private String imageUrl;

        private TextView nameTextView = itemView.findViewById(R.id.listView_user_name);
        private TextView phoneNumTextView = itemView.findViewById(R.id.listView_user_phone);
        private ImageView profileImageView = itemView.findViewById(R.id.listView_user_icon);
        private View keyView = itemView.findViewById(R.id.key_value_icon);

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(FriendRoomDto friend) {
            nameTextView.setText(friend.getUserName());
            transNumber(friend.getPhoneNum());
            if (friend.getKey() == 0) {
                keyView.setBackgroundResource(R.drawable.icon_key_ver_0);
            } else if (friend.getKey() == 1) {
                keyView.setBackgroundResource(R.drawable.icon_key_ver_1);
            } else if (friend.getKey() == 2) {
                keyView.setBackgroundResource(R.drawable.icon_key_ver_2);
            }
            imageUrl = friend.getProfileImg();
            loadGlideImage(imageUrl, profileImageView);
        }

        // Glide (이미지 로드)
        public void loadGlideImage(String imageUrl, ImageView profileImage) {
            Glide.with(profileImage.getContext())
                    .load(imageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(profileImage);
        }

        public void transNumber(String userNumber) {
            if (userNumber != null) {
                if (userNumber.length() == 11) {
                    String formattedNumber = userNumber.substring(0, 3) + "-" + userNumber.substring(3, 7) + "-" + userNumber.substring(7);
                    phoneNumTextView.setText(formattedNumber);
                } else {
                    phoneNumTextView.setText(userNumber);
                }
            }
        }
    }

    public FriendRoomDto getFriendAtPosition(int position) {
        return friendList.get(position);
    }
}
