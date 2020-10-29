package com.example.tezbalang.Anasayfa.Adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tezbalang.Anasayfa.Model.Market;
import com.example.tezbalang.Anasayfa.MrktKonumActivity;
import com.example.tezbalang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarketAdapter extends BaseAdapter {
    Context context;
    ArrayList<Market> markets;
    LayoutInflater layoutInflater;

    public MarketAdapter(Activity activity , ArrayList<Market> markets)  {
        this.context=activity;
        this.markets = markets;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return markets.size();
    }

    @Override
    public Market getItem(int position) {
        return markets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = layoutInflater.inflate(R.layout.mrkt_card_tasarim, null);

        ImageView Market_Foto = (ImageView) view.findViewById(R.id.mrkt_foto);
        TextView Market_Isim = (TextView) view.findViewById(R.id.mrkt_isim);
        //TextView Sube_Sayi = (TextView) view.findViewById(R.id.mrkt_sube_sayi);
        Button Konum = (Button) view.findViewById(R.id.mrkt_konum_btn);

        Picasso.with(context).load(markets.get(position).getMarket_foto()).into(Market_Foto);
        Market_Isim.setText(markets.get(position).getMarket_isim().toString());
        //Sube_Sayi.setText(markets.get(position).getMarket_sube_sayi().toString());
        final String market_isim = markets.get(position).getMarket_isim().toString();
        Konum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(context, MrktKonumActivity.class);
                ıntent.putExtra("MarketPage_Market",market_isim);
                context.startActivity(ıntent);
                Log.d("Market ismi : ",market_isim);
            }
        });
        return view;
    }
}
