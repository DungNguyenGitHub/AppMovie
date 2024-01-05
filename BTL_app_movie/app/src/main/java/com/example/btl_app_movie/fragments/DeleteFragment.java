package com.example.btl_app_movie.fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;

public class DeleteFragment extends Fragment {
    // Khai báo biến
    Button mDelete;
    Spinner mSpinTen;
    TextView mImage, mUrl, mMoTa, mDaoDien, mDienVien, mTheLoai, mThoiLuong, mQuocGia, mNamSX;
    ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private int idMovie;
    //===================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        // Ánh xạ view
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
        mDelete = view.findViewById(R.id.btnDelete1);
        progressDialog = new ProgressDialog(getContext());

        //===================================================================
        // Tạo Spinner hiển thị tên phim
        // Lấy danh sách phim

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
        // xử lý sự kiện click delete
        mDelete.setOnClickListener(v -> {
            progressDialog.show();

            // Tạo AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Xác nhận");
            // Tạo layout
            LinearLayout linearLayout = new LinearLayout(getActivity());
            final TextView tvConfirm = new TextView(getActivity());
            tvConfirm.setText(R.string.text_confirm);   // Bạn muốn xóa phim không?
            tvConfirm.setMinEms(16);
            linearLayout.addView(tvConfirm);
            linearLayout.setPadding(10, 10, 10, 10);
            builder.setView(linearLayout);

            builder.setPositiveButton("Delete", (dialog, which) -> {
                // Xóa movie
                // Giảm số lượng movie
                updateSoLuongMovie();
                // Xóa Admin/movie
                deleteData("Admin/movie/" + idMovie);
                System.out.println("xóa tại admin, id = "+idMovie);
                // Xóa trong User/i/movie/idMovie

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Xác định key
                DatabaseReference myRef1 = database.getReference("soLuongUser");
                myRef1.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // không thành công
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        // Thành công
                        // Get soLuongUser
                        String value = String.valueOf(task.getResult().getValue());
                        int soLuongUser = Integer.parseInt(value);

                        // Xóa phim trong từng user
                        int indexMovie = idMovie;
                        for (int i = 1; i <= soLuongUser; i++) {

                            deleteData("User/" + i + "/movie/" + indexMovie);
                            System.out.println("Xóa tại user "+i +" , id = "+indexMovie);
                        }
                        progressDialog.dismiss();
                    }
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Hủy dialog
                dialog.dismiss();
            });
            // Hiển thị dialog
            builder.create().show();
        });

        return view;
    }

    private void updateSoLuongMovie() {
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
                // Set số lượng movie
                setSoLuongMovie(soLuongMovie-1);
            }
        });
    }

    private void setSoLuongMovie(int i) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Xác định key
        DatabaseReference myRef = database.getReference("soLuongMovie");
        // Set Value
        myRef.setValue(i);
    }

    private void deleteData(String path) {
        DatabaseReference myRef = database.getReference(path);
        // Xóa key
        myRef.removeValue();
    }

}