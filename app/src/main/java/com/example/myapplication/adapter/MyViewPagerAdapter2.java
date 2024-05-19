package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.CompleteFragment;
import com.example.myapplication.fragment.ProcessingFragment;
import com.example.myapplication.fragment.StaffOfflineFragment;
import com.example.myapplication.fragment.StaffOnlineFragment;

public class MyViewPagerAdapter2 extends FragmentStateAdapter {
    //

    public MyViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StaffOfflineFragment();
            case 1:
                return new StaffOnlineFragment();
            default:
                return new StaffOnlineFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
