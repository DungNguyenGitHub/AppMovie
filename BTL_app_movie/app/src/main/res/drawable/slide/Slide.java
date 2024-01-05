package com.example.appmovie.slide;

import androidx.fragment.app.Fragment;

public class Slide extends Fragment {
    private int Image ;
    private String Title;
    // Add more field depand on whay you wa&nt ...




    public Slide(int image, String title) {
        Image = image;
        Title = title;
    }

    public Slide(int image) {
        Image = image;
    }


    public int getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setImage(int image) {
        Image = image;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
