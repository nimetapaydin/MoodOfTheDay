package com.example.developer.moodoftheday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityAra extends AppCompatActivity {
DatabaseReference dbref;
    List<Kisiler> kisi=new ArrayList<Kisiler>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ara);



        dbref = FirebaseDatabase.getInstance().getReference("users");

        dbref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Kisiler ara = postSnapshot.getValue(Kisiler.class);
                    ara.getName();
                    ara.getKisiResmi();
                    kisi.add(ara);

                }


               ListView araListe = (ListView) findViewById(R.id.araListe);
                araAdapter adapter = new araAdapter(ActivityAra.this, kisi);
                araListe.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
