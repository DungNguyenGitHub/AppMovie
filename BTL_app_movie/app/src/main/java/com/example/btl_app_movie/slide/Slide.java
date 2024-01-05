package com.example.btl_app_movie.slide;

import androidx.fragment.app.Fragment;

public class Slide extends Fragment {
    private String Image ;
    private String Title;
    // Add more field depand on whay you wa&nt ...




    public Slide(String image, String title) {
        Image = image;
        Title = title;
    }

    public Slide(String image) {
        Image = image;
    }


    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
