package com.example.andriod.besafe786.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andriod.besafe786.R;

public class AdPopUp2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    String comment;
    TextView cmnt;
    Intent intent;
    int index;
    String time [] = {"Select","Morning","Afternoon","Evening","Night"};
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_pop_up2);
        popup();
        intent = getIntent();
        cmnt = findViewById(R.id.comment);
        spinner = findViewById(R.id.SELECT);
        arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,time);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
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

    public void closePopup(View view) {
        comment = cmnt.getText().toString();
        String spin = time[index];
        if (comment.isEmpty()) {
            Toast.makeText(this, "Please Describe the reason you added this marker", Toast.LENGTH_SHORT).show();
        }else if(spin.equals("Select")){
            Toast.makeText(this,"Please select time of day when danger is more",Toast.LENGTH_SHORT).show();
        }else{
            intent.putExtra("COMMENT",comment);
            intent.putExtra("Tstamp",time[index]);
            setResult(31,intent);
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        index = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
