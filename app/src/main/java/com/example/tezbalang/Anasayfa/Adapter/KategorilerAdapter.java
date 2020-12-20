package com.example.tezbalang.Anasayfa.Adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezbalang.Anasayfa.Model.ÜrünGenelBilgi;
import com.example.tezbalang.Anasayfa.UrunDetayActivity;
import com.example.tezbalang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KategorilerAdapter extends BaseAdapter {
    Context context;
    ArrayList<ÜrünGenelBilgi> ürünGenelBilgis;
    LayoutInflater layoutInflater;
    ArrayList<String> aramalistesi;
    private List<ÜrünGenelBilgi> kategorilersFull;

    /*public KategorilerAdapter(Activity activity, ArrayList<Kategoriler> kategorilers,ArrayList<String> aramalistesi) {
        this.context = activity;
        this.aramalistesi = aramalistesi;
        this.kategorilers = kategorilers;
        kategorilersFull = new ArrayList<>(kategorilers);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

    }*/
    public KategorilerAdapter(Activity activity, ArrayList<ÜrünGenelBilgi> ürünGenelBilgis) {
        this.context = activity;
        this.ürünGenelBilgis = ürünGenelBilgis;
        kategorilersFull = new ArrayList<>(ürünGenelBilgis);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        if (ürünGenelBilgis.isEmpty()){
            Toast.makeText(context,"Kayıtlı ürün bulunamadı...",Toast.LENGTH_LONG).show();
        }
        return ürünGenelBilgis.size();
    }

    @Override
    public ÜrünGenelBilgi getItem(int position) {
        return ürünGenelBilgis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
           /* ArrayAdapter<String> adapter =  new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1,aramalistesi);
            Log.d("ADAPTER SONUCU :",aramalistesi.get(0)+" "+aramalistesi.get(1).toString());*/
            View view = layoutInflater.inflate(R.layout.urunler_card_tasarim, null);


            ImageView Foto = (ImageView) view.findViewById(R.id.urun1foto);
            TextView Isim = (TextView) view.findViewById(R.id.urun1isim);



            Isim.setText(ürünGenelBilgis.get(position).getIsim());
            Picasso.with(context).load(ürünGenelBilgis.get(position).getFoto_path()).into(Foto);

            Foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String barc= ürünGenelBilgis.get(position).getBarkod();
                    String foto_isim = ürünGenelBilgis.get(position).getIsim();
                    String ktgr = ürünGenelBilgis.get(position).getKategori();
                    Toast.makeText(context, ürünGenelBilgis.get(position).getIsim() + " listeleniyor...", Toast.LENGTH_SHORT).show();
                    Intent ıntent = new Intent(context, UrunDetayActivity.class);
                    ıntent.putExtra("barkod",barc);
                    ıntent.putExtra("ktgr",ktgr);
                    context.startActivity(ıntent);
                }
            });
            return view;


    }
}