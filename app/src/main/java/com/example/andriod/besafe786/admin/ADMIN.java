package com.example.andriod.besafe786.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andriod.besafe786.R;
import com.google.firebase.auth.FirebaseAuth;

public class ADMIN extends AppCompatActivity {

    Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_d_m_i_n);
    }
    public void openMap(View v) {
        startActivity(new Intent(this, AdMap.class));
    }

    public void OpenSpam(View view) {
        startActivity(new Intent(this, Spams.class));
    }

    public void OpenDang(View view) {
        startActivity(new Intent(this, Dangerous.class));
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openProfile(View view){
        startActivity(new Intent(this, AdProfile.class));
    }
}
