package com.example.andriod.besafe786.ui.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.andriod.besafe786.R;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {
    EditText cont1,cont2,cont3,cont4,cont5;
    SharedPreferences s1;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        cont1=root.findViewById(R.id.num1);
        cont2=root.findViewById(R.id.num2);
        cont3=root.findViewById(R.id.num3);
        cont4=root.findViewById(R.id.num4);
        cont5=root.findViewById(R.id.num5);
        cont1.setEnabled(false);
        cont2.setEnabled(false);
        cont3.setEnabled(false);
        cont4.setEnabled(false);
        cont5.setEnabled(false);
        s1 = this.getActivity().getSharedPreferences("Besafecont",MODE_PRIVATE);
        cont1.setText(s1.getString("cont1", "0"));
        cont2.setText(s1.getString("cont2", "0"));
        cont3.setText(s1.getString("cont3", "0"));
        cont4.setText(s1.getString("cont4", "0"));
        cont5.setText(s1.getString("cont5", "0"));
        Button sb=root.findViewById(R.id.startb);
        sb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startService(v);
            }
        });
        sb=root.findViewById(R.id.stopb);
        sb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopService(v);
            }
        });
        sb=root.findViewById(R.id.edit);
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContacts(v);
            }
        });
        sb=root.findViewById(R.id.save);
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContacts(v);
            }
        });
        return root;
    }
    public void startService(View v) {
        Intent serviceIntent = new Intent(getContext(), ExampleService.class);
        ContextCompat.startForegroundService(getContext(), serviceIntent);
        Button b1=root.findViewById(R.id.startb);
        b1.setVisibility(View.GONE);
        b1=root.findViewById(R.id.stopb);
        b1.setVisibility(View.VISIBLE);
    }
    public void stopService(View v) {
        Intent serviceIntent = new Intent(getContext(), ExampleService.class);
        getActivity().stopService(serviceIntent);
        Button b1=root.findViewById(R.id.stopb);
        b1.setVisibility(View.GONE);
        b1=root.findViewById(R.id.startb);
        b1.setVisibility(View.VISIBLE);
    }
    public void editContacts(View view) {
        cont1.setEnabled(true);
        cont2.setEnabled(true);
        cont3.setEnabled(true);
        cont4.setEnabled(true);
        cont5.setEnabled(true);
        Button b1=root.findViewById(R.id.edit);
        b1.setVisibility(View.GONE);
        b1=root.findViewById(R.id.save);
        b1.setVisibility(View.VISIBLE);
    }

    public void saveContacts(View view) {
        s1=this.getActivity().getSharedPreferences("Besafecont", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = s1.edit();
        myEdit.putString("cont1", cont1.getText().toString());
        myEdit.putString("cont2", cont2.getText().toString());
        myEdit.putString("cont3", cont3.getText().toString());
        myEdit.putString("cont4", cont4.getText().toString());
        myEdit.putString("cont5", cont5.getText().toString());
        myEdit.apply();
        cont1.setEnabled(false);
        cont2.setEnabled(false);
        cont3.setEnabled(false);
        cont4.setEnabled(false);
        cont5.setEnabled(false);
        cont1.getText();
        cont2.getText();
        cont3.getText();
        cont4.getText();
        cont5.getText();
        Button b1=root.findViewById(R.id.save);
        b1.setVisibility(View.GONE);
        b1=root.findViewById(R.id.edit);
        b1.setVisibility(View.VISIBLE);
    }
}