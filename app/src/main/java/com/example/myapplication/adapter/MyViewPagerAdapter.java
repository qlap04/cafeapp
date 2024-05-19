package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.CompleteFragment;
import com.example.myapplication.fragment.ProcessingFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    //

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProcessingFragment();
            case 1:
                return new CompleteFragment();
            default:
                return new ProcessingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
