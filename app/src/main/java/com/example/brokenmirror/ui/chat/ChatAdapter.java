package com.example.brokenmirror.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.MessageDto;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String sender = "test@tiasolution.net";

    private static final int TYPE_MY_MESSAGE = 1;
    private static final int TYPE_OTHERS_MESSAGE = 2;

    private List<MessageDto> messageList;

    public ChatAdapter(List<MessageDto> messageList) {
        this.messageList = messageList;
    }


    // getItemViewType()으로 메시지 종류에 따른 뷰타입 리턴 (일단 지금은 나랑 상대방 뷰타입만)
    // 그리고 뷰타입에 따른 뷰홀더를 각각 따로 정의할거임
    // 그러고나서 뷰홀더생성메서드에서 뷰타입에 따른 레이아웃을 인플레이트해서 뷰홀더 생성
    // 온바인드에서 데이터 바인딩만 해주면 끝
    @Override
    public int getItemViewType(int position) {
        MessageDto message = messageList.get(position);
        if (message.getSender().equals(sender)) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_OTHERS_MESSAGE;
        }
    }


    // 뷰홀더 정의
    public class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView chat_my_message;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            chat_my_message = itemView.findViewById(R.id.me_gnrl_message_textView);
        }
        // 데이터를 뷰에 바인딩
        public void bind(MessageDto message) {
            chat_my_message.setText(message.getMessage());
        }
    }

    public class OthersMessageViewHolder extends RecyclerView.ViewHolder {
        TextView chat_others_message;

        public OthersMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            chat_others_message = itemView.findViewById(R.id.other_gnrl_message_textView);
        }

        public void bind(MessageDto message) {
            chat_others_message.setText(message.getMessage());
        }
    }


    // 뷰홀더 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_room_adapter_me_gnrl, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_room_adapter_other_gnrl, parent, false);
            return new OthersMessageViewHolder(view);
        }
    }

    // 뷰홀더에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDto message = messageList.get(position);

        if (holder instanceof MyMessageViewHolder) {
            ((MyMessageViewHolder) holder).bind(message);
        } else if (holder instanceof OthersMessageViewHolder) {
            ((OthersMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    // 리스트에 아이템 추가
//    public void addItem(MessageDto message) {
//        messageList.add(message);
//        notifyItemInserted(messageList.size()-1);
//    }
    public void addItem(int viewType, String message) {
        if (viewType == TYPE_MY_MESSAGE) {
            //MessageDto messageDto = new MessageDto("roomId", message, "test@tiasolution.net", "otherUserId", Instant.now());
            MessageDto messageDto = new MessageDto("roomId", message, "test@tiasolution.net", "otherUserId");
            messageList.add(messageDto);
        } else {
            //MessageDto messageDto = new MessageDto("roomId", message, "otherUserId", "test@tiasolution.net", Instant.now());
            MessageDto messageDto = new MessageDto("roomId", message, "otherUserId", "test@tiasolution.net");
            messageList.add(messageDto);
        }
        notifyItemInserted(messageList.size() - 1);
    }

}

