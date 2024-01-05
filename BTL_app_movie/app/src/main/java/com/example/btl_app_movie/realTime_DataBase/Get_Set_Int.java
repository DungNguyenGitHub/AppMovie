package com.example.btl_app_movie.realTime_DataBase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Get_Set_Int {
    public int value;
    public int get_int(String key)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference(key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get thành công

                // Lấy value vào biến value bên ngoài
                Integer value = dataSnapshot.getValue(Integer.class);
                int variable = value==null?0:value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return value;
    }

    public void set_data(String key, int value)
    {
        // Value có thể là các kiểu dữ liệu: String, Long, Double, Boolean, Map<String,Object>, List<Object>

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference(key);
        // Set Value

        /*
        myRef.setValue(value);
         */

        // Ngoài ra, thực tế đôi khi có các lỗi xảy ra ví dụ như sự cố mạng,...
        //  chúng ta thêm interface DatabaseReference.CompletionListener và ghi đè onComplete()
        //  khi thực hiện setValue thành công chương trình sẽ thực hiện lệnh trong onComplete()

        myRef.setValue(value, new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                // Thông báo cho người dùng biết đã setValue thành công
            }
        });
    }

}
