package com.example.btl_app_movie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app_movie.movie.Movie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText mEmail, mPassword, mPassword2;
    Button mSignUp;
    TextView mSignIn;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Ánh xạ
        backButton = findViewById(R.id.back_button);
        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPassword);
        mPassword2 = findViewById(R.id.edtPassword2);
        mSignUp = findViewById(R.id.btnSignUp);
        mSignIn = findViewById(R.id.tvSignIn);
        progressDialog = new ProgressDialog(this);

        // Xử lý back_button
        backButton.setOnClickListener(v -> {
            finish(); // kết thúc activity hiện tại và quay lại activity trước đó
        });

        // Xử lý click tvSignIn
        mSignIn.setOnClickListener(v -> {
            finish(); // kết thúc activity hiện tại và quay lại activity trước đó (LoginActivity)
        });

        // Xử lý click button 'SIGN UP'
        mSignUp.setOnClickListener(v -> {
            // Lấy giá trị ở editText
            String strEmail = mEmail.getText().toString().trim();
            String strPassword = mPassword.getText().toString().trim();
            String strPassword2 = mPassword2.getText().toString().trim();

            // Kiểm tra độ dài email + mật khẩu
            if (strEmail.length() == 0 || strPassword.length() < 6 || strPassword2.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Bạn phải nhập đủ các thông tin..., và Mật khẩu phải lớn hơn 5 kí tự", Toast.LENGTH_SHORT).show();
            } else if (strPassword.compareTo(strPassword2) != 0) { // Nhập confirm sai
                mPassword.setText("");
                mPassword2.setText("");
                Toast.makeText(SignUpActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            } else {
                /*
                // Tài khoản realtime database
                // Tạo 1 tài khoản mới
                FirebaseAuth auth = FirebaseAuth.getInstance();
                // Gọi API
                progressDialog.show();  // Để người dùng biết đang chạy API
                auth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Chạy API xong -> trả kết quả về onComplete
                        progressDialog.dismiss();   // Tắt progressDialog

                        if (task.isSuccessful()) {  // Đăng kí thành công
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Hủy các activity trước đó
                            finishAffinity();
                        } else {    // Đăng kí không thành công
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                 */

                // Tạo tài khoản user trong csdl
                // Kiểm tra email đã tồn tại chưa

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                progressDialog.show();  // Để người dùng biết đang chạy API
                // Xác định key
                DatabaseReference myRef = database.getReference("User");
                myRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {

                        Gson gson = new Gson();
                        String json = gson.toJson(task.getResult().getValue());
                        List<User> lstUsers = gson.fromJson(json, new TypeToken<List<User>>(){}.getType());

                        boolean exist = false;
                        // Kiểm tra email tồn tại chưa
                        for(User user : lstUsers)
                        {
                            if(user!=null && user.getEmail()!=null)
                            {
                                if(user.getEmail().equals(strEmail)){
                                    exist = true;
                                    Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    mEmail.setText("");
                                    mPassword.setText("");
                                    mPassword2.setText("");
                                }


                            }
                        }
                        if(!exist)
                        {   // insert new user

                            createNewUser(lstUsers.size(), strEmail, strPassword);
                        }
                    }
                });
            }
        });
    }

    private void createNewUser(int id, String strEmail, String strPassword) {
        // Lấy danh sách phim
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Admin/movie");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Gson gson = new Gson();
                String json = gson.toJson(task.getResult().getValue());
                List<Movie> lstMovie = gson.fromJson(json, new TypeToken<List<Movie>>(){}.getType());
                // Tạo user
                User newUser = new User(id, lstMovie, strPassword, strEmail);

                insertNewUser(newUser);
                update_soLuongUser(id+1);

                // Chuyển sang Main
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                // Sử dụng bundle để gửi id của user sang MainActivity
                Bundle bundle = new Bundle();
                bundle.putInt("id_user", newUser.getId()); //  idUser
                // Đưa bundle vào intent
                intent.putExtras(bundle);
                startActivity(intent);

                finishAffinity();// Hủy các activity trước đó
            }
        });
    }

    private void update_soLuongUser(int i) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("soLuongUser");
        // Set value
        myRef.setValue(i, (error, ref) -> {
            // Xử lý các lệnh khi set value thành công
            System.out.println("Update soLuongUser:"+i);
        });
    }

    private void insertNewUser(User newUser) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User/"+newUser.getId());
        // Set value
        myRef.setValue(newUser, (error, ref) -> {
            // Xử lý các lệnh khi set value thành công
            System.out.println("Thêm new_user thành công");
        });
    }
}