package com.example.vivek.parking;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvData, tvData2, tvData3, tvData4;
    String irValue, irValue2, irValue3, irValue4, msg;
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView) findViewById(R.id.tvData);
        tvData2 = (TextView) findViewById(R.id.tvData2);
        tvData3 = (TextView) findViewById(R.id.tvData3);
        tvData4 = (TextView) findViewById(R.id.tvData4);
        mRef = new Firebase("https://parking-bdf3b.firebaseio.com/NewDb");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map map = dataSnapshot.getValue(Map.class);
                String gate = (String) map.get("gate");
                if (Integer.parseInt(gate) == 0) {

                    irValue = (String) map.get("answer");
                    if (Integer.parseInt(irValue) == 0) {
                        tvData.setBackgroundResource(R.color.green);
                    } else {
                        tvData.setBackgroundResource(R.color.red);
                    }
                    irValue2 = (String) map.get("answer2");
                    if (Integer.parseInt(irValue2) == 0) {
                        tvData2.setBackgroundResource(R.color.green);
                    } else {
                        tvData2.setBackgroundResource(R.color.red);
                    }
                    irValue3 = (String) map.get("answer3");
                    if (Integer.parseInt(irValue3) == 0) {
                        tvData3.setBackgroundResource(R.color.green);
                    } else {
                        tvData3.setBackgroundResource(R.color.red);
                    }
                    irValue4 = (String) map.get("answer4");
                    if (Integer.parseInt(irValue4) == 0) {
                        tvData4.setBackgroundResource(R.color.green);
                    } else {
                        tvData4.setBackgroundResource(R.color.red);
                    }
                } else {
                    if (Integer.parseInt(irValue) == 0) {
                        msg = msg + " id1";
                    }
                    if (Integer.parseInt(irValue2) == 0) {
                        msg = msg + " id2";
                    }
                    if (Integer.parseInt(irValue3) == 0) {
                        msg = msg + " id3";
                    }
                    if (Integer.parseInt(irValue4) == 0) {
                        msg = msg + " id4";
                    }

                    Toast.makeText(MainActivity.this, "Available parking slots are : " + msg, Toast.LENGTH_SHORT).show();
                    msg = "";
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit Find Parking application?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create();
        builder.show();
    }
}
