package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class editphoneverify extends AppCompatActivity {

    ConnectionDetector ConDet;
    DatabaseReference current_user_db;
    private FirebaseAuth mAuthe;
    TextView phnum,editNum,ermsg,txtTimeClk,tmtxt,pmsg;
    private String verificationId;
    FloatingActionButton signin,bck;
    ProgressBar pBar;
    EditText entCode;
    private static  final long COUNTDOWN_IN_MILLIS=60000;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    String fname,lname,phonenumber,name1,name2,userId1;
    String mail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editphoneverify);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuthe=FirebaseAuth.getInstance();
        pBar=(ProgressBar)findViewById(R.id.progressbar);
        phonenumber = getIntent().getStringExtra("phonenumber");
        pmsg=(TextView)findViewById(R.id.msg);
        ermsg=(TextView)findViewById(R.id.errmsg);
        phnum=(TextView)findViewById(R.id.phnum);
        editNum=(TextView)findViewById(R.id.editnum);
        entCode=(EditText)findViewById(R.id.edtOTP);
        tmtxt=(TextView)findViewById(R.id.timetxt);
        txtTimeClk=(TextView)findViewById(R.id.timeclk);
        bck=(FloatingActionButton)findViewById(R.id.editphoneback);
        signin=(FloatingActionButton) findViewById(R.id.floatverfrntbtn);
        ConDet = new ConnectionDetector(this);
        phnum.setText(phonenumber);

        if (ConDet.isConnected()) {
            userId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference CustDetailsref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId1);
            CustDetailsref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        name1 = dataSnapshot.child("First name").getValue().toString();
                        name2 = dataSnapshot.child("Last name").getValue().toString();
                        if (dataSnapshot.child("E-mail").exists()) {
                            mail = dataSnapshot.child("E-mail").getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast toast = new Toast(getApplicationContext());
            TextView tv = new TextView(editphoneverify.this);
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

        sendVerificationCode(phonenumber);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=entCode.getText().toString();
                if (code.isEmpty() ||code.length()<6){
                    ermsg.setVisibility(View.VISIBLE);
                }
                else {
                    if (ConDet.isConnected()) {
                        pBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(editphoneverify.this);
                        tv.setBackgroundColor(Color.BLACK);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        Typeface ty= Typeface.create("serif", Typeface.NORMAL);
                        tv.setTypeface(ty);
                        tv.setPadding(10,10,10,10);
                        tv.setText("Something went wrong, please try again.");
                        toast.setView(tv);
                        toast.show();
                    }
                }
            }
        });

      bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(editphoneverify.this,phonenumedit.class);
                startActivity(in);
            }
        });

        editNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(editphoneverify.this,phonenumedit.class);
                startActivity(in);
            }
        });
    }

    private  void verifyCode(String code){
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuthe.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pBar.setVisibility(View.INVISIBLE);
                            String Cust_Id=mAuthe.getCurrentUser().getUid();
                            current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(Cust_Id);
                            SaveMechanicInfo();
                           DatabaseReference current_user_db1= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId1);
                           current_user_db1.removeValue();
                            Intent intent=new Intent(editphoneverify.this,accountactivity.class);
                            intent.putExtra("remove",userId1);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(editphoneverify.this,"Enter valid code",Toast.LENGTH_SHORT).show();
                            Toast.makeText(editphoneverify.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
                entCode.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(editphoneverify.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    };

    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updatetxtTimeClk();

            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updatetxtTimeClk();
                tmtxt.setVisibility(View.INVISIBLE);
                txtTimeClk.setVisibility(View.INVISIBLE);
                pmsg.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void updatetxtTimeClk(){
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeformatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        txtTimeClk.setText(timeformatted);
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeLeftInMillis=COUNTDOWN_IN_MILLIS;
        startCountDown();
    }

    private void SaveMechanicInfo() {
        Map CustInfo = new HashMap();
        CustInfo.put("First name",name1);
        CustInfo.put("Last name",name2);
        CustInfo.put("Mobile number",phonenumber);
        CustInfo.put("E-mail",mail);
        current_user_db.updateChildren(CustInfo);

    }

}
