//package com.example.brokenmirror.ui.chat;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.os.Bundle;
////import android.os.Message;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.brokenmirror.R;
//import com.example.brokenmirror.data.Message;
//import com.example.brokenmirror.data.MessageDto;
//import com.example.brokenmirror.ui.setting.setting_key_purchase;
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.TimeZone;
//import java.util.UUID;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//
//
//public class chat_room extends AppCompatActivity {
//    private String user_name;
//    private int user_key;
//    int previousState = BottomSheetBehavior.STATE_COLLAPSED;
//
//    private final int FILE_SELECT_REQUEST_CODE = 123;
//
//    final private int TYPE_DATE = 0;
//    final private int TYPE_EXPIRED = 1;
//    final private int TYPE_ME_KEY_COMPLETE = 2;
//    final private int TYPE_OTHER_KEY_COMPLETE = 3;
//    final private int TYPE_OTHER_KEY_REQ = 4;
//    final private int TYPE_ME_KEY_EXPIRED = 5;
//    final private int TYPE_OTHER_KEY_EXPIRED = 6;
//    final private int TYPE_ME_NOTICE = 7;
//    final private int TYPE_OTHER_NOTICE = 8;
//    final private int TYPE_ME_GNRL = 9;
//    final private int TYPE_OTHER_GNRL = 10;
//
//    EditText chatInput;
//    ImageButton chatSend;
//
//    RecyclerView recyclerView;
//    chat_room_adapter adapter = new chat_room_adapter(this);
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chat_room);
//
//        Button back_button = findViewById(R.id.back_button);
//        Button close_button = findViewById(R.id.close_button);
//
//        View key_icon = findViewById(R.id.key_value_view);
//
//        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet_behavior);
//        LinearLayout bottom_close_button = findViewById(R.id.bottom_close_button);
//
//        ConstraintLayout add_button = findViewById(R.id.add_button);
//        ConstraintLayout opt1_layout = findViewById(R.id.bottom_opt1);
//        ConstraintLayout opt2_layout = findViewById(R.id.bottom_opt2);
//        ConstraintLayout opt3_layout = findViewById(R.id.bottom_opt3);
//        ConstraintLayout opt4_layout = findViewById(R.id.bottom_opt4);
//        ConstraintLayout header_view = findViewById(R.id.header);
//        ConstraintLayout notice_layout = findViewById(R.id.notice_layout);
//
//        TextView user_name_textView = findViewById(R.id.chatRoomUserName);
//
//        //recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(adapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        chatInput = findViewById(R.id.chat_input);
//        chatSend = findViewById(R.id.chat_send);
//
//        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
//
//        Intent intent = getIntent();
//        user_name = intent.getStringExtra("name");
//        user_key = intent.getIntExtra("key", 2);
//
//        // View
//        // key_icon : display key icon
//        if (user_key == 3) {
//            key_icon.setVisibility(View.GONE);
//        } else if (user_key == 2) {
//            key_icon.setBackgroundResource(R.drawable.icon_key_ver_2);
//        } else if (user_key == 1) {
//            key_icon.setBackgroundResource(R.drawable.icon_key_ver_1);
//        } else if (user_key == 0) {
//            key_icon.setBackgroundResource(R.drawable.icon_key_ver_0);
//        }
//
//        user_name_textView.setText(user_name);
//
//
//        // Button
//        // back_button : finish
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        // close_button : OnClickListener
//        close_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//        //chat_send
//        chatSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//
//        // add_button : show bottomSheet
//        add_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                showBottomSheet();
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//
//        // bottom_close_button : close bottomSheet
//        bottom_close_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        });
//
//        // Bottom Sheet Behavior : bottom state change callback
//        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (previousState == BottomSheetBehavior.STATE_EXPANDED) {      // set add_button visibility
//                    add_button.setVisibility(View.VISIBLE);
//                } else if (previousState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    add_button.setVisibility(View.GONE);
//                }
//
//                previousState = newState;
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//
//        // opt1_layout : OnClickListener
//        opt1_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(chat_room.this, getString(R.string.test_need_server), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // opt2_layout : OnClickListener
//        opt2_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(chat_room.this, getString(R.string.test_need_server), Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivityForResult(intent, 1);
//            }
//        });
//
//        // opt3_layout : OnClickListener
//        opt3_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(chat_room.this, "2단계 기능 입니다. (현재 1단계)", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////                intent.addCategory(Intent.CATEGORY_OPENABLE);
////                intent.setType("*/*"); // 모든 유형의 파일을 선택할 수 있도록 설정
////                startActivityForResult(intent, FILE_SELECT_REQUEST_CODE);
//            }
//        });
//
//        // opt4_layout : OnClickListener
//        opt4_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupDialog();
//            }
//        });
//
//        // recyclerView : Collapsing the BottomSheetBehavior
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                add_button.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
//
//        header_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                add_button.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        notice_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                add_button.setVisibility(View.GONE);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                showNextChatMessage();
//            }
//        });
//
//
//        // OkHttpClient 생성 --------------------------------------------------------------------------------------
//
//        client = new OkHttpClient();  // OkHttpClient객체는 HTTP 및 WebSocket 요청을 처리하기 위한 클라이언트
//        Request request = new Request.Builder().url(WEBSOCKET_URL).build();  // 웹소켓 연결을 위한 요청 생성(Request.Builder) 및 연결할 서버 URL 지정
//        webSocket = client.newWebSocket(request, new EchoWebSocketListener());  // 위에서 생성한 request와 listener를 사용하여 웹소켓 연결 시작
//
//
//        // chat_send 버튼 클릭 시 서버로 메시지 전송
//        chatSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 작성한 채팅 메시지 가져오기
//                String message = chatInput.getText().toString().trim();
//
//                if (!message.isEmpty()) {
//                    // 서버로 메시지 전송
//                    sendChatMessage(message);
//                    chatInput.setText("");
//                    adapter.addItem(TYPE_ME_GNRL, message, user_key, user_name);
//                    adapter.notifyDataSetChanged();
//                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//                }
//            }
//        });
//
//    }   // onCreate
//
//    OkHttpClient client;
//    WebSocket webSocket;
//    private final String WEBSOCKET_URL = "ws://172.22.7.244:8080/ws";
//    private final String SEND_MESSAGE_ENDPOINT = "/app/chat.sendMessage";
//    private static final String SUBSCRIBE_ENDPOINT = "/topic/chat";
//    private String roomId = "roomID";
//
//
//    private final class EchoWebSocketListener extends WebSocketListener {
//
//        @Override
//        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
//
//            Log.d("웹소켓 연결 성공", "WebSocket connection opened.");
//            super.onOpen(webSocket, response);
//            subscribeToTopic(SUBSCRIBE_ENDPOINT);
//
//        }
//
//        @Override
//        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
//
//            Log.d("서버로부터 메시지 수신 성공", "Received message: " + text);
//
//            try {
//                Message receivedMessage = new Gson().fromJson(text, (Type) MessageDto.class);
//                Log.d("수신된 메시지 객체", "Message object: " + receivedMessage.toString());
//
//                runOnUiThread(() -> {
//                    adapter.addItem(TYPE_OTHER_GNRL, receivedMessage.getMessage(), 2, "Client B");
//                    adapter.notifyDataSetChanged();
//                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//                });
//            } catch (Exception e) {
//                Log.e("메시지 파싱 실패", "Error parsing message", e);
//            }
//        }
//
//        @Override
//        public void onClosing(WebSocket webSocket, int code, String reason) {
//            Log.d("웹소켓 닫힘", "WebSocket connection closing: " + reason);
//        }
//
//        @Override
//        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
//            Log.e("웹소켓 실패", "WebSocket connection failed: " + t.getMessage());
//        }
//
//    }
//
//    // ▼ 구독프레임
//    private void subscribeToTopic(String roomId) {
//        String subscriptionId = "sub-" + UUID.randomUUID().toString();
//        String frame = "SUBSCRIBE\nid:" + subscriptionId + "\ndestination:/topic/chat/" + roomId + "\n\n\0";
//        Log.d("구독 프레임", "Sending frame: " + frame);
//        webSocket.send(frame);
//        Log.d("구독 메시지 전송", "Subscribed to topic: /topic/chat/" + roomId + " with id: " + subscriptionId);
//    }
//
//
//    private void sendChatMessage(String message) {
//        JSONObject jsonMessage = new JSONObject();
//        try {
//            jsonMessage.put("roomId", roomId);
//            jsonMessage.put("message", message);
//            jsonMessage.put("sender", "Client A");
//            jsonMessage.put("receiver", "Client B");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//                ZonedDateTime kstTime = ZonedDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId());
//                String formattedTimestamp = kstTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//                jsonMessage.put("timestamp", formattedTimestamp);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("메시지 전송 실패", "Error creating JSON message", e);
//        }
//
//        String jsonString = jsonMessage.toString();
//        // ▼ 메시지 전송 프레임
//        String frame2 = "SEND\ndestination:" + SEND_MESSAGE_ENDPOINT + "\ncontent-type:application/json\n\n" + jsonString + "\n\0";
//        webSocket.send(frame2);
//        Log.d("메시지 전송 성공", "Message sent: " + jsonString);
//
//    }
//
//    // 액티비티 소멸 시, 웹소켓 연결도 종료
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        webSocket.close(1000, "Activity destroyed");
//    }
//
//
//    // ----------------------------------------------------------------------------------------------
//
//
//    // 빈 화면 클릭 시, 임시 채팅 출력
//    private int currentIndex = 0;
//
//    private void showNextChatMessage() {
//        if (currentIndex < 9) {
//            switch (currentIndex) {
//                case 0:
//                    showChatMessage(TYPE_DATE, "", user_key, null);
//                    break;
//                case 1:
//                    showChatMessage(TYPE_ME_KEY_COMPLETE, "", user_key, null);
//                    break;
//                case 2:
//                    showChatMessage(TYPE_OTHER_KEY_COMPLETE, "", user_key, user_name);
//                    break;
//                case 3:
//                    showChatMessage(TYPE_OTHER_KEY_REQ, "", user_key, user_name);
//                    break;
//                case 4:
//                    showChatMessage(TYPE_ME_KEY_EXPIRED, "", user_key, null);
//                    break;
//                case 5:
//                    showChatMessage(TYPE_OTHER_KEY_EXPIRED, "", user_key, user_name);
//                    break;
//                case 6:
//                    showChatMessage(TYPE_EXPIRED, "", user_key, null);
//                    break;
//                case 7:
//                    showChatMessage(TYPE_ME_NOTICE, "", user_key, null);
//                    break;
//                case 8:
//                    showChatMessage(TYPE_OTHER_NOTICE, "", user_key, user_name);
//                    break;
//                case 9:
//                    showChatMessage(TYPE_ME_GNRL, getString(R.string.popup_do_not), user_key, null);
//                    break;
//                case 10:
//                    showChatMessage(TYPE_OTHER_GNRL, getString(R.string.popup_do_not), user_key, user_name);
//                    break;
//                default:
//                    break;
//
//            }
//            currentIndex++;
//        } else {
//            // currentIndex 초기화
//            currentIndex = 0;
//        }
//
//        // RecyclerView의 마지막 아이템으로 스크롤 다운
//        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//    }
//
//    private void showChatMessage(int messageType, String message, int key, String name) {
//        adapter.addItem(messageType, message, key, name);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void showPopupDialog() {
//        // create dialog object
//        Dialog popupDialog = new Dialog(chat_room.this);
//
//        // connect layout xml
//        popupDialog.setContentView(R.layout.popup_dialog_key_use);
//
//        // background color of the dialog to transparent
//        Window window = popupDialog.getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(window.getAttributes());
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(layoutParams);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//
//        TextView pop_userName_textView = popupDialog.findViewById(R.id.user_name);
//
//        Button confirm_button = popupDialog.findViewById(R.id.confirm_button);
//        Button cancel_button = popupDialog.findViewById(R.id.cancel_button);
//
//        // setText
//        pop_userName_textView.setText(user_name);
//
//        // Button
//        // confirm_button : dismiss
//        confirm_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupDialogLack();
//                popupDialog.dismiss();      // close popup
//            }
//        });
//
//        // close_button : dismiss
//        cancel_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // close popup
//                popupDialog.dismiss();
//            }
//        });
//
//        popupDialog.show();     // start
//    }
//
//    private void showPopupDialogLack() {
//        // create dialog object
//        Dialog popupDialog = new Dialog(chat_room.this);
//
//        // connect layout xml
//        popupDialog.setContentView(R.layout.popup_dialog_two_button);
//
//        // background color of the dialog to transparent
//        Window window = popupDialog.getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(window.getAttributes());
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(layoutParams);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//
//        TextView textView = popupDialog.findViewById(R.id.textView);
//        Button cancel_button = popupDialog.findViewById(R.id.cancel_button);
//        Button confirm_button = popupDialog.findViewById(R.id.confirm_button);
//
//        textView.setText(R.string.popup_key_lack);
//        cancel_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupDialog.dismiss();
//            }
//        });
//        confirm_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupDialog.dismiss();
//                Intent intent = new Intent(chat_room.this, setting_key_purchase.class);
//                startActivity(intent);
//                onPause();
//
//            }
//        });
//
//        popupDialog.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILE_SELECT_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                // (파일전송 버튼 클릭 시) 선택된 파일에 대한 작업 수행 -> 채팅으로 파일 올리기(서버전송 등)
//            }
//        }
//    }
//
//}