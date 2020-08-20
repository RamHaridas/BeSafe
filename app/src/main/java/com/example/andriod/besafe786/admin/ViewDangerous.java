package com.example.andriod.besafe786.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;

public class ViewDangerous extends AppCompatActivity {

    TextView glink,time,com,stamp,spam,agree;
    MapMarker m1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dangerous);

        glink = findViewById(R.id.gmaplink);
        time = findViewById(R.id.time);
        com = findViewById(R.id.comment);
        stamp = findViewById(R.id.stamp);
        agree = findViewById(R.id.agree2);
        spam = findViewById(R.id.spam2);
        Intent i = getIntent();
        int pos = i.getIntExtra("Pos",0);
        m1 = Dangerous.mList.get(pos-1);
        double lat = (Double) m1.getLat();
        double lng = (Double) m1.getLong();
        String geoUri = "http://maps.google.com/maps?q=loc:"+lat+","+lng ;
        glink.setText(geoUri);
        time.setText("Date and Time: "+m1.getFormat().toString().trim());
        com.setText("Comment: "+m1.getComment());
        stamp.setText("Danger Time Stamp: "+m1.getStamp());
        agree.setText("Agree Count: "+String.valueOf(m1.getAgree()));
        spam.setText("Spam Count: "+String.valueOf(m1.getSpam()));
    }
}
