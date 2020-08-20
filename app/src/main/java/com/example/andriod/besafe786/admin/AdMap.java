package com.example.andriod.besafe786.admin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener{
    double l1,l2;
    boolean act = false;
    String comment;
    public static List<MapMarker> mList;
    DatabaseReference databaseReference;
    public boolean add, remove;
    Switch aSwitch, bSwitch;
    private GoogleMap mMap;
    int size = 0;
    public MapMarker marker;
    Location currLoc;
    public static Location loc;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_map);

        firebaseAuth = FirebaseAuth.getInstance();
        add = false;
        remove = false;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        aSwitch = findViewById(R.id.add);
        bSwitch = findViewById(R.id.remove);
        add = false;
        remove = false;
        marker = new MapMarker();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MARKERS");
        countMarker();
        mList = new ArrayList<MapMarker>();
    }
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currLoc = location;
                    //Toast.makeText(getApplicationContext(),currLoc.toString(),Toast.LENGTH_LONG).show();
                    //Log.i("Location",location.toString());
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(AdMap.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        loc = currLoc;
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        LatLng latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        //mMap.addMarker(markerOptions);
        addMarkerOnCreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    public void addMarker(View view) {
        add = true;
    }

    public void removeMarker(View view) {
        remove = true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (add) {
            //countMarker();
            l1 = latLng.latitude;
            l2 = latLng.longitude;
            //mMap.addMarker(new MarkerOptions().position(latLng).title("MARKERS"));
            aSwitch.toggle();
            Toast.makeText(this, "Marker Added Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AdMap.this, AdPopUp2.class);
            startActivityForResult(i,31);
            add = false;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Marker marker1 = marker;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("MARKERS");
        if (remove) {
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot markeSnapshot : dataSnapshot.getChildren()) {
                        MapMarker m1 = markeSnapshot.getValue(MapMarker.class);
                        if (((Double) m1.getLat()).equals(marker1.getPosition().latitude)) {
                            if (((Double) m1.getLong()).equals(marker1.getPosition().longitude)) {
                                markeSnapshot.getRef().removeValue();
                                remove = false;
                                bSwitch.toggle();
                                marker1.remove();
                                Toast.makeText(getApplicationContext(),"Marker is removed from Database, it will reflect shortly in the map",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return true;
        }else {
            if(!act)
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot markeSnapshot : dataSnapshot.getChildren()) {
                            MapMarker m1 = markeSnapshot.getValue(MapMarker.class);
                            if (((Double) m1.getLat()).equals(marker1.getPosition().latitude)) {
                                if (((Double) m1.getLong()).equals(marker1.getPosition().longitude)) {
                                    Intent i = new Intent(getApplicationContext(), AdPopUp.class);
                                    i.putExtra("Lat", m1.getLat());
                                    i.putExtra("Long", m1.getLong());
                                    i.putExtra("COMMENT",m1.getComment());
                                    i.putExtra("Date",m1.getFormat());
                                    i.putExtra("Stamp",m1.getStamp());
                                    startActivityForResult(i,31);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            return false;
        }
    }
    public void countMarker() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /* To fetch markers for firebase and add them onto map */
    public void addMarkerOnCreate() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot markeSnapshot : dataSnapshot.getChildren()) {
                    MapMarker m1 = markeSnapshot.getValue(MapMarker.class);
                    mList.add(m1);
                }
                for (int i = 0; i < mList.size(); ++i) {
                    LatLng latLng = new LatLng(mList.get(i).getLat(), mList.get(i).getLong());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Danger").icon(bitmapDescriptorFromVector(AdMap.this,R.drawable.ic_redmarker));
                    mMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void refreshActivity(View view){
        finish();
        overridePendingTransition(0,0);
        startActivity(new Intent(AdMap.this,AdMap.class));
        overridePendingTransition(0,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 31 && resultCode == 31){
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());
            //Toast.makeText(this,data.getStringExtra("COMMENT"),Toast.LENGTH_SHORT).show();
            comment = data.getStringExtra("COMMENT");
            String stamp = data.getStringExtra("Tstamp");
            marker.setComment(comment);
            marker.setLat(l1);
            marker.setLong(l2);
            marker.setSpam(0);
            marker.setFormat(format);
            marker.setStamp(stamp);
            marker.setAgree(0);
            marker.setuid(firebaseAuth.getUid());
            databaseReference.child(format+"_UID:"+firebaseAuth.getUid()).setValue(marker);
        }else if(requestCode == 32 && resultCode == 32){
            data.getIntExtra("Spam",0);
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(context,vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
