package mdad.localdata.mypracticeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {


    public SqliteDatabaseHelper(@Nullable Context context) {

        super(context, "userinfo.db", null, 1);

    }

    //Creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQLComm = "CREATE TABLE UserInfo" +
                "(UserId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL, " +
                "UserGender TEXT NOT NULL, " +
                "UserAge INTEGER NOT NULL, " +
                "UserWeight INTEGER NOT NULL, " +
                "UserWakeUpTime TEXT NOT NULL, " +
                "UserBedTime TEXT NOT NULL)";

        db.execSQL(createTableSQLComm);
    }

    //This is when database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Inserting Data to UserInfo table
    public boolean insertData(int UserId, String Username, String Password, String UserGender, int UserAge, int UserWeight, String UserWakeUpTime, String UserBedTime) {

        SQLiteDatabase db = this.getWritableDatabase(); // Writing to Database to insert Data

        ContentValues contentValues = new ContentValues(); // Content Values store values in pairs
        contentValues.put("UserId", UserId);
        contentValues.put("Username", Username);
        contentValues.put("Password", Password);
        contentValues.put("UserGender", UserGender);
        contentValues.put("UserAge", UserAge);
        contentValues.put("UserWeight", UserWeight);
        contentValues.put("UserWakeUpTime", UserWakeUpTime);
        contentValues.put("UserBedTime", UserBedTime);

        long result = db.insert("UserInfo", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }


    //Update Data in UserInfo table
//    public boolean insertData(int UserId, String Username, String Password, String UserGender, int UserAge, int UserWeight, String UserWakeUpTime, String UserBedTime) {
//
//        SQLiteDatabase db = this.getWritableDatabase(); // Writing to Database to insert Data
//
//        ContentValues contentValues = new ContentValues(); // Content Values store values in pairs
//        contentValues.put("UserId", UserId);
//        contentValues.put("Username", Username);
//        contentValues.put("Password", Password);
//        contentValues.put("UserGender", UserGender);
//        contentValues.put("UserAge", UserAge);
//        contentValues.put("UserWeight", UserWeight);
//        contentValues.put("UserWakeUpTime", UserWakeUpTime);
//        contentValues.put("UserBedTime", UserBedTime);
//
//        long result = db.insert("UserInfo", null, contentValues);
//        if (result == -1) {
//            return false;
//        }
//        else {
//            return true;
//        }
//    }

}
