package com.example.andriod.besafe786.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.andriod.besafe786.LoginActivity;
import com.example.andriod.besafe786.ui.*;
import com.example.andriod.besafe786.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdProfile extends AppCompatActivity {

    RadioButton radioButton;
    EditText fname,lname,phone,gender;
    RadioGroup radioGroup;
    Button logout,save;
    FirebaseAuth firebaseAuth;
    String gend;
    DatabaseReference databaseReference;
    String fn,ln,type,ph,pswd,email;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_profile);

        sharedPreferences = getSharedPreferences("ADMIN",0);
        editor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("UserID:"+firebaseAuth.getUid());
        gend = "";
        save = findViewById(R.id.save);
        save.setEnabled(false);
        radioGroup = findViewById(R.id.radiogroup);
        fname = findViewById(R.id.FirstName);
        lname = findViewById(R.id.LastName);
        phone = findViewById(R.id.mobileNumber);
        gender = findViewById(R.id.sex);
        firebaseAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        fetchUserData();
        fname.setEnabled(false);
        lname.setEnabled(false);
        phone.setEnabled(false);
        gender.setEnabled(false);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent i = new Intent(AdProfile.this, LoginActivity.class);
                i.putExtra("LOGOUT",true);
                editor.putBoolean("ADMIN",false);
                editor.apply();
                startActivity(i);
            }
        });
    }

    public void onEdit(View view){
        save.setEnabled(true);
        radioGroup.setVisibility(View.VISIBLE);
        gender.setVisibility(View.INVISIBLE);
        fname.setEnabled(true);
        lname.setEnabled(true);
        phone.setEnabled(true);
        gender.setEnabled(true);
    }

    public void onSave(View view){

        if(!isStringOnlyAlphabet(fname.getText().toString().trim())){
            fname.setError("First Name can only contain Alphabets");
            return;
        }else if(!isStringOnlyAlphabet(lname.getText().toString().trim())){
            lname.setError("Last Name can only contain Alphabets");
            return;
        }else if(phone.getText().toString().length() < 10 || phone.getText().toString().length() > 13){
            phone.setError("Invalid Mobile number");
            return;
        }else if(gend.equals("")){
            Toast.makeText(this,"Please select your gender",Toast.LENGTH_SHORT).show();
            return;
        }
        fn = fname.getText().toString().trim();
        ln = lname.getText().toString().trim();
        ph = phone.getText().toString().trim();
        User user = new User();
        user.setType(type);
        user.setPassword(pswd);
        user.setEmail(email);
        user.setFirstName(fn);
        user.setLastname(ln);
        user.setPhone(ph);
        user.setGender(gend);
        user.setuid(firebaseAuth.getUid());
        databaseReference.setValue(user);
        radioGroup.setVisibility(View.INVISIBLE);
        save.setEnabled(false);
        radioGroup.setVisibility(View.INVISIBLE);
        gender.setVisibility(View.VISIBLE);
        fname.setEnabled(false);
        lname.setEnabled(false);
        phone.setEnabled(false);
        gender.setEnabled(false);
        Toast.makeText(this,"Your Changes are recorded in the database, it will reflect here shortly after",Toast.LENGTH_SHORT).show();
    }

    public void radioClick(View view){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton)findViewById(id);
        switch (view.getId()){
            case R.id.male:
                Toast.makeText(this,"MALE",Toast.LENGTH_SHORT).show();
                gend = radioButton.getText().toString();
                break;
            case R.id.female:
                Toast.makeText(this,"FEMALE",Toast.LENGTH_SHORT).show();
                gend = radioButton.getText().toString();
                break;
            default:
                break;
        }
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null) && (!str.equals("")) && (str.matches("^[a-zA-Z]*$")));
    }

    public void fetchUserData(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u1 = dataSnapshot.getValue(User.class);
                fn = u1.getFirstName();
                ln = u1.getLastname();
                ph = u1.getPhone();
                type = u1.getType();
                pswd = u1.getPassword();
                email = u1.getEmail();
                //gn = u1.getGender();
                gend = u1.getGender().toString();
                fname.setText(fn);
                lname.setText(ln);
                phone.setText(ph);
                gender.setText(gend);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
