package com.example.btl_app_movie.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.btl_app_movie.ChiTietXemPhim;
import com.example.btl_app_movie.R;
import com.example.btl_app_movie.xemphim.XemPhim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements Filterable {
    Fragment fragment ;
    int idUser;
    List<Movie> mData;
    List<Movie> dataBackUp;
    MovieItemClickListener movieItemClickListener;
    public MovieAdapter(Fragment fragment, List<Movie> mData, int idUser) {
        this.fragment=fragment;
        this.mData = mData;
        this.idUser = idUser;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {
        final Movie movie=mData.get(i);
        holder.TvTitle.setText(mData.get(i).getTen());
        if (fragment.isAdded() && fragment.getContext() != null) {
            // Load image using Glide
            Glide.with(fragment.getContext())
                    .load(mData.get(i).getImage()).fitCenter()
                    .placeholder(R.drawable.slide1)
                    .into(holder.ImgMovie);
        }
        if(movie.isFavorite()){
            holder.yeuthich.setImageResource(R.drawable.ic_favorite);
        }
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+idUser+"/movie/"+i+"/favorite");
        holder.yeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(movie.isFavorite()){
                            //movie.setFavorite(false);
                            holder.yeuthich.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(v.getContext(), "Bỏ thích", Toast.LENGTH_SHORT).show();
                            reference.setValue(false);
                        }
                        else{
                            //movie.setFavorite(true);
                            holder.yeuthich.setImageResource(R.drawable.ic_favorite);
                            reference.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                if(dataBackUp==null){
                    dataBackUp=new ArrayList<>(mData);
                }
                if(charSequence==null||charSequence.length()==0){
                    fr.count=dataBackUp.size();
                    fr.values=dataBackUp;
                }
                else {
                    ArrayList<Movie> newData=new ArrayList<>();
                    for(Movie c:dataBackUp){
                        if(c.getTen().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            newData.add(c);
                        }
                    }
                    fr.count=newData.size();
                    fr.values=newData;
                }
                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mData=new ArrayList<Movie>();
                ArrayList<Movie> tmp = (ArrayList<Movie>) filterResults.values;
                for(Movie c: tmp)
                    mData.add(c);
                notifyDataSetChanged();
            }
        };
        return f;
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
        }

    }
}



