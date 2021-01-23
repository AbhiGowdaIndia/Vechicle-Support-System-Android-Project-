package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class phoneverify extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView phnum,editNum,ermsg,txtTimeClk,tmtxt,pmsg;
    private String verificationId;
    FloatingActionButton signin,bck;
    ProgressBar pBar;
    EditText entCode;
    private static  final long COUNTDOWN_IN_MILLIS=60000;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverify);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth=FirebaseAuth.getInstance();
        pBar=(ProgressBar)findViewById(R.id.progressbar);
        phonenumber=getIntent().getStringExtra("phonenumber");
        pmsg=(TextView)findViewById(R.id.msg);
        ermsg=(TextView)findViewById(R.id.errmsg);
        phnum=(TextView)findViewById(R.id.phnum);
        editNum=(TextView)findViewById(R.id.editnum);
        entCode=(EditText)findViewById(R.id.edtOTP);
        tmtxt=(TextView)findViewById(R.id.timetxt);
        txtTimeClk=(TextView)findViewById(R.id.timeclk);
        phnum.setText(phonenumber);
        bck=(FloatingActionButton)findViewById(R.id.floatverbackbtn);
        signin=(FloatingActionButton)findViewById(R.id.floatverfrntbtn);

        sendVerificationCode(phonenumber);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pBar.setVisibility(View.VISIBLE);
                String code=entCode.getText().toString();
                if (code.isEmpty() ||code.length()<6){
                    ermsg.setVisibility(View.VISIBLE);
                }
                else {
                    verifyCode(code);
                }
            }
        });
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btn= new Intent(phoneverify.this,custregister2.class);
                startActivity(btn);
            }
        });
        editNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(phoneverify.this,custregister2.class);
                startActivity(in);
            }
        });
    }

    private  void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pBar.setVisibility(View.INVISIBLE);
                            String Customer_Id=mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(Customer_Id);
                            current_user_db.setValue(true);
                            Intent intent=new Intent(phoneverify.this,CustomerName.class);
                            intent.putExtra("phonenumber",phonenumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(phoneverify.this,"Enter valid code",Toast.LENGTH_SHORT).show();
                            Toast.makeText(phoneverify.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(phoneverify.this,e.getMessage(),Toast.LENGTH_SHORT).show();

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

}
