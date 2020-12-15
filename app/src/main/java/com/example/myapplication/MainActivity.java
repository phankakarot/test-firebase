package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText num,code;
    Button confirm,btn;
    String phoneNumber = "+84379357956";
    //String verificationIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = findViewById(R.id.editTextNumber);
        confirm = findViewById(R.id.button);
        btn = findViewById(R.id.button2);
        code = findViewById(R.id.editTextNumber2);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = num.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    num.setError("Valid number is required");
                    num.requestFocus();
                    return;
                }


                Intent intent = new Intent(MainActivity.this, VerifyPhone.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);

            }
        });
    }

    private void enableUserManuallyInputCode() {
    }

}