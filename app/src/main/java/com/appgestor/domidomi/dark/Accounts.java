package com.appgestor.domidomi.dark;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.appgestor.domidomi.Activities.ActEstadoPedido;
import com.appgestor.domidomi.Activities.MaterialNavigationDrawerCosmm;
import com.appgestor.domidomi.LocationFragment12;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.mockedFragments.FragListEmpresas;
import com.appgestor.domidomi.mockedFragments.FragmentCarrito;
import com.appgestor.domidomi.mockedFragments.FragmentPeril;

import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;


public class Accounts extends MaterialNavigationDrawerCosmm {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void init(Bundle savedInstanceState) {

        // create sections
        setDrawerHeaderImage(R.drawable.side_nav_bar);
        setUsername("Delivery Domicilios");
        setUserEmail("La manera más facíl de pedir un domicilio");
        setFirstAccountPhoto(getResources().getDrawable(R.drawable.logo_transparente));

        // create sections
        this.addSection(newSection("Establecimientos", R.drawable.ic_restaurant_menu_black_24dp, new FragListEmpresas()));
        this.addSection(newSection("Mis Pedidos", R.drawable.ic_playlist_add_check_black_24dp,new Intent(this, ActEstadoPedido.class)));
        this.addSection(newSection("Mi Ubicación", R.drawable.ic_pin_drop_black_48dp, new LocationFragment12()));
        this.addSection(newSection("Carrito", R.drawable.ic_local_grocery_store_black_24dp, new FragmentCarrito()));
        //this.addSection(newSection("Perfil", R.drawable.ic_face_black_24dp, new FragmentPeril()));

        // create bottom section
        this.addBottomSection(newSection("Perfil", R.drawable.ic_face_black_24dp, new FragmentPeril()));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Pulse otra vez para salir", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onResume(){
        super.onResume();
        MyService gps = new MyService(this);
        if (gps.canGetLocation()){

            setLatitudStatic(gps.getLatitude());
            setLongitudStatic(gps.getLongitude());

        }
    }

}
