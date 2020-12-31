package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.tezbalang.Anasayfa.Adapter.UrunlerDetayAdapter;
import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Ürünler;
import com.example.tezbalang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UrunDetayActivity extends AppCompatActivity {
    private UrunlerDetayAdapter urunadapter;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog; //Veri çekilirken dönen yuvarlak
    ListView list;

    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/urunler.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detay);

        list = (ListView) findViewById(R.id.liste);

        new getProductDetails().execute();//async task bölümünün classı çağırılır.
    }
    private class  getProductDetails extends AsyncTask<Void,Void,Void> {
        ArrayList<Ürünler> ürünlerArrayList = new ArrayList<>();
        ArrayList<JSONArray> jsonArrayArrayList2 = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
            progressDialog = new ProgressDialog(UrunDetayActivity.this);
            progressDialog.setMessage("Ürünler Listeleniyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //işlem tamamlandığında
            super.onPostExecute(aVoid);
            urunadapter = new UrunlerDetayAdapter(UrunDetayActivity.this,ürünlerArrayList);
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

            //Diğer sayfalardan gelen veriler alınıp bir değişkene atılıyor.
            String barc = intent.getStringExtra("barkod");
            String foto_isim = intent.getStringExtra("Foto_urun");
            String str = intent.getStringExtra("ktgr");

            //Diğer sayfalardan gelen veriler log kayıtlarında kontrol ediliyor.
            Log.d("Barkod veya Fotoğraf ismi veya ktgr : ",barc+" "+foto_isim+" "+str);


            //internetten json veriler çekildikten sonraki işlemler
            if (jsonString !=null){
                //json sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);
                    Log.d("json object bölümü : ","çalıştı");

                    //Ürünler veya Kategoriler bölümünden gelinmiş ise
                    if (str !=null){

                        JSONArray ürünler = jsonObject.getJSONArray(str);
                        Log.d("json array bölümü : ","çalıştı");
                        for (int i = 0; i<ürünler.length();i++){

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
                            //Log.d("STR Bilgi",str);
                            if (ürün.getBarkod().equals(barc)){
                                ürünlerArrayList.add(ürün);
                                Log.d("Barkod Eşleşti",barc+" : "+ürün.getBarkod());
                            }
                            else{
                                Log.d("Ürün Bilgi","Ürün barkodu eşleşmedi");
                            }
                    }
                    }
                    //Fotoğraftan tanıma bölümünden gelinmiş ise burası çalışır.
                    else {
                        jsonArrayArrayList2 = BarkodOkumaActivity.JsonArraysMethod(jsonObject,jsonArrayArrayList2,"Ürünler");

                        for (int k = 0; k<jsonArrayArrayList2.size(); k++) {
                            Log.d("json array bölümü : ","çalıştı");
                            for (int i = 0; i<jsonArrayArrayList2.get(k).length();i++) {

                                JSONObject ürüns = jsonArrayArrayList2.get(k).getJSONObject(i);

                                String barkod = ürüns.getString("barkod");
                                String isim = ürüns.getString("isim");
                                String model_isim = ürüns.getString("model-ismi");
                                String foto_path = ürüns.getString("foto-path");
                                String market = ürüns.getString("market");
                                String stok = ürüns.getString("stok");
                                double fiyat = ürüns.getDouble("fiyat");
                                String kategori = ürüns.getString("kategori");
                                String acıklama = ürüns.getString("aciklama");

                                Ürünler ürün = new Ürünler(barkod, fiyat, isim,model_isim, foto_path, market, stok, kategori, acıklama);
                                if (ürün.getModel_isim().equals(foto_isim)){
                                    ürünlerArrayList.add(ürün);
                                    Log.d("Foto ismi Eşleşti",foto_isim+" : "+ürün.getModel_isim());
                                }
                                else{
                                    Log.d("Ürün Bilgi","Fotoğrafa ait ürün bulunamadı.");
                                }
                            }
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
