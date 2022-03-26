package com.example.vivek.parking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Date;

public class ParkingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Declare inside class
    TextView tvData, tvData2, tvData3, tvData4, tvMsg;
    String irValue, irValue2, irValue3, irValue4, gate, msg = "";
    private Firebase mRef;
    ArrayList<String> sensorKey = new ArrayList<>();
    ArrayList sensorValue = new ArrayList<>();
    int shortestFrom[] = {100, 100, 100, 100};
    int available[][] = new int[4][2];
    int pos, min, count = 0;
    EditText etParking;
    Button btnSave;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        //Bind inside onCreate

        tvData = (TextView) findViewById(R.id.tvData);
        tvData2 = (TextView) findViewById(R.id.tvData2);
        tvData3 = (TextView) findViewById(R.id.tvData3);
        tvData4 = (TextView) findViewById(R.id.tvData4);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        etParking = (EditText) findViewById(R.id.etParking);
        btnSave = (Button) findViewById(R.id.btnSave);

        db = new DatabaseHandler(this);

        //Create reference of Firebase and provide link of database NewDb
        mRef = new Firebase("https://parking-bdf3b.firebaseio.com/NewDb");

        //Portrait orientation fixed
        int o = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        //2nd column is initialized with distances from gate
        available[0][1] = 5;
        available[1][1] = 5;
        available[2][1] = 2;
        available[3][1] = 2;

        //Check whether connected to internet or not
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!(networkInfo != null && networkInfo.isConnected())) {
            Toast.makeText(ParkingActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        } else {

            //addChildEventListener() of Firebase is responsible for downloading data from database and values are downloaded in dataSnapshot variable.

            mRef.addChildEventListener(new ChildEventListener() {
                @Override

                //onChildAdded() method is called each time a child is added from Firebase real time database

                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    count = (count + 1) % 5;

                    //Get key of all JSON objects one by one
                    String keys = (String) dataSnapshot.getKey();
                    sensorKey.add(keys);

                    //Get values of all JSON objects one by one
                    String values = (String) dataSnapshot.getValue();
                    sensorValue.add(values);

                    //First time called
                    if (count == 1) {
                        if (Integer.parseInt((String) sensorValue.get(0)) == 0) {
                            tvData.setBackgroundResource(R.color.green);
                        } else {
                            tvData.setBackgroundResource(R.color.red);
                        }
                    }

                    //Second time called
                    if (count == 2) {
                        if (Integer.parseInt((String) sensorValue.get(1)) == 0) {
                            tvData2.setBackgroundResource(R.color.green);
                        } else {
                            tvData2.setBackgroundResource(R.color.red);
                        }
                    }

                    //Third time called
                    if (count == 3) {
                        if (Integer.parseInt((String) sensorValue.get(2)) == 0) {
                            tvData3.setBackgroundResource(R.color.green);
                        } else {
                            tvData3.setBackgroundResource(R.color.red);
                        }
                    }

                    //Fourth time called
                    if (count == 4) {
                        if (Integer.parseInt((String) sensorValue.get(3)) == 0) {
                            tvData4.setBackgroundResource(R.color.green);
                        } else {
                            tvData4.setBackgroundResource(R.color.red);
                        }
                    }
                }

                @Override

                //onChildChanged() method is called for each child changed in Firebase real time database.
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    //Get key of updated value
                    String key = dataSnapshot.getKey();
                    //Get updated value
                    String value = (String) dataSnapshot.getValue();

                    //Get index of updated key
                    int index = sensorKey.indexOf(key);
                    //Set updated value to sensorValue ArrayList at that particular index
                    sensorValue.set(index, value);

                    //Get values from sensorValue ArrayList
                    irValue = (String) sensorValue.get(0);
                    irValue2 = (String) sensorValue.get(1);
                    irValue3 = (String) sensorValue.get(2);
                    irValue4 = (String) sensorValue.get(3);
                    gate = (String) sensorValue.get(4);

                    if (Integer.parseInt(gate) == 0) {

                        msg = "";

                        if (Integer.parseInt(irValue) == 0) {
                            tvData.setBackgroundResource(R.color.green);
                        } else {
                            tvData.setBackgroundResource(R.color.red);
                        }
                        if (Integer.parseInt(irValue2) == 0) {
                            tvData2.setBackgroundResource(R.color.green);
                        } else {
                            tvData2.setBackgroundResource(R.color.red);
                        }
                        if (Integer.parseInt(irValue3) == 0) {
                            tvData3.setBackgroundResource(R.color.green);
                        } else {
                            tvData3.setBackgroundResource(R.color.red);
                        }
                        if (Integer.parseInt(irValue4) == 0) {
                            tvData4.setBackgroundResource(R.color.green);
                        } else {
                            tvData4.setBackgroundResource(R.color.red);
                        }
                    } else {

                        msg = "";

                        if (Integer.parseInt(irValue) == 0) {
                            msg = "id1";
                            available[0][0] = 0;
                            tvData.setBackgroundResource(R.color.green);
                        } else {
                            tvData.setBackgroundResource(R.color.red);
                            available[0][0] = 1;
                            shortestFrom[0] = 100;        //For second call, if that parking slot is blocked, then no need to find shortestFrom this parking and hence make it 100.
                        }
                        if (Integer.parseInt(irValue2) == 0) {
                            msg = msg + " id2";
                            available[1][0] = 0;
                            tvData2.setBackgroundResource(R.color.green);
                        } else {
                            tvData2.setBackgroundResource(R.color.red);
                            available[1][0] = 1;
                            shortestFrom[1] = 100;
                        }
                        if (Integer.parseInt(irValue3) == 0) {
                            msg = msg + " id3";
                            available[2][0] = 0;
                            tvData3.setBackgroundResource(R.color.green);
                        } else {
                            tvData3.setBackgroundResource(R.color.red);
                            available[2][0] = 1;
                            shortestFrom[2] = 100;
                        }
                        if (Integer.parseInt(irValue4) == 0) {
                            msg = msg + " id4";
                            available[3][0] = 0;
                            tvData4.setBackgroundResource(R.color.green);
                        } else {
                            tvData4.setBackgroundResource(R.color.red);
                            available[3][0] = 1;
                            shortestFrom[3] = 100;
                        }
                    }
                    if (Integer.parseInt(gate) == 0) {
                        tvMsg.setText("Welcome to VESIT Parking Allotment System");
                    }
                    if (Integer.parseInt(gate) == 1 && msg == "") {
                        tvMsg.setText("No parking available");
                    }
                    if (Integer.parseInt(gate) == 1 && msg != "") {

                        for (int i = 0; i < 4; i++) {
                            if (available[i][0] == 0) {
                                shortestFrom[i] = available[i][1];
                            }
                        }

                        //Find minimum of shortestFrom[] array
                        min = shortestFrom[0];
                        pos = 0;

                        for (int i = 0; i < shortestFrom.length; i++) {
                            if (shortestFrom[i] <= min) {
                                min = shortestFrom[i];
                                pos = i + 1;
                            }
                        }
                        tvMsg.setText("Parking available : " + msg + " and shortest parking is : id" + pos);
                    }
                }


                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String parking = etParking.getText().toString();
                if (parking.length() == 0) {
                    Toast.makeText(ParkingActivity.this, "Please enter the parking id alloted to you", Toast.LENGTH_SHORT).show();
                    return;
                }
                int parkingId = Integer.parseInt(parking);
                String date = String.valueOf(new Date());
                if (available[parkingId - 1][0] == 1) {
                    if (parkingId > 0 && parkingId < 5) {
                        db.addParking(parkingId, date);
                    }
                } else {
                    Toast.makeText(ParkingActivity.this, "No vehicle alloted on specified parking id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to exit Parking Allotment System application?");
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parking, menu);
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navEmailUs) {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:2015vivek.sohal@ves.ac.in"));
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            startActivity(i);
        } else if (id == R.id.navAboutUs) {
            Intent i = new Intent(ParkingActivity.this, AboutUs.class);
            startActivity(i);
        } else if (id == R.id.navHistory) {
            String data = db.getParking();
            if (data.length() == 0) {
                Toast.makeText(this, "No records to show", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(ParkingActivity.this, ViewHistory.class);
                i.putExtra("msg", data);
                startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
