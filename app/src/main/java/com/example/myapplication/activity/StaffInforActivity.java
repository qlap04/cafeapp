package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MyViewPagerAdapter2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StaffInforActivity extends AppCompatActivity {
    //
    private ImageView backBtn;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private MyViewPagerAdapter2 myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_staff_infor);
        backBtn = findViewById(R.id.backBtn);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        myViewPagerAdapter = new MyViewPagerAdapter2(this);
        mViewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("OFFLINE");
                    break;
                case 1:
                    tab.setText("ONLINE");
                    break;
            }
        }).attach();
        backBtn.setOnClickListener(v -> finish());
    }
}
