package com.example.btl_app_movie.xemphim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_app_movie.ChiTietXemPhim;
import com.example.btl_app_movie.R;
import com.example.btl_app_movie.movie.Movie;
import com.example.btl_app_movie.movie.MovieAdapter;
import com.example.btl_app_movie.movie.MovieItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class XemPhimAdapter extends RecyclerView.Adapter<XemPhimAdapter.MyViewHolder> {
    Context Context ;

    List<Movie> mData;
    MovieItemClickListener movieItemClickListener;

    public XemPhimAdapter() {
    }

    public XemPhimAdapter(android.content.Context context, List<Movie> mData) {
        Context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public XemPhimAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film,viewGroup,false);
        return new XemPhimAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final Movie movie=mData.get(i);
        holder.TvTitle.setText(mData.get(i).getTen());

            Glide.with(holder.itemView.getContext())
                    .load(mData.get(i).getImage())
                    .fitCenter()
                    .into(holder.ImgMovie);
            //Toast.makeText(holder.itemView.getContext(), ""+mData.get(i).getImage(), Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Chuyển sang activity khác và truyền dữ liệu qua intent
                Intent intent = new Intent(view.getContext(), ChiTietXemPhim.class);

                Bundle b=new Bundle();
                b.putString("DaoDien", movie.getDaoDien());
                b.putString("DienVien", movie.getDienVien());
                b.putBoolean("favorite",movie.isFavorite());
                b.putBoolean("history",movie.isHistory());
                b.putInt("Id", movie.getId());
                b.putString("Image",movie.getImage());
                b.putString("Ten", movie.getTen());
                b.putBoolean("Lastest", movie.isLastest());
                b.putString("MoTa", movie.getMoTa());
                b.putInt("NamSX", movie.getNamSX());
                b.putString("QuocGia",movie.getQuocGia());
                b.putString("TheLoai", movie.getTheLoai());
                b.putString("ThoiLuong", movie.getThoiLuong());
                b.putString("Url", movie.getUrl());
                intent.putExtras(b);
                view.getContext().startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView TvTitle;
        private FloatingActionButton yeuthich;
        private ImageView ImgMovie;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TvTitle = itemView.findViewById(R.id.item_movie_title);
            ImgMovie = itemView.findViewById(R.id.item_movie_img);
            yeuthich=itemView.findViewById(R.id.yeuthich);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()),ImgMovie);
                }
            });

        }
    }
}