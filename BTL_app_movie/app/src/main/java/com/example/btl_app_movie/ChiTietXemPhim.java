package com.example.btl_app_movie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btl_app_movie.movie.Movie;
import com.example.btl_app_movie.xemphim.XemPhim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChiTietXemPhim extends AppCompatActivity {
    Movie movie;
    TextView daoDien,dienVien,favorite,history,id,moTa,namSX,thoiLuong,url;
    Fragment context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xem_phim);
        //Lấy Bundle khỏi Intent
        Bundle b = getIntent().getExtras();
        if (b != null) {
            movie = new Movie();
            movie.setDaoDien(b.getString("DaoDien"));
            movie.setDienVien(b.getString("DienVien"));
            movie.setFavorite(b.getBoolean("favorite"));
            movie.setHistory(b.getBoolean("history"));
            movie.setId(b.getInt("Id"));
            movie.setImage(b.getString("Image"));
            movie.setTen(b.getString("Ten"));
            movie.setLastest(b.getBoolean("Lastest"));
            movie.setMoTa(b.getString("MoTa"));
            movie.setNamSX(b.getInt("NamSX"));
            movie.setQuocGia(b.getString("QuocGia"));
            movie.setTheLoai(b.getString("TheLoai"));
            movie.setThoiLuong(b.getString("ThoiLuong"));
            movie.setUrl(b.getString("Url"));

            TextView tenPhim = findViewById(R.id.tenPhim);
            tenPhim.setText("Tên phim:"+movie.getTen());
            ImageView imageView=findViewById(R.id.image);
            Glide.with(this).load(movie.getImage()).fitCenter().into(imageView);
            daoDien =findViewById(R.id.daoDien) ;
            daoDien.setText("Đạo diễn: "+movie.getDaoDien());
            dienVien=findViewById(R.id.dienVien);
            dienVien.setText("Diễn Viên: "+movie.getDienVien());
            moTa=findViewById(R.id.moTa);
            moTa.setText("Mô tả: "+movie.getMoTa());
            namSX=findViewById(R.id.namSx);
            namSX.setText("Năm Sản Xuất: "+movie.getNamSX());
            thoiLuong=findViewById(R.id.thoiLuong) ;
            thoiLuong.setText("Thười lượng: "+movie.getThoiLuong());
            FloatingActionButton play=findViewById(R.id.playChiTietXemPhim);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ChiTietXemPhim.this, XemPhim.class);
                    Bundle b=new Bundle();
                    b.putString("tenPhim",movie.getTen());
                    b.putString("DaoDien",movie.getDaoDien());
                    b.putString("DienVien",movie.getDienVien());
                    b.putString("MoTa",movie.getMoTa());
                    b.putString("Url",movie.getUrl());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
    }
}