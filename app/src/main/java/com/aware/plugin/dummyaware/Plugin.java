package com.aware.plugin.dummyaware;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.net.Uri;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.utils.Aware_Plugin;
import com.aware.plugin.dummyaware.Provider.Dummy_AWARE_Data;

public class Plugin extends Aware_Plugin {

    public static final String ACTION_AWARE_PLUGIN_DUMMYAWARE = "ACTION_AWARE_PLUGIN_DUMMYAWARE";

    public static final String EXTRA_DATA = "data";

    //context
    private static ContextProducer sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "AWARE::"+getResources().getString(R.string.app_name);

        //Activate programmatically any sensors/plugins you need here
        //e.g., Aware.setSetting(this, Aware_Preferences.STATUS_ACCELEROMETER,true);
        //NOTE: if using plugin with dashboard, you can specify the sensors you'll use there.

        //Any active plugin/sensor shares its overall context using broadcasts
        sContext = new ContextProducer() {
            @Override
            public void onContext() {
                //Broadcast your context here
                ContentValues data = new ContentValues();
                data.put(Dummy_AWARE_Data.TIMESTAMP, System.currentTimeMillis());
                data.put(Dummy_AWARE_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
                //send to AWARE
                Intent context_unlock = new Intent();
                context_unlock.setAction(ACTION_AWARE_PLUGIN_DUMMYAWARE);
                context_unlock.putExtra(EXTRA_DATA,data);
                sendBroadcast(context_unlock);
                getContentResolver().insert(Dummy_AWARE_Data.CONTENT_URI, data);
            }
        };
        CONTEXT_PRODUCER = sContext;

        createSimulatedData();





        //Add permissions you need (Support for Android M) e.g.,
        //REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //To sync data to the server, you'll need to set this variables from your ContentProvider
        DATABASE_TABLES = Provider.DATABASE_TABLES;
        TABLES_FIELDS = Provider.TABLES_FIELDS;

        CONTEXT_URIS = new Uri[]{ Provider.Dummy_AWARE_Data.CONTENT_URI };

        Aware.startPlugin(this, "com.aware.plugin.dummyaware");





    }

    public void createSimulatedData() {

        ContentValues data = new ContentValues();
        data.put(Dummy_AWARE_Data.TIMESTAMP, System.currentTimeMillis());
        data.put(Dummy_AWARE_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        //send to AWARE
        Intent context_unlock = new Intent();
        context_unlock.setAction(ACTION_AWARE_PLUGIN_DUMMYAWARE);
        context_unlock.putExtra(EXTRA_DATA,data);
        sendBroadcast(context_unlock);
        getContentResolver().insert(Dummy_AWARE_Data.CONTENT_URI, data);

    }



    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Deactivate any sensors/plugins you activated here

        //Stop plugin
        Aware.stopPlugin(this, "com.aware.plugin.dummyaware");
    }



}
