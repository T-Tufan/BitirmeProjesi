package com.example.tezbalang.Anasayfa.Model;

public class Kategoriler {
    private String barkod;
    private  String isim;
    private  String foto_path;
    private  String kategori;

    public Kategoriler() {
    }

    public Kategoriler(String barkod, String isim, String foto_path) {
        this.barkod = barkod;
        this.isim = isim;
        this.foto_path = foto_path;
    }
    public Kategoriler(String barkod, String isim, String foto_path,String kategori) {
        this.barkod = barkod;
        this.isim = isim;
        this.foto_path = foto_path;
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public void setFoto_path(String foto_path) {
        this.foto_path = foto_path;
    }

    public String getBarkod() {
        return barkod;
    }

    public String getIsim() {
        return isim;
    }

    public String getFoto_path() {
        return foto_path;
    }
}
