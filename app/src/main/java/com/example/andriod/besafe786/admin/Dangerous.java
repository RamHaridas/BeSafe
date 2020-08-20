package com.example.andriod.besafe786.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.example.andriod.besafe786.R;
import com.example.andriod.besafe786.MapMarker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dangerous extends AppCompatActivity implements AdListAdapter.OnItemList{
    private RecyclerView r;
    private RecyclerView.Adapter<AdListAdapter.MyViewHolder> ra;
    DatabaseReference databaseReference;
    ArrayList<AdListItem> list;
    private RecyclerView.LayoutManager lmr;
    static ArrayList<MapMarker> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous);

        mList = new ArrayList<>();
        list=new ArrayList<AdListItem>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MARKERS");
        list.add(new AdListItem(" ",0));
        r=findViewById(R.id.recycle);
        r.setHasFixedSize(true);
        lmr=new LinearLayoutManager(this);
        ra=new AdListAdapter(list,this);
        r.setLayoutManager(lmr);
        r.setAdapter(ra);
        addDangerousOnCreate();
    }

    @Override
    public void OnItemCilck(int position) {
        if(position == 0){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(this,Dangerous.class));
            overridePendingTransition(0,0);
        }else {
            Intent i = new Intent(this, ViewDangerous.class);
            i.putExtra("Pos",position);
            startActivity(i);
        }
    }

    public void addDangerousOnCreate(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    MapMarker m1 = d.getValue(MapMarker.class);
                    int agree = (Integer)m1.getAgree();
                    if(agree >= 5){
                        mList.add(m1);
                        list.add(new AdListItem("Dangerous Marker",m1.getAgree()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
