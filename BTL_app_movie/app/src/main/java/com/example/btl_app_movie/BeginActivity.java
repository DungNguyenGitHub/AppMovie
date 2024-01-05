package com.example.btl_app_movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BeginActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        // Handler để đặt một đoạn mã thực thi vào một thời điểm nhất định trong tương lai, dừng lại 2s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, SPLASH_TIME_OUT);
    }
    private void nextActivity() {
        // Lấy user hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null)
        {
            // Chưa login -> chuyển đến SignInActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else    // Đã login
        {
            // Chuyển đến MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}