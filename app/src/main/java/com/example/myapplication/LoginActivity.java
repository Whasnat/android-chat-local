package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText uphone, vcode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks ucallbacks;
    private Button usend;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        vcode = findViewById(R.id.code);
        //1.Checks if the user is logged In
        userActive();
        //

        //2.Enter Phone Number
        uphone = findViewById(R.id.phone_no);
        usend = findViewById(R.id.send_btn);
        //3.On Button Press
        usend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If automatic Verification fails
                if (verificationID != null)
                    verifyPhoneNumberWithCode(verificationID, vcode.getText().toString());

                else
                    //4.Authenticate By sending OTP to Phone and verifying User Login
                    Phoneauth();
            }
        });

        //6.Use the Callback from "5" check PASS/FAIL
        ucallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //When the Verification Code is sent
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                usend.setText("Verify");
                Log.d(TAG, "onCodeSent: Code Sent: " + s);
                verificationID = s;
            }

            @Override
            //IF PASS
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //call to Authenticate with PhoneCredentials
                signInWithCredentials(phoneAuthCredential);
            }

            @Override
            //IF FAIL
            public void onVerificationFailed(FirebaseException e) {

            }


        };
    }

    //Verify the Code With the Phone Verification Code
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithCredentials(credential);
    }


    //7.SignIN with FirebaseAuth
    private void signInWithCredentials(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //IF PASS
                        if (task.isSuccessful()) {

                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                final DatabaseReference userDB = FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("user").child("userUID");

                                userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("phone", user.getPhoneNumber());
                                            userMap.put("name", user.getPhoneNumber());
                                            userDB.updateChildren(userMap);
                                        }

                                        userActive();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        //call to mark the current User as Active
                    }
                });
    }


    //8. make the current user as Active and Go to HOME PAGE
    private void userActive() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Check again if the user is active
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            //Finish the Login Activity so the Active user cant go back to login page without logging out
            finish();
            return;
        }

    }

    //5.Send Otp and return the Callback from Phone Number Within Time Limit
    private void Phoneauth() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                uphone.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                ucallbacks);
    }
}
