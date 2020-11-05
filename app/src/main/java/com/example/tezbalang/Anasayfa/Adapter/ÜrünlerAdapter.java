package com.example.tezbalang.Anasayfa.Adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezbalang.Anasayfa.AnasayfaActivity;
import com.example.tezbalang.Anasayfa.BarkodOkumaActivity;
import com.example.tezbalang.Anasayfa.Model.Ürünler;
import com.example.tezbalang.Anasayfa.MrktKonumActivity;
import com.example.tezbalang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ÜrünlerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Ürünler> ürünlers;
    LayoutInflater layoutInflater;

    public ÜrünlerAdapter(Activity activity, ArrayList<Ürünler> ürünlers){
        this.context=activity;
        this.ürünlers = ürünlers;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ürünlers.size();
    }

    @Override
    public Ürünler getItem(int position) {
        return ürünlers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.urun_detay_card_tasarim, null);

        ImageView Foto2 = (ImageView) view.findViewById(R.id.urunfoto);
        TextView Isim2 = (TextView) view.findViewById(R.id.urunisim);
        TextView Fiyat = (TextView) view.findViewById(R.id.urunfiyat);
        final TextView Market = (TextView) view.findViewById(R.id.urunmarket);
        Button Konum = (Button) view.findViewById(R.id.mrktkonum);
        TextView Acıklama = (TextView) view.findViewById(R.id.urunacıklama);

        Picasso.with(context).load(ürünlers.get(position).getFoto_path()).into(Foto2);
        String fiyat= new Double(ürünlers.get(position).getFiyat()).toString();
        Isim2.setText(ürünlers.get(position).getIsim());
        Market.setText(Html.fromHtml(ürünlers.get(position).getMarket()).toString());
        Market.setMovementMethod(LinkMovementMethod.getInstance());
        final String market_isim = ürünlers.get(position).getMarket();
        Konum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(context, MrktKonumActivity.class);
                ıntent.putExtra("UrunlerPage_Market",market_isim);
                context.startActivity(ıntent);
                Log.d("Market ismi : ",market_isim);
            }
        });
        //Stok.setText("Stok : "+ürünlers.get(position).getStok());
        Acıklama.setText(ürünlers.get(position).getAcıklama());
        Fiyat.setText("Fiyat : "+fiyat+" TL");
        Foto2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

        return view;
    }
}
