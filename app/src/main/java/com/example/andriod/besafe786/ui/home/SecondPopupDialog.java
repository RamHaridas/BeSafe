package com.example.andriod.besafe786.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.andriod.besafe786.R;


public class SecondPopupDialog extends DialogFragment implements AdapterView.OnItemSelectedListener  {
    public interface DataCarrier2{
        void sendComment(String comment);
        void sendStamp(String stamp);
    }
    private static final String TAG="Second popup";
    DataCarrier2 dc2;
    private View view;
    EditText etcomment;
    Button bclose;
    Spinner spinner;
    String comment;
    int index;
    String time [] = {"Select","Morning","Afternoon","Evening","Night"};
    ArrayAdapter arrayAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_popup_dialog, container, false);
        etcomment = view.findViewById(R.id.comment);
        spinner = view.findViewById(R.id.SELECT);
        bclose=view.findViewById(R.id.close);
        arrayAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,time);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        bclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(v);
            }
        });
        return view;
    }
    public void closePopup(View view) {
        comment = etcomment.getText().toString().trim();
        String spin = time[index];
        if (comment.isEmpty()) {
            Toast.makeText(getContext(), "Please Describe the reason you added this marker", Toast.LENGTH_SHORT).show();
        }else if(spin.equals("Select")){
            Toast.makeText(getContext(),"Please select time of day when danger is more likely encountered",Toast.LENGTH_SHORT).show();
        }else{
            dc2.sendComment(comment);
            dc2.sendStamp(time[index]);
            getDialog().dismiss();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        index = position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            dc2=(SecondPopupDialog.DataCarrier2) getTargetFragment();
        }catch (ClassCastException c){
            Log.e(TAG,"OnAttachException"+c.getMessage());
        }
    }

}
