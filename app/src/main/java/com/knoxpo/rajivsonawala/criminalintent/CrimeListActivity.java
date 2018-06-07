package com.knoxpo.rajivsonawala.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment creatFragment() {
        return new CrimeListFragment();
    }
}
