package com.example.andriod.besafe786.ui.gallery;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.andriod.besafe786.R;

public class TimerActivity extends AppCompatActivity {
    SharedPreferences s1;
    FusedLocationProviderClient fusedLocationProviderClient;
    double lat,lon;
    String uri;
    private TextView cdt;
    private Button cdb;
    private TextView tv;
    private CountDownTimer count;
    private long timeleft=6000;
    boolean timerrunning;
    Location l;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 111;
    String cont1,cont2,cont3,cont4,cont5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        cdt = findViewById(R.id.countdown_txt);
        cdb = findViewById(R.id.countdown_btn);
        tv = findViewById(R.id.alert);
        updateTimer();
        startTimer();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    l = location;
                    Log.i("Location",location.toString());
                    lat=l.getLatitude();
                    lon=l.getLongitude();
                    String geoUri = "ALERT!! I AM IN DANGER"+"\n"+"http://maps.google.com/maps?q=loc:" + lat + "," + lon ;
                    SmsManager smsManager = SmsManager.getDefault();
                    s1 = getSharedPreferences("Besafecont",MODE_PRIVATE);
                    cont1=s1.getString("cont1", "0");
                    cont2=s1.getString("cont2", "0");
                    cont3=s1.getString("cont3", "0");
                    cont4=s1.getString("cont4", "0");
                    cont5=s1.getString("cont5", "0");
                    try{
                        if(!cont1.equals("0")){
                            smsManager.sendTextMessage(cont1, null, geoUri, null, null);
                        }
                        if(!cont2.equals("0")){
                            smsManager.sendTextMessage(cont2, null, geoUri, null, null);
                        }
                        if(!cont3.equals("0")){
                            smsManager.sendTextMessage(cont3, null, geoUri, null, null);
                        }
                        if(!cont4.equals("0")){
                            smsManager.sendTextMessage(cont4, null, geoUri, null, null);
                        }
                        if(!cont5.equals("0")){
                            smsManager.sendTextMessage(cont5, null, geoUri, null, null);
                        }
                }
                    catch (Exception e){
                        Toast.makeText(TimerActivity.this,"Some Contacts were Incorrect",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Toast.makeText(this,"SMS Sent",Toast.LENGTH_SHORT).show();
    }
    public void startTimer(){
        count=new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft=millisUntilFinished;
                updateTimer();
            }
            @Override
            public void onFinish() {
                fetchLastLocation();
                cdt.setVisibility(View.GONE);
                cdb.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
        }.start();
        timerrunning=true;
    }
    public void Cancelcd(View view) {
        count.cancel();
        timerrunning=false;
        String a = "Alert Canceled";
        tv.setText(a);
        cdt.setVisibility(View.GONE);
        cdb.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
    }
    public void updateTimer(){
        int secounds=(int)timeleft%6000/1000;
        String timelefttext=""+secounds;
        cdt.setText(timelefttext);
    }

}
