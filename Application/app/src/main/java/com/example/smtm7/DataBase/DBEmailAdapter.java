package com.example.smtm7.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBEmailAdapter {
    public static final String EMAIL = "email";
    public static final String INDEXS = "indexs";

    private DatabaseEHelper databaseHelper;
    private SQLiteDatabase database;

    private static final String DATABASE_ECREATE =
            "create table if not exists email_table (email text primary key not null, indexs integer not null);";

    private static final String DATABASE_ENAME = "email_db";
    private static final String DATABASE_ETABLE = "email_table";
    private static final int DATABASE_EVERSION = 1;

    private final Context context;

    private static class DatabaseEHelper extends SQLiteOpenHelper {

        DatabaseEHelper(Context context){
            super(context, DATABASE_ENAME,null,DATABASE_EVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_ECREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS email_table");
            onCreate(db);
        }
    }

    public DBEmailAdapter(Context context){
        this.context = context;
    }

    public DBEmailAdapter open() throws SQLException {
        databaseHelper = new DatabaseEHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public long insertEmail(String email, int indexs) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(INDEXS, indexs);

        return database.insert(DATABASE_ETABLE, null, contentValues);
    }

    public Cursor searchAllEmail() {
        return database.query(DATABASE_ETABLE, new String[] { EMAIL, INDEXS }, null, null, null, null, null);
    }

    public boolean updateEmail(String email, int indexs) {
        ContentValues args = new ContentValues();
        args.put(EMAIL, email);
        args.put(INDEXS, indexs);
        return database.update(DATABASE_ETABLE, args, EMAIL + "=" + email, null) > 0;
    }

    public void deleteAllEmail(){
        String sql = "delete from "+DATABASE_ETABLE;
        database.execSQL(sql);
    }
}
