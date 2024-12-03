/*
__author__ = 'Song Chae Young'
__date__ = 'Sep.05, 2023'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'MainActivity.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Production'
*/

package com.example.brokenmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import com.example.brokenmirror.ui.setting.login_main;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(MainActivity.this, first_access.class);zs
//        startActivity(intent);
//        finish();
//    }

    // test
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(MainActivity.this, chat_main.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, login_main.class);
        startActivity(intent);
        finish();
    }
}