package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.Toast;


import com.example.tezbalang.Anasayfa.Adapter.KategorilerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Kategoriler;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

        new getKategoriData().execute();//async task bölümünün classı çağırılır.


    }
    //web üzerinde işlem yapıyoruz.
    //Kategorilerilere ait ürünleri çekiyoruz.3 parça olarak çekilecek.Gerceklesme zamanı ,öncesi  ve sonrasında ne olacak.
    private class  getKategoriData extends AsyncTask<Void,Void,Void>{
        ArrayList<Kategoriler> kategorilerArrayList = new ArrayList<>();
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

            urunadapter = new KategorilerAdapter(KategoriIcerikActivity.this,kategorilerArrayList);
            list.setAdapter(urunadapter);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();//progress dialog kapatılır.
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            httpHandler = new HttpHandler();//içerisine url olarak verdiğim websitenin kaynağını döner.
            String jsonString = httpHandler.makeServiceCall(url);
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

                        Kategoriler kategoriler1 = new Kategoriler(barkod,isim,foto_path,kategori);
                        kategorilerArrayList.add(kategoriler1);

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
