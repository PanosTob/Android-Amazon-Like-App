package com.example.ecommercetestapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommercetestapp.Buyer.HomeActivity;
import com.example.ecommercetestapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity
{

    private Button applyChangesBtn, deleteProductBtn;
    private EditText txtFieldName, txtFieldPrice, txtFieldDescription;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        deleteProductBtn = findViewById(R.id.delete_product_btn);
        txtFieldName = findViewById(R.id.product_name_maintain);
        txtFieldPrice = findViewById(R.id.product_price_maintain);
        txtFieldDescription = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();



        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                applyChanges();
            }
        });
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteChosenProduct();
            }
        });
    }

    private void deleteChosenProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "The product was deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges()
    {
        String pName = txtFieldName.getText().toString();
        String pPrice = txtFieldPrice.getText().toString();
        String pDescription = txtFieldDescription.getText().toString();

        if(pName.equals(""))
        {
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(pPrice.equals(""))
        {
            Toast.makeText(this, "Write down Product Price", Toast.LENGTH_SHORT).show();
        }
        else if(pDescription.equals(""))
        {
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changed applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

    private void displaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    txtFieldName.setText(pName);
                    txtFieldPrice.setText(pPrice);
                    txtFieldDescription.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
