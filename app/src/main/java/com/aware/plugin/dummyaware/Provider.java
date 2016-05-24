package com.aware.plugin.dummyaware;


import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.aware.Aware;
import com.aware.utils.DatabaseHelper;


/**
 * Created by Comet on 21/01/16.
 */
public class Provider extends ContentProvider {
    public static final int DATABASE_VERSION = 1;
    /**
     * Provider authority: com.aware.plugin.dummyaware.provider.dummyaware
     */
    public static String AUTHORITY = "com.aware.plugin.dummyaware.provider.dummyaware";
    //store activity data
    private static final int DUMMYAWARE = 1;
    private static final int DUMMYAWARE_ID = 2;

    public static final String DATABASE_NAME = "dummyaware.db";

    public static final String[] DATABASE_TABLES = {
            "plugin_dummyaware"
    };

    public static final class Dummy_AWARE_Data_Applications_Crashes implements BaseColumns {
        private Dummy_AWARE_Data_Applications_Crashes(){}

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/plugin_dummyaware");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aware.plugin.dummyaware";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aware.plugin.dummyaware";

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";
        public static final String PACKAGE_NAME  = "package_name";
        public static final String APPLICATION_NAME  = "application_name";
        public static final String APPLICATION_VERSION  = "application_version"; //ERROR_SHORT  ERROR_LONG  ERROR_CONDITION    IS_SYSTEM_APP
        public static final String ERROR_SHORT  = "error_short";
        public static final String ERROR_LONG  = "error_long";
        public static final String ERROR_CONDITION  = "error_condition";
        public static final String IS_SYSTEM_APP  = "is_system_app";
    }

    public static final String[] TABLES_FIELDS = {
        Dummy_AWARE_Data_Applications_Crashes._ID + " integer primary key autoincrement," +
        Dummy_AWARE_Data_Applications_Crashes.TIMESTAMP + " real default 0," +
        Dummy_AWARE_Data_Applications_Crashes.DEVICE_ID + " text default ''," +
        Dummy_AWARE_Data_Applications_Crashes.PACKAGE_NAME + " text default ''," +
        Dummy_AWARE_Data_Applications_Crashes.APPLICATION_NAME + " text default ''," +
        Dummy_AWARE_Data_Applications_Crashes.APPLICATION_VERSION + " real default 0," +
        Dummy_AWARE_Data_Applications_Crashes.ERROR_SHORT + " text default ''," +
        Dummy_AWARE_Data_Applications_Crashes.ERROR_LONG + " text default ''," +
        Dummy_AWARE_Data_Applications_Crashes.ERROR_CONDITION + " integer default 0," +
        Dummy_AWARE_Data_Applications_Crashes.IS_SYSTEM_APP + " integer default 0," +
        "UNIQUE("+ Dummy_AWARE_Data_Applications_Crashes.TIMESTAMP+","+ Dummy_AWARE_Data_Applications_Crashes.DEVICE_ID+")"
    };

    private static UriMatcher URIMatcher;
    private static HashMap<String, String> databaseMap;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        //AUTHORITY = getContext().getPackageName() + ".provider.template";
        URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0], DUMMYAWARE);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0] + "/#", DUMMYAWARE_ID);

        databaseMap = new HashMap<>();
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes._ID, Dummy_AWARE_Data_Applications_Crashes._ID);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.TIMESTAMP, Dummy_AWARE_Data_Applications_Crashes.TIMESTAMP);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.DEVICE_ID, Dummy_AWARE_Data_Applications_Crashes.DEVICE_ID);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.PACKAGE_NAME, Dummy_AWARE_Data_Applications_Crashes.PACKAGE_NAME);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.APPLICATION_NAME, Dummy_AWARE_Data_Applications_Crashes.APPLICATION_NAME);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.APPLICATION_VERSION, Dummy_AWARE_Data_Applications_Crashes.APPLICATION_VERSION);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.ERROR_SHORT, Dummy_AWARE_Data_Applications_Crashes.ERROR_SHORT);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.ERROR_LONG, Dummy_AWARE_Data_Applications_Crashes.ERROR_LONG);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.ERROR_CONDITION, Dummy_AWARE_Data_Applications_Crashes.ERROR_CONDITION);
        databaseMap.put(Dummy_AWARE_Data_Applications_Crashes.IS_SYSTEM_APP, Dummy_AWARE_Data_Applications_Crashes.IS_SYSTEM_APP);

        return true;
    }


    private boolean initializeDB() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            database = databaseHelper.getWritableDatabase();
        }
        return( database != null && databaseHelper != null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case DUMMYAWARE:
                count = database.delete(DATABASE_TABLES[0], selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URIMatcher.match(uri)) {
            case DUMMYAWARE:
                return Dummy_AWARE_Data_Applications_Crashes.CONTENT_TYPE;
            case DUMMYAWARE_ID:
                return Dummy_AWARE_Data_Applications_Crashes.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (!initializeDB()) {
            Log.w(AUTHORITY, "Database unavailable...");
            return null;
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (URIMatcher.match(uri)) {
            case DUMMYAWARE:
                long weather_id = database.insert(DATABASE_TABLES[0], Dummy_AWARE_Data_Applications_Crashes.DEVICE_ID, values);

                if (weather_id > 0) {
                    Uri new_uri = ContentUris.withAppendedId(
                            Dummy_AWARE_Data_Applications_Crashes.CONTENT_URI,
                            weather_id);
                    getContext().getContentResolver().notifyChange(new_uri,
                            null);
                    return new_uri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (URIMatcher.match(uri)) {
            case DUMMYAWARE:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(databaseMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG)
                Log.e(Aware.TAG, e.getMessage());
            return null;
        }
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case DUMMYAWARE:
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}