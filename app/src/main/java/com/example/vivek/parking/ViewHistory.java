package com.example.vivek.parking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewHistory extends AppCompatActivity {

    //Declare inside class
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        //Bind inside onCreate()
        tvData = (TextView) findViewById(R.id.tvData);

        //Display HomeButton

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Get message from BMIActivity
        Intent i = getIntent();
        String msg = i.getStringExtra("msg");

        tvData.setText(msg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(ViewHistory.this, ParkingActivity.class);
            //startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
