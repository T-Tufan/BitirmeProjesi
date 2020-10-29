package com.example.tezbalang.Anasayfa.Model;

public class Ürünler {
private String barkod;
private double fiyat;
private  String isim;
private  String foto_path;
private  String market;
private  String stok;
private  String kategori;
private  String acıklama;


    public String getBarkod() {
        return barkod;
    }

    public double getFiyat() {
        return fiyat;
    }

    public String getIsim() {
        return isim;
    }

    public String getFoto_path() {
        return foto_path;
    }

    public String getMarket() {
        return market;
    }


    public String getAcıklama() {
        return acıklama;
    }

    public Ürünler(String barkod, double fiyat, String isim, String foto_path, String market, String stok, String acıklama) {
        this.barkod = barkod;
        this.fiyat = fiyat;
        this.isim = isim;
        this.foto_path = foto_path;
        this.market = market;
        this.stok = stok;
        this.acıklama = acıklama;
    }

    public Ürünler(String barkod, double fiyat, String isim, String foto_path, String market, String stok, String kategori, String acıklama) {
        this.barkod = barkod;
        this.fiyat = fiyat;
        this.isim = isim;
        this.foto_path = foto_path;
        this.market = market;
        this.stok = stok;
        this.kategori = kategori;
        this.acıklama = acıklama;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public void setFoto_path(String foto_path) {
        this.foto_path = foto_path;
    }

    public void setMarket(String market) {
        this.market = market;
    }


    public void setAcıklama(String acıklama) {
        this.acıklama = acıklama;
    }

    public String getKategori() {
        return kategori;
    }






    @Override
    public String toString() {
        return "Ürünler{" +
                "barkod='" + barkod + '\'' +
                ", fiyat=" + fiyat +
                ", isim='" + isim + '\'' +
                ", foto_path='" + foto_path + '\'' +
                ", market='" + market + '\'' +
                ", stok='" + stok + '\'' +
                ", kategori='" + kategori + '\'' +
                ", acıklama='" + acıklama + '\'' +
                '}';
    }

/*
    int barkod=icecek.getInt("barkod");
    String isim=icecek.getString("isim");
    String foto_path=icecek.getString("foto-path");
    String market = icecek.getString("market");
    String stok= icecek.getString("stok");
    int fiyat = icecek.getInt("fiyat");
    String acıklama = icecek.getString("açıklama");*/
}
