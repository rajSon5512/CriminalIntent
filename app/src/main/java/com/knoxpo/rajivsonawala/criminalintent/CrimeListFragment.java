package com.knoxpo.rajivsonawala.criminalintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {


    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapterFist mCrimeAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        CrimeLab cm = CrimeLab.get(getActivity());
        List<Crime> crimes = cm.getCrime();

        mCrimeAdapter = new CrimeAdapterFist(crimes);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);

    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }
    }

    private class SeriousCrimeHolder extends CrimeHolder {

        private Button mCallButton;

        public SeriousCrimeHolder(View itemView) {
            super(itemView);
            mCallButton = itemView.findViewById(R.id.btn_call);
            mCallButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_call:
                    //make a call
                    Intent callIntent = new Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:100")
                    );
                    //callIntent.setData);
                    startActivity(callIntent);
                    break;
                default:
                    super.onClick(view);
            }
        }
    }

    private class CrimeAdapterFist extends RecyclerView.Adapter<CrimeHolder> {

        private static final int
                TYPE_NORMAL_CRIME = 0,
                TYPE_SERIOUS_CRIME = 1;

        private List<Crime> mCrimes;

        public CrimeAdapterFist(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case TYPE_NORMAL_CRIME:
                    View normalView = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                    return new CrimeHolder(normalView);
                case TYPE_SERIOUS_CRIME:
                    View seriousView = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false);
                    return new SeriousCrimeHolder(seriousView);
                default:
                    throw new RuntimeException("Not a valid view type: " + viewType);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.isSerious()) {
                return TYPE_SERIOUS_CRIME;
            }
            return TYPE_NORMAL_CRIME;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

    }


}

