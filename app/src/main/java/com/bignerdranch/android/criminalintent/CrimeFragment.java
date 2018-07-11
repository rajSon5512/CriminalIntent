package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT=1;
    private static final int REQUEST_CAMERA=2;
    private static final String TAG="your_item";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mSendCrimeReport;
    private Button mGetSuspect;
    private File mPhotoFiles;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;



    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFiles=CrimeLab.get(getActivity()).getPhotoFile(mCrime);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mSendCrimeReport=(Button)v.findViewById(R.id.crime_report);
        mGetSuspect=(Button)v.findViewById(R.id.choose_suspect);

        final Intent pickCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);


        mPhotoButton=(ImageButton)v.findViewById(R.id.crime_camera);
        mPhotoView=(ImageView)v.findViewById(R.id.crime_photo);

        mGetSuspect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect()!=null){

            mGetSuspect.setText(mCrime.getSuspect());
        }

        mPhotoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri= FileProvider.getUriForFile(getActivity(),"com.bignerdranch.android.criminalintent.fileprovider",mPhotoFiles);

                pickCamera.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities=getActivity().getPackageManager().queryIntentActivities(pickCamera,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo activity:cameraActivities){

                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }


                startActivityForResult(pickCamera,REQUEST_CAMERA);
            }
        });

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        mSendCrimeReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                startActivity(i);

            }
        });

        PackageManager packageManager=getActivity().getPackageManager();

        if(packageManager.resolveActivity(pickContact,packageManager.MATCH_DEFAULT_ONLY)==null){

            mGetSuspect.setEnabled(false);
        }

        updatePhotoView();

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        else if(requestCode==REQUEST_CONTACT && data!=null){

            Log.d(TAG, "onActivityResult: Your enter in onActivityResult");
            
            
            Uri contactUri=data.getData();

            String[] queryFields=new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            try{

                if(c.getCount()==0){
                    return;
                }


                c.moveToFirst();
                String suspect=c.getString(0);

                mCrime.setSuspect(suspect);
                mGetSuspect.setText(suspect);

            }
            catch (Exception e){

                Log.d(TAG, "onActivityResult: "+e.toString());

            }
            finally {

                c.close();
            }
        }
        else if(resultCode==REQUEST_CAMERA){

            Uri uri=FileProvider.getUriForFile(getActivity(),"com.bignerdranch.android.criminalintent.fileprovider",mPhotoFiles);

            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();

        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }


    private String getCrimeReport(){

        String isCrimeSolved;

        if(mCrime.isSolved()){

            isCrimeSolved=getString(R.string.crime_report_solved);

        }
        else{
            isCrimeSolved=getString(R.string.crime_report_unsolved);

        }

        String dateFormat="EEE,MMM dd";
        String dateString= android.text.format.DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect=mCrime.getSuspect();

        if(suspect==null){

            suspect=getString(R.string.crime_report_no_suspect);

        }
        else{
            suspect=getString((R.string.crime_report_suspect),suspect);
        }


        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,isCrimeSolved,suspect);

        return report;


    }

    private void updatePhotoView(){

        if(mPhotoFiles==null||!mPhotoFiles.exists()){

            mPhotoView.setImageDrawable(null);
        }
        else{

            Bitmap bitmap=PictureUtil.getScaBitmap(mPhotoFiles.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}
