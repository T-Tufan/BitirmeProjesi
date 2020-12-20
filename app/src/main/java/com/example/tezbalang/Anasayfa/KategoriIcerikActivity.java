package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;


import com.example.tezbalang.Anasayfa.Adapter.KategorilerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.ÜrünGenelBilgi;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class KategoriIcerikActivity extends AppCompatActivity {
    private KategorilerAdapter urunadapter;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog; //Veri çekilirken dönen yuvarlak
    ListView list;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/kategoriler.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urun_liste);
        list = (ListView) findViewById(R.id.liste);

        new getCategoryData().execute();//async task bölümünün classı çağırılır.


    }
    //web üzerinde işlem yapıyoruz.
    //Kategorilerilere ait ürünleri çekiyoruz.3 parça olarak çekilecek.Gerceklesme zamanı ,öncesi  ve sonrasında ne olacak.
    private class  getCategoryData extends AsyncTask<Void,Void,Void>{
        ArrayList<ÜrünGenelBilgi> ürünGenelBilgiArrayList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
            progressDialog = new ProgressDialog(KategoriIcerikActivity.this);
            progressDialog.setMessage("Ürünler Listeleniyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //işlem tamamlandığında
            super.onPostExecute(aVoid);

            urunadapter = new KategorilerAdapter(KategoriIcerikActivity.this, ürünGenelBilgiArrayList);
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
            Intent intent = getIntent();
            String str = intent.getStringExtra("jsonArray");
            //işlem gerçekleştirilirken yapılan işlemler
            if (jsonString !=null){
                //sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);

                    JSONArray kategoriler = jsonObject.getJSONArray(str);
                    for (int i = 0; i<kategoriler.length();i++){

                        JSONObject kategorilerJSONObject= kategoriler.getJSONObject(i);
                        String barkod=kategorilerJSONObject.getString("barkod");
                        String isim=kategorilerJSONObject.getString("isim");
                        String foto_path=kategorilerJSONObject.getString("foto-path");
                        String kategori = kategorilerJSONObject.getString("kategori");

                        ÜrünGenelBilgi ürünGenelBilgi1 = new ÜrünGenelBilgi(barkod,isim,foto_path,kategori);
                        ürünGenelBilgiArrayList.add(ürünGenelBilgi1);

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
