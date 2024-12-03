package com.example.brokenmirror.ui.notification;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.ui.BottomNavigation;
import com.example.brokenmirror.R;

import java.util.ArrayList;
import java.util.List;

public class notification_main extends AppCompatActivity {

    private HorizontalScrollView subMenuScroll;
    private NestedScrollView notificationScrollView;
    private RecyclerView recyclerView;
    private notification_main_adapter adapter;

    private final int submenu_all = R.id.notification_submenu_all;
    private final int submenu_img = R.id.notification_submenu_img;
    private final int submenu_add_friends = R.id.notification_submenu_add_friends;
    private final int submenu_buy_key = R.id.notification_submenu_buy_key;

    private TextView firstBtn;
    private TextView secondBtn;
    private TextView thirdBtn;
    private TextView fourthBtn;

    private GestureDetector gestureDetector;
    private int[] submenuBtnIds = {submenu_all, submenu_img, submenu_add_friends, submenu_buy_key};
    private int currentSubMenuIndex = 0;  // 서브 메뉴의 현재 인덱스 번호를 추적하는 변수

    // 로그인 후 유저의 정보
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

        id = getIntent().getStringExtra("id");

        subMenuScroll = findViewById(R.id.subMenu_Hscroll);
        firstBtn = findViewById(submenu_all);
        secondBtn = findViewById(submenu_img);
        thirdBtn = findViewById(submenu_add_friends);
        fourthBtn = findViewById(submenu_buy_key);

        notificationScrollView = findViewById(R.id.notification_scrollView);

        recyclerView = findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new notification_main_adapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        adapter.setData(generateUserData());

        findViewById(R.id.notification_submenu_all).performClick();

        setSubmenuWidth();

        initSubMenuBtn();

        handleSubMenuBtnClick(submenuBtnIds[currentSubMenuIndex]);  // 초기 선택된 서브메뉴 설정

        int id = getIntent().getIntExtra("menu", -1);
        if(id != -1){
            handleSubMenuBtnClick(id);
        }

        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        //bottomNavigationBar
        // 로그인 후 유저의 id
        BottomNavigation bottomNavigation = new BottomNavigation(this, findViewById(R.id.notification_layout));
        bottomNavigation.setupBottomNavigation();

