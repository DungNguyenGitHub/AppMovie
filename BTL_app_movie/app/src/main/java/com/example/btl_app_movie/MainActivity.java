package com.example.btl_app_movie;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Khai bao bien
    private int id_user;
    private boolean isAdmin = false;
    private ViewPager mViewPager;
    private BottomNavigationView mNavigationView;

    private ViewPagerAdapter viewPagerAdapter;
    TextView toolbar;
    //===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra id user để hiển thị view phù hợp __ Admin ?

        /*
        // Tài khoản user realtime database
        // Lấy user hiện tại đã login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Lấy email
            String email = user.getEmail();
            // Kiểm tra email có là admin ? (email admin = "admin@gmail.com")
            if (email.compareTo("admin@gmail.com") == 0) {   // Admin -> Set Layout admin
                setContentView(R.layout.activity_admin);
                isAdmin = true;
            } else {   // User -> Set Layout main
                setContentView(R.layout.activity_main);
            }
        }
        */

        // Lấy id_user gửi từ loginActivity
        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");
        if (id_user == 0)
        {
            // Admin -> Set Layout admin
            setContentView(R.layout.activity_admin);
            isAdmin = true;
        }
        else {
            // User -> Set Layout main
            setContentView(R.layout.activity_main);
        }


        // Ánh xạ view
        mViewPager = findViewById(R.id.view_pager);
        mNavigationView = findViewById(R.id.bottom_nav);
        toolbar=findViewById(R.id.toolbarTieuDe);
        //toolbar.setText("Home");
        //===================================================
        setUpViewPager();
        //===================================================

        // Xử lí xự kiện click vào BottomNavigationView
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isAdmin) {
                    // Admin -> menu_bottom_nav_admin
                    switch (item.getItemId()) {
                        case R.id.mnuThem:// Click vào Add
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.mnuSua:// Click vào Update
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.mnuXoa:// Click vào Delete
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.mnuLogOut:// Click vào Logout
                            mViewPager.setCurrentItem(3);
                            break;
                    }
                } else {
                    // User -> menu_bottom_nav
                    switch (item.getItemId()) {
                        case R.id.action_home:// Click vào home
                            mViewPager.setCurrentItem(0);
                            toolbar.setText("Home");
                            break;
                        case R.id.action_favorite:// Click vào favorite
                            mViewPager.setCurrentItem(1);

                            toolbar.setText("Favorite");
                            break;
                        case R.id.action_history:// Click vào history
                            mViewPager.setCurrentItem(2);
                            toolbar.setText("History");
                            break;
                        case R.id.action_account:// Click vào account
                            mViewPager.setCurrentItem(3);
                            break;
                    }
                }

                return true;
            }
        });
        //===================================================
    }

    private void setUpViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, isAdmin, id_user);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // Xử lý sự kiện chuyển page của ViewPager -> set checked cho các MenuItem hiện tại
            @Override
            public void onPageSelected(int position) {
                if (isAdmin)
                {
                    // Admin
                    switch (position) {
                        case 0:
                            mNavigationView.getMenu().findItem(R.id.mnuThem).setChecked(true);
                            break;
                        case 1:
                            mNavigationView.getMenu().findItem(R.id.mnuSua).setChecked(true);
                            break;
                        case 2:
                            mNavigationView.getMenu().findItem(R.id.mnuXoa).setChecked(true);
                            break;
                        case 3:
                            mNavigationView.getMenu().findItem(R.id.mnuLogOut).setChecked(true);
                            break;
                    }
                }
                else
                {
                    // User
                    switch (position) {
                        case 0:
                            mNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                            toolbar.setText("Home");
                            break;
                        case 1:
                            mNavigationView.getMenu().findItem(R.id.action_favorite).setChecked(true);
                            toolbar.setText("Favorite");
                            break;
                        case 2:
                            mNavigationView.getMenu().findItem(R.id.action_history).setChecked(true);
                            toolbar.setText("History");
                            break;
                        case 3:
                            mNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public int getId_user(){ return id_user;}
}