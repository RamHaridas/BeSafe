package com.example.andriod.besafe786.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.andriod.besafe786.LoginActivity;
import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.admin.User;
import com.example.andriod.besafe786.admin.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlideshowFragment extends Fragment {
    View root;
    RadioButton r1,r2;
    RadioButton radioButton;
    EditText fname,lname,phone,gender;
    RadioGroup radioGroup;
    Button logout,save,edit;
    FirebaseAuth firebaseAuth;
    String gend;
    DatabaseReference databaseReference;
    String fn,ln,type,ph,pswd,email;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("UserID:"+firebaseAuth.getUid());
        gend = "";
        r1 = root.findViewById(R.id.male);
        r2 = root.findViewById(R.id.female);
        save = root.findViewById(R.id.save);
        save.setEnabled(false);
        edit = root.findViewById(R.id.edit);
        radioGroup = root.findViewById(R.id.radiogroup);
        fname = root.findViewById(R.id.FirstName);
        lname = root.findViewById(R.id.LastName);
        phone = root.findViewById(R.id.mobileNumber);
        gender = root.findViewById(R.id.sex);
        firebaseAuth = FirebaseAuth.getInstance();
        logout = root.findViewById(R.id.logout);
        fetchUserData();
        fname.setEnabled(false);
        lname.setEnabled(false);
        phone.setEnabled(false);
        gender.setEnabled(false);
        logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Intent i = new Intent(getContext(), LoginActivity.class);
            i.putExtra("LOGOUT",true);
            startActivity(i);
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(v);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave(v);
            }
        });
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioClick(v);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioClick(v);
            }
        });
        return root;
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
            Toast.makeText(getContext(),"Please select your gender",Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(),"Your Changes are recorded in the database, it will reflect here shortly after",Toast.LENGTH_SHORT).show();
    }

    public void radioClick(View view){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) root.findViewById(id);
        switch (view.getId()){
            case R.id.male:
                Toast.makeText(getContext(),"MALE",Toast.LENGTH_SHORT).show();
                gend = radioButton.getText().toString();
                break;
            case R.id.female:
                Toast.makeText(getContext(),"FEMALE",Toast.LENGTH_SHORT).show();
                gend = radioButton.getText().toString();
                break;
            default:
                break;
        }
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null) && (!str.equals("")) && (str.matches("^[a-zA-Z]*$")));
    }
}
