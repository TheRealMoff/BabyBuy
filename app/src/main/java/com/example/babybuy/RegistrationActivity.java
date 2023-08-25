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

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextFName, editTextEmail, editTextPass, editTextConfirmPass;
    ImageButton imageButtonDone;
    TextView signIn;

     DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddressRA);
        editTextFName = (EditText) findViewById(R.id.editTextFullNameRA);
        editTextPass  = (EditText) findViewById(R.id.editTextPasswordRA);
        editTextConfirmPass = (EditText) findViewById(R.id.editTextConfirmPasswordRA);
        signIn = (TextView) findViewById(R.id.textViewSignInSA);

        dbHelper = new DBHelper(this);

        imageButtonDone = (ImageButton) findViewById(R.id.imageButtonDone);

        imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextFName.getText().toString().isEmpty() || editTextEmail.getText().toString().isEmpty()
                        || editTextPass.getText().toString().isEmpty() || editTextConfirmPass.getText().toString().isEmpty()){

                    Toast.makeText(RegistrationActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }else

                if (!editTextPass.getText().toString().equals(editTextConfirmPass.getText().toString())){

                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                } else {
                    Users user = new Users(editTextFName.getText().toString(),
                            editTextEmail.getText().toString(), editTextPass.getText().toString());

                    if (dbHelper.registerUser(user)){
                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        editTextFName.setText("");
                        editTextEmail.setText("");
                        editTextPass.setText("");
                        editTextConfirmPass.setText("");
                        Toast.makeText(RegistrationActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}