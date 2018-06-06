package com.knoxpo.rajivsonawala.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CriminalIntent extends SingleFragmentActivity {

    @Override
    protected Fragment creatFragment() {
        return new CrimeFragment();
    }


}
