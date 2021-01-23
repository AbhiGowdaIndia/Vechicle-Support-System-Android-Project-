package com.example.admin.vehicleservice3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class editimage extends AppCompatActivity {
    ImageView ProfilePic;
    Button saveimage,cancel;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private String userid;
    ConnectionDetector ConDet;
    DatabaseReference curent_user_db;
    String profurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editimage);
        ConDet = new ConnectionDetector(this);
        mAuth = FirebaseAuth.getInstance();
        userid=mAuth.getCurrentUser().getUid();
        curent_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userid);
        ProfilePic=(ImageView)findViewById(R.id.ProfilePic) ;
        saveimage=(Button)findViewById(R.id.saveimage);
        cancel=(Button)findViewById(R.id.cancel);
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
        loadimage();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editimage.this,editactivity.class);
                startActivity(inte);
                finish();
            }
        });

        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConDet.isConnected()) {

                    if (resultUri != null) {
                        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile images").child(userid);
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadtask = filepath.putBytes(data);
                        uploadtask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                finish();
                                return;
                            }
                        });
                        uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUri = taskSnapshot.getDownloadUrl();
                                Map newImage = new HashMap();
                                newImage.put("ProfileImageurl", downloadUri.toString());
                                curent_user_db.updateChildren(newImage);
                                finish();
                                return;

                            }
                        });

                    } else {
                        finish();
                    }
                }else{
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(editimage.this);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    Typeface ty = Typeface.create("serif", Typeface.NORMAL);
                    tv.setTypeface(ty);
                    tv.setPadding(10, 10, 10, 10);
                    tv.setText("Something went wrong, please try again.");
                    toast.setView(tv);
                    toast.show();
                }
            }
        });
    }

    private void loadimage() {
        if (ConDet.isConnected()) {

            curent_user_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {

                        Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();

                        if(map.get("ProfileImageurl")!=null){
                            profurl=map.get("ProfileImageurl").toString();
                            Glide.with(getApplication()).load(profurl).into(ProfilePic);
                        }
                       /* name1 = dataSnapshot.child("First name").getValue().toString();
                        name2 = dataSnapshot.child("Last name").getValue().toString();
                        phnum = dataSnapshot.child("Mobile number").getValue().toString();
                        if (dataSnapshot.child("E-mail").exists()) {
                            mail = dataSnapshot.child("E-mail").getValue().toString();
                            }
                             fname.setText(name1);
                             sname.setText(name2);
                        pnum.setText(phnum);
                        email.setText(mail);
                        }*/


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            Toast toast = new Toast(getApplicationContext());
            TextView tv = new TextView(editimage.this);
            tv.setBackgroundColor(Color.BLACK);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
            Typeface ty = Typeface.create("serif", Typeface.NORMAL);
            tv.setTypeface(ty);
            tv.setPadding(10, 10, 10, 10);
            tv.setText("Something went wrong, please try again.");
            toast.setView(tv);
            toast.show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri=data.getData();

           /*CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);*/



            resultUri=imageUri;
            ProfilePic.setImageURI(resultUri);

        }
       /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

        }*/
    }
}
