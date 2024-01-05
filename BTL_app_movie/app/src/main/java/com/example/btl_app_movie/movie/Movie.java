package com.example.btl_app_movie.movie;

public class Movie {

    public String daoDien;
    public String dienVien;
    public boolean favorite;
    public boolean history;
    public int id;
    public String image;

    public boolean isLastest;
    public String moTa;
    public int namSX;
    public String quocGia;
    public String ten;
    public String theLoai;
    public String thoiLuong;
    public String url;

    public Movie() {
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLastest() {
        return isLastest;
    }

    public void setLastest(boolean lastest) {
        isLastest = lastest;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(String thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Movie(String daoDien, String dienVien, boolean favorite, boolean history, int id, String image, boolean isLastest, String moTa, int namSX, String quocGia, String ten, String theLoai, String thoiLuong, String url) {
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.favorite = favorite;
        this.history = history;
        this.id = id;
        this.image = image;
        this.isLastest = isLastest;
        this.moTa = moTa;
        this.namSX = namSX;
        this.quocGia = quocGia;
        this.ten = ten;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "daoDien='" + daoDien + '\'' +
                ", dienVien='" + dienVien + '\'' +
                ", favorite=" + favorite +
                ", history=" + history +
                ", id=" + id +
                ", image='" + image + '\'' +
                ", isLastest=" + isLastest +
                ", moTa='" + moTa + '\'' +
                ", namSX=" + namSX +
                ", quocGia='" + quocGia + '\'' +
                ", ten='" + ten + '\'' +
                ", theLoai='" + theLoai + '\'' +
                ", thoiLuong='" + thoiLuong + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
