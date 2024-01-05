package com.example.btl_app_movie.xemphim;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.btl_app_movie.MainActivity;
import com.example.btl_app_movie.R;
import com.example.btl_app_movie.fragments.HistoryFragment;
import com.example.btl_app_movie.fragments.HomeFragment;
import com.example.btl_app_movie.movie.Movie;
import com.example.btl_app_movie.movie.MovieAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class XemPhim extends AppCompatActivity {
    private VideoView videoView;
    List<Movie> movieList = new ArrayList<>();

    private final Movie phim=new Movie();
    //private int position = 0;
    private FloatingActionButton play;
    private List<Movie> lstMovies;//Movie Liên quan
    private Movie movie;
    private XemPhimAdapter movieAdapter;
    private RecyclerView MoviesRV ;
    @Override
    public void onStart() {
        super.onStart();
        // MainActivity để lấy idUser


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_phim);
        Bundle b=getIntent().getExtras();
        TextView title=findViewById(R.id.title);
        title.setText("Tên phim"+b.getString("tenPhim"));
        TextView tomTat=findViewById(R.id.tt);
        TextView dd=findViewById(R.id.dd_dv);
        dd.setText("Đạo diễn ,diễn viên: "+b.getString("DienVien")+", "+b.getString("DaoDien"));
        tomTat.setText("Mô tả: "+b.getString("MoTa"));
        String url=b.getString("Url");
        WebView video=findViewById(R.id.webView);
        MoviesRV=findViewById(R.id.lstphimLienQuan);
        video.getSettings().setLoadsImagesAutomatically(true);
        video.getSettings().setJavaScriptEnabled(true);
        video.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        video.loadUrl(url);

//        HomeFragment homeFragment = new HomeFragment();
//        int idUser = homeFragment.getId_user();
        //System.out.println("xem phim"+idUser);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+1+"/movie");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // List phim lien quan

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.isLastest())
                        movieList.add(movie);
                    System.out.println(movie.toString());
                }
                // Cập nhật danh sách đối tượng trên RecyclerView

                movieAdapter = new XemPhimAdapter(XemPhim.this,movieList);
                MoviesRV.setAdapter(movieAdapter);
                MoviesRV.setLayoutManager(new LinearLayoutManager(XemPhim.this, LinearLayoutManager.HORIZONTAL,false));
                GridLayoutManager layoutManager = new GridLayoutManager(XemPhim.this, 3);
                MoviesRV.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }
}