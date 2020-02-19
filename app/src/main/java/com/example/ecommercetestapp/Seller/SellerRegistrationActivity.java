package com.example.ecommercetestapp.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommercetestapp.Buyer.MainActivity;
import com.example.ecommercetestapp.R;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginBtn, registerBtn;
    private EditText nameinput, phoneInput, emailInput, passwordInput, addressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        nameinput = findViewById(R.id.seller_name);
        phoneInput= findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);

        registerBtn = findViewById(R.id.seller_register_btn);
        sellerLoginBtn = findViewById(R.id.seller_login_btn);

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                registerSeller();
            }
        });
    }

    private void registerSeller()
    {
        String name = nameinput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {

        }
        else
        {
            Toast.makeText(this, "Please fill up the registration fields", Toast.LENGTH_SHORT).show();
        }

    }
}
