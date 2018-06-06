package com.knoxpo.rajivsonawala.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CriminalIntent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal_intent);

        FragmentManager fm=getSupportFragmentManager();

        Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){

            fragment=new CrimeFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();


        }


    }
}
