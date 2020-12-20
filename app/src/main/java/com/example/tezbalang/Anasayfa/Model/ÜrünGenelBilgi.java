package com.example.tezbalang.Anasayfa.Model;

public class ÜrünGenelBilgi {
    private String barkod;
    private  String isim;
    private  String foto_path;
    private  String kategori;

    public ÜrünGenelBilgi() {
    }

    /*public Kategoriler(String barkod, String isim, String foto_path) {
        this.barkod = barkod;
        this.isim = isim;
        this.foto_path = foto_path;
    }*/
    public ÜrünGenelBilgi(String barkod, String isim, String foto_path, String kategori) {
        this.barkod = barkod;
        this.isim = isim;
        this.foto_path = foto_path;
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }
    public String getBarkod() {
        return barkod;
    }

    public String getIsim() {
        return isim;
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

    public String getFoto_path() {
        return foto_path;
    }
}
