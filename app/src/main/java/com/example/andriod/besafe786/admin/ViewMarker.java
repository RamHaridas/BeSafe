package com.example.andriod.besafe786.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewMarker extends AppCompatActivity {

    TextView glink,time,com,stamp,spam,agree,uid;
    MapMarker m1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marker);

        glink = findViewById(R.id.gmaplink);
        time = findViewById(R.id.time);
        com = findViewById(R.id.comment);
        stamp = findViewById(R.id.stamp);
        agree = findViewById(R.id.agree2);
        spam = findViewById(R.id.spam2);
        uid = findViewById(R.id.uid);
        Intent i = getIntent();
        int pos = i.getIntExtra("Pos",0);
        m1 = (MapMarker) Spams.mlist.get(pos-1);
        Log.i("LAT",String.valueOf(m1.getLat()));
        double lat = (Double) m1.getLat();
        double lng = (Double) m1.getLong();
        String geoUri = "http://maps.google.com/maps?q=loc:"+lat+","+lng ;
        glink.setText(geoUri);
        time.setText("Date and Time: "+m1.getFormat().toString().trim());
        com.setText("Comment: "+m1.getComment());
        stamp.setText("Danger Time Stamp: "+m1.getStamp());
        agree.setText("Agree Count: "+String.valueOf(m1.getAgree()));
        spam.setText("Spam Count: "+String.valueOf(m1.getSpam()));
        uid.setText("User ID: "+m1.getuid());
    }

    public void deleteMarker(View view){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MARKERS")
                .child(m1.getFormat()+"_UID:"+m1.getuid());
        databaseReference.removeValue();
        Toast.makeText(this,"Marker Successfully Deleted from database, it will be reflected shortly",Toast.LENGTH_SHORT).show();
    }
}
