package com.example.developer.moodoftheday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.Toast;


public class ActivityAnaSayfa extends AppCompatActivity {
Button ara,profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        ara=(Button) findViewById(R.id.ara);

        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent git=new Intent(getApplicationContext(),ActivityAra.class);
                startActivity(git);


            }
        });

        Intent alındı = getIntent();
        final String alınan = alındı.getExtras().getString("gelecekOlanKisi");

        profil=(Button) findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ac=new Intent(getApplicationContext(),ActivityProfilSayfasi.class);
                ac.putExtra("gelecekOlanKisi",alınan);
                startActivity(ac);
            }
        });

    }

    public void click(View view) {
        Toast.makeText(this, "OKEYt", Toast.LENGTH_SHORT).show();
    }
}
