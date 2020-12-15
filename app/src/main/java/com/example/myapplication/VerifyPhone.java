package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    EditText etcode;//abcahuahu
    Button confirm;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth auth;
    PhoneAuthOptions options;
    TextView noitify;
    private String verificationIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        etcode = findViewById(R.id.editTextCode);
        confirm = findViewById(R.id.buttonConfirm);
        noitify  = findViewById(R.id.textView);

        String phonenumber = getIntent().getStringExtra("phonenumber");

        sendVerificationCode(phonenumber);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etcode.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    etcode.setError("Enter code...");
                    etcode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIds, code);
        signInWithPhoneAuthCredential(credential);
//        credential.getSmsCode();

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

//                            Intent intent = new Intent(VerifyPhone.this, ProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                            startActivity(intent);
                    noitify.setText("Xác nhận thành công");

                } else {
                    Toast.makeText(VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    noitify.setText(task.getException().getMessage());
                }
            }
        });
    }
    void sendVerificationCode(String num){
        auth = FirebaseAuth.getInstance();
        options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(num)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...

                        // The corresponding whitelisted code above should be used to complete sign-in.
                        super.onCodeSent(verificationId, forceResendingToken);
                        verificationIds = verificationId;
                        VerifyPhone.this.enableUserManuallyInputCode();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                        Toast.makeText(VerifyPhone.this,phoneAuthCredential.getSmsCode(), Toast.LENGTH_LONG).show();
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        //editText.setText(e.getMessage());
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void enableUserManuallyInputCode() {
    }

}