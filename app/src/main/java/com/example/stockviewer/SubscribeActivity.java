package com.example.stockviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;


public class SubscribeActivity extends AppCompatActivity {

    private EditText textMobile;
    private Spinner spinner;

    public static final String [] countryNames={ "Afghanistan", "Albania", "Algeria", "India"};

    public static final String[] countryCodes={"93", "355", "213", "91"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);

        spinner=findViewById(R.id.countryCode);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryNames));
        textMobile = findViewById(R.id.number);

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= countryCodes[spinner.getSelectedItemPosition()];

                String mobile = textMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    textMobile.setError("Enter a valid mobile number");
                    textMobile.requestFocus();
                    return;
                }
                String phoneNumber="+"+code+mobile;

                Intent intent = new Intent(SubscribeActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent=new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

