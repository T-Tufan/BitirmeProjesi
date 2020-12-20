package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.tezbalang.Anasayfa.Adapter.MarketAdapter;
import com.example.tezbalang.Anasayfa.Adapter.ÜrünlerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Market;
import com.example.tezbalang.Anasayfa.Model.Ürünler;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MrktActivity extends AppCompatActivity {

    private MarketAdapter marketAdapter;
    ProgressDialog progressDialog;
    public HttpHandler httpHandler;
    ListView list;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/marketler.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrkt);
        list = findViewById(R.id.market_liste);

        new getMarketNames().execute();
    }
    private class  getMarketNames extends AsyncTask<Void,Void,Void> {
        ArrayList<Market> marketArrayList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
             progressDialog= new ProgressDialog(MrktActivity.this);
            progressDialog.setMessage("Marketler Listeleniyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //işlem tamamlandığında
            super.onPostExecute(aVoid);
            marketAdapter = new MarketAdapter(MrktActivity.this,marketArrayList);
            list.setAdapter(marketAdapter);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();//progress dialog kapatılır.
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            httpHandler = new HttpHandler();//içerisine url olarak verdiğim websitenin kaynağını döner.
            String jsonString = httpHandler.sunucuBaglantisi(url);

            Log.d("JSON_RESPONSE ",jsonString);



            //işlem gerçekleştirilirken yapılan işlemler
            if (jsonString !=null){
                //json sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);
                    Log.d("json object bölümü : ","çalıştı");

                        JSONArray marketler = jsonObject.getJSONArray("Marketler");
                        //JSONArray Migros = jsonObject.getJSONArray("Migros");
                        Log.d("json array bölümü : ","çalıştı");
                        for (int i = 0; i<marketler.length();i++){
                            //Her bir icecek bloğu object olarak geçiyor.
                            //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                            JSONObject calfourSaJSONObject= marketler.getJSONObject(i);
                            Log.d("Marketler : ",calfourSaJSONObject.toString());
                            String isim=calfourSaJSONObject.getString("isim");
                            String foto=calfourSaJSONObject.getString("foto_path");
                            //Double enlem=Double.valueOf(calfourSaJSONObject.getString("enlem"));
                            //Double boylam =Double.valueOf(calfourSaJSONObject.getString("boylam"));
                            Integer sube_sayisi = Integer.valueOf(marketler.length());
                            Market CalfourMarket = new Market(isim,foto,sube_sayisi);
                            marketArrayList.add(CalfourMarket);

                            //Log.d("STR Bilgi",str);
                        }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d("Json_Response", "Veri yok");
            }
            return null;
        }
    }
}