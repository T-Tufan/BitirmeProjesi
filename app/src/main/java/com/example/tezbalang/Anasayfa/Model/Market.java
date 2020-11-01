package com.example.tezbalang.Anasayfa.Model;

public class Market {
    private String market_foto;
    private Integer market_sube_sayi;
    private String market_isim;
    private String market_aciklama;
    private Double market_boylam;
    private Double market_enlem;

    public Market(String market_isim, String market_aciklama,Double market_boylam,Double market_enlem,String market_foto) {
        this.market_isim = market_isim;
        this.market_aciklama = market_aciklama;
        this.market_boylam = market_boylam;
        this.market_enlem = market_enlem;
        this.market_foto = market_foto;
    }

    public Market(String market_isim,String market_foto,Integer market_sube_sayi, Double market_boylam,Double market_enlem) {
        this.market_isim = market_isim;
        this.market_boylam = market_boylam;
        this.market_enlem = market_enlem;
        this.market_foto = market_foto;
        this.market_sube_sayi = market_sube_sayi;
    }
    public Market(String market_isim,String market_foto,Integer market_sube_sayi) {
        this.market_isim = market_isim;
        this.market_foto = market_foto;
        this.market_sube_sayi = market_sube_sayi;
    }
    public String getMarket_isim() {
        return market_isim;
    }

    public String getMarket_aciklama() {
        return market_aciklama;
    }

    public Double getMarket_boylam() {
        return market_boylam;
    }

    public Double getMarket_enlem() {
        return market_enlem;
    }

    public String getMarket_foto() {
        return market_foto;
    }

    public Integer getMarket_sube_sayi() {
        return market_sube_sayi;
    }

    public void setMarket_foto(String market_foto) {
        this.market_foto = market_foto;
    }

    public void setMarket_sube_sayi(Integer market_sube_sayi) {
        this.market_sube_sayi = market_sube_sayi;
    }

    public void setMarket_isim(String market_isim) {
        this.market_isim = market_isim;
    }

    public void setMarket_aciklama(String market_aciklama) {
        this.market_aciklama = market_aciklama;
    }

    public void setMarket_boylam(Double market_boylam) {
        this.market_boylam = market_boylam;
    }

    public void setMarket_enlem(Double market_enlem) {
        this.market_enlem = market_enlem;
    }


}
