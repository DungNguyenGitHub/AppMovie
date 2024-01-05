package com.example.btl_app_movie.realTime_DataBase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Delete_Data {
    public void delete_data(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference(key);
        // Xóa key
        /*
        myRef.removeValue();
         */

        // Ngoài ra, có thể dùng cách sau để thông báo khi xóa thành công
        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                // Thông báo người dùng xóa thành công
            }
        });
    }
}
