package com.cell0.remind.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.cell0.remind.R;
import com.cell0.remind.fragments.TimeConditionFragment;

public class ConditionActivity extends AppCompatActivity implements TimeConditionFragment.OnFragmentInteractionListener{
    private Toolbar toolbar;
    private int minTime;
    private int maxTime;
    private int frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ReMind");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_check);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_condition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void done(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("minTime", minTime);
        resultIntent.putExtra("maxTime", maxTime);
        resultIntent.putExtra("frequency", frequency);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onTimeChanged(int minTime, int maxTime, int frequency) {
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.frequency = frequency;
    }
}
