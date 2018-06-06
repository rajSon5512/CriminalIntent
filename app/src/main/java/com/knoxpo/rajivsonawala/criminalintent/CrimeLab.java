package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;

    public static CrimeLab get(Context context) {

        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;

    }

    private CrimeLab(Context context) {

        mCrimeList = new ArrayList<Crime>();
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