        // BottomNavigationBar Color
        findViewById(R.id.nav_noti_icon).setBackgroundResource(R.drawable.bottom_navigation_icon_bell_emph);
        ((TextView) findViewById(R.id.nav_noti_text)).setTextColor(ContextCompat.getColor(this, R.color.main));

    } //onCreate


    // Adjust Submenu Btn Width (screen is rotated)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSubmenuWidth();
    }

    private void setSubmenuWidth() {
        int orientation = getResources().getConfiguration().orientation;
        int width = orientation == Configuration.ORIENTATION_LANDSCAPE ?
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT : ConstraintLayout.LayoutParams.WRAP_CONTENT;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) findViewById(R.id.menu_layout));
        constraintSet.constrainWidth(firstBtn.getId(), width);
        constraintSet.constrainWidth(secondBtn.getId(), width);
        constraintSet.constrainWidth(thirdBtn.getId(), width);
        constraintSet.constrainWidth(fourthBtn.getId(), width);
        constraintSet.applyTo(findViewById(R.id.menu_layout));
    }

    // Start Swipe Code
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 200;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    // 왼쪽으로 스와이프 (이전 서브메뉴)
                    showPreSubMenu();
                } else {
                    // 오른쪽으로 스와이프 (다음 서브메뉴)
                    showNextSubMenu();
                }
                return true;
            }
            return false;
        }
    }

    private void showPreSubMenu() {
        // 현재 선택된 서브메뉴의 이전 서브메뉴로 이동하는 로직
        int preButtonId = getPreSubMenuBtnId(getCurrentSubMenuBtnId());
        handleSubMenuBtnClick(preButtonId);
        scrollToSelectedSubMenu(preButtonId);
        notificationScrollView.smoothScrollTo(0, 0);
    }

    private void showNextSubMenu() {
        // 현재 선택된 서브메뉴의 다음 서브메뉴로 이동하는 로직
        int nextButtonId = getNextSubMenuBtnId(getCurrentSubMenuBtnId());
        handleSubMenuBtnClick(nextButtonId);
        scrollToSelectedSubMenu(nextButtonId);
        notificationScrollView.smoothScrollTo(0, 0);
    }

    private void updateCurrentSubMenuIndex(int newSubMenuBtnId) {
        // 새로운 서브메뉴 버튼의 ID를 기반으로 현재 서브메뉴의 인덱스 갱신
        currentSubMenuIndex = getSubMenuBtnIndex(newSubMenuBtnId);
    }

    // Auto-scroll Submenu
    private void scrollToSelectedSubMenu(int buttonId) {

        int allButtonRightEnd = firstBtn.getLeft() + firstBtn.getWidth();
        int imgButtonRightEnd = secondBtn.getLeft() + secondBtn.getWidth();
        int addButtonRightEnd = thirdBtn.getLeft() + thirdBtn.getWidth();
        int buyButtonRightEnd = fourthBtn.getLeft() + fourthBtn.getWidth();
        int buttonRightEnd = 0;

        if (buttonId == submenu_all) {
            buttonRightEnd = allButtonRightEnd;
        } else if (buttonId == submenu_img) {
            buttonRightEnd = imgButtonRightEnd;
        } else if (buttonId == submenu_add_friends) {
            buttonRightEnd = addButtonRightEnd;
        } else if (buttonId == submenu_buy_key) {
            buttonRightEnd = buyButtonRightEnd;
        }

        if (buttonRightEnd > subMenuScroll.getWidth()) {
            //int scrollRight = buttonRightEnd - subMenuScroll.getWidth();
            int scrollRight = buttonRightEnd - subMenuScroll.getWidth() + fourthBtn.getWidth();
            subMenuScroll.smoothScrollBy(scrollRight, 0);
        } else {

            int buttonLeftEnd = 0;

            if (buttonId == submenu_all) {
                buttonLeftEnd = firstBtn.getLeft();
            } else if (buttonId == submenu_img) {
                buttonLeftEnd = secondBtn.getLeft();
            } else if (buttonId == submenu_add_friends) {
                buttonLeftEnd = thirdBtn.getLeft();
            } else if (buttonId == submenu_buy_key) {
                buttonLeftEnd = fourthBtn.getLeft();
            }

            if (buttonLeftEnd < subMenuScroll.getWidth()) {
                int scrollLeft = subMenuScroll.getWidth() - buttonLeftEnd;
                subMenuScroll.smoothScrollBy(-scrollLeft, 0);
            }
        }
    }

    private int getCurrentSubMenuBtnId() {
        // 현재 선택된 서브메뉴 버튼 ID 반환
        return submenuBtnIds[currentSubMenuIndex];
    }

    private int getPreSubMenuBtnId(int currentBtnId) {
        // 이전 서브메뉴 버튼 ID 계산하여 반환
        int currentIndex = getSubMenuBtnIndex(currentBtnId);
        int preIndex = (currentIndex - 1 + submenuBtnIds.length) % submenuBtnIds.length;
        return submenuBtnIds[preIndex];
    }

    private int getNextSubMenuBtnId(int currentBtnId) {
        // 다음 서브메뉴 버튼 ID 계산하여 반환
        int currentIndex = getSubMenuBtnIndex(currentBtnId);
        int nextIndex = (currentIndex + 1) % submenuBtnIds.length;
        return submenuBtnIds[nextIndex];
    }

    private int getSubMenuBtnIndex(int btnId) {
        for (int i = 0; i < submenuBtnIds.length; i++) {
            if (submenuBtnIds[i] == btnId) {
                return i;
            }
        }
        return -1; // 버튼 ID를 찾지 못한 경우
    }
    // Finish Swipe Code

    private void initSubMenuBtn() {
        for (final int buttonId : submenuBtnIds) {
            TextView SubMenuBtn = findViewById(buttonId);
            SubMenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSubMenuBtnClick(buttonId);
                }
            });
        }
    }

    private void handleSubMenuBtnClick(int buttonId) {
        // 서브 메뉴 버튼을 클릭할 때 서브 메뉴 인덱스를 올바르게 설정하는 로직
        updateCurrentSubMenuIndex(buttonId);

        List<notification_main_adapter.NotificationItem> newData = generateSubMenuData(buttonId);
        adapter.setData(newData);
        adapter.notifyDataSetChanged();

        for (int SubMenuBtnId : submenuBtnIds) {
            TextView SubMenuBtn = findViewById(SubMenuBtnId);
            SubMenuBtn.setBackgroundResource(R.drawable.btn_round_grey);
            SubMenuBtn.setTextColor(getResources().getColor(R.color.black));
        }

        TextView currentButton = findViewById(buttonId);
        currentButton.setBackgroundResource(R.drawable.btn_round_main);
        currentButton.setTextColor(getResources().getColor(R.color.white));
    }

    private List<notification_main_adapter.NotificationItem> generateSubMenuData(int buttonId) {
        List<notification_main_adapter.NotificationItem> subMenuData = new ArrayList<>();
        if (buttonId == submenu_all) {
            subMenuData.addAll(generateUserData());
            subMenuData.addAll(generateAddFriendsData());
            subMenuData.addAll(generateBuyKeyData());
            //subMenuData.addAll(generateNoticeData());
        } else if (buttonId == submenu_img) {
            subMenuData = generateUserData();
        } else if (buttonId == submenu_add_friends) {
            subMenuData = generateAddFriendsData();
        } else if (buttonId == submenu_buy_key) {
            subMenuData = generateBuyKeyData();
//        } else if (buttonId == submenu_notice) {
//            subMenuData = generateNoticeData();
//        }
        }
        return subMenuData;
    }

    private List<notification_main_adapter.NotificationItem> generateUserData() {
        List<notification_main_adapter.NotificationItem> userData = new ArrayList<>();
        String[] senderNames = {"은수", "4이명훈", "이승헌", "정수연", "정재우", "조은서", "타나"};

        for (String senderName : senderNames) {
            // NotificationItem 객체 생성 및 데이터 설정
            notification_main_adapter.NotificationItem item = new notification_main_adapter.NotificationItem(
                    getString(R.string.noti_submenu_img_title) + senderName + getString(R.string.noti_submenu_img_message),
                    R.drawable.notification_navy_logo
            );
            userData.add(item);
        }
        return userData;
    }

    private List<notification_main_adapter.NotificationItem> generateAddFriendsData() {
        List<notification_main_adapter.NotificationItem> addFriendsData = new ArrayList<>();
        String[] friendNames = {"강예진", "권하형", "1기용", "house", "김민정", "2김선호", "jellyfish", "김수민"};

        for (String friendName : friendNames) {
            notification_main_adapter.NotificationItem item = new notification_main_adapter.NotificationItem(
                    getString(R.string.noti_submenu_add_friend_title) + friendName + getString(R.string.noti_submenu_add_friend_message), R.drawable.notification_navy_logo);
            //리스트에 객체 추가
            addFriendsData.add(item);
        }
        return addFriendsData;
    }

    private List<notification_main_adapter.NotificationItem> generateBuyKeyData() {
        List<notification_main_adapter.NotificationItem> buyKeyData = new ArrayList<>();
        int[] keyQuantities = {10, 10, 20, 10, 40, 20, 20};

        for (int quantity : keyQuantities) {
            notification_main_adapter.NotificationItem item = new notification_main_adapter.NotificationItem(
                    getString(R.string.noti_submenu_buy_key_title) + quantity + getString(R.string.noti_submenu_buy_key_message), R.drawable.notification_navy_logo);

            buyKeyData.add(item);
        }
        return buyKeyData;
    }

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