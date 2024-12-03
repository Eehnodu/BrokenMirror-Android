package com.example.brokenmirror.ui.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.brokenmirror.R;

import java.util.Locale;

public class setting_add_friend extends AppCompatActivity {

    EditText name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_add_friend);

        ConstraintLayout parent_layout_setting_add_friend = findViewById(R.id.parent_layout_setting_add_friend);
        Button cancel_button = findViewById(R.id.cancel_button);
        Button done_button = findViewById(R.id.done_button);
        Button delete_name = findViewById(R.id.delete_name);
        Button delete_phone_number = findViewById(R.id.delete_phone_number);
        TextView count_name = findViewById(R.id.count_name);
        name = findViewById(R.id.name);
        EditText friend_phone_number = findViewById(R.id.friend_phone_number);

        // Cancel Button
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Done Button
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(setting_add_friend.this, "미구현", Toast.LENGTH_SHORT).show();

                // 이름이 공백인지 확인
                String nameText = name.getText().toString().trim();
                if (nameText.isEmpty()) {
                    // 경고창 표시
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting_add_friend.this);
                    builder.setMessage(getString(R.string.no_blank));
                    builder.setPositiveButton(getString(R.string.find_id_result_null_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }

            }
        });

        // Name -> Delete Button
        delete_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                updateCharacterCount(0, count_name);
            }
        });

        // Phone Number -> Delete Button
        delete_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend_phone_number.setText("");
            }
        });

        // EditText의 텍스트 변경 감지 리스너
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 텍스트가 변경될 때 호출됨
                updateCharacterCount(charSequence.length(), count_name);

                // 추가: 20글자가 넘어가면 입력 방지
                if (charSequence.length() > 20) {
                    String limitedText = charSequence.subSequence(0, 20).toString();
                    name.setText(limitedText);
                    name.setSelection(20);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        parent_layout_setting_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트 변경 전에 호출되는 메서드
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트 변경될 때 호출되는 메서드
                String nameText = name.getText().toString().trim();
                String phoneNumberText = friend_phone_number.getText().toString().trim();

                // 둘 중 하나라도 비어 있으면 done_button을 비활성화
                done_button.setEnabled(!nameText.isEmpty() && !phoneNumberText.isEmpty());
                done_button.setTextColor(done_button.isEnabled() ? Color.WHITE : Color.GRAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 변경 후에 호출되는 메서드
            }
        };

        name.addTextChangedListener(textWatcher);
        friend_phone_number.addTextChangedListener(textWatcher);

    }  // onCreate

    // 글자 수 업데이트 메소드
    private void updateCharacterCount(int currentLength, TextView countTextView) {
        // 최대 글자 수
        int maxLength = 20;

        // 현재 글자 수 표시
        countTextView.setText(String.format(Locale.getDefault(), "%d/%d", currentLength, maxLength));
    }

    // 소프트 키보드를 감추는 메서드
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
