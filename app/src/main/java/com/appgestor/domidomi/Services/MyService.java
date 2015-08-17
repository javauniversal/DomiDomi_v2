package com.appgestor.domidomi.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;

public class MyService extends Service implements LocationListener {

    private Context ctx;
    double latitud;
    double longitud;

    public MyService() {
        super();
        this.ctx = getApplicationContext();
    }

    public MyService(Context c) {
        super();
        this.ctx = c;
        getLocation();
    }

    public void setView(View txt1, View txt2){
        EditText _longitud = (EditText)txt1;
        _longitud.setText(longitud+"");
        EditText _latitud = (EditText)txt2;
        _latitud.setText(latitud+"");
    }

    public void getLocation(){
        try {
            LocationManager locationManager = (LocationManager)ctx.getSystemService(LOCATION_SERVICE);
            boolean gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (gpsActivo){
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,1000*60,10,this);
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                latitud = location.getLongitude();
                longitud = location.getLatitude();

            }else{
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        }catch (Exception e){}

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
