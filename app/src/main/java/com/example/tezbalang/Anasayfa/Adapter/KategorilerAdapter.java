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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezbalang.Anasayfa.Model.Kategoriler;
import com.example.tezbalang.Anasayfa.KategoriIcerikActivity;
import com.example.tezbalang.Anasayfa.Model.Ürünler;
import com.example.tezbalang.Anasayfa.UrunDetayActivity;
import com.example.tezbalang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KategorilerAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Kategoriler> kategorilers;
    LayoutInflater layoutInflater;
    private List<Kategoriler> kategorilersFull;

    public KategorilerAdapter(Activity activity, ArrayList<Kategoriler> kategorilers) {
        this.context = activity;
        this.kategorilers = kategorilers;
        kategorilersFull = new ArrayList<>(kategorilers);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        return kategorilers.size();
    }

    @Override
    public Kategoriler getItem(int position) {
        return kategorilers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            View view = layoutInflater.inflate(R.layout.urunler_card_tasarim, null);

            ImageView Foto = (ImageView) view.findViewById(R.id.urun1foto);
            TextView Isim = (TextView) view.findViewById(R.id.urun1isim);



            Isim.setText(kategorilers.get(position).getIsim());
            Picasso.with(context).load(kategorilers.get(position).getFoto_path()).into(Foto);

            Foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String barc= kategorilers.get(position).getBarkod();
                    String foto_isim = kategorilers.get(position).getIsim();
                    String ktgr = kategorilers.get(position).getKategori();
                    Toast.makeText(context, kategorilers.get(position).getIsim() + " listeleniyor...", Toast.LENGTH_SHORT).show();
                    Intent ıntent = new Intent(context, UrunDetayActivity.class);
                    ıntent.putExtra("barkod",barc);
                    ıntent.putExtra("ktgr",ktgr);
                    context.startActivity(ıntent);
                }
            });
            return view;


    }

    @Override
    public Filter getFilter() {
        return kategorilerFilter;
    }
    private Filter kategorilerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Kategoriler>  filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() ==0){
                filteredList.addAll(kategorilersFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Kategoriler item : kategorilersFull){
                    if (item.getIsim().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                        Log.d("ADFJFNDAINFDADDAD",item.getIsim());
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            kategorilers.clear();
            kategorilers.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}