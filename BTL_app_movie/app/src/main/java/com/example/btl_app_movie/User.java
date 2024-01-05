package com.example.btl_app_movie;

import com.example.btl_app_movie.movie.Movie;

import java.util.List;

public class User {
    int id;
    List<Movie> lstmovie;
    String password;
    String email;

    public User() {}

    public User(int id, List<Movie> lstmovie, String password, String email) {
        this.id = id;
        this.lstmovie = lstmovie;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Movie> getMovie() {
        return lstmovie;
    }

    public void setMovie(List<Movie> lstmovie) {
        this.lstmovie = lstmovie;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
