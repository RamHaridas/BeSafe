package com.example.andriod.besafe786.ui.home;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.andriod.besafe786.MapMarker;
import com.example.andriod.besafe786.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, com.example.andriod.besafe786.ui.home.PopupDialog.DataCarrier,SecondPopupDialog.DataCarrier2 {
    private double l1,l2;
    DataSnapshot tempSnapshot;
    double lat_a,long_a;
    private boolean act = false;
    MapMarker m1;
    Button btn_search;
    EditText search_loc;
    String comment;
    MapMarker tempMarker;
    private MapMarker marker;
    public static List<MapMarker> mList;
    FirebaseAuth firebaseAuth;
    FusedLocationProviderClient fusedLocationProviderClient;
//    private DataSnapshot tempSnaphot;
    private DatabaseReference databaseReference;
    private boolean add, remove;
    private Switch aSwitch, bSwitch;
    private GoogleMap mMap;
    private int size = 0;
    Location currLoc;
    public static Location loc;
    private MapView mMapView;
    GoogleMap googleMap;
    ArrayList<MapMarker> spamList;
    ArrayList<MapMarker> agreeList;
    private static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private String[] permissions= new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_LOCATION=101;
    private View view;
    private int rspam,ragree;
    String rcomment,rstamp;
    private static final String TAG="HomeFragment";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        checkPermissions();
        search_loc = view.findViewById(R.id.search_loc);
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        agreeList = new ArrayList<MapMarker>();
        spamList = new ArrayList<MapMarker>();
        btn_search = view.findViewById(R.id.search);
        add = false;
        remove = false;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MARKERS");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        fetchLastLocation();
        boolean per=checkPermissions();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        aSwitch = view.findViewById(R.id.add);
        bSwitch = view.findViewById(R.id.remove);
        add = false;
        remove = false;
        marker = new MapMarker();
        countMarker();
        mList = new ArrayList<MapMarker>();
        mMapView.getMapAsync(HomeFragment.this);
        Button re=view.findViewById(R.id.refresh);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshActivity(v);
            }
        });
        /*aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add = true;
            }
        });
        bSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove = true;
            }
        });*/
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    add = true;
                    //Toast.makeText(getContext(),"ADD is TRUE",Toast.LENGTH_SHORT).show();
                }else{
                    add = false;
                    //Toast.makeText(getContext(),"ADD is FALSE",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    remove = true;
                    //Toast.makeText(getContext(),"REM is TRUE",Toast.LENGTH_SHORT).show();
                }else{
                    remove = false;
                    //Toast.makeText(getContext(),"REM is FALSE",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate(v);
            }
        });
        addMarkerOnCreate();
        return view;
    }
    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 200, 10, 0);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        LatLng latLng = new LatLng(lat_a,long_a);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        //googleMap.addMarker(markerOptions);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        if (add) {
            //countMarker();
            l1 = latLng.latitude;
            l2 = latLng.longitude;
            //Toast.makeText(getContext(), "Marker Added Successfully", Toast.LENGTH_SHORT).show();
            SecondPopupDialog  spd=new SecondPopupDialog();
            spd.setTargetFragment(HomeFragment.this,1);
            spd.show(getFragmentManager(),"Second popup");
            aSwitch.toggle();
            add = false;

            //Toast.makeText(this,data.getStringExtra("COMMENT"),Toast.LENGTH_SHORT).show();
            comment = rcomment;
            String stamp = rstamp;

        }
        if(!add){
            Toast.makeText(getContext(),"Switch is OFF",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Marker marker1 = marker;
     //   final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Markers");
        if (remove) {
            act = true;
          databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot markeSnapshot : dataSnapshot.getChildren()) {
                        MapMarker m1 = markeSnapshot.getValue(MapMarker.class);
                        if (((Double) m1.getLat()).equals(marker1.getPosition().latitude)) {
                            if (((Double) m1.getLong()).equals(marker1.getPosition().longitude)) {
                                if(m1.getuid().equals(firebaseAuth.getUid())) {
                                    markeSnapshot.getRef().removeValue();
                                    remove = false;
                                    bSwitch.toggle();
                                    marker1.remove();
                                    Toast.makeText(getContext(), "Marker is removed from Database, it will reflect shortly in the map", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "You cannot remove markers that are added by other users", Toast.LENGTH_SHORT).show();
                                    remove = false;
                                    bSwitch.toggle();
                                }
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
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot markeSnapshot : dataSnapshot.getChildren()) {
                            MapMarker m1 = markeSnapshot.getValue(MapMarker.class);
                            if (((Double) m1.getLat()).equals(marker1.getPosition().latitude)) {
                                if (((Double) m1.getLong()).equals(marker1.getPosition().longitude)) {
                                 Bundle args=new Bundle();
                                 PopupDialog pd=new PopupDialog();
                                 pd.setTargetFragment(HomeFragment.this,1);
                                 args.putDouble("Lat",m1.getLat());
                                 args.putDouble("Lng",m1.getLong());
                                 args.putString("comment",m1.getComment());
                                 args.putString("Date",m1.getFormat());
                                 args.putString("stamp",m1.getStamp());
                                 args.putString("uid",m1.getuid());
                                 tempSnapshot = markeSnapshot;
                                 tempMarker = m1;
                                 pd.setArguments(args);
                                 pd.show(getFragmentManager(),"PopupDialog");
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;
                        }
                    }
                }
                return;
            }
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
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currLoc = location;
                    lat_a = location.getLatitude();
                    long_a = location.getLongitude();
                    //Toast.makeText(getContext(),currLoc.toString(),Toast.LENGTH_LONG).show();
                    Log.i("Location",location.toString());
                    MapView mv =view.findViewById(R.id.mapView);
                    mv.getMapAsync(HomeFragment.this);
                }
            }
        });
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    public void refreshActivity(View view){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
    @Override
    public void sendSpam(int spam) {
        rspam=spam;
        if(spam == 0){
            MapMarker mTemp = tempSnapshot.getValue(MapMarker.class);
            tempSnapshot.getRef().child("spam").setValue(mTemp.getSpam()+1);
            spamList.add(tempMarker);
            saveSharedPreferencesLogList(getContext(),spamList);
        }else if(spam == 1){
            Toast.makeText(getContext(),"Already Spammed by you",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void sendAgree(int agree) {
        rspam=agree;
        if(agree == 0){
            MapMarker mt = tempSnapshot.getValue(MapMarker.class);
            tempSnapshot.getRef().child("agree").setValue(mt.getAgree()+1);
            agreeList.add(mt);
            saveSharedPreferencesLogList2(getContext(),agreeList);
        }else if(agree == 1){
            Toast.makeText(getContext(),"Your Agree Response already recorded",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void sendComment(String comment) {
        rcomment = comment;
    }
    @Override
    public void sendStamp(String stamp) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = s.format(new Date());
        rstamp = stamp;
        marker.setComment(rcomment);
        marker.setLat(l1);
        marker.setLong(l2);
        marker.setSpam(0);
        marker.setFormat(format);
        marker.setStamp(rstamp);
        marker.setAgree(0);
        marker.setuid(firebaseAuth.getUid());
        databaseReference.child(format+"_UID:"+firebaseAuth.getUid()).setValue(marker);
        LatLng latLng = new LatLng(l1,l2);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Danger").position(latLng)
                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_pinkmarker));
        googleMap.addMarker(markerOptions);
    }
    public static void saveSharedPreferencesLogList(Context context, List<MapMarker> markerList) {
        SharedPreferences mPrefs = context.getSharedPreferences("spam", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(markerList);
        prefsEditor.putString("mySpam", json);
        prefsEditor.apply();
    }

    public static void saveSharedPreferencesLogList2(Context context, List<MapMarker> markerList) {
        SharedPreferences mPrefs = context.getSharedPreferences("agree", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(markerList);
        prefsEditor.putString("myAgree", json);
        prefsEditor.apply();
    }

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
                    if(mList.get(i).getuid().equals(firebaseAuth.getUid())) {
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Danger").position(latLng)
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_pinkmarker));
                        googleMap.addMarker(markerOptions);
                    }else{
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Danger").position(latLng)
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_redmarker));
                        googleMap.addMarker(markerOptions);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(context,vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    public void geoLocate(View view){
        if(search_loc.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please Enter Location First",Toast.LENGTH_SHORT).show();
        }
        Log.d("Geo","Geo Loacating");
        String source = search_loc.getText().toString();
        //source = "friends colony, nagpur";
        //String des = dest.getText().toString().trim();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(source,1);
        }catch(IOException e){
            Toast.makeText(view.getContext(),"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double l = address.getLatitude();
            double l2 = address.getLongitude();
            addMarkerOnSearch(l,l2);
            Log.d("Location found",address.toString());
            //Toast.makeText(view.getContext(),"Address:"+address,Toast.LENGTH_SHORT).show();
        }
    }
    public void addMarkerOnSearch(double lat, double lng){
        LatLng src = new LatLng(lat,lng);
        //sourceMarker = new MarkerOptions().position(src).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(src));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(src,15));
        //mMap.addMarker(sourceMarker);
    }
}
