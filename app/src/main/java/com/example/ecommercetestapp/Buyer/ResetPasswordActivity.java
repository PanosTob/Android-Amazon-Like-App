package com.example.ecommercetestapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercetestapp.Prevalent.Prevalent;
import com.example.ecommercetestapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView resetActivityTitle, titleQuestions;
    private EditText txtPhoneNumber, question1, question2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check = getIntent().getStringExtra("check");

        resetActivityTitle = findViewById(R.id.reset_activity_title);
        titleQuestions = findViewById(R.id.title_questions);
        txtPhoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyBtn = findViewById(R.id.verify_btn);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        txtPhoneNumber.setVisibility(View.GONE);

        if(check.equals("settings"))
        {
            resetActivityTitle.setText("Set Questions");
            titleQuestions.setText("Please set answers for the following security questions in case you forget your password");
            verifyBtn.setText("Apply");

            displaySavedAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    setAnswers();
                }
            });
        }
        else if(check.equals("login"))
        {
            txtPhoneNumber.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers(){
        if(question1.equals("") && question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions to apply", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String answer1 = question1.getText().toString().toLowerCase();
            String answer2 = question2.getText().toString().toLowerCase();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPasswordActivity.this, "You have successfully set the security questions", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    private void displaySavedAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("answer1").exists() && dataSnapshot.child("answer2").exists()) {
                    String answer1 = dataSnapshot.child("answer1").getValue().toString();
                    String answer2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(answer1);
                    question2.setText(answer2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void verifyUser()
    {
        final String phoneGiven = txtPhoneNumber.getText().toString();
        final String answer1Txt = question1.getText().toString().toLowerCase();
        final String answer2Txt = question2.getText().toString().toLowerCase();

        if(!phoneGiven.equals("") && !answer1Txt.equals("") && !answer2Txt.equals("")) {

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(phoneGiven);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        if (dataSnapshot.hasChild("answer1") && dataSnapshot.hasChild("answer2")) {
                            String ans1Db = dataSnapshot.child("answer1").getValue().toString();
                            String ans2Db = dataSnapshot.child("answer2").getValue().toString();

                            if (!ans1Db.equals(answer1Txt)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your answer to the first question is incorrect", Toast.LENGTH_SHORT).show();
                            } else if (!ans2Db.equals(answer2Txt)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your answer to the second question is incorrect", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPasswordTxt = new EditText(ResetPasswordActivity.this);
                                newPasswordTxt.setHint("Write New Password Here...");
                                builder.setView(newPasswordTxt);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i) {
                                        if (!newPasswordTxt.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPasswordTxt.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions. Please contact our support team", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        }
    }
}
