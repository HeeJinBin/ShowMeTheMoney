package com.example.smtm7.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBTransactionAdapter {
    public static final String PG = "pg";
    public static final String DATE = "date";
    public static final String OFFICE = "office";
    public static final String ITEM = "item";
    public static final String PRICE = "price";

    private DatabaseTHelper databaseHelper;
    private SQLiteDatabase database;

    private static final String DATABASE_TNAME = "transaction_db";
    private static final String DATABASE_TTABLE = "transaction_table";
    private static final int DATABASE_TVERSION = 1;

    private final Context context;

    private static class DatabaseTHelper extends SQLiteOpenHelper{

        DatabaseTHelper(Context context){
            super(context, DATABASE_TNAME,null, DATABASE_TVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table if not exists transaction_table (_id integer primary key autoincrement, "+
                    "pg text not null, date integer not null, office text, item text not null, price integer not null)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS transaction_table");
            onCreate(db);
        }
    }

    public DBTransactionAdapter(Context context){
        this.context = context;
    }

    public DBTransactionAdapter open() throws SQLException{
        databaseHelper = new DatabaseTHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public long insertTransaction(String pg, int date, String office, String item, int price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PG, pg);
        contentValues.put(DATE, date);
        String off = "";
        if (office != null)
            off = office;
        contentValues.put(OFFICE, off);
        contentValues.put(ITEM, item);
        contentValues.put(PRICE, price);

        return database.insert(DATABASE_TTABLE, null, contentValues);
    }

    public Cursor searchAllTransaction() {
        String sql = "select * from "+DATABASE_TTABLE+" order by date desc;";
        return database.rawQuery(sql, null);
    }

    public Cursor searchhBottomSheetResult(String pgname, String datename, int pricename){
        String sql = "select * from "+DATABASE_TTABLE;

        String pgquery = "";
        if(!pgname.equals("")){
            pgquery += "pg = '"+pgname+"'";
        }

        String datequery = "";
        if(!datename.equals("")){
            if(datename.length()==4){
                datequery += "date = '2019"+datename+"' or date = '2018"+datename+"' or date = '2017"+datename+"' or date = '2016"+datename+"'";
            } else{
                datequery += "date = '"+datename+"'";
            }
        }

        String pricequery = "";
        if(pricename != -1){
            pricequery += "price = '"+pricename+"'";
        }

        if(!pgquery.equals("")){
            sql+=" where "+pgquery;
            if(!datequery.equals("")){
                sql+=" and "+datequery;
            }
            if(!pricequery.equals("")){
                sql+=" and "+pricequery;
            }
        } else{
            if(!datequery.equals("")){
                sql+=" where "+datequery;
                if(!pricequery.equals("")){
                    sql+=" and "+pricequery;
                }
            } else{
                if(!pricequery.equals("")){
                    sql+=" where "+pricequery;
                }
            }
        }

        sql+=" order by date desc;";

        Log.d("DBTransaction", sql);

        return database.rawQuery(sql, null);
    }

    public Cursor searchTransaction(ArrayList<String> pglist, int firstDate, int secondDate, int firstPrice, int secondPrice){
        String sql = "select * from "+DATABASE_TTABLE;

        String pgquery = "";
        if(pglist.size()!=0){
            pgquery +="( ";
            for(int i=0;i<pglist.size();i++){
                pgquery+="pg = '"+pglist.get(i)+"'";
                if(i!=(pglist.size()-1)){
                    pgquery+=" or ";
                }
            }
            pgquery+=" )";
        }

        String datequery = "";
        if(firstDate!= -1){
            datequery+="( date >='"+firstDate+"' and date <= '"+secondDate+"' )";
        }

        String pricequery = "";
        if(firstPrice != -1){
            pricequery += "( price >= '"+firstPrice+"' and price <= '"+secondPrice+"' )";
        }

        if(!pgquery.equals("")){
            sql+=" where "+pgquery;
            if(!datequery.equals("")){
                sql+=" and "+datequery;
            }
            if(!pricequery.equals("")){
                sql+=" and "+pricequery;
            }
        } else{
            if(!datequery.equals("")){
                sql+=" where "+datequery;
                if(!pricequery.equals("")){
                    sql+=" and "+pricequery;
                }
            } else{
                if(!pricequery.equals("")){
                    sql+=" where "+pricequery;
                }
            }
        }

        sql+=" order by date desc;";

        Log.d("DBTransaction", sql);

        return database.rawQuery(sql, null);
    }

    public void deleteTransaction(String pg, int date, String item, int price){
        String sql = "delete from "+DATABASE_TTABLE+" where pg = '"+pg+"' and date = '"+date+"' and item = '"+item+"' and price = '"+price+"';";
        database.execSQL(sql);
    }

    public void deleteAllTransaction(){
        String sql = "delete from "+DATABASE_TTABLE+";";
        database.execSQL(sql);
    }
}
