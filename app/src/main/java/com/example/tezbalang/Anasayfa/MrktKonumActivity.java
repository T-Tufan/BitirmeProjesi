package com.example.tezbalang.Anasayfa;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.example.tezbalang.Anasayfa.Model.HttpHandler;
import com.example.tezbalang.Anasayfa.Model.Market;
import com.example.tezbalang.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MrktKonumActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int REQUEST_LOCATION = 90;
    public HttpHandler httpHandler;
    ProgressDialog progressDialog;
    ArrayList<String> aciklamaList = new ArrayList<>();
    ArrayList<Double> boylamList = new ArrayList<>();
    ArrayList<Double> enlemList = new ArrayList<>();
    ArrayList<Bitmap> logoList = new ArrayList<>();
    public static String url = "https://raw.githubusercontent.com/T-Tufan/Tez/master/marketler.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrkt_konum);

        //Toast.makeText(getApplicationContext(),"Market : "+aciklamaList.get(0),Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new getSingleMarketInfo().execute();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Kamera zoom kontrolü sağlanıyor.
        ZoomControls zoomControls=(ZoomControls)findViewById(R.id.git_zoom);
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        //Harita görünümü ayarlanıyor.
        final Button btn_MType = (Button)findViewById(R.id.btn_uydu);
        btn_MType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    btn_MType.setText("Varsayılan");
                }
                else
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                btn_MType.setText("Uydu");
            }
        });
        //Haritada adres arama yapılıyor.
        Button btngit = (Button)findViewById(R.id.btn_git);
        btngit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editLocation = (EditText)findViewById(R.id.git_location);
                String location = editLocation.getText().toString();

                if(editLocation!=null && !location.equals("")){
                    List<Address> addressList = null;
                    Geocoder geocoder = new Geocoder(MrktKonumActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }


                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Aranan Konum : "+location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mMap.setTrafficEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        }
        mMap.setOnMyLocationChangeListener(new ChangeLocation());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    mMap.setMyLocationEnabled(true);
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "İzinler Verilmedi",Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public class ChangeLocation implements GoogleMap.OnMyLocationChangeListener{
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
            Projection projection = mMap.getProjection();
            Point point = projection.toScreenLocation(loc);

            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(loc);
            circleOptions.fillColor(Color.BLACK);
            circleOptions.radius(10);
            circleOptions.strokeWidth(1);
            mMap.addCircle(circleOptions);

        }
    }
    private class  getSingleMarketInfo extends AsyncTask<Void,Void,Void> {
        ArrayList<Market> marketArrayList = new ArrayList<>();
        ArrayList<JSONArray> jsonArrayArrayListMarket = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //işlem başladığında çalışan kısımlar...
            progressDialog = new ProgressDialog(MrktKonumActivity.this);
            progressDialog.setMessage("Market verileri Çekiliyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = getIntent();
            String urunlerpage_gelen_market = intent.getStringExtra("UrunlerPage_Market");
            String marktpage_gelen_market = intent.getStringExtra("MarketPage_Market");

            Log.d("Gelen marketler :  ",urunlerpage_gelen_market+" "+marktpage_gelen_market);
            for (int i=0;i<marketArrayList.size();i++){
                Log.d("Okunan deger ",String.valueOf(i));
                if (urunlerpage_gelen_market !=null && urunlerpage_gelen_market.equals(marketArrayList.get(i).getMarket_isim())){
                    Log.d("Okunan Market ",urunlerpage_gelen_market);
                    Log.d("Market İsim",marketArrayList.get(0).getMarket_isim());
                    aciklamaList.add(marketArrayList.get(i).getMarket_aciklama());
                    boylamList.add(marketArrayList.get(i).getMarket_boylam());
                    enlemList.add(marketArrayList.get(i).getMarket_enlem());
                    try {
                        URL adres = new URL(marketArrayList.get(i).getMarket_foto());
                        Bitmap image = BitmapFactory.decodeStream(adres.openConnection().getInputStream());
                        logoList.add(image);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {

                    }
                }
                else if (marktpage_gelen_market !=null && marktpage_gelen_market.equals(marketArrayList.get(i).getMarket_isim())){
                    Log.d("Okunan Market ",marktpage_gelen_market);
                    Log.d("Market İsim",marketArrayList.get(0).getMarket_isim());
                    aciklamaList.add(marketArrayList.get(i).getMarket_aciklama());
                    boylamList.add(marketArrayList.get(i).getMarket_boylam());
                    enlemList.add(marketArrayList.get(i).getMarket_enlem());
                    try {
                        URL adres = new URL(marketArrayList.get(i).getMarket_foto());
                        Bitmap image = BitmapFactory.decodeStream(adres.openConnection().getInputStream());
                        logoList.add(image);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.d("Eşlesme Durumu","Market ismi eşleşmedi");
                }
            }
            Log.d("Acıklama Listesi ",String.valueOf(aciklamaList.size()));
            Log.d("Boylam Listesi ",boylamList.get(0)+" "+boylamList.get(1)+" "+boylamList.get(2));
            Log.d("Enlem Listesi ",enlemList.get(0)+" "+enlemList.get(1)+" "+enlemList.get(2));

            if (progressDialog.isShowing()){
                progressDialog.dismiss();//progress dialog kapatılır.
            }

            //Konumlar bu fonksiyon ile yerleştiriliyor.Önemli !!!!!!!!!!!!!!
            Konumlar(mMap,aciklamaList,enlemList,boylamList,logoList);
        }


        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            httpHandler = new HttpHandler();//içerisine url olarak verdiğim websitenin kaynağını döner.
            String jsonString = httpHandler.sunucuBaglantisi(url);
            Log.d("JSON_RESPONSE ",jsonString);

           /* Intent intent = getIntent();
            String str = intent.getStringExtra("jsonArray");*/
            //işlem gerçekleştirilirken yapılan işlemler
            if (jsonString !=null){
                //sayfa iceriği boş değilse
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);

                    JSONArray calfour_Sa = jsonObject.getJSONArray("Calfour-Sa");
                    JSONArray migros = jsonObject.getJSONArray("Migros");

                    jsonArrayArrayListMarket.add(calfour_Sa);
                    jsonArrayArrayListMarket.add(migros);

                    for (int k = 0; k<jsonArrayArrayListMarket.size(); k++){
                        Log.d("Json array dizi boyutu ", String.valueOf(jsonArrayArrayListMarket.size()));
                        for (int i = 0; i<jsonArrayArrayListMarket.get(k).length();i++){
                            Log.d("Json array dizi elemanları ", String.valueOf(jsonArrayArrayListMarket.get(k)));
                            //Her bir adres verisine object olarak geçiyor.
                            //Her bir market verisine döngü içersinde tek tek ulaşılıyor.
                            JSONObject marketjson= jsonArrayArrayListMarket.get(k).getJSONObject(i);
                            String isim=marketjson.getString("isim");
                            String aciklama=marketjson.getString("aciklama");
                            Double enlem=Double.valueOf(marketjson.getString("enlem"));
                            Double boylam =Double.valueOf(marketjson.getString("boylam"));
                            String foto_path = marketjson.getString("foto_path");

                            Market marktInfo = new Market(isim,aciklama,boylam,enlem,foto_path);
                            marketArrayList.add(marktInfo);
                        }
                    }
                    /*for (int i = 0; i<calfour_Sa.length();i++){
                        //Her bir adres verisine object olarak geçiyor.
                        //Her bir market verisine döngü içersinde tek tek ulaşılıyor.
                        JSONObject calfourjson= calfour_Sa.getJSONObject(i);
                        String isim=calfourjson.getString("isim");
                        String aciklama=calfourjson.getString("aciklama");
                        Double enlem=Double.valueOf(calfourjson.getString("enlem"));
                        Double boylam =Double.valueOf(calfourjson.getString("boylam"));
                        String foto_path = calfourjson.getString("foto_path");
                        Market CalfourMarket = new Market(isim,aciklama,boylam,enlem,foto_path);
                        marketArrayList.add(CalfourMarket);
                    }
                    for (int i = 0; i<migros.length();i++){
                        //Her bir adres verisine object olarak geçiyor.
                        //Her bir market verisine döngü içersinde tek tek ulaşılıyor.
                        JSONObject migrosjson= migros.getJSONObject(i);
                        String isim=migrosjson.getString("isim");
                        String aciklama=migrosjson.getString("aciklama");
                        Double enlem=Double.valueOf(migrosjson.getString("enlem"));
                        Double boylam =Double.valueOf(migrosjson.getString("boylam"));
                        String foto_path = migrosjson.getString("foto_path");
                        Market MigrosMarket = new Market(isim,aciklama,boylam,enlem,foto_path);
                        marketArrayList.add(MigrosMarket);
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
    public void Konumlar (GoogleMap mMap, ArrayList<String> aciklamaList, ArrayList<Double> enlemList, ArrayList<Double> boylamList, ArrayList<Bitmap> icon){
        onStart();
        for (int i=0;i<aciklamaList.size();i++){

            Bitmap kucukLogo = Bitmap.createScaledBitmap(logoList.get(i), 100, 100,false);
            Log.d("Harita Konum Sayısı ",String.valueOf(i));
            LatLng konum = new LatLng(enlemList.get(i),boylamList.get(i));
            mMap.addMarker(new MarkerOptions().position(konum).title(aciklamaList.get(i)).icon(BitmapDescriptorFactory.fromBitmap(kucukLogo)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(konum));
        }
    }

}