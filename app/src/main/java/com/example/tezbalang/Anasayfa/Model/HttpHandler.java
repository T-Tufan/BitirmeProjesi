package com.example.tezbalang.Anasayfa.Model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {
    public HttpHandler() {
    }
    public String sunucuBaglantisi(String requestUrl){
        String response = null;
        try {//bağlantıda sorun olabilme ihtimali için hata yakala blokları kullanılmalı.
            URL url = new URL(requestUrl);
            //githuba gider veriyi okur.Get ve post işemide yapabilir.
            //HttpUrlConnection ile http bağlantısı oluşturuluyor.
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = veriFormatıCevirme(in);
        }
        catch (MalformedURLException e ){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  response; //json dosya içerisindek bütün verileri aynı formatta  döndürür.
    }
     private String veriFormatıCevirme(InputStream is){
        //Buffrered reader Web sunucudan dosyadan okur gibi satır satır veri okumaya yarıyor.
         BufferedReader reader = new BufferedReader(new InputStreamReader( (is)));
         //Bu bölümden sonra veriler satır satır okunur.
         StringBuilder sb = new StringBuilder();

         String satir ="";
         try{
             while ((satir = reader.readLine()) != null){//dosya sonuna gelene kadar oku
                 sb.append(satir).append("\n");//satır sonuna geldiyse çık.alt satıra geç.string builder içerisine satırı ekle.
             }
         }catch (IOException e){

         }finally {
             try {
                 is.close();
             }catch (IOException e){

             }
         }
         return  sb.toString();
     }
}
