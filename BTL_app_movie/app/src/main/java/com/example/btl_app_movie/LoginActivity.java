package com.example.btl_app_movie;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText Email, password;
    Button btnSignIn;
    TextView forgotPassword, signUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ View
        Email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);
        progressDialog = new ProgressDialog(this);

        // Xử lý click button SIGN IN
        btnSignIn.setOnClickListener(v -> {

            progressDialog.show();  // Để người dùng biết đang xử lý

            String str_email = Email.getText().toString().trim();
            String str_password = password.getText().toString().trim();

            if (str_email.length() == 0 || str_password.length() == 0) {
                Toast.makeText(LoginActivity.this, "Bạn phải nhập đầy đủ email và password", Toast.LENGTH_SHORT).show();
            } else if (str_email.compareTo("admin@gmail.com") == 0)   // Kiem tra admin
            {
                // Lấy password admin

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Xác định key
                DatabaseReference myRef = database.getReference("Admin/password");
                myRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        // Thành công
                        String pass = String.valueOf(task.getResult().getValue());
                        if (pass.compareTo(str_password) == 0) {
                            // Đăng nhập admin
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // Sử dụng bundle để gửi id của user sang MainActivity
                            Bundle bundle = new Bundle();
                            bundle.putInt("id_user", 0);
                            // Đưa bundle vào intent
                            intent.putExtras(bundle);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finishAffinity();// Hủy các activity trước đó
                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            password.setText("");
                        }

                    }
                });

            } else {  // Đăng nhập

                /*
                // Phần này đăng nhập vào tài khoản realtime database, không phải user trong csdl

                FirebaseAuth Auth = FirebaseAuth.getInstance();
                progressDialog.show();  // Để người dùng biết đang chạy API
                Auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Chạy API xong -> trả kết quả về onComplete
                        progressDialog.dismiss();   // Tắt progressDialog

                        if (task.isSuccessful()) {
                            // Thành công
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Hủy các activity trước đó
                            finishAffinity();

                        } else {
                            // Không thành công
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(LoginActivity.this, "Bên ngoài"+ value, Toast.LENGTH_SHORT).show();
                    }
                });


                 */


                // Đăng nhập bằng tài khoản user trong realtime database

                // Lấy list<User>
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Xác định key
                DatabaseReference myRef = database.getReference("User");
                myRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        // Ép kiểu list Json thành list<Object>
                        Gson gson = new Gson();
                        String json = gson.toJson(task.getResult().getValue());
                        List<User> lstUsers = gson.fromJson(json, new TypeToken<List<User>>(){}.getType());

                        // Kiểm tra email, password
                        for(User user: lstUsers)
                        {
                            if (user != null && user.getEmail() != null && user.getPassword() != null) {
                                if (user.getEmail().equals(str_email) && user.getPassword().equals(str_password)) {
                                    // Đăng nhập
                                    progressDialog.dismiss();   // Tắt progressDialog

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    // Sử dụng bundle để gửi id của user sang MainActivity
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id_user", user.getId()); // i là idUser
                                    // Đưa bundle vào intent
                                    intent.putExtras(bundle);

                                    startActivity(intent);

                                    finishAffinity();// Hủy các activity trước đó
                                }
                            }
                        }
                        // Không tìm thấy tài khoản
                        progressDialog.dismiss();   // Tắt progressDialog
                        Toast.makeText(LoginActivity.this, "Email or Password không đúng", Toast.LENGTH_SHORT).show();
                        // Reset EditText
                        Email.setText("");
                        password.setText("");
                    }
                });
            }
        });


        // Xử lý click "Sign Up"
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        // Xử lý click "forgot password"
        forgotPassword.setOnClickListener(v -> showRecoverPasswordDialog());
    }

    private void showRecoverPasswordDialog() {
        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        // Tạo layout
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText edtEmail = new EditText(this);
        edtEmail.setHint("Email");
        edtEmail.setMinEms(16);
        edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(edtEmail);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", (dialog, which) -> {
            // Gửi 1 email từ firebase đến email vừa nhập
            String email = edtEmail.getText().toString().trim();
            /*
            // Xử lý theo tài khoản realtime database, không phải user trong csdl
            beginRecovery(email);
             */
            showPasword(email);
            // Xử lý theo user trong csdl

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Hủy dialog
            dialog.dismiss();
        });
        // Hiển thị dialog
        builder.create().show();
    }

    private void showPasword(String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("User");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                // Thành công
                // Ép kiểu list json thành list<object>
                Gson gson = new Gson();
                String json = gson.toJson(task.getResult().getValue());
                List<User> lstUsers = gson.fromJson(json, new TypeToken<List<User>>(){}.getType());

                boolean timThayEmail = false;
                for (User user1 : lstUsers) {
                    if (user1 != null && user1.getEmail() != null && user1.getPassword() != null) {
                        if (user1.getEmail().compareTo(email) == 0) {
                            timThayEmail = true;
                            Toast.makeText(LoginActivity.this, "Password:" + user1.getPassword(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if (!timThayEmail)
                    Toast.makeText(LoginActivity.this, "Email không tồn tại:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    // Dùng để gửi email đặt lại mật khẩu đến người dùng qua email cho tài khoản realtime database
    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending Email....");
        // không bị huỷ khi người dùng nhấn bất kỳ nơi nào
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Gửi email từ firebase cho email cần resetPassword

        // Tạo FirebaseAuth để kết nối đến firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Gọi API: gửi email đặt lại mật khẩu đến người dùng qua email
        // Hàm xử lý quá trình gửi thành công
        // Hàm xử lý quá trình gửi thành công
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            // API chạy xong -> Trả kết quả về onComplete
            // Hủy progressDialog
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Gửi thành công", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Xảy ra lỗi", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            // Hủy progressDialog
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Gửi thất bại", Toast.LENGTH_LONG).show();
        });
    }

     */
}