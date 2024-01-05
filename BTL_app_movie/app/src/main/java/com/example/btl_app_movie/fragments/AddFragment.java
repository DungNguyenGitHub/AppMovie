package com.example.btl_app_movie.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.btl_app_movie.R;
import com.example.btl_app_movie.movie.Movie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFragment extends Fragment {

    // Khai báo biến
    Button mSave;
    EditText mTenPhim, mImage, mUrl, mMoTa, mDaoDien, mDienVien, mTheLoai, mThoiLuong, mQuocGia, mNamSX;
    ProgressDialog progressDialog;
    //===================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Ánh xạ
        unitUI(view);

        progressDialog = new ProgressDialog(getActivity());
        //===================================================================

        // Xử lý click btnSave
        mSave.setOnClickListener(v -> {

            // Lấy dữ liệu từ các editText
            String tenPhim = mTenPhim.getText().toString().trim();
            String image = mImage.getText().toString().trim();
            String url = mUrl.getText().toString().trim();
            String moTa = mMoTa.getText().toString().trim();
            String daoDien = mDaoDien.getText().toString().trim();
            String dienVien = mDienVien.getText().toString().trim();
            String theLoai = mTheLoai.getText().toString().trim();
            String thoiLuong = mThoiLuong.getText().toString().trim();
            String quocGia = mQuocGia.getText().toString().trim();
            int namSX = Integer.parseInt(mNamSX.getText().toString().trim());


            // Lấy thời gian hiện tại
            Calendar calendar = Calendar.getInstance();
            // Lấy năm hiện tại
            int year = calendar.get(Calendar.YEAR);

            // Kiểm tra độ dài các trường được nhập vào
            if (tenPhim.length() == 0 || image.length() == 0 || url.length() == 0 || moTa.length() == 0 || daoDien.length() == 0 ||
                    dienVien.length() == 0 || theLoai.length() == 0 || quocGia.length() == 0 || thoiLuong.length() == 0 || namSX > year) {
                Toast.makeText(getActivity(), "Bạn cần nhập đúng và đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {  // add new movie
                progressDialog.show();
                // Lấy số lượng phim hiện tại

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Xác định key
                DatabaseReference myRef = database.getReference("soLuongMovie");
                myRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        // Thành công
                        String value = String.valueOf(task.getResult().getValue());
                        int soLuongMovie = Integer.parseInt(value);
                        // Tạo Movie
                        Movie newMovie = new Movie(daoDien, dienVien, false, false, soLuongMovie, image, true, moTa, namSX, quocGia, tenPhim, theLoai, thoiLuong, url);

                        System.out.println(newMovie.toString());
                        // Cập nhật soLuongMovie
                        updateSoLuongMovie(soLuongMovie + 1);
                        // Cập nhật vào Admin/movie
                        updateAdminMovie(newMovie);
                        System.out.println("add trong Admin/movie"+newMovie.toString());
                        // Cập nhật vào User/i/movie
                        updateUserMovie(newMovie);

                        Toast.makeText(getActivity(), "Thêm movie thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

                // Reset các trường
                resetEditText();
            }
        });
        //===================================================================
        return view;
    }

    private void updateUserMovie(Movie newMovie) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("soLuongUser");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                // Thành công
                String value = String.valueOf(task.getResult().getValue());
                int soLuongUser = Integer.parseInt(value);
                for (int i=1;i<= soLuongUser;i++)
                {
                    DatabaseReference myRef1 = database.getReference("User/"+i+"/movie/"+newMovie.getId() );
                    // Set Value
                    myRef1.setValue(newMovie);

                    // Xóa lastest
                    DatabaseReference myRef2 = database.getReference("User/"+i+"/movie/"+newMovie.getId()+"/lastest");
                    myRef2.removeValue();
                }
                System.out.println("add trong User/i/movie"+newMovie.toString());
            }
        });
    }

    private void updateAdminMovie(Movie newMovie) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("Admin/movie/"+newMovie.getId() );
        // Set Value
        myRef.setValue(newMovie);

        DatabaseReference myRef1 = database.getReference("Admin/movie/"+newMovie.getId()+"/lastest" );
        myRef1.removeValue();
    }

    private void updateSoLuongMovie(int i) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("soLuongMovie");
        // Set Value
        myRef.setValue(i);
    }

    private void resetEditText() {
        mTenPhim.setText("");
        mImage.setText("");
        mUrl.setText("");
        mMoTa.setText("");
        mDaoDien.setText("");
        mDienVien.setText("");
        mTheLoai.setText("");
        mThoiLuong.setText("");
        mQuocGia.setText("");
        mNamSX.setText("");
    }

    private void unitUI(View view) {
        mTenPhim = view.findViewById(R.id.edtTen);
        mImage = view.findViewById(R.id.edtImage);
        mUrl = view.findViewById(R.id.edtUrl);
        mMoTa = view.findViewById(R.id.edtmota);
        mDaoDien = view.findViewById(R.id.edtDaoDien);
        mDienVien = view.findViewById(R.id.DienVien);
        mTheLoai = view.findViewById(R.id.edtTheLoai);
        mThoiLuong = view.findViewById(R.id.edtThoiLuong);
        mQuocGia = view.findViewById(R.id.edtQuocGia);
        mNamSX = view.findViewById(R.id.edtNamSX);
        mSave = view.findViewById(R.id.btnSave);
    }
}