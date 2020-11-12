package com.example.tezbalang.Anasayfa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.net.Uri;
import android.provider.Settings;
import android.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tezbalang.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import pub.devrel.easypermissions.EasyPermissions;

public class AnasayfaActivity extends AppCompatActivity {
    public CardView urunler_buton,kategori_buton,barkod_buton,foto_buton,markt_buton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        //Gerekli galeri ve kamera izinlei alınıyor...
        EasyPermissions.requestPermissions(this, "Uygulama içi kamera kullanımı için izin verir misiniz ? ",0,Manifest.permission.CAMERA);
        //Diğer sayfalara butonlar ile yönlendirme yapılıyor.
        foto_buton = findViewById(R.id.kamera);
        markt_buton = findViewById(R.id.marketler);
        kategori_buton = findViewById(R.id.kategoriler);
        urunler_buton = findViewById(R.id.urunler);
        barkod_buton = findViewById(R.id.barkod);
        urunler_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ürünler listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), TumUrunlerActivity.class);
                startActivity(ıntent);
            }
        });
        foto_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ürün fotosunu çekiniz",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), FotoTanimaActivity.class);
                startActivity(ıntent);
            }
        });
        markt_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Marketler listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), MrktActivity.class);
                startActivity(ıntent);
            }
        });
        kategori_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Kategoriler listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), KategorilerActivity.class);
                startActivity(ıntent);
            }
        });
        barkod_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bu activity içinde çalıştırıyoruz.
                IntentIntegrator integrator = new IntentIntegrator(AnasayfaActivity.this);
                //Kütüphanede bir kaç kod tipi var biz hepsini tarayacak şekilde çalıştırdık.
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                //şeklindede sadece qr code taratabilirsiniz.
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                //Kamera açıldığında aşağıda yazı gösterecek
                integrator.setPrompt("Ürünü Tara");
                //telefonun kendi kamerasını kullandırıcaz
                integrator.setCameraId(0);
                //okuduğunda 'beep' sesi çıkarır
                integrator.setBeepEnabled(true);
                //okunan barkodun image dosyasını kaydediyor
                integrator.setBarcodeImageEnabled(true);
                //scan başlatılıyor
                integrator.initiateScan();
            }
        });
    }

    //Barkod Okumadan dönen sonuç kontrol ediliyor.Veri var ise diğer aktivite sayfasına geçiliyor.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(AnasayfaActivity.this,"Barkod okunamadı",Toast.LENGTH_LONG).show();

            } else {
                Intent  ıntent=new Intent(AnasayfaActivity.this, BarkodOkumaActivity.class);
                ıntent.putExtra("BarkodNo",result.getContents());
                Log.d("OkunanBarkod",result.getContents());
                startActivity(ıntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
