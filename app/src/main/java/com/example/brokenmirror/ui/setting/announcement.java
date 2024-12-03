/*
__author__ = 'Song Chae Young, Jung Chae Lin'
__date__ = 'Feb.27, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_main.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;

public class announcement extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement);

        Button back_button = findViewById(R.id.back_button);
        Button close_button = findViewById(R.id.close_button);

        WebView webView = findViewById(R.id.webView);

        String url = "file:///android_asset/announcement.html";

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);

        webView.loadUrl(url);


        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // close_button : OnClickListener
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
