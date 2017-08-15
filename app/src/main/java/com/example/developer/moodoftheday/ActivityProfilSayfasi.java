package com.example.developer.moodoftheday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class ActivityProfilSayfasi extends AppCompatActivity {
    static ArrayList <String> keys=new ArrayList<>();
    ImageButton profilresmi,menu;
    Button mod;
    ModAdapter adapter;
    private static final int fotograf = 1;
    private static final int resim = 2;
    private Uri imageUri;
    DatabaseReference dbref,refKisiFoto;
    ListView durumListesi;
    List<modumProfil> liste=new ArrayList<modumProfil>();
    Kisiler kisi=new Kisiler();
    Menu menumuz;
    StorageReference storageReference;
    Button gizlilikAyarlari;

    public static String alınan;

    public static void setAlınan(String alınan) {
        ActivityProfilSayfasi.alınan = alınan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_sayfasi);


        Intent fakeDataa = getIntent();
        final String gelecekOlanKisi = fakeDataa.getExtras().getString("gelecekOlanKisi");

        dbref = FirebaseDatabase.getInstance().getReference("kullaniciModlari").child(alınan);
     //   refKisiFoto = FirebaseDatabase.getInstance().getReference("users").child("");
        profilresmi = (ImageButton) findViewById(R.id.profilResmi);
        profilresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder secimDialog = new AlertDialog.Builder(ActivityProfilSayfasi.this);
                secimDialog.setTitle("Profil Resmini Görüntüle veya Profil Resmini Değiştir");
                secimDialog.setIcon(R.drawable.fotogalerii); //İkonun projedeki konumu set edelir.


                secimDialog.setPositiveButton("Profil Resmini Görüntüle ",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                Intent profResm = new Intent(getApplicationContext(),profilResmi.class);
                                profResm.putExtra("fakeRes","");
                                startActivity(profResm);
                            }
                        });

                secimDialog.setNegativeButton("Profil Resmini Değiştir",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder secimDialog = new AlertDialog.Builder(ActivityProfilSayfasi.this);
                                secimDialog.setTitle("Resim Çek veya Galeriden Resim Yükle");
                                secimDialog.setIcon(R.drawable.fotogalerii); //İkonun projedeki konumu set edelir.


                                secimDialog.setPositiveButton("Resim Çek ",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent kamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                startActivityForResult(kamera, resim);
                                            }
                                        });

                                secimDialog.setNegativeButton("Galeriden Resim Yükle",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent galeri = new Intent(Intent.ACTION_GET_CONTENT);
                                                galeri.setType("image/*");
                                                startActivityForResult(galeri, fotograf);
                                            }
                                        });

                                secimDialog.show();
                            }
                        });

                secimDialog.show();
            }
        });



        Intent alındı = getIntent();
        final String alınannn = alındı.getExtras().getString("gelecekOlanKisi");
        mod = (Button) findViewById(R.id.Mood);

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modDialog = new AlertDialog.Builder(ActivityProfilSayfasi.this);
                modDialog.setTitle("Modun ne olsun istersin ?");
                modDialog.setIcon(R.drawable.fotogalerii); //İkonun projedeki konumu set edelir.

                modDialog.setPositiveButton("Modum Değişti ",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), ActivityModumSayfasi.class);
                                intent.putExtra("kisiReference", gelecekOlanKisi);
                                startActivity(intent);
                                finish();
                            }
                        });

                modDialog.setNegativeButton("Modumu Gör",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                               /* Intent ac = new Intent(profilSayfasi.this,ModumSayfasi.class);
                                galeri.setType("image/*");
                                startActivityForResult(galeri, fotograf);*/
                            }
                        });

                modDialog.show();
            }
        });


        Intent alinanGizlilikResmi=getIntent();
        final int alinanGizlilik=alinanGizlilikResmi.getExtras().getInt("gizlilik esası");

        dbref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    modumProfil customer = postSnapshot.getValue(modumProfil.class);
                    liste.add(customer);

                }



                durumListesi = (ListView) findViewById(R.id.profildekiModlar);
                profilModAdapterr adapter = new profilModAdapterr(ActivityProfilSayfasi.this, liste);
                durumListesi.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



//        Intent databaseDurum=getIntent();
//        final String databaseDurumEkle=databaseDurum.getExtras().getString("databaseGizlilik");


        gizlilikAyarlari=(Button) findViewById(R.id.gizlilikAyarlari);
        gizlilikAyarlari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.widget.PopupMenu popup = new android.widget.PopupMenu(ActivityProfilSayfasi.this, gizlilikAyarlari);

                popup.getMenuInflater().inflate(R.menu.gizlilikmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gdüzenle:
                            {


                                Intent git=new Intent(ActivityProfilSayfasi.this,ActivityGizlilik.class);
                                git.putExtra("kisiIdsiGönder",gelecekOlanKisi);
                                startActivity(git);

                            }

                                break;

                        }
                       return true;
                    }
                });

                popup.show();


            }
        });

       gizlilikAyarlari.setBackgroundResource(alinanGizlilik);

    }
    String id;
    Kisiler res;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resim && resultCode == RESULT_OK) {

            imageUri = data.getData();
            profilresmi.setImageURI(imageUri);
            storageReference= FirebaseStorage.getInstance().getReference("profilResmi");
            final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference.child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") String alll = taskSnapshot.getDownloadUrl().toString();

                    id = refKisiFoto.push().getKey();
                    res = new Kisiler(alll);
                    //  paylasilacakResim.setVisibility(View.INVISIBLE);

                    refKisiFoto.child(id).setValue(res);
                    Toast.makeText(ActivityProfilSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();

                }});



        } else if (requestCode == fotograf && resultCode == RESULT_OK) {

            imageUri = data.getData();
            profilresmi.setImageURI(imageUri);

            storageReference= FirebaseStorage.getInstance().getReference("profilResmi");
            final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference.child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") String alll = taskSnapshot.getDownloadUrl().toString();

                    id = refKisiFoto.push().getKey();
                    res = new Kisiler(alll);
                    //  paylasilacakResim.setVisibility(View.INVISIBLE);

                    refKisiFoto.child(id).setValue(res);

                    Toast.makeText(ActivityProfilSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();

                }});



        }

    }


    private void düzenle(modumProfil modum) {
             Intent git=new Intent(getApplicationContext(),ActivityModumSayfasi.class);
              startActivity(git);
    }







}
