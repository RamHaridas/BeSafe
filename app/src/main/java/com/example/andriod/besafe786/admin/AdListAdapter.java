package com.example.andriod.besafe786.admin;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.andriod.besafe786.R;

import java.util.ArrayList;

public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.MyViewHolder>{
    ArrayList<AdListItem> list;
    OnItemList oil;
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView t1,t2,t3;
        OnItemList oil;
        public MyViewHolder(@NonNull View itemView,OnItemList oil) {
            super(itemView);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            this.oil=oil;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oil.OnItemCilck(getAdapterPosition());
        }
    }
    public AdListAdapter(ArrayList list,OnItemList oil){
        this.list=list;
        this.oil=oil;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        MyViewHolder vh=new MyViewHolder(v,oil);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AdListItem curr=list.get(position);
        if(position == 0){
            holder.t1.setText("Click Here to Refresh");
            holder.t2.setText("");
            holder.t3.setText("");
        }else {
            holder.t1.setText("Marker " + position);
            holder.t2.setText(curr.getText() + ":");
            holder.t3.setText(Integer.toString(curr.getCount()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface OnItemList{
        public void OnItemCilck(int position);
    }
}

