package com.example.andriod.besafe786.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.andriod.besafe786.MapMarker;
import com.example.andriod.besafe786.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PopupDialog extends DialogFragment {
    public interface DataCarrier{
        void sendSpam(int spam);
        void sendAgree(int agree);
    }
    public DataCarrier dc;
    private static final String TAG="PopupDialog";
    private View view;

    private TextView lattw,lngtw,commenttw,stamptw;
    public Button spam,agree;
    FirebaseAuth firebaseAuth;
    MapMarker m1;
    Intent intent;
    ArrayList<MapMarker> spamList,agreeList;
    Double lat,lng;
    Boolean user=false;
    String date,stamp,comment;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.popup_dialog, container, false);

        Bundle margs=getArguments();
        agreeList = new ArrayList<MapMarker>();
        spamList = new ArrayList<MapMarker>();
        firebaseAuth = FirebaseAuth.getInstance();
        spam=view.findViewById(R.id.spam);
        agree=view.findViewById(R.id.agree);
        if(firebaseAuth.getUid().equals(margs.getString("uid","default"))){
            spam.setEnabled(false);
            agree.setEnabled(false);
        }
        date=margs.getString("Date","ERROR");
        lat = margs.getDouble("Lat",0.0);
        lng = margs.getDouble("Lng",0.0);
        stamp=margs.getString("stamp","ERROR");
        comment=margs.getString("comment","ERROR");
        lattw=view.findViewById(R.id.lat);
        lngtw=view.findViewById(R.id.lng);
        commenttw=view.findViewById(R.id.comment);
        stamptw=view.findViewById(R.id.stamp);
        spamList = (ArrayList<MapMarker>) loadSharedPreferencesLogList(view.getContext());
        agreeList = (ArrayList<MapMarker>) loadSharedPreferencesLogList2(view.getContext());
        user=false;
        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<spamList.size(); ++i)
                    if (spamList.get(i).getLat() == lat) {
                        if (spamList.get(i).getLong() == lng) {
                            dc.sendSpam(1);
                            Log.d(TAG, "onClick: closing dialog");
                            getDialog().dismiss();
                            return;
                        }
                    }
                dc.sendSpam(0);
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree(v);
            }
        });

        if(user){
            spam.setEnabled(false);
            agree.setEnabled(false);
        }
        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng ;
        commenttw.setText("Comment: "+comment);
        lattw.setText(geoUri);
        lngtw.setText("Date and Time: "+date);
        stamptw.setText("Possible Danger during "+stamp+" time");
        return view;
    }
    public void agree(View view) {
        for (int i = 0; i < agreeList.size(); ++i) {
            if (agreeList.get(i).getLat() == lat) {
                if (agreeList.get(i).getLong() == lng) {
                    dc.sendAgree(1);
                    Log.d(TAG, "onClick: closing dialog");
                    getDialog().dismiss();
                    return;
                }
            }
        }
        dc.sendAgree(0);
        Log.d(TAG, "onClick: closing dialog");
        getDialog().dismiss();
    }
    public void spam(View v){


    }
    public static List<MapMarker> loadSharedPreferencesLogList(Context context) {
        List<MapMarker> cars = new ArrayList<MapMarker>();
        SharedPreferences mPrefs = context.getSharedPreferences("spam", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("mySpam", "");
        if (json.isEmpty()) {
            cars = new ArrayList<MapMarker>();
        } else {
            Type type = new TypeToken<List<MapMarker>>() {
            }.getType();
            cars = gson.fromJson(json, type);
        }
        return cars;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            dc=(DataCarrier) getTargetFragment();
        }catch (ClassCastException c){
            Log.e(TAG,"OnAttachException"+c.getMessage());
        }
    }
    public static List<MapMarker> loadSharedPreferencesLogList2(Context context) {
        List<MapMarker> cars = new ArrayList<MapMarker>();
        SharedPreferences mPrefs = context.getSharedPreferences("agree", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("myAgree", "");
        if (json.isEmpty()) {
            cars = new ArrayList<MapMarker>();
        } else {
            Type type = new TypeToken<List<MapMarker>>() {
            }.getType();
            cars = gson.fromJson(json, type);
        }
        return cars;
    }
}
