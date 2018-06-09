package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;

    public static CrimeLab get(FragmentActivity context) {

        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;

    }

    private CrimeLab(Context context) {

        mCrimeList = new ArrayList<Crime>();


        for(int i=0;i<100;i++){

            Crime crime=new Crime();
            crime.setTitle("Crime :"+i);
            crime.getDate();
            crime.setSolved(i%2==0);//Every other one
            crime.setSerious(i%3 == 0);
            mCrimeList.add(crime);
        }

    }


    public List<Crime> getCrime() {

        return mCrimeList;
    }

    public Crime getCrime(UUID id) {

        for (Crime crime : mCrimeList) {

            if (crime.getId().equals(id)) {
                return crime;
            }

        }

        return null;
    }


}
