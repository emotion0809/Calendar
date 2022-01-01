package com.example.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SqlTable = "CREATE TABLE IF NOT EXISTS Note (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT not null," +
                "type TEXT not null," +
                "color INTEGER not null," +
                "isAllDay TEXT not null," +
                "startYear INTEGER not null," +
                "startMonth INTEGER not null," +
                "startDate INTEGER not null," +
                "startHour INTEGER not null," +
                "startMinute INTEGER not null," +
                "endYear INTEGER not null," +
                "endMonth INTEGER not null," +
                "endDate INTEGER not null," +
                "endHour INTEGER not null," +
                "endMinute INTEGER not null" + ")";
        sqLiteDatabase.execSQL(SqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL = "DROP TABLE Note";
        sqLiteDatabase.execSQL(SQL);
    }

}
