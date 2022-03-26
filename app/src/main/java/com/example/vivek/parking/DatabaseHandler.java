package com.example.vivek.parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by vivek on 25-02-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    Context context;

    public DatabaseHandler(Context context) {
        //Name of DB is PARKING_DB, factory is null and version is 1
        super(context, "PARKING_DB", null, 1);
        //Log.d("PARKING_DB", "PARKING_DB database created");
        this.context = context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table name is parking_table and db name is PARKING_DB
        String sql = "create table parking_table(id number,start_date text)";
        db.execSQL(sql);
        //Log.d("PARKING_DB", "parking_table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists parking_table";
        db.execSQL(sql);
        onCreate(db);
        //Log.d("DB", "parking_table dropped successfully");
    }

    public void addParking(int id, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("start_date", date);
        long rid = db.insert("parking_table", null, contentValues);
        if (rid == -1) {
            Toast.makeText(context, "Insert issue", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "1 record inserted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public String getParking() {
        StringBuffer sb = new StringBuffer();
        Cursor cursor = db.query("parking_table", null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No records to show", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                sb.append(id + " " + date + "\n");
                //Log.d("PARKING_DB", "Selected row");
            } while (cursor.moveToNext());
        }
        return sb.toString();
    }
}
