package com.knoxpo.rajivsonawala.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleFeild;
    private Button mDateButton;
    private CheckBox mCheckBox;


    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        mCrime=new Crime();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstance){

        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleFeild=(EditText)v.findViewById(R.id.crime_title_box);

        mTitleFeild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //black
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //this is one
            }
        });

        mDateButton=(Button)v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCrime.setSolved(isChecked);

            }
        });

        return v;
    }




}
