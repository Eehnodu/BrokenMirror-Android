/*
__author__ = 'Song Chae Young'
__date__ = 'Jan.30, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'join_agreement_detail.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.GlobalVariables;
import com.example.brokenmirror.R;

import java.util.List;

public class join_agreement_detail extends AppCompatActivity {

    Button agree_button;
    int contextCode;
    ActivityManager activityManager;
    List<ActivityManager.AppTask> appTasks;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_agreement_detail);

        TextView textView = findViewById(R.id.textView);
        TextView title_textView = findViewById(R.id.title_textView);

        Button close_button = findViewById(R.id.close_button);
        agree_button = findViewById(R.id.agree_button);

        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        appTasks = activityManager.getAppTasks();

        String title_100 = getString(R.string.join_agree_agreement_0);
        String title_200 = getString(R.string.join_agree_agreement_1);
        String title_300 = getString(R.string.join_agree_agreement_2);
        String title_400 = getString(R.string.join_agree_agreement_3);
        String title_500 = getString(R.string.join_agree_agreement_4);

        title_textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title_textView.setSelected(true);

        // get Intent
        Intent get_intent = getIntent();
        if (get_intent != null) {
            contextCode = get_intent.getIntExtra("contextCode", -1);
            if (contextCode == 100) {
                textView.setText(R.string.join_agree_det_context_0);
                title_textView.setText(title_100);
                agreeButtonClickListener();
            }
            if (contextCode == 200) {
                textView.setText(R.string.join_agree_det_context_1);
                title_textView.setText(title_200);
                agreeButtonClickListener();
            }
            if (contextCode == 300) {
                textView.setText(R.string.join_agree_det_context_2);
                title_textView.setText(title_300);
                agreeButtonClickListener();
            }
            if (contextCode == 400) {
                textView.setText(R.string.join_agree_det_context_3);
                title_textView.setText(title_400);
                agreeButtonClickListener();
            }
            if (contextCode == 500) {
                textView.setText(R.string.join_agree_det_context_4);
                title_textView.setText(title_500);
                agreeButtonClickListener();
            }
        }

        // Button
        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(join_agreement_detail.this, join_agreement.class);
                startActivity(intent);
                finish();
            }
        });
    }   // onCreate

    private void agreeButtonClickListener() {
        agree_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미 동의된 항목들은 건너뛰고, 해제된 항목들만 처리
                if (!GlobalVariables.JOIN_AGREEMENT_LIST_STATE[contextCode / 100 - 1]) {
                    // 현재 항목에 대해 동의가 해제된 경우만 처리
                    GlobalVariables.JOIN_AGREEMENT_LIST_STATE[contextCode / 100 - 1] = true;
                }
                // 다음 항목으로 이동
                int newContextCode = contextCode + 100;
                // 해제된 항목만 다시 처리, 이미 true인 항목들은 건너뛰기
                while (newContextCode <= 400 && GlobalVariables.JOIN_AGREEMENT_LIST_STATE[newContextCode / 100 - 1]) {
                    newContextCode += 100;
                }
                // 아직 처리해야 할 동의 항목이 남아있으면 그 항목으로 이동
                if (newContextCode <= 400) {
                    Intent intent = new Intent(join_agreement_detail.this, join_agreement_detail.class);
                    intent.putExtra("contextCode", newContextCode);
                    finish();
                    startActivity(intent);
                } else {
                    // 모든 동의가 완료된 경우
                    Intent intent = new Intent(join_agreement_detail.this, join_agreement.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}   // join_agreement_detail.java
