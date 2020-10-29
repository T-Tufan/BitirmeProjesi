package com.example.tezbalang.Anasayfa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tezbalang.R;
public class KategorilerActivity extends AppCompatActivity {
    public LinearLayout icecekler_buton,etvesut_buton,teknolojik_buton,kisiselbakım_buton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_listele);
        icecekler_buton=findViewById(R.id.iceceklerLayout);
        etvesut_buton = findViewById(R.id.etvesutLayout);
        kisiselbakım_buton = findViewById(R.id.kisiselbakımLayout);
        icecekler_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"İçecekler listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), KategoriIcerikActivity.class);
                ıntent.putExtra("jsonArray","icecekler");
                startActivity(ıntent);
            }
        });
        etvesut_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Et ve süt ürünleri listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), KategoriIcerikActivity.class);
                ıntent.putExtra("jsonArray","Et ve Süt Ürünleri");
                startActivity(ıntent);
            }
        });
        kisiselbakım_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Kişisel bakım ürünleri listeleniyor...",Toast.LENGTH_SHORT).show();
                Intent ıntent=new Intent(getApplicationContext(), KategoriIcerikActivity.class);
                ıntent.putExtra("jsonArray","KisiselBakim");
                startActivity(ıntent);
            }
        });
    }
}
