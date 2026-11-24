package com.findabin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.findabin.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText firstNameEditText = findViewById(R.id.firstName);
        final EditText lastNameEditText = findViewById(R.id.lastName);
        final EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        final EditText passwordEditText = findViewById(R.id.editTextNumberPassword);

        final Button signupButton = findViewById(R.id.signUp);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                // Empty checks first, so an empty field gets the clearest message
                if(firstName.isEmpty()){
                    firstNameEditText.setError("Field cannot be empty");
                    return;
                }
                if(lastName.isEmpty()){
                    lastNameEditText.setError("Field cannot be empty");
                    return;
                }
                if(email.isEmpty()){
                    emailEditText.setError("Field cannot be empty");
                    return;
                }
                if(password.isEmpty()){
                    passwordEditText.setError("Field cannot be empty");
                    return;
                }

                // Then format checks
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEditText.setError("invalid email address");
                    return;
                }
                if(password.length() < 6){
                    passwordEditText.setError("Password must be at least 6 characters");
                    return;
                }

                signupButton.setEnabled(false);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("SignUp", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    String uid = user.getUid();
                                    UserProfile userObj = new UserProfile();
                                    userObj.setFirstName(firstName);
                                    userObj.setLastName(lastName);
                                    userObj.setEmail(email);

                                    FirebaseFirestore.getInstance().collection("users").document(uid).set(userObj)
                                            .addOnSuccessListener(unused -> goToMain())
                                            .addOnFailureListener(e -> {
                                                // The account exists and the user is signed in;
                                                // only the profile save failed, so let them in anyway.
                                                Log.e("SignUp", "Saving user profile failed", e);
                                                Toast.makeText(RegisterActivity.this,
                                                        "Account created, but your profile couldn't be saved",
                                                        Toast.LENGTH_LONG).show();
                                                goToMain();
                                            });
                                } else {
                                    // If sign up fails, display Firebase's message to the user.
                                    Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    signupButton.setEnabled(true);
                                }
                            }
                        });
            }
        });
    }

    // Opens the main screen and clears sign-up / start screen from the back stack,
    // so pressing Back from the main screen leaves the app instead of returning here.
    private void goToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
