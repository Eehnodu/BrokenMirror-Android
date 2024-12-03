/*
__author__ = 'Song Chae Young'
__date__ = 'Jan.30, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'join_member_success.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.friend.friendList;

public class join_member_success extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_member_success);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        TextView textView = findViewById(R.id.text);
        Button start_button = findViewById(R.id.start_button);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(join_member_success.this, friendList.class);
                startActivity(intent);
                finish();
            }
        });

        textView.setText(name + getString(R.string.join_member_success_notice0) + "\n" +
                email + getString(R.string.join_member_success_notice1) + "\n" +
                getString(R.string.join_member_success_notice2));

    }
    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.join_member_success_please), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }
}
