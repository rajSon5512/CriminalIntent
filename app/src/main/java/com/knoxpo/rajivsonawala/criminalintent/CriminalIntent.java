package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CriminalIntent extends SingleFragmentActivity {

    private static final String TAG = CriminalIntent.class.getSimpleName();
    public static final String EXTRA_NAME_LAST = TAG + ".EXTRA_NAME_LAST";


    @Override
    protected Fragment creatFragment() {

        UUID crim_Id=(UUID)getIntent().getSerializableExtra(EXTRA_NAME_LAST);
        return CrimeFragment.newInstance(crim_Id);

    }

    public static Intent newIntent(Context packageContext, UUID Id){

        Intent intent=new Intent(packageContext,CriminalIntent.class);
        intent.putExtra(EXTRA_NAME_LAST,Id);
        return intent;

    }



}
