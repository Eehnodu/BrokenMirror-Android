/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.22, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'service_guide_ViewPagerAdapter_top.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.brokenmirror.R;

public class service_guide_ViewPagerAdapter_top extends FragmentStateAdapter {

    public service_guide_ViewPagerAdapter_top(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new service_guide_top_0();
            case 1:
                return new service_guide_top_1();
            case 2:
                return new service_guide_top_2();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class service_guide_top_0 extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.service_guide_top_0, container, false);
        }
    }

    public static class service_guide_top_1 extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.service_guide_top_1, container, false);
        }
    }

    public static class service_guide_top_2 extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.service_guide_top_2, container, false);
        }
    }
}
