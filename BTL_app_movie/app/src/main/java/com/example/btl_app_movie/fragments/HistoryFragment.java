package com.example.btl_app_movie.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.btl_app_movie.R;
import com.example.btl_app_movie.movie.Movie;
import com.example.btl_app_movie.movie.MovieAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private int id_user;
    private List<Movie> lstMovies;
    private ViewPager sliderPager;
    private ViewPager mViewPager;
    private RecyclerView MoviesRV ;
    private MovieAdapter movieAdapter;
    public HistoryFragment() { }
    @Override
    public void onStart() {
        super.onStart();

        // Lấy id_user gửi từ ViewPagerAdapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_user = bundle.getInt("id_user");
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+id_user+"/movie");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Movie> movieList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.isHistory())
                        movieList.add(movie);
                }
                // Cập nhật danh sách đối tượng trên RecyclerView
                movieAdapter = new MovieAdapter(HistoryFragment.this,movieList, id_user);
                MoviesRV.setAdapter(movieAdapter);
                MoviesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                MoviesRV.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_history, container, false);
        MoviesRV=v.findViewById(R.id.lstHistory);
        lstMovies=new ArrayList<>();
//        lstMovies.add(new Movie("The Martian",R.drawable.slide1));
//        lstMovies.add(new Movie("The Martian",R.drawable.slide1));
//        lstMovies.add(new Movie("The Martian",R.drawable.slide1));
//        lstMovies.add(new Movie("The Martian",R.drawable.slide1));

//        MovieAdapter movieAdapter = new MovieAdapter(HistoryFragment.this,lstMovies);
//
//        MoviesRV.setAdapter(movieAdapter);
//        MoviesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
//        MoviesRV.setLayoutManager(layoutManager);
        return v;
    }
}