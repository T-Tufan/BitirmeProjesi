package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.tezbalang.Anasayfa.Adapter.ÜrünlerAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Ürünler;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarkodOkumaActivity extends AppCompatActivity {
    private ÜrünlerAdapter urunadapter;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog; //Veri çekilirken dönen yuvarlak
    ListView list;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/urunler.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barkod_okuma);

        list = (ListView) findViewById(R.id.liste);

        new getProductWithBarcode().execute();//async task bölümünün classı çağırılır.
    }
    private class  getProductWithBarcode extends AsyncTask<Void,Void,Void> {
        ArrayList<Ürünler> ürünlerArrayList = new ArrayList<>();
        ArrayList<JSONArray> jsonArrayArrayListBarcode = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
            progressDialog = new ProgressDialog(BarkodOkumaActivity.this);
            progressDialog.setMessage("Ürünler Listeleniyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //işlem tamamlandığında
            super.onPostExecute(aVoid);
            urunadapter = new ÜrünlerAdapter(BarkodOkumaActivity.this,ürünlerArrayList);
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
            String barc = intent.getStringExtra("BarkodNo");
            String str = intent.getStringExtra("ktgr");
           /* Intent intent = getIntent();
            String str = intent.getStringExtra("jsonArray");*/
            //işlem gerçekleştirilirken yapılan işlemler
            if (jsonString !=null){
                //sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);

                    JSONArray icecekler = jsonObject.getJSONArray("İcecekler");
                    JSONArray et_ve_süt_ürünleri = jsonObject.getJSONArray("Et ve Süt Ürünleri");
                    JSONArray teknoloji = jsonObject.getJSONArray("Teknoloji Ürünleri");
                    JSONArray kisiselBakim = jsonObject.getJSONArray("KisiselBakim");

                    jsonArrayArrayListBarcode.add(icecekler);
                    jsonArrayArrayListBarcode.add(et_ve_süt_ürünleri);
                    jsonArrayArrayListBarcode.add(teknoloji);
                    jsonArrayArrayListBarcode.add(kisiselBakim);
                    for (int k = 0; k<jsonArrayArrayListBarcode.size(); k++){
                        for (int i = 0; i<jsonArrayArrayListBarcode.get(k).length();i++){
                            //Her bir icecek bloğu object olarak geçiyor.
                            //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                            JSONObject ürüns= jsonArrayArrayListBarcode.get(k).getJSONObject(i);
                            String barkod=ürüns.getString("barkod");
                            String isim=ürüns.getString("isim");
                            String foto_path=ürüns.getString("foto-path");
                            String market = ürüns.getString("market");
                            String stok= ürüns.getString("stok");
                            double fiyat = ürüns.getDouble("fiyat");
                            String kategori=ürüns.getString("kategori");
                            String acıklama = ürüns.getString("aciklama");

                            Ürünler ürün = new Ürünler(barkod,fiyat,isim,foto_path,market,stok,kategori,acıklama);

                            Log.d("Ürün barkod",ürün.getBarkod());
                            Log.d("Barkod Bilgi",barc);
                            if (ürün.getBarkod().equals(barc)){
                                ürünlerArrayList.add(ürün);
                                Log.d("Barkod Eşleşti",barc+" : "+ürün.getBarkod());
                            }
                            else{
                                Log.d("Ürün Bilgi","Ürün barkodu eşleşmedi");
                            }
                        }
                    }
                    /*for (int i = 0; i<ürünler.length();i++){
                        //Her bir icecek bloğu object olarak geçiyor.
                        //Her bir içecek bloğuna döngü içersinde tek tek ulaşılıyor.
                        JSONObject ürüns= ürünler.getJSONObject(i);
                        String barkod=ürüns.getString("barkod");
                        String isim=ürüns.getString("isim");
                        String foto_path=ürüns.getString("foto-path");
                        String market = ürüns.getString("market");
                        String stok= ürüns.getString("stok");
                        double fiyat = ürüns.getDouble("fiyat");
                        String kategori=ürüns.getString("kategori");
                        String acıklama = ürüns.getString("aciklama");

                        Ürünler ürün = new Ürünler(barkod,fiyat,isim,foto_path,market,stok,kategori,acıklama);

                        Log.d("Ürün barkod",ürün.getBarkod());
                        Log.d("Barkod Bilgi",barc);
                        if (ürün.getBarkod().equals(barc)){
                            ürünlerArrayList.add(ürün);
                            Log.d("Barkod Eşleşti",barc+" : "+ürün.getBarkod());
                        }
                        else{
                            Log.d("Ürün Bilgi","Ürün barkodu eşleşmedi");
                        }
                    }*/

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
