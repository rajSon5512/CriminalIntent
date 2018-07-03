package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {


    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapterFist mCrimeAdapter;
    private Crime mCrime;
    private String TAG="Your_Item";
    private int tempPosition=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        setHasOptionsMenu(true);

        return v;
    }


    public void update_subtitle(){

        CrimeLab crimeLab=CrimeLab.get(getActivity());
        int crimeSize=crimeLab.getCrime().size();
        String subtite_show=String.valueOf("No Of Crimes:"+crimeSize);
        Log.i(TAG, "update_subtitle: Your"+subtite_show);
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setTitle(subtite_show);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.new_Crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;

            case R.id.show_subtitle:
                update_subtitle();
                return true;

        default:
            return super.onOptionsItemSelected(item);
         }
    }




    private void updateUI() {
        CrimeLab cm = CrimeLab.get(getActivity());
        List<Crime> crimes = cm.getCrime();

        if(mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapterFist(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        }
        else{
            mCrimeAdapter.notifyItemChanged(tempPosition);
            Log.d(TAG, "updateUI: Your Notify Data Changed Called");
        }
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mImageView;


        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mImageView=(ImageView)itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Intent intent =CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);

           // tempPosition=mCrimeRecyclerView.getChildAdapterPosition(view);
            tempPosition = getAdapterPosition();

        }

        public final void  bind(Crime crime,int position) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            mDateTextView.setText(DateFormat.format("EEEE,d-MM-yy",crime.getDate()).toString());
            mImageView.setVisibility(crime.isSolved()? itemView.VISIBLE:itemView.INVISIBLE);



        }
    }

    private class CrimeAdapterFist extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;


        public CrimeAdapterFist(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {


            Crime crime = mCrimes.get(position);
            holder.bind(crime,position);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();

    }

}

