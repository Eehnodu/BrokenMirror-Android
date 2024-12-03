package com.example.brokenmirror.ui.friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.FriendApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.room.FriendRoomDatabase;
import com.example.brokenmirror.sharedpref.FriendInfoPref;
import com.example.brokenmirror.sharedpref.UserSharedPref;
import com.example.brokenmirror.ui.BottomNavigation;
import com.example.brokenmirror.ui.setting.user_profile_other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class friendList extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(friendList.class);
    private FriendAdapter certifiedAdapter;
    private final List<FriendRoomDto> certifiedList = new ArrayList<>();
    private RecyclerView certified_recyclerView;
    private FriendAdapter uncertifiedAdapter;
    private final List<FriendRoomDto> uncertifiedList = new ArrayList<>();
    private RecyclerView uncertified_recyclerView;

    private final List<String> certifiedNameList = new ArrayList<>();
    private final List<String> uncertifiedNameList = new ArrayList<>();
    Map<String, Integer> certifiedIndexes = new HashMap<>();
    Map<String, Integer> uncertifiedIndexes = new HashMap<>();

    private TextView index_guide;
    private ConstraintLayout index_certified_layout;
    private ConstraintLayout index_uncertified_layout;
    private ConstraintLayout index_land_certified_layout;
    private ConstraintLayout index_land_uncertified_layout;

    List<FriendDto> friendInfoList;

    TextView certified_num;
    TextView friend_num;
    private NestedScrollView nestScrollView;

    CustomComparator customComparator = new CustomComparator();
    private final Handler handler = new Handler();
    String search = "";

    private FriendInfoPref friendInfoPref;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstandceState) {
        super.onCreate(savedInstandceState);
        setContentView(R.layout.activity_friend);

        friendInfoPref = new FriendInfoPref(this);

        // 인증된 친구 및 어댑터 설정
        certified_recyclerView = findViewById(R.id.ver_friend_recyclerView);
        certified_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        certifiedAdapter = new FriendAdapter(certifiedList);
        certified_recyclerView.setAdapter(certifiedAdapter);

        // 친구 및 어댑터 설정
        uncertified_recyclerView = findViewById(R.id.friend_recyclerView);
        uncertified_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uncertifiedAdapter = new FriendAdapter(uncertifiedList);
        uncertified_recyclerView.setAdapter(uncertifiedAdapter);

        nestScrollView = findViewById(R.id.nestedScrollView);
        index_certified_layout = findViewById(R.id.certified_index);
        index_uncertified_layout = findViewById(R.id.uncertified_index);
        index_land_certified_layout = findViewById(R.id.land_certified_index);
        index_land_uncertified_layout = findViewById(R.id.land_uncertified_index);
        index_guide = findViewById(R.id.friend_indexer_guid_textView);
        certified_num = findViewById(R.id.ver_division_num_textView);
        friend_num = findViewById(R.id.division_num_textView);

        View ver_fold_button = findViewById(R.id.friend_ver_fold_view);
        View fold_button = findViewById(R.id.friend_fold_view);

        ConstraintLayout header = findViewById(R.id.header);
        ConstraintLayout ver_div_layout = findViewById(R.id.ver_friend_division);
        ConstraintLayout div_layout = findViewById(R.id.friend_division);
        ConstraintLayout key_notice_layout = findViewById(R.id.notice);

        //bottomNavigationBar
        BottomNavigation bottomNavigation = new BottomNavigation(this, findViewById(R.id.friend_layout_parent));
        bottomNavigation.setupBottomNavigation();
        // BottomNavigationBar Color
        findViewById(R.id.nav_friend_icon).setBackgroundResource(R.drawable.bottom_navigation_icon_friend_emph);
        ((TextView) findViewById(R.id.nav_friend_text)).setTextColor(ContextCompat.getColor(this, R.color.main));

        // userPref
        // userSharedPref
        UserSharedPref user_pref = new UserSharedPref(this);
        UserDto user_info = user_pref.getUser();

        loadFriends();

        // ConstraintLayout
        // index_layout : OnTouchListener
        index_certified_layout.setOnTouchListener(new View.OnTouchListener() {
            boolean isSliding = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getY();
                int total_height = header.getHeight() + key_notice_layout.getHeight();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:       // touch start
                        isSliding = true;       // sliding check
                        visibleIndexGuide(y);
                        handleIndexScroll(certifiedIndexes, search, certified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_MOVE:       // touching(sliding)
                        if (isSliding) {
                            index_certified_layout.setVisibility(View.VISIBLE);
                            visibleIndexGuide(y);
                        }
                        handleIndexScroll(certifiedIndexes, search, certified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_UP:     // touch end
                        isSliding = false;      // sliding check
                        touchEnd(index_certified_layout);      // sleep in 1sec
                        break;
                }
                return true;
            }
        });

        index_uncertified_layout.setOnTouchListener(new View.OnTouchListener() {
            boolean isSliding = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getY();
                int total_height = key_notice_layout.getHeight() + findViewById(R.id.ver_friend_layout).getHeight() + div_layout.getHeight();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:       // touch start
                        isSliding = true;       // sliding check
                        visibleIndexGuide(y);
                        handleIndexScroll(uncertifiedIndexes, search, uncertified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_MOVE:       // touching(sliding)
                        if (isSliding) {
                            index_uncertified_layout.setVisibility(View.VISIBLE);
                            visibleIndexGuide(y);
                        }
                        handleIndexScroll(uncertifiedIndexes, search, uncertified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_UP:     // touch end
                        isSliding = false;      // sliding check
                        touchEnd(index_uncertified_layout);      // sleep in 1sec
                        break;
                }
                return true;
            }
        });

        // land_index_layout : OnTouchListener
        index_land_certified_layout.setOnTouchListener(new View.OnTouchListener() {
            boolean isSliding = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getY();
                int total_height = header.getHeight() + key_notice_layout.getHeight();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:       // 터치 시작
                        isSliding = true;       // 슬라이딩 체크
                        visibleIndexGuide(y);
                        handleIndexScroll(certifiedIndexes, search, certified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_MOVE:       // 터치 중
                        if (isSliding) {
                            index_land_certified_layout.setVisibility(View.VISIBLE);
                            visibleIndexGuide(y);
                        }
                        handleIndexScroll(certifiedIndexes, search, certified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_UP:     // 터치 종료
                        isSliding = false;      // 슬라이딩 체크 해제
                        touchEnd(index_land_certified_layout);     // 1초 후에 사라지게 설정
                        break;
                }
                return true;
            }
        });

        index_land_uncertified_layout.setOnTouchListener(new View.OnTouchListener() {
            boolean isSliding = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getY();
                int total_height = key_notice_layout.getHeight() + findViewById(R.id.ver_friend_layout).getHeight() + div_layout.getHeight();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:       // touch start
                        isSliding = true;       // sliding check
                        visibleIndexGuide(y);
                        handleIndexScroll(uncertifiedIndexes, search, uncertified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_MOVE:       // touching(sliding)
                        if (isSliding) {
                            index_land_uncertified_layout.setVisibility(View.VISIBLE);
                            visibleIndexGuide(y);
                        }
                        handleIndexScroll(uncertifiedIndexes, search, uncertified_recyclerView, total_height);
                        break;

                    case MotionEvent.ACTION_UP:     // touch end
                        isSliding = false;      // sliding check
                        touchEnd(index_land_uncertified_layout);      // sleep in 1sec
                        break;
                }
                return true;
            }
        });

        // Layout
        // ver_div_layout : OnClickListener (list fold func)
        ver_div_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (certified_recyclerView.getVisibility() == View.GONE) {
                    certified_recyclerView.setVisibility(View.VISIBLE);
                    ver_fold_button.setBackgroundResource(R.drawable.btn_up_arrow);
                } else {
                    certified_recyclerView.setVisibility(View.GONE);
                    ver_fold_button.setBackgroundResource(R.drawable.btn_down_arrow);
                }
            }
        });

        // div_layout : OnClickListener (list fold func)
        div_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uncertified_recyclerView.getVisibility() == View.GONE) {
                    uncertified_recyclerView.setVisibility(View.VISIBLE);
                    fold_button.setBackgroundResource(R.drawable.btn_up_arrow);
                } else {
                    uncertified_recyclerView.setVisibility(View.GONE);
                    fold_button.setBackgroundResource(R.drawable.btn_down_arrow);
                }
            }
        });

        // Nested ScrollView
        // nestScrollView : setOnScrollChangeListener
        nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                boolean isCertifiedView = selectVisibleView() == certified_recyclerView;
                if (isPortrait) {
                    index_certified_layout.setVisibility(isCertifiedView ? View.VISIBLE : View.GONE);
                    index_uncertified_layout.setVisibility(isCertifiedView ? View.GONE : View.VISIBLE);
                    index_land_certified_layout.setVisibility(View.GONE);
                    index_land_uncertified_layout.setVisibility(View.GONE);
                } else {
                    index_certified_layout.setVisibility(View.GONE);
                    index_uncertified_layout.setVisibility(View.GONE);
                    index_land_certified_layout.setVisibility(isCertifiedView ? View.VISIBLE : View.GONE);
                    index_land_uncertified_layout.setVisibility(isCertifiedView ? View.GONE : View.VISIBLE);
                }
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                        fadeOut.setDuration(200);
                        ConstraintLayout result_layout = getLayout();
                        result_layout.startAnimation(fadeOut);
                        result_layout.setVisibility(View.GONE);
                        index_guide.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });

        // 인증된 친구 프로필 클릭
        certified_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {
                    int position = rv.getChildAdapterPosition(childView);
                    if (position != RecyclerView.NO_POSITION) {
                        FriendRoomDto friend = ((FriendAdapter) Objects.requireNonNull(rv.getAdapter())).getFriendAtPosition(position);
                        Intent intent = new Intent(friendList.this, user_profile_other.class);
                        friendInfoPref.putFriendInfo(friend);
                        startActivity(intent);
                        onPause();
                    } else {
                        Log.d("RecyclerView Click", "NO POSITION");
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // 비인증된 친구 프로필 클릭
        uncertified_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {
                    int position = rv.getChildAdapterPosition(childView);
                    if (position != RecyclerView.NO_POSITION) {
                        FriendRoomDto friend = ((FriendAdapter) Objects.requireNonNull(rv.getAdapter())).getFriendAtPosition(position);
                        Intent intent = new Intent(friendList.this, user_profile_other.class);
                        friendInfoPref.putFriendInfo(friend);
                        startActivity(intent);
                        onPause();
                    } else {
                        Log.d("RecyclerView Click", "NO POSITION");
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    // getConstraintLayout
    private ConstraintLayout getLayout() {
        boolean isCertified = selectVisibleView() == certified_recyclerView;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return isCertified ? index_certified_layout : index_uncertified_layout;
        } else {  // Landscape
            return isCertified ? index_land_certified_layout : index_land_uncertified_layout;
        }
    }

    // touch end
    public void touchEnd(ConstraintLayout constraintLayout) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {     // 애니메이션
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);        // 투명도 변화
                fadeOut.setDuration(200);       // 애니메이션 지속 시간
                constraintLayout.startAnimation(fadeOut);
                index_guide.startAnimation(fadeOut);
                constraintLayout.setVisibility(View.GONE);
                index_guide.setVisibility(View.GONE);
            }
        }, 1000);
    }

    // 인덱스 맵과 검색값을 받아서, 해당 위치로 스크롤하는 함수
    private void handleIndexScroll(Map<String, Integer> indexes, String search, RecyclerView recyclerView, int totalHeight) {
        if (indexes.containsKey(search)) {
            Integer value = indexes.get(search);
            if (value != null && value >= 0 && value < uncertifiedNameList.size()) {
                scrollView(recyclerView, value, totalHeight);
            }
        }
    }

    // visible Index Guide
    public void visibleIndexGuide(float y) {
        index_guide.setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            search = calculateSection(y);
        } else {
            search = calculateLandSection(y);
        }
        index_guide.setText(search);
    }

    // Scroll to TargetView
    public void scrollView(RecyclerView recyclerView, Integer value, int total_height) {
        View targetView = recyclerView.getLayoutManager().findViewByPosition(value);
        int item_height = targetView.getTop();
        nestScrollView.scrollTo(0, total_height + item_height);
    }

    // 화면의 영역 계산
    private RecyclerView selectVisibleView() {
        Rect certifiedRect = new Rect();
        Rect nucertifiedRect = new Rect();

        certified_recyclerView.getGlobalVisibleRect(certifiedRect);
        uncertified_recyclerView.getGlobalVisibleRect(nucertifiedRect);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // certified_view와 nocertified_view의 보이는 영역 높이 계산
        int certified_visibleHeight = Math.min(certifiedRect.bottom, screenHeight) - Math.max(certifiedRect.top, 0);
        int uncertified_visibleHeight = Math.min(nucertifiedRect.bottom, screenHeight) - Math.max(nucertifiedRect.top, 0);

        return (certified_visibleHeight > uncertified_visibleHeight) ? certified_recyclerView : uncertified_recyclerView;
    }

    // IndexSelector(세로)
    private String calculateSection(float y) {
        // height of index_layout
        int indexHeight = findViewById(R.id.certified_index).getHeight();
        // height of 1 section
        int sectionHeight = indexHeight / 21;
        // transform y axis to section
        int section = (int) (y / sectionHeight);
        // 'ㄱ'위로 스크롤 시 '#'으로의 이동 방지 코드
        section = Math.max(section, 0);
        section = Math.min(section, 20);
        // transform section to sectionLetter

        String sectionLetter;
        switch (section) {
            case 0:
                sectionLetter = getString(R.string.friend_section_0);
                break;
            case 1:
                sectionLetter = getString(R.string.friend_section_1);
                break;
            case 2:
                sectionLetter = getString(R.string.friend_section_2);
                break;
            case 3:
                sectionLetter = getString(R.string.friend_section_3);
                break;
            case 4:
                sectionLetter = getString(R.string.friend_section_4);
                break;
            case 5:
                sectionLetter = getString(R.string.friend_section_5);
                break;
            case 6:
                sectionLetter = getString(R.string.friend_section_6);
                break;
            case 7:
                sectionLetter = getString(R.string.friend_section_7);
                break;
            case 8:
                sectionLetter = getString(R.string.friend_section_8);
                break;
            case 9:
                sectionLetter = getString(R.string.friend_section_9);
                break;
            case 10:
                sectionLetter = getString(R.string.friend_section_10);
                break;
            case 11:
                sectionLetter = getString(R.string.friend_section_11);
                break;
            case 12:
                sectionLetter = getString(R.string.friend_section_12);
                break;
            case 13:
                sectionLetter = getString(R.string.friend_section_13);
                break;
            case 14:
                sectionLetter = getString(R.string.friend_section_14);
                break;
            case 15:
                sectionLetter = getString(R.string.friend_section_15);
                break;
            case 16:
                sectionLetter = getString(R.string.friend_section_16);
                break;
            case 17:
                sectionLetter = getString(R.string.friend_section_17);
                break;
            case 18:
                sectionLetter = getString(R.string.friend_section_18);
                break;
            case 19:
                sectionLetter = getString(R.string.friend_section_19);
                break;
            default:
                sectionLetter = getString(R.string.friend_section_20);
                break;
        }
        return sectionLetter;
    }

    // NEW
    // IndexSelector(가로)
    private String calculateLandSection(float y) {
        // height of index_layout
        int indexHeight = findViewById(R.id.land_certified_index).getHeight();
        // height of 1 section
        int sectionHeight = indexHeight / 8;
        // transform y axis to section
        int section = (int) (y / sectionHeight);
        // 'ㄱ'위로 스크롤 시 '#'으로의 이동 방지 코드
        section = Math.max(section, 0);
        section = Math.min(section, 7);
        // transform section to sectionLetter
        String sectionLetter;
        switch (section) {
            case 0:
                sectionLetter = getString(R.string.friend_section_0);
                break;
            case 1:
                sectionLetter = getString(R.string.friend_section_2);
                break;
            case 2:
                sectionLetter = getString(R.string.friend_section_4);
                break;
            case 3:
                sectionLetter = getString(R.string.friend_section_6);
                break;
            case 4:
                sectionLetter = getString(R.string.friend_section_8);
                break;
            case 5:
                sectionLetter = getString(R.string.friend_section_10);
                break;
            case 6:
                sectionLetter = getString(R.string.friend_section_12);
                break;
            default:
                sectionLetter = getString(R.string.friend_section_14);
                break;
        }
        return sectionLetter;
    }

    // callback interface
    public interface OnFriendInfoLoaded {
        void onFriendInfoLoaded(List<FriendRoomDto> roomList);
    }

    // room에서 모든 친구 데이터 가져오기
    public void loadFriends() {
        FriendRoomDatabase db = FriendRoomDatabase.getInstance(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // getAll (친구 정보 조회)
                List<FriendRoomDto> friendRoomList = db.friendRoomDao().getAll();
                updateRecyclerView(friendRoomList); // UI 업데이트
            }
        }).start();
    }

    // 새로 가져온 데이터 adapter에 추가
    private void updateRecyclerView(List<FriendRoomDto> friendRoomList) {
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                for (FriendRoomDto friend : friendRoomList) {
                    (friend.getKey() == 2 ? certifiedList : uncertifiedList).add(friend);
                }
                // 인증 비인증 구분
                certifiedList.sort((f1, f2) -> customComparator.compare(f1, f2));
                uncertifiedList.sort((f1, f2) -> customComparator.compare(f1, f2));
                for (FriendRoomDto dto : certifiedList) {
                    certifiedNameList.add(dto.getUserName());
                }
                for (FriendRoomDto dto : uncertifiedList) {
                    uncertifiedNameList.add(dto.getUserName());
                }

                certifiedAdapter.notifyDataSetChanged();
                uncertifiedAdapter.notifyDataSetChanged();

                certifiedIndexes = customComparator.getSectionIndexes(new ArrayList<>(certifiedList));
                uncertifiedIndexes = customComparator.getSectionIndexes(new ArrayList<>(uncertifiedList));

                certified_num.setText(String.valueOf(certifiedList.size()));
                friend_num.setText(String.valueOf(uncertifiedList.size()));
            }
        });
    }
}
