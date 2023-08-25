package com.example.babybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    TextView textViewForgotPass;
    ImageButton imageButtonSignIn, imageButtonSignUp;

    static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewForgotPass = (TextView) findViewById(R.id.textViewForgetPass);
        imageButtonSignIn = (ImageButton) findViewById(R.id.imageButtonSignIn);
        imageButtonSignUp = (ImageButton) findViewById(R.id.imageButtonCreateAcc);

        dbHelper = new DBHelper(this);

        imageButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean result = dbHelper.checkEmailPass(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                    if (result == true){
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                    }else {
                        editTextEmail.setText("");
                        editTextPassword.setText("");
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });

        imageButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

}