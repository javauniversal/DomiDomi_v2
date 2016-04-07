package com.appgestor.domidomi.Activities;

import android.content.Intent;

import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.ConfigSplash;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.cnst.Flags;
import com.appgestor.domidomi.dark.ActivityMain;
import com.daimajia.androidanimations.library.Techniques;

import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;


//extends AwesomeSplash!
public class YourActivity extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!

    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo_transparente); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.DropOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("Domi Domi");
        configSplash.setTitleTextColor(R.color.actionBarColorText);
        configSplash.setTitleTextSize(19f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.Tada);
        //configSplash.setTitleFont("fonts/speed.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {

        DBHelper mydb = new DBHelper(this);
        mydb.insertIntro("Inicio_sesion");

        isInternetPresent = cd.isConnected();

        if (isInternetPresent) {

            MyService gps = new MyService(this);
            if(gps.getLatitude() == 0.0){

                startActivity(new Intent(this, ActUbicacion.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {

                setLatitudStatic(gps.getLatitude());
                setLongitudStatic(gps.getLongitude());

                startActivity(new Intent(this, ActivityMain.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }


        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            startActivity(new Intent(this, DetailsActivity.class).putExtra("STATE", "ERROR"));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }



    }
}
