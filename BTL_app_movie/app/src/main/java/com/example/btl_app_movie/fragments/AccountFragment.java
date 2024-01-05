package com.example.btl_app_movie.fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.btl_app_movie.LoginActivity;
import com.example.btl_app_movie.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    // Khai báo biến
    private int id_user;
    TextView mEmail, mChangePassword, mSave, mSignOut;
    EditText mPassword, mPassword1;
    /*FirebaseUser user;*/
    ProgressDialog progressDialog;

    //===================================================================

    public AccountFragment() { }

    @Override
    public void onStart() {
        super.onStart();
        // Lấy id_user gửi từ ViewPagerAdapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_user = bundle.getInt("id_user");
            System.out.println("id trong account:" + id_user);
        }
        // Email user trong csdl
        // Tạo path_key
        String path_key = "";
        if (id_user == 0)   // admin
            path_key = "Admin/email";
        else
            path_key = "User/" + id_user + "/email";
        System.out.println(path_key);
        // Gọi api truy xuất email đưa vào editText
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference(path_key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get thành công
                String email = "Email:" + dataSnapshot.getValue(String.class);
                System.out.println(dataSnapshot.getValue(String.class));
                // Set email cho textview email
                mEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        // Ánh xạ
        unitUI(view);

        progressDialog = new ProgressDialog(getActivity()); // getActivity(): truy cập activity chứa fragment
        //===================================================================

        // Lấy email của account hiện tại
        /*
        // Email account realtime database
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = "Email:" + user.getEmail();
            // Set email cho textview email
            mEmail.setText(email);
        }
         */



        //===================================================================

        // Xử lý xự kiện click Change Password
        mChangePassword.setOnClickListener(v -> {
            // Hiển thị EditText newPassword + btnLuu
            mPassword.setVisibility(View.VISIBLE);
            mPassword1.setVisibility(View.VISIBLE);
            mSave.setVisibility(View.VISIBLE);
        });
        //===================================================================

        // Xử lý xự kiện click btnSave
        mSave.setOnClickListener(v -> {
            // Kiểm tra input có rỗng không?
            String str_password = mPassword.getText().toString().trim();
            String str_password1 = mPassword1.getText().toString().trim();
            if (str_password.length() < 6 || str_password1.length() < 6) {
                Toast.makeText(getActivity(), "Mật khẩu phải lớn hơn 5 kí tự", Toast.LENGTH_SHORT).show();
                mPassword.setText("");
                mPassword1.setText("");
            } else if (str_password.compareTo(str_password1) != 0) {
                Toast.makeText(getActivity(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                mPassword.setText("");
                mPassword1.setText("");
            } else {

                /*
                // Dùng để set password cho tài khoản realtime database
                user = FirebaseAuth.getInstance().getCurrentUser();
                // Hiển thị ProgressDialog để người dùng biết đang gọi API
                progressDialog.show();
                // Gọi API
                user.updatePassword(str_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Chạy API xong -> trả kết quả về onComplete
                        progressDialog.dismiss();   // Tắt progressDialog
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            // Ẩn edtNewPassword, edtNewPassword1 và btnLuu
                            mPassword.setVisibility(View.GONE);
                            mPassword1.setVisibility(View.GONE);
                            mSave.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getActivity(), "Lỗi. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                 */

                // Change password theo user trong csdl
                // Tạo path_key
                String path_key1;
                if (id_user == 0)   // admin
                    path_key1 = "Admin/password";
                else path_key1 = "Admin/" + id_user + "/password";
                // Gọi api để set data
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                // Xác định key
                DatabaseReference myRef1 = database1.getReference(path_key1);
                // Set Value

                /*
                myRef.setValue(value);
                 */

                // Ngoài ra, thực tế đôi khi có các lỗi xảy ra ví dụ như sự cố mạng,...
                //  chúng ta thêm interface DatabaseReference.CompletionListener và ghi đè onComplete()
                //  khi thực hiện setValue thành công chương trình sẽ thực hiện lệnh trong onComplete()

                myRef1.setValue(str_password, (error, ref) -> {
                    // Thông báo cho người dùng biết đã setValue thành công
                    Toast.makeText(getActivity(), "Change password thành công", Toast.LENGTH_SHORT).show();
                });
            }
        });
        //===================================================================
        // Xử lý SignOut
        mSignOut.setOnClickListener(v -> {
            /*
            // Gọi API cho account realtime database
            FirebaseAuth.getInstance().signOut();
             */
            // Chuyển về LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        //===================================================================
        return view;
    }

    private void unitUI(View view) {
        mEmail = view.findViewById(R.id.tvEmail);
        mChangePassword = view.findViewById(R.id.tvChangePassword);
        mPassword = view.findViewById(R.id.edtNewPassword);
        mPassword1 = view.findViewById(R.id.edtNewPassword1);
        mSave = view.findViewById(R.id.btnSave);
        mSignOut = view.findViewById(R.id.tvSignOut);
    }
}