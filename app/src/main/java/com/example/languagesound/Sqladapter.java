package com.example.languagesound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonUiContext;
import androidx.annotation.Nullable;

import java.net.PortUnreachableException;

public class Sqladapter extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bangla.bd";
    private static final int VERSION_NAME = 3;
    private static final String TABLE_NAME = "bangla_word";
    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String ORTHO = "ortho";
    private static final String LANGUAGE = "language";

    private Context context;


    public Sqladapter(@Nullable Context context) {
        super(context, DATABASE_NAME, null,VERSION_NAME);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+TEXT+" VARCHAR,"+ORTHO+" VARCHAR,"+LANGUAGE+" VARCHAR)");

            Toast.makeText(context, "table create", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Table can not create ", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
            Toast.makeText(context, "new  tablae create ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

            Toast.makeText(context, "no create", Toast.LENGTH_SHORT).show();
        }
    }

    public long insettData(String text,String ortho, String language){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Text",text);
        contentValues.put("Ortho",ortho);
        contentValues.put("Language",language);
        long data = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return data;

    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+ID+" ASC", null);
        return cursor;

    }

    public int deleteData(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int delete = sqLiteDatabase.delete(TABLE_NAME,ID+" =  ?",new String[]{id});
        return delete;
    }


    public void deleteLast10Rows(int i) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a subquery to select the last 10 rows
        String subquery = "SELECT ID FROM " + TABLE_NAME + " ORDER BY ID ASC LIMIT "+i;

        // Create the DELETE query using the subquery
        String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE ID IN (" + subquery + ")";

        // Execute the DELETE query
        db.execSQL(deleteQuery);

        db.close();
    }
}
