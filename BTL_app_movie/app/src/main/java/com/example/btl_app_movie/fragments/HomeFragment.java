package com.example.btl_app_movie.fragments;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.btl_app_movie.R;
import com.example.btl_app_movie.movie.Movie;
import com.example.btl_app_movie.movie.MovieAdapter;
import com.example.btl_app_movie.slide.MyPagerAdapter;
import com.example.btl_app_movie.slide.Slide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private int id_user;
    private List<Slide> lstSiSlides=new ArrayList<>();
    public int getId_user(){return id_user;}

    private static List<Movie> lstMovies=new ArrayList<>();
    private ViewPager sliderPager;
    private ViewPager mViewPager;
    private RecyclerView MoviesRV ;
    private MyPagerAdapter mPagerAdapter;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private DatabaseReference mDatabaseReference;
    private MovieAdapter movieAdapter;
    public HomeFragment() {
    }
    @Override
    public void onStart() {
        super.onStart();

        // Lấy id_user gửi từ ViewPagerAdapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_user = bundle.getInt("id_user");
            System.out.println("id tronggggggg home:"+id_user);


        }
        // Lấy list<Movie> trong User
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+id_user+"/movie");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Movie> movieList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    movieList.add(movie);
                    if(movie.isLastest())
                    {
                        lstSiSlides.add(new Slide(movie.getImage(),movie.getTen()));
                    }

                }
                //slides

                mPagerAdapter = new MyPagerAdapter(lstSiSlides);
                mViewPager.setAdapter(mPagerAdapter);

                // Cập nhật danh sách đối tượng trên RecyclerView
                movieAdapter = new MovieAdapter(HomeFragment.this,movieList, id_user);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = mViewPager.getCurrentItem();
                int nextItem = mPagerAdapter.getNextItemPosition(currentItem);
                mViewPager.setCurrentItem(nextItem, true);
                mHandler.postDelayed(mRunnable, 3000); // Chuyển đổi mỗi 5 giây
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 2000); // Bắt đầu chuyển đổi khi Activity tiếp tục được hiển thị
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context = getContext();
    }
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable); // Ngưng chuyển đổi khi Activity bị đóng hoặc tạm dừng
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = v.findViewById(R.id.view_pager);
        lstSiSlides=new ArrayList<>();
        System.out.println(lstMovies.size());

        mPagerAdapter = new MyPagerAdapter(lstSiSlides);
        mViewPager.setAdapter(mPagerAdapter);

        //anh xa movie
        MoviesRV=v.findViewById(R.id.lstPhim);
        movieAdapter = new MovieAdapter(HomeFragment.this,lstMovies, id_user);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        MoviesRV.setLayoutManager(layoutManager);
        EditText editText=v.findViewById(R.id.edtext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                movieAdapter.getFilter().filter(s.toString());
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }


}