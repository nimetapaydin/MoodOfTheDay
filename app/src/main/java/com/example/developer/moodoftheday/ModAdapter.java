package com.example.developer.moodoftheday;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.support.constraint.R.id.parent;


public class ModAdapter extends ArrayAdapter<ModClass> {

     List<ModClass> modList;
     Context context;


    public ModAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ModClass> objects) {
        super(context, resource, objects);
        this.modList = objects;
        this.context=context;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mod_adapter, viewGroup,false);
        }


        TextView ModAd = (TextView) view.findViewById(R.id.modAd);
        TextView modDurumu=(TextView) view.findViewById(R.id.modDurumu);
        ImageView ModRes = (ImageView) view.findViewById(R.id.modRes);

        ModClass mod = modList.get(i);
        ModRes.setImageResource(mod.getModResmi());
        ModAd.setText(mod.getModAdi());
        modDurumu.setText(mod.getModDurumu());
        return view;
    }
}