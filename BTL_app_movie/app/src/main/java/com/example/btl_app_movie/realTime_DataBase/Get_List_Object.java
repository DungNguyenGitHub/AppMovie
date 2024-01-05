package com.example.btl_app_movie.realTime_DataBase;

import android.util.Log;

import com.example.btl_app_movie.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Get_List_Object {

    public void get_list_object()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("key");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {

                Gson gson = new Gson();
                String json = gson.toJson(task.getResult().getValue());
                List<User> lstUsers = gson.fromJson(json, new TypeToken<List<User>>(){}.getType());
                // xử lý danh sách đối tượng ở đây
            }
        });
    }


}
