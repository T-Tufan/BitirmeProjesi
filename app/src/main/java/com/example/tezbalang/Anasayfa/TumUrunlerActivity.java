package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.tezbalang.Anasayfa.Adapter.KategorilerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Kategoriler;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TumUrunlerActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private KategorilerAdapter urunadapter;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog; //Veri çekilirken dönen yuvarlak
    ListView list;
    ArrayList<String> aramaListesi = new ArrayList<>();
    SearchView aramakutusu;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/kategoriler.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_urunler);
        aramakutusu = (SearchView) findViewById(R.id.make_search);
        list = (ListView) findViewById(R.id.liste);
        //async task bölümünün classı çağırılır.

        new getRecipe().execute();
    }

    private void setupArama(){
        aramakutusu.setIconifiedByDefault(false);
        aramakutusu.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        aramakutusu.setSubmitButtonEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


   /*@SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu_arama,menu);
        aramakutusu= (SearchView) menu.findItem(R.id.make_search).getActionView();
        aramakutusu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Text Submit : ",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Text Cahnge : ",newText);
                return false;
            }

        });
        return true;
    }*/

    private class  getRecipe extends AsyncTask<Void,Void,Void>  {
        ArrayList<Kategoriler> ürünlerArrayList = new ArrayList<>();


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
            urunadapter = new KategorilerAdapter(TumUrunlerActivity.this,ürünlerArrayList);
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

           /* Intent intent = getIntent();
            String str = intent.getStringExtra("jsonArray");*/
            //işlem gerçekleştirilirken yapılan işlemler
            if (jsonString !=null){
                //sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);

                    JSONArray icecekler = jsonObject.getJSONArray("icecekler");
                    JSONArray et_ve_süt_ürünleri = jsonObject.getJSONArray("Et ve Süt Ürünleri");
                    JSONArray kisiselBakim = jsonObject.getJSONArray("KisiselBakim");
                    for (int i = 0; i<icecekler.length();i++){
                        //Her bir icecek bloğu object olarak geçiyor.
                        //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                        JSONObject ürüns= icecekler.getJSONObject(i);
                        String barkod=ürüns.getString("barkod");
                        String isim=ürüns.getString("isim");
                        String foto_path=ürüns.getString("foto-path");
                        String kategori =ürüns.getString("kategori");
                        Kategoriler ürün = new Kategoriler(barkod,isim,foto_path,kategori);
                        aramaListesi.add(isim);
                        Log.d("ürün listesi ",aramaListesi.get(i));
                        ürünlerArrayList.add(ürün);
                    }
                    for (int j = 0; j<et_ve_süt_ürünleri.length();j++){
                        //Her bir icecek bloğu object olarak geçiyor.
                        //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                        JSONObject ürüns= et_ve_süt_ürünleri.getJSONObject(j);
                        String barkod=ürüns.getString("barkod");
                        String isim=ürüns.getString("isim");
                        String foto_path=ürüns.getString("foto-path");
                        String kategori =ürüns.getString("kategori");
                        Kategoriler ürün = new Kategoriler(barkod,isim,foto_path,kategori);
                        aramaListesi.add(isim);

                        ürünlerArrayList.add(ürün);
                    }
                    for (int k = 0; k<kisiselBakim.length();k++){
                        //Her bir icecek bloğu object olarak geçiyor.
                        //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                        JSONObject ürüns= kisiselBakim.getJSONObject(k);
                        String barkod=ürüns.getString("barkod");
                        String isim=ürüns.getString("isim");
                        String foto_path=ürüns.getString("foto-path");
                        String kategori =ürüns.getString("kategori");
                        aramaListesi.add(isim);
                        Kategoriler ürün = new Kategoriler(barkod,isim,foto_path,kategori);
                        ürünlerArrayList.add(ürün);
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
