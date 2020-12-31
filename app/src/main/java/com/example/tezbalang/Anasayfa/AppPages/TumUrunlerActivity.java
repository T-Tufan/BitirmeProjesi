package com.example.tezbalang.Anasayfa.AppPages;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.tezbalang.Anasayfa.Adapter.UrunlerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.ÜrünGenelBilgi;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TumUrunlerActivity extends AppCompatActivity{
    private UrunlerAdapter urunadapter;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog; //Veri çekilirken dönen yuvarlak
    ListView list;
    SearchView aramakutusu;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/kategoriler.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_urunler);
        list = (ListView) findViewById(R.id.liste);
        //async task bölümünün classı çağırılır.

        new getAllProducts().execute();
    }

    private class  getAllProducts extends AsyncTask<Void,Void,Void>  {
        ArrayList<ÜrünGenelBilgi> ürünlerArrayList = new ArrayList<>();
        ArrayList<JSONArray> jsonArrayArrayList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
            progressDialog = new ProgressDialog(TumUrunlerActivity.this);
            progressDialog.setMessage("Ürünler Listeleniyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //işlem tamamlandığında
            super.onPostExecute(aVoid);
            urunadapter = new UrunlerAdapter(TumUrunlerActivity.this,ürünlerArrayList);
            list.setAdapter(urunadapter);

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
                //sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);
                    //Json dosyalarının ilgili kısımları alınır.
                    jsonArrayArrayList = BarkodOkumaActivity.JsonArraysMethod(jsonObject,jsonArrayArrayList,"Ürünler");

                    for (int k = 0; k<jsonArrayArrayList.size(); k++){
                        Log.d("Json array dizi boyutu ", String.valueOf(jsonArrayArrayList.size()));
                        for (int i = 0; i<jsonArrayArrayList.get(k).length();i++){
                            Log.d("Json array dizi elemanları ", String.valueOf(jsonArrayArrayList.get(k)));
                            //Her bir ürün bloğu object olarak geçiyor.
                            //Her bir ürün bloğuna döngü içersinde tek tek ulaşılıyor.
                            JSONObject ürüns= jsonArrayArrayList.get(k).getJSONObject(i);
                            String barkod=ürüns.getString("barkod");
                            String isim=ürüns.getString("isim");
                            String foto_path=ürüns.getString("foto-path");
                            String kategori =ürüns.getString("kategori");

                            ÜrünGenelBilgi ürün = new ÜrünGenelBilgi(barkod,isim,foto_path,kategori);
                            ürünlerArrayList.add(ürün);
                        }
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
