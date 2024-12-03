package com.example.brokenmirror;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class androidTestWebSocketClient {
    private Context context;
    private WebSocket webSocket;
    private TextView myChatTextView;
    private TextView otherChatTextView;
    private EditText chatInputEditText;
    private Button sendButton;

    // 생성자를 통해 Context를 전달받아 저장
    public androidTestWebSocketClient(TextView chat1, EditText chat2) {
        this.context = context;
        this.myChatTextView = myChatTextView;
        this.otherChatTextView = otherChatTextView;
        this.chatInputEditText = chatInputEditText;
        this.sendButton = sendButton;
    }

    // WebSocket 연결
    public void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://localhost:8080/chat") // WebSocket 엔드포인트 URL
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // WebSocket 연결이 열린 후 실행되는 코드
                showToast("WebSocket 연결이 열렸습니다!");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 서버로부터 메시지를 받았을 때 실행되는 코드
                otherChatTextView.append("상대방: " + text + "\n");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket 연결이 닫힌 후 실행되는 코드
                showToast("WebSocket 연결이 닫혔습니다: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // 연결이 실패했을 때 실행되는 코드
                showToast("WebSocket 연결이 실패했습니다: " + t.getMessage());
            }
        };

        webSocket = client.newWebSocket(request, listener);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatInputEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                }
            }
        });
    }

    // 메시지 전송
    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message); // 메시지를 서버로 전송
            myChatTextView.append("나: " + message + "\n"); // 자신이 보낸 메시지를 TextView에 표시
            chatInputEditText.setText(""); // EditText를 비움
        } else {
            showToast("WebSocket 연결이 되어있지 않습니다.");
        }
    }

    // 토스트 메시지를 표시하는 메서드
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}