package com.example.developer.moodoftheday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityAra extends AppCompatActivity {
DatabaseReference dbref,ArkadasListesi;
    List<Kisiler> kisi=new ArrayList<Kisiler>();
    List<String> elemanlar=new ArrayList<>();
    ListView araListe;
    Kisiler ara;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ara);

         Intent araFake=getIntent();
        araFake.getExtras().getString("ara");

        dbref = FirebaseDatabase.getInstance().getReference("users");
        ArkadasListesi=FirebaseDatabase.getInstance().getReference("ArkadasListesi");
        user = firebaseAuth.getInstance().getCurrentUser();



        dbref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ara = postSnapshot.getValue(Kisiler.class);
                    ara.getName();
                    ara.getKisiResmi();
                    ara.getProfilGizlilik();
                    kisi.add(ara);
                }
                araListe = (ListView) findViewById(R.id.araListe);
                araAdapter adapter = new araAdapter(ActivityAra.this, kisi);
                araListe.setAdapter(adapter);


                araListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        if(kisi.get(position).getProfilGizlilik().equals("Arkadaşlar")){


                                ArkadasListesi.child(kisi.get(position).getId()).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        elemanlar.add(postSnapshot.getKey());


                                    }
                                        if (elemanlar.contains(user.getUid())) {

                                            Intent git = new Intent(ActivityAra.this, ActivityArkadaslar.class);
                                            startActivity(git);

                                        } else if(!elemanlar.contains(user.getUid())) {
                                            Intent git = new Intent(ActivityAra.this, ActivityArkadasDegil.class);
                                            git.putExtra("profResmi", kisi.get(position).getKisiResmi());
                                            git.putExtra("kisiAdi", kisi.get(position).getName());
                                            git.putExtra("kisiId", kisi.get(position).getId());
                                            git.putExtra("gönderenKisininId", user.getUid());


                                            startActivity(git);

                                        }
                                    else if(elemanlar.isEmpty()){

                                            Intent git = new Intent(ActivityAra.this, ActivityArkadasDegil.class);
                                            git.putExtra("profResmi", kisi.get(position).getKisiResmi());
                                            git.putExtra("kisiAdi", kisi.get(position).getName());
                                            git.putExtra("kisiId", kisi.get(position).getId());
                                            git.putExtra("suandakiKisininId", user.getUid());


                                            startActivity(git);



                                        }






                                    }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                       }}
                });


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
