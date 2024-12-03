/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.22, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'service_guide.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.brokenmirror.R;

import me.relex.circleindicator.CircleIndicator3;

public class service_guide extends AppCompatActivity {
    NestedScrollView nestedScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_guide);

        ViewPager2 viewPagerTop = findViewById(R.id.viewPager_top);
        ViewPager2 viewPagerBottom = findViewById(R.id.viewPager_bottom);

        CircleIndicator3 indicator = findViewById(R.id.indicator);

        nestedScrollView = findViewById(R.id.nestedScrollView);

        Button next_button = findViewById(R.id.next_button);
        Button close_button = findViewById(R.id.close_button);

        // Top ViewPager
        service_guide_ViewPagerAdapter_top adapterTop = new service_guide_ViewPagerAdapter_top(this);
        viewPagerTop.setAdapter(adapterTop);
        viewPagerTop.registerOnPageChangeCallback(new PageChangeCallback(next_button, this, viewPagerTop, viewPagerBottom, nestedScrollView));
        viewPagerTop.setUserInputEnabled(false);

        // Bottom ViewPager
        service_guide_ViewPagerAdapter_bottom adapterBottom = new service_guide_ViewPagerAdapter_bottom(this);
        viewPagerBottom.setAdapter(adapterBottom);
        viewPagerBottom.registerOnPageChangeCallback(new PageChangeCallback(next_button, this, viewPagerBottom, viewPagerTop, nestedScrollView));
        viewPagerBottom.setUserInputEnabled(false);


        // CircleIndicator3
        indicator.setViewPager(viewPagerTop);


        // Button
        // close_button : finish
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }   // onCreate

    private boolean backButtonPressedOnce = false;
    private Handler handler = new Handler();
    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds
    @Override
    public void onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed(); // 두 번째 뒤로가기 버튼 눌림
            Log.d("ghkrdls", "Activity destroyed");
            handler.removeCallbacksAndMessages(null); // 핸들러 메시지 제거
        } else {
            if (isTaskRoot()) {
                backButtonPressedOnce = true; // 첫 번째 뒤로가기 버튼 눌림
                Toast.makeText(this, getResources().getText(R.string.app_exit), Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backButtonPressedOnce = false; // 2초가 지나면 초기화
                    }
                }, DOUBLE_BACK_PRESS_INTERVAL);
            } else {
                super.onBackPressed(); // 루트가 아닐 때
            }
        }
    }
}

class PageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private Button button;
    private Context context;
    private ViewPager2 viewpager;
    private ViewPager2 otherViewPager;
    private NestedScrollView nestedScrollView;

    public PageChangeCallback(Button button, Context context, ViewPager2 viewpager, ViewPager2 otherViewPager, NestedScrollView nestedScrollView) {
        this.button = button;
        this.context = context;
        this.viewpager = viewpager;
        this.otherViewPager = otherViewPager;
        this.nestedScrollView = nestedScrollView;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        nestedScrollView.scrollTo(0, 0);

        if (position == 2) {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.main));
            button.setTextColor(ContextCompat.getColor(context, R.color.white));
            button.setText(R.string.find_id_result_null_confirm);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, login_main.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });
        } else {
            button.setBackgroundResource(R.drawable.login_main_loginbtn);
            button.setTextColor(ContextCompat.getColor(context, R.color.main));
            button.setText(R.string.find_id_next);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewpager.setCurrentItem(position + 1);
                    otherViewPager.setCurrentItem(position + 1);
                }
            });
        }
    }
}
