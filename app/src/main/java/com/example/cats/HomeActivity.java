package com.example.cats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cats.adapter.HomeViewPagerAdapter;
import com.example.cats.fragment.ChatFragment;
import com.example.cats.fragment.DiscoverFragment;
import com.example.cats.fragment.ProfileFragment;
import com.example.cats.fragment.SignInFragment;
import com.example.cats.fragment.SignUpFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mHomeTabs;
    private HomeViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = findViewById(R.id.homeview_pager);
        mHomeTabs = findViewById(R.id.home_tab);
        adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new DiscoverFragment(), "Discover");
        adapter.addFragment(new ChatFragment(), "Chat");

        mViewPager.setAdapter(adapter);
        mHomeTabs.setupWithViewPager(mViewPager);

        mHomeTabs.getTabAt(0).setIcon(R.drawable.profile);
        mHomeTabs.getTabAt(1).setIcon(R.drawable.discover);
        mHomeTabs.getTabAt(2).setIcon(R.drawable.chat);

        mHomeTabs.selectTab(mHomeTabs.getTabAt(1));

        mHomeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}