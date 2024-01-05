package com.example.btl_app_movie.realTime_DataBase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_app_movie.movie.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Get_Set_Object {

    Movie movie;

    List<Movie> lstMovie;

    // Get Object
    public Movie getMovie(String key)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định reference
        DatabaseReference myRef = database.getReference(key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get thành công

                // Lấy value vào object movie bên ngoài
                movie = dataSnapshot.getValue(Movie.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return movie;
    }
    // Set Object
    public void set_Movie(String key, Movie value)
    {
        // Kết nối database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định reference
        DatabaseReference myRef = database.getReference(key);
        // Set value
        myRef.setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                // Xử lý các lệnh khi set value thành công
                // Ví dụ: thông báo người dùng biết
            }
        });
    }

    // Get List<Object>
    public List<Movie> getListMovie(String key)
    {
        lstMovie = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định reference
        DatabaseReference myRef = database.getReference(key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get thành công

                // for item in mảng dataSnapshot.getChildren()
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    //Lấy từng item đưa vào ArrayList
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    lstMovie.add(movie);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return lstMovie;
    }

    // Set List<Object>
    // Tương tự các setValue khác, nhưng firebase tự sinh reference từ 0
}
