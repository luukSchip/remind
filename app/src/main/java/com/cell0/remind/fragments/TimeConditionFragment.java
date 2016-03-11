package com.cell0.remind.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cell0.remind.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.w3c.dom.Text;

public class TimeConditionFragment extends Fragment implements RangeSeekBar.OnRangeSeekBarChangeListener, SeekBar.OnSeekBarChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TimeConditionFragment";

    private String mParam1;
    private String mParam2;

    private int frequency = 1;
    private int minTime = 0;
    private int maxTime = 24;

    private TextView timeTextView;
    private RangeSeekBar timeRangeBar;
    private SeekBar frequencySeekBar;

    private OnFragmentInteractionListener mListener;

    public static TimeConditionFragment newInstance(String param1, String param2) {
        TimeConditionFragment fragment = new TimeConditionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeConditionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_condition, container, false);
        timeTextView = (TextView) v.findViewById(R.id.timeText);
        timeRangeBar = (RangeSeekBar) v.findViewById(R.id.timeRangeBar);
        frequencySeekBar = (SeekBar) v.findViewById(R.id.frequencySeekBar);

        timeRangeBar.setOnRangeSeekBarChangeListener(this);
        frequencySeekBar.setOnSeekBarChangeListener(this);

        updateTimeText();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar rangeSeekBar, Object o, Object t1) {
        minTime = (Integer) o;
        maxTime = (Integer) t1;
        updateTimeText();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        frequency = progress;
        updateTimeText();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public interface OnFragmentInteractionListener {
        public void onTimeChanged(int min, int max, int frequency);
    }

    private void updateTimeText(){
        String timeText = "Remind me " + frequency +
                " times between " + minTime + " and " + maxTime + " hrs.";
        timeTextView.setText(timeText);
        mListener.onTimeChanged(minTime,maxTime,frequency);
    }

}
