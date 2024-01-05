package com.example.btl_app_movie.slide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.btl_app_movie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {


    private List<Slide> lstSlide=new ArrayList<>();

    public MyPagerAdapter(List<Slide> lstSlide) {
        this.lstSlide = lstSlide;
    }

    public int getNextItemPosition(int currentPosition) {
        if (currentPosition == lstSlide.size() - 1) {
            return 0;
        } else {
            return currentPosition + 1;
        }
    }
    @Override
    public int getCount() {
        return lstSlide.size();
    }

    @Override
    public boolean isViewFromObject( View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.slider, container, false);

        ImageView imageView = view.findViewById(R.id.slide_img);
        Glide.with(view.getContext())
                .load(lstSlide.get(position).getImage())
                .fitCenter()
                .into(imageView);
        //imageView.setImageResource(lstSlide.get(position).getImage());
        TextView titleTextView = view.findViewById(R.id.slide_title);
        titleTextView.setText(lstSlide.get(position).getTitle());
        FloatingActionButton floatingActionButton=view.findViewById(R.id.watch);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

