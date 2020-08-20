package com.example.andriod.besafe786.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Spams extends AppCompatActivity implements AdListAdapter.OnItemList {
    private RecyclerView r;
    private RecyclerView.Adapter<AdListAdapter.MyViewHolder> ra;
    private RecyclerView.LayoutManager lmr;
    DatabaseReference databaseReference;
    ArrayList<AdListItem> list;
    static ArrayList<MapMarker> mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spams);

        mlist = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MARKERS");
        list = new ArrayList<>();
        list.add(new AdListItem("",0));
        addSpamOnCreate();
        if(mlist.size() <= 0){
            Toast.makeText(this,"Spammed Marker List is Empty",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Spammed Marker List is not empty, Please Refresh",Toast.LENGTH_SHORT).show();
        }
        r = findViewById(R.id.recycle);
        r.setHasFixedSize(true);
        lmr = new LinearLayoutManager(this);
        ra = new AdListAdapter(list,this);
        r.setLayoutManager(lmr);
        r.setAdapter(ra);
    }

    @Override
    public void OnItemCilck(int position) {
        if(position == 0){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(Spams.this,Spams.class));
            overridePendingTransition(0,0);
        }else {
            Intent i = new Intent(this, ViewMarker.class);
            i.putExtra("Pos",position);
            startActivity(i);
        }
    }

    public void addSpamOnCreate(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren()){
                    MapMarker m1 = s.getValue(MapMarker.class);
                    int spm = (Integer) m1.getSpam();
                    if(spm >= 5){
                        //Log.i("SPAM",String.valueOf(m1.getSpam()));
                        mlist.add(m1);
                        list.add(new AdListItem("Spam Count",m1.getSpam()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
