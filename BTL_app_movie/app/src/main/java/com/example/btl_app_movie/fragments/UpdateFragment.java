package com.example.btl_app_movie.fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.btl_app_movie.R;
import com.example.btl_app_movie.movie.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateFragment extends Fragment {
    // Khai báo biến
    Button mSave;
    Spinner mSpinTen;
    EditText mImage, mUrl, mMoTa, mDaoDien, mDienVien, mTheLoai, mThoiLuong, mQuocGia, mNamSX;
    ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private int idMovie;
    //===================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        // Ánh xạ view
        unitUI(view);
        progressDialog = new ProgressDialog(getContext());

        //===================================================================
        // Tạo Spinner hiển thị tên phim


        // Xác định reference
        DatabaseReference myRef = database.getReference("Admin/movie");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Gson gson = new Gson();
                String json = gson.toJson(task.getResult().getValue());
                List<Movie> lstValue = gson.fromJson(json, new TypeToken<List<Movie>>(){}.getType());

                // Lấy danh sách phim
                ArrayList<Movie> lstMovie = new ArrayList<>();
                // List tên phim
                ArrayList<String> lstTenPhim = new ArrayList<>();
                for( Movie movie : lstValue)
                {
                    if (movie != null && movie.getTen()!=null)
                    {
                        lstMovie.add(movie);
                        lstTenPhim.add(movie.getTen());
                    }
                }

                // Tạo adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstTenPhim) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        String item = getItem(position);
                        textView.setText(item); // Tên phim hiển thị trong Spinner
                        textView.setTextColor(Color.WHITE);  // Màu trắng
                        textView.setTextSize(25);
                        return textView;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // set adapter
                mSpinTen.setAdapter(adapter);

                //===================================================================
                // Xử lý sự kiện khi người dùng chọn một mục trong Spinner
                mSpinTen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Xử lý sự kiện khi một mục được chọn trong Spinner
                        idMovie = lstMovie.get(position).getId(); // Lưu lại idMovie để xử lý click Update
                        mImage.setText(lstMovie.get(position).getImage());
                        mUrl.setText(lstMovie.get(position).getUrl());
                        mMoTa.setText(lstMovie.get(position).getMoTa());
                        mDaoDien.setText(lstMovie.get(position).getDaoDien());
                        mDienVien.setText(lstMovie.get(position).getDienVien());
                        mTheLoai.setText(lstMovie.get(position).getTheLoai());
                        mThoiLuong.setText(lstMovie.get(position).getThoiLuong());
                        mQuocGia.setText(lstMovie.get(position).getQuocGia());
                        mNamSX.setText(String.valueOf(lstMovie.get(position).getNamSX()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Xử lý sự kiện khi không có mục nào được chọn trong Spinner
                    }
                });
            }
        });
        //===================================================================

        // xử lý sự kiện click save
        mSave.setOnClickListener(v -> {
            progressDialog.show();

            // Lấy dữ liệu từ các editText
            String image = mImage.getText().toString().trim();
            String url = mUrl.getText().toString().trim();
            String moTa = mMoTa.getText().toString().trim();
            String daoDien = mDaoDien.getText().toString().trim();
            String dienVien = mDienVien.getText().toString().trim();
            String theLoai = mTheLoai.getText().toString().trim();
            String thoiLuong = mThoiLuong.getText().toString().trim();
            String quocGia = mQuocGia.getText().toString().trim();
            int namSX = Integer.parseInt(mNamSX.getText().toString().trim());
            // kiểm tra rỗng
            // Lấy thời gian hiện tại
            Calendar calendar = Calendar.getInstance();
            // Lấy năm hiện tại
            int year = calendar.get(Calendar.YEAR);

            // Kiểm tra độ dài các trường được nhập vào
            if (image.length() == 0 || url.length() == 0 || moTa.length() == 0 || daoDien.length() == 0 || dienVien.length() == 0 || theLoai.length() == 0 || quocGia.length() == 0 || thoiLuong.length() == 0 || namSX > year) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Bạn cần nhập đúng và đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {  // Cập nhật thông tin phim

                // Cập nhật trong Admin/movie:
                setdata("Admin/movie/" + idMovie + "/image", image);
                setdata("Admin/movie/" + idMovie + "/url", url);
                setdata("Admin/movie/" + idMovie + "/moTa", moTa);
                setdata("Admin/movie/" + idMovie + "/daoDien", daoDien);
                setdata("Admin/movie/" + idMovie + "/dienVien", dienVien);
                setdata("Admin/movie/" + idMovie + "/theLoai", theLoai);
                setdata("Admin/movie/" + idMovie + "/thoiLuong", thoiLuong);
                setdata("Admin/movie/" + idMovie + "/quocGia", quocGia);
                setdata("Admin/movie/" + idMovie + "/namSX", namSX);

                // Cập nhật trong User/i/movie
                updateMovie(image, url, moTa, daoDien, dienVien, theLoai, thoiLuong, quocGia, namSX);
            }
        });


        return view;
    }

    private void updateMovie(String image, String url, String moTa, String daoDien, String dienVien, String theLoai, String thoiLuong, String quocGia, int namSX) {
        // Lấy số lượng user
        DatabaseReference myRef = database.getReference("soLuongUser");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get thành công
                Integer value = dataSnapshot.getValue(Integer.class);
                int soLuongUser = value == null ? 0 : value;

                // Update cho User/i/movie
                for (int i = 1; i <= soLuongUser; i++) {
                    setdata("User/" + i + "/movie/" + idMovie + "/image", image);
                    setdata("User/" + i + "/movie/" + idMovie + "/url", url);
                    setdata("User/" + i + "/movie/" + idMovie + "/moTa", moTa);
                    setdata("User/" + i + "/movie/" + idMovie + "/daoDien", daoDien);
                    setdata("User/" + i + "/movie/" + idMovie + "/dienVien", dienVien);
                    setdata("User/" + i + "/movie/" + idMovie + "/theLoai", theLoai);
                    setdata("User/" + i + "/movie/" + idMovie + "/thoiLuong", thoiLuong);
                    setdata("User/" + i + "/movie/" + idMovie + "/quocGia", quocGia);
                    setdata("User/" + i + "/movie/" + idMovie + "/namSX", namSX);
                }

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Update thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setdata(String path, String value) {
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(value);
    }

    private void setdata(String path, int value) {
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(value);
    }


    private void unitUI(View view) {
        mSpinTen = view.findViewById(R.id.spinTen);
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