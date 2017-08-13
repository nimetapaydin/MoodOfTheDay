package com.example.developer.moodoftheday;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityModumSayfasi extends AppCompatActivity {
    ImageButton resEkle, modEkle,paylasilacakResim;
    EditText sonDurum;
    TextView modunDurumu;
    String gelenData;
    Intent intent;
    private static final int fotograf = 1;
    private static final int resim = 2;
    private Uri image;
    Button paylas;
    Bitmap Resim;
    ListView modList;
    ModAdapter adapter;
    DatabaseReference dbref,databaseReference;
    modumProfil al;
    StorageReference storageReference,resimlerReference;
    String id;
    ImageView profileGit;
    int profResmi;
    Calendar mcurrentTime = Calendar.getInstance();
    String tarih,saat;

    //todo:mod seim sayfasındaki modlarımızın aktarılması
    public static List<ModClass> modlar=new ArrayList<ModClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modum_sayfasi);

        modlar.add(new ModClass("Kırmızı","Hareketli Canlı Hissediyorum",R.drawable.kirmizi));
        modlar.add(new ModClass("Gri","Bunalmış Hissediyorum",R.drawable.gri));
        modlar.add(new ModClass("Mavi","Sakin,Rahat ve Özgür Hissediyorum",R.drawable.mavi));
        modlar.add(new ModClass("Kahverengi","Duygusal Hissediyorum",R.drawable.kahaverengi));
        modlar.add(new ModClass("Mor","Umutlu Hissediyorum",R.drawable.mor));
        modlar.add(new ModClass("Pembe","Neşeli Hissediyorum",R.drawable.pembe));
        modlar.add(new ModClass("Turuncu","Cesaretli ve Güven Dolu Hissediyorum",R.drawable.turuncu));
        modlar.add(new ModClass("Yeşil","Huzurlu Hissediyorum",R.drawable.yesil));
        modlar.add(new ModClass("Sarı","Hüzünlü ve Özlemiş Hissediyorum",R.drawable.sari));
        modlar.add(new ModClass("Siyah","Üzgün ve Mutsuz Hissediyorum",R.drawable.siyah));
        modlar.add(new ModClass("Beyaz","Mutlu Hissediyorum",R.drawable.beyaz));




//todo:kameradan resim çekerke resim yan dönmesi sorunu ve resim boyutu ayarlaması nasıl olacak
        resEkle = (ImageButton) findViewById(R.id.resDurEkle);
        resEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tooltip tooltip = new Tooltip.Builder(v)
                        .setText("Resim Ekle")
                        .setTextColor(Color.BLUE)
                        .setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();


                AlertDialog.Builder secimDialog = new AlertDialog.Builder(ActivityModumSayfasi.this);
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


//todo:Image buttona seçtiğimiz modun eklenmesi creatCustomDialog ile
        modEkle=(ImageButton) findViewById(R.id.modEkle);
        modEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatCustomDialog(ActivityModumSayfasi.this);

            }
        });
        //todo:database ekleme işlemlerini yapalım
        modunDurumu=(TextView) findViewById(R.id.modunDurumu);
        sonDurum=(EditText) findViewById(R.id.sonDurum);
        paylasilacakResim=(ImageButton) findViewById(R.id.paylasilacakResim) ;
        intent=getIntent();
        dbref= FirebaseDatabase.getInstance().getReference("kullaniciModlari").child(intent.getStringExtra("kisiReference"));
         tarih= String.valueOf(mcurrentTime.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(mcurrentTime.get(Calendar.MONTH))+"."+String.valueOf(mcurrentTime.get(Calendar.YEAR));
         saat= String.valueOf(mcurrentTime.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf( mcurrentTime.get(Calendar.MINUTE));

        paylas=(Button) findViewById(R.id.paylas);
        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(image == null) {

                    id = dbref.push().getKey();
                    al = new modumProfil(modunDurumu.getText().toString(), sonDurum.getText().toString(),saat,tarih, profResmi);
                  //  paylasilacakResim.setVisibility(View.INVISIBLE);

                    dbref.child(id).setValue(al);

                    Toast.makeText(ActivityModumSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();

                }
                else if(sonDurum.getText().toString()==null){
                    storageReference= FirebaseStorage.getInstance().getReference("resimler");
                    final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference.child(image.getLastPathSegment()).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") String alll = taskSnapshot.getDownloadUrl().toString();

                            id = dbref.push().getKey();
                            al = new modumProfil(modunDurumu.getText().toString(), alll, profResmi, saat, tarih);
                            //  paylasilacakResim.setVisibility(View.INVISIBLE);

                            dbref.child(id).setValue(al);

                            Toast.makeText(ActivityModumSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();

                        }});}
                else if(image==null && sonDurum.getText().toString()==null){



                    id = dbref.push().getKey();
                    al = new modumProfil(modunDurumu.getText().toString(), profResmi, saat, tarih);
                    //  paylasilacakResim.setVisibility(View.INVISIBLE);

                    dbref.child(id).setValue(al);

                    Toast.makeText(ActivityModumSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();


                }

                else {
                    storageReference= FirebaseStorage.getInstance().getReference("resimler");
                    final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference.child(image.getLastPathSegment()).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") String alll = taskSnapshot.getDownloadUrl().toString();

                            id = dbref.push().getKey();
                            al = new modumProfil(modunDurumu.getText().toString(), sonDurum.getText().toString(), alll, profResmi,saat,tarih);
                            dbref.child(id).setValue(al);

                            Toast.makeText(ActivityModumSayfasi.this, "Upload Done", Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }
        }); }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resim) {
            image = data.getData();
            resEkle.setImageURI(image);


        } else if (requestCode == fotograf && resultCode == RESULT_OK) {
            Log.d("aaaa", String.valueOf(requestCode));

            image = data.getData();
            resEkle.setImageURI(image);
        }

    }
    public void creatCustomDialog(Context context){
        View dialogView = View.inflate(context,R.layout.modlarim, null);
        modList= (ListView) dialogView.findViewById(R.id.ListlerimMod);
        adapter = new ModAdapter(context, R.layout.mod_adapter, modlar);
        modList.setAdapter(adapter);
        final AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(dialogView);


        final  AlertDialog dialog=builder.create();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        // builder.create().show();
        modList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.hide();
                modEkle.setImageResource(modlar.get(i).getModResmi());
                modunDurumu.setText((modlar.get(i).getModAdi())+":"+modlar.get(i).getModDurumu());
                profResmi=  modlar.get(i).setModResmi(modlar.get(i).getModResmi());
            }
        });
        dialog.show();


    }
}
