package com.example.sentry;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sentry.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameTextView, lastNameTextView, ageTextView, emailTextView, passwordTextView, phoneTextView;
    private Button register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore data_storage;
    String userID;
    User push;
    String first_name, last_name, age, email, password, phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        data_storage = FirebaseFirestore.getInstance();
        // initialising all views through id defined above
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        ageTextView = findViewById(R.id.age);
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passowrd);
        phoneTextView = findViewById(R.id.phone_number);
        register = findViewById(R.id.registerButton);

        // Set on Click Listener on Registration button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        // Take the value of two edit texts in Strings
        first_name = firstNameTextView.getText().toString();
        last_name = lastNameTextView.getText().toString();
        age = ageTextView.getText().toString();
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        phone_number = phoneTextView.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(first_name)) {
            Toast.makeText(getApplicationContext(), "Please enter first name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(last_name)) {
            Toast.makeText(getApplicationContext(), "Please enter last name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(getApplicationContext(), "Please enter age!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phone_number)) {
            Toast.makeText(getApplicationContext(), "Please enter phone number!!", Toast.LENGTH_LONG).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = data_storage.collection("users").document(userID);
                            push = new User();
                            set_data();
                            documentReference.set(push).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "User profile is created");
                                }
                            });

                            // if the user created intent to login activity
                            firstNameTextView.setText("");
                            lastNameTextView.setText("");
                            ageTextView.setText("");
                            emailTextView.setText("");
                            passwordTextView.setText("");
                            phoneTextView.setText("");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else {

                            // Registration failed
                            Toast.makeText(getApplicationContext(), "Registration failed" + " or existing user.", Toast.LENGTH_LONG).show();
                            firstNameTextView.setText("");
                            lastNameTextView.setText("");
                            ageTextView.setText("");
                            emailTextView.setText("");
                            passwordTextView.setText("");
                            phoneTextView.setText("");

                        }
                    }
                });
    }
    public void set_data(){
        push.setFirst_name(first_name);
        push.setLast_name(last_name);
        push.setAge(age);
        push.setEmail(email);
        push.setPhone_number(phone_number);
    }
}
