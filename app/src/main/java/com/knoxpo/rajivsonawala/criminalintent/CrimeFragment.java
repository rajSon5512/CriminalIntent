package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Intent;
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

import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleFeild;
    private Button mDateButton;
    private CheckBox mCheckBox;
    private static final String ARG_CRIME_ID="Crime_ID";
    private Button mbackToFirst;
    private Button mGotoEnd;



    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        mCrime=new Crime();

      //  UUID uuid=(UUID)getActivity().getIntent().getSerializableExtra(CriminalIntent. EXTRA_NAME_LAST);

        UUID uuid=(UUID)getArguments().getSerializable(ARG_CRIME_ID);

        mCrime=CrimeLab.get(getActivity()).getCrime(uuid);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstance){

        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleFeild=(EditText)v.findViewById(R.id.crime_title_box);
        mTitleFeild.setText(mCrime.getTitle());


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
        mCheckBox.setChecked(mCrime.isSolved());

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCrime.setSolved(isChecked);

            }
        });

        mbackToFirst=v.findViewById(R.id.back_to_first);
        mGotoEnd=v.findViewById(R.id.go_to_end);
        final CrimeLab Crime=CrimeLab.get(getActivity());



        mbackToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCrime=Crime.getCrime().get(0);

                Intent intent =CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
                startActivity(intent);

            }
        });

        mGotoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCrime=Crime.getCrime().get(Crime.getCrime().size()-1);

                Intent intent =CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
                startActivity(intent);


            }
        });

        return v;
    }

    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }




}
