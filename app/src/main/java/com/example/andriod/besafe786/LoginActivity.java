package com.example.andriod.besafe786;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andriod.besafe786.admin.ADMIN;
import com.example.andriod.besafe786.admin.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    User user;
    String userType = "";
    EditText email,password;
    Button registetr,login;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    boolean isAdmin = false;
    String[] type = {"Select","User", "Admin"};
    ArrayAdapter<String> myAdapter2;
    Spinner spinner;
    EditText key;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static boolean isLogout = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = new User();
        Intent i = getIntent();
        isLogout = i.getBooleanExtra("LOGOUT",false);
        sharedPreferences = getSharedPreferences("ADMIN",0);
        editor = sharedPreferences.edit();
        spinner = findViewById(R.id.spinner);
        key = findViewById(R.id.key);
        key.setVisibility(INVISIBLE);
        myAdapter2 = new ArrayAdapter<>(LoginActivity.this,android.R.layout.simple_spinner_item,type);
        spinner.setAdapter(myAdapter2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth =FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        email = findViewById(R.id.login_emailid);
        password = findViewById(R.id.login_password);
        registetr = findViewById(R.id.button4);
        login = findViewById(R.id.button2);
        progressBar.setVisibility(View.INVISIBLE);
        if(firebaseAuth.getCurrentUser() != null){
            if(!isLogout){
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }
        //checkUserType();
        registetr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(myAdapter2.getItem(position).toString().equals("Admin")){
                    Toast.makeText(getApplicationContext(),"Admin mode activated",Toast.LENGTH_SHORT).show();
                    key.setVisibility(VISIBLE);
                }else if(myAdapter2.getItem(position).toString().equals("User") || myAdapter2.getItem(position).toString().equals("Select")){
                    key.setVisibility(INVISIBLE);
                    Toast.makeText(getApplicationContext(),"User mode activated",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loginClick(View view){
        String ema = email.getText().toString().trim();
        String pswd = password.getText().toString().trim();
        String adminkey = key.getText().toString().trim();
        if(ema.isEmpty()){
            email.setError("Email is mandatory");
            return;
        }else if(pswd.isEmpty()){
            password.setError("Password field is mandatory");
            return;
        }else if(key.getVisibility() == VISIBLE){
            if(adminkey.isEmpty()){
                key.setError("This is field is mandatory for Admin Login");
                return;
            }else if(!adminkey.equals("DP06NK30RH64YC67")){
                key.setError("Invalid Key");
                return;
            }
            isAdmin = true;
            editor.putBoolean("ADMIN",true);
            editor.commit();
        }

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(ema,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Login Succesfull",Toast.LENGTH_SHORT).show();
                        if(isAdmin) {
                            startActivity(new Intent(LoginActivity.this, ADMIN.class));
                        }else{
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Login Failed:"+task.getException().toString().trim(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
    }
}
