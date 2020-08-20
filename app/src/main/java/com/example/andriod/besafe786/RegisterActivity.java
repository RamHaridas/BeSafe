package com.example.andriod.besafe786;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.*;

public class RegisterActivity extends AppCompatActivity {

    TextView link;
    MapMarker mapMarker;
    User user;
    boolean admin = false;
    String gender = "";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String[] type = {"Select","User", "Admin"};
    ArrayAdapter<String> myAdapter2;
    EditText fname,lname,email,phone,password,confirm,key;
    Spinner spinner;
    RadioGroup radioGroup;
    ProgressBar progressBar;
    RadioButton radioButton;
    String ema,fn,ln,ph,pswd,sex,type1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //checkUserType();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
        sharedPreferences = getSharedPreferences("ADMIN",0);
        admin = sharedPreferences.getBoolean("ADMIN",false);
        link = findViewById(R.id.linkredirect);
        mapMarker = new MapMarker();
        user = new User();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        spinner = (Spinner)findViewById(R.id.spinner);
        myAdapter2 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item,type);
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter2);
        fname = findViewById(R.id.FirstName);
        lname = findViewById(R.id.LastName);
        email = findViewById(R.id.userEmailId);
        phone = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirmPassword);
        key = findViewById(R.id.key);
        key.setVisibility(INVISIBLE);
        radioGroup = findViewById(R.id.radiogroup);
        if(firebaseAuth.getCurrentUser() != null){
            if(!LoginActivity.isLogout) {
                if (admin) {
                    startActivity(new Intent(RegisterActivity.this, ADMIN.class));
                    finish();
                } else {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(myAdapter2.getItem(position).toString().equals("Admin")){
                    Toast.makeText(getApplicationContext(),"Admin mode activated",Toast.LENGTH_SHORT).show();
                    key.setVisibility(VISIBLE);
                }else if(myAdapter2.getItem(position).toString().equals("User") || myAdapter2.getItem(position).toString().equals("Select")){
                    key.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void radioClick(View view){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(id);
        switch (view.getId()){
            case R.id.male:
                Toast.makeText(this,"MALE",Toast.LENGTH_SHORT).show();
                gender = radioButton.getText().toString();
                break;
            case R.id.female:
                Toast.makeText(this,"FEMALE",Toast.LENGTH_SHORT).show();
                gender = radioButton.getText().toString();
                break;
            default:
                break;
        }
    }

    public void register(View view){

        if(spinner.getSelectedItem().toString().equals("Select")){
            Toast.makeText(this,"Please Select User or Admin Mode:",Toast.LENGTH_SHORT).show();
            return;
        }else if(!isStringOnlyAlphabet(fname.getText().toString())){
            fname.setError("First Name can only contain Alphabets");
            return;
        }else if(!isStringOnlyAlphabet(lname.getText().toString())){
            lname.setError("Last Name can only contain Alphabets");
            return;
        }else if(!isEmailValid(email.getText().toString())){
            email.setError("Invalid email");
            return;
        }else if(phone.getText().toString().length() < 10 || phone.getText().toString().length() > 13){
            phone.setError("Invalid Mobile number");
            return;
        }else if(password.getText().toString().isEmpty() || password.getText().toString().length() < 8){
            password.setError("Password is mandatory and it should contain more than 8 chracters");
            return;
        }else if(!(confirm.getText().toString().equals(password.getText().toString()))){
            confirm.setError("Password does not match");
            return;
        }else if(key.getText().toString().equals("DP06NK30RH64YC67")){
            admin = true;
        }else if(gender.equals("")){
            Toast.makeText(this,"Please select your gender",Toast.LENGTH_SHORT).show();
            return;
        }

            type1 = Boolean.toString(admin);
            ema = email.getText().toString().trim();
            fn = fname.getText().toString().trim();
            ln = lname.getText().toString().trim();
            ph = phone.getText().toString().trim();
            pswd = password.getText().toString().trim();
            sex = gender;
            progressBar.setVisibility(VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(ema,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Registered Succesfully",Toast.LENGTH_SHORT).show();
                        user.setFirstName(fn);
                        user.setLastname(ln);
                        user.setEmail(ema);
                        user.setPassword(pswd);
                        user.setPhone(ph);
                        user.setType(type1);
                        user.setGender(sex);
                        user.setuid(firebaseAuth.getUid());
                        mapMarker.setStamp("Test");
                        mapMarker.setSpam(0);
                        mapMarker.setFormat("Date");
                        mapMarker.setComment("TEST COMMENT");
                        mapMarker.setLat(0);
                        mapMarker.setLong(0 );
                        databaseReference.child("UserID:"+firebaseAuth.getUid().toString()).setValue(user);
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"Error"+ task.getException(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(INVISIBLE);
                    }
                }
            });
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null) && (!str.equals("")) && (str.matches("^[a-zA-Z]*$")));
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void redirect(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }

}
