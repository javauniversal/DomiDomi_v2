package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.dark.Accounts;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;

public class ActUbicacion extends AppCompatActivity implements View.OnClickListener{

    private Button buttonU;
    private Button buttonD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ubicacion);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/speed.ttf");
        TextView title = (TextView) findViewById(R.id.txtTitle);

        TextView buttonU = (TextView) findViewById(R.id.buttonU);
        buttonU.setOnClickListener(this);
        TextView buttonD = (TextView) findViewById(R.id.buttonD);
        buttonD.setOnClickListener(this);


        final TextView descripcion = (TextView) findViewById(R.id.textView7);

        title.setTypeface(custom_font);
        title.setTextSize(13);

        title.setText("Domi Domi");

        ImageView imagen = (ImageView) findViewById(R.id.imageView9);

        YoYo.with(Techniques.Tada).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                descripcion.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).duration(3000).playOn(imagen);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonU:
                MyService gps = new MyService(this);

                if (gps.canGetLocation()){

                    setLatitudStatic(gps.getLatitude());
                    setLongitudStatic(gps.getLongitude());

                    startActivity(new Intent(this, Accounts.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }else {
                    cargarDialogConfiguracion();
                }
                break;
            case R.id.buttonD:

                startActivity(new Intent(this, ActDireccionEscrita.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
        }
    }

    private void cargarDialogConfiguracion() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS Configuración");

        // Setting Dialog Message
        alertDialog.setMessage("GPS no está habilitado. ¿Quieres ir al menú de ajustes?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
        MyService gps = new MyService(this);
        if (gps.canGetLocation()){

            setLatitudStatic(gps.getLatitude());
            setLongitudStatic(gps.getLongitude());

            startActivity(new Intent(this, Accounts.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
}
