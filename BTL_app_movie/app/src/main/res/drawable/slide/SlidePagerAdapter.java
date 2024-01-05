package com.example.appmovie.slide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<Slide> lstSlide=new ArrayList<>();

    public SlidePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lstSlide.get(position);
    }

    public SlidePagerAdapter(@NonNull FragmentManager fm, List<Slide> lstSlide) {
        super(fm);
        this.lstSlide = lstSlide;
    }
    @Override
    public int getCount() {
        return lstSlide.size();
    }
}
