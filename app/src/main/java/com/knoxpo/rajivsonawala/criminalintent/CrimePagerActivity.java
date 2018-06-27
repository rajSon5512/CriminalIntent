package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;


public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_ID=CrimePagerActivity.class.getSimpleName();
    private Button mbackToFirst;
    private Button mGotoEnd;




    public static Intent newIntent(Context pageContext,UUID crimeId){

        Intent intent=new Intent(pageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_ID,crimeId);
        return intent;
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager_fragment);


        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_ID);

        mViewPager=(ViewPager)findViewById(R.id.crime_view_pager);
        mbackToFirst=findViewById(R.id.back_to_first);
        mGotoEnd=findViewById(R.id.go_to_end);


        mCrimes=CrimeLab.get(this).getCrime();

        FragmentManager fragmentManager=getSupportFragmentManager();


        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {

                Crime crime=mCrimes.get(position);


                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }



        });

        mbackToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mViewPager.setCurrentItem(0);

            }
        });

        mGotoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(mCrimes.size()-1);


            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }

        }


        }
}
