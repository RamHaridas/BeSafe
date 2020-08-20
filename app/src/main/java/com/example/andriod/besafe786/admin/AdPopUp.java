package com.example.andriod.besafe786.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;

public class AdPopUp extends AppCompatActivity {
    TextView t1,t2,cmnt,tstamp;
    MapMarker m1;
    Intent intent;
    //DatabaseReference databaseReference;
    Double lat;
    Double lng;@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_pop_up);
        intent = getIntent();
        tstamp = findViewById(R.id.stamp);
        //spam = findViewById(R.id.spam);
        cmnt = findViewById(R.id.comment);
        t1 = findViewById(R.id.lat);
        t2 = findViewById(R.id.lng);
        final Intent intent = getIntent();
        lat = (Double) intent.getDoubleExtra("Lat",0.0);
        lng = (Double) intent.getDoubleExtra("Long",0.0);
        String comment = intent.getStringExtra("COMMENT");
        String date = intent.getStringExtra("Date");
        String stamp = intent.getStringExtra("Stamp");
        //t1.setText("Lat:"+lat.toString());
        //t2.setText("Long:"+lng.toString());
        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng ;
        cmnt.setText("Comment: "+comment);
        t1.setText(geoUri);
        t2.setText("Date and Time: "+date);
        tstamp.setText("Possible Danger during "+stamp+" time");
        popup();
    }

    protected void popup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.5));
        WindowManager.LayoutParams params= getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
    }
}
