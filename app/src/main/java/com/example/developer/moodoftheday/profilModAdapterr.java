package com.example.developer.moodoftheday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class profilModAdapterr extends BaseAdapter {

    List<modumProfil> modDurumlarıList= new ArrayList<modumProfil>();
    LayoutInflater modInflater;
    Context context;

    PopupMenu popup;
    public profilModAdapterr(Context activity, List<modumProfil> modDurumlarıList) {
        modInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.modDurumlarıList = modDurumlarıList;
        this.context = activity;
    }
    @Override
    public int getCount() {
        return modDurumlarıList.size();
    }

    @Override
    public Object getItem(int position) {
        return modDurumlarıList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lineView;
        lineView = modInflater.inflate(R.layout.activity_profil_mod_adapterr, null);
        EditText PmodAdi = (EditText) lineView.findViewById(R.id.PmodAdi);
        final EditText PmodDurumu = (EditText) lineView.findViewById(R.id.PmodDurumu);
        TextView tarih= (TextView) lineView.findViewById(R.id.tarih);
        TextView saat=  (TextView) lineView.findViewById(R.id.saat);
        ImageView PmodResmi = (ImageView) lineView.findViewById(R.id.PmodResmi);
        ImageButton PmodPaylasilanResim = (ImageButton) lineView.findViewById(R.id.paylasilacakResim);
        final ImageButton  menu = (ImageButton) lineView.findViewById(R.id.menu);




       final modumProfil mod = modDurumlarıList.get(position);
        PmodAdi.setText(mod.getModAdi());
        PmodDurumu.setText(mod.getPaylasilanDurum());
        PmodResmi.setImageResource(mod.getProfResmi());
        Glide.with(context).load(mod.getResimUrl()).into(PmodPaylasilanResim);
        tarih.setText(mod.getTarih());
        saat.setText(mod.getSaat());


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, menu);

                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gizliligiDzenle:
                            //   gizliliği düzenle fonsiyonu gelecek
                                break;
                            case R.id.sil:
                                //sil fonksiyonu gelecek
                                case R.id.düzenle:
                               //     düzenle fonsiyonu gelecek
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });


        return lineView;
    }
}



