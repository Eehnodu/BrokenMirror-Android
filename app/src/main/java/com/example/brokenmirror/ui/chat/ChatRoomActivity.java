package com.example.brokenmirror.ui.chat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.MessageDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class ChatRoomActivity extends AppCompatActivity {

    ImageButton chatSend;
    EditText chatInput;
    RecyclerView chatRecyclerview;
    ChatAdapter adapter;

    StompClient stompClient;
    private static final String WEBSOCKET_URL = "ws://172.22.7.37:8080/ws";
    private static final String TOPIC_DESTINATION = "/topic/chat";  // STOMP 구독 경로
    private static final String SEND_DESTINATION = "/app/chat.sendMessage";  // 메시지 전송 경로

    private static final int TYPE_MY_MESSAGE = 1;
    private static final int TYPE_OTHERS_MESSAGE = 2;
    private String roomId = "room1";
    private String sender = "test@tiasolution.net";
    private String receiver = "other";

    private Gson gson;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        chatSend = findViewById(R.id.chat_send);
        chatInput = findViewById(R.id.chat_input);
        chatRecyclerview = findViewById(R.id.ChattingRecyclerView);

        adapter = new ChatAdapter(new ArrayList<>());
        chatRecyclerview.setAdapter(adapter);
        chatRecyclerview.setLayoutManager(new LinearLayoutManager(this));


        // GsonBuilder 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new JsonSerializer<Instant>() {
                        @Override
                        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.toString());  // Instant를 문자열로 변환
                        }
                    })
                    .create();
        }


        //websocket 연결 설정
        initStompClient();

        chatSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 작성한 채팅메시지 가져오기
                String messageText = chatInput.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                    chatInput.setText("");
                    adapter.addItem(TYPE_MY_MESSAGE,messageText);
                    adapter.notifyDataSetChanged();
                    chatRecyclerview.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

    }  // onCreate


    // STOMP 클라이언트 초기화
    @SuppressLint("CheckResult")
    private void initStompClient() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("ChatActivity", "WebSocket 연결됨");
                    break;
                case ERROR:
                    Log.e("ChatActivity", "WebSocket 에러 발생", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("ChatActivity", "WebSocket 연결 끊김");
                    break;
            }
        });

        // 서버로부터 메시지 수신
        stompClient.topic(TOPIC_DESTINATION).subscribe(stompMessage -> {
            runOnUiThread(() -> {
                // 수신한 메시지를 리스트에 추가하고 RecyclerView 업데이트
                MessageDto message = new Gson().fromJson(stompMessage.getPayload(), MessageDto.class);
                adapter.addItem(TYPE_OTHERS_MESSAGE, message.getMessage() + " - From server");
                chatRecyclerview.scrollToPosition(adapter.getItemCount() - 1);  // 마지막 메시지로 스크롤
            });
        });

        stompClient.connect();

    }

    // sendMessage()
    private void sendMessage(String messageText) {
        //MessageDto message = new MessageDto(roomId, messageText, sender, receiver, Instant.now());   // MessageDto 객체 생성
        MessageDto message = new MessageDto(roomId, messageText, sender, receiver);   // MessageDto 객체 생성
        String jsonMessage = new Gson().toJson(message);  // 메시지를 JSON 형식으로 변환
        stompClient.send(SEND_DESTINATION, jsonMessage).subscribe(() -> {
            Log.d("ChatActivity", "메시지 전송 성공");
        }, throwable -> {
            Log.e("ChatActivity", "메시지 전송 실패", throwable);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();  // WebSocket 연결 종료
        }
    }

}
