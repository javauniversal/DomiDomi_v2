package com.appgestor.domidomi.dark;

import android.content.Intent;
import android.os.Bundle;

import com.appgestor.domidomi.ActMaps;
import com.appgestor.domidomi.Activities.ActEstadoPedido;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.mockedActivity.Settings;
import com.appgestor.domidomi.mockedFragments.FragListEmpresas;
import com.appgestor.domidomi.mockedFragments.FragmentCarrito;
import com.appgestor.domidomi.mockedFragments.FragmentFavoritos;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


public class Accounts extends MaterialNavigationDrawer  {

    @Override
    public void init(Bundle savedInstanceState) {

        // create sections
        setDrawerHeaderImage(R.drawable.side_nav_bar);
        setUsername("Domi Domi");
        setUserEmail("La manera más facíl de pedir un domicilio");
        setFirstAccountPhoto(getResources().getDrawable(R.drawable.logo_transparente));

        // create sections
        this.addSection(newSection("Establecimientos", R.drawable.ic_restaurant_menu_black_24dp, new FragListEmpresas()));
        this.addSection(newSection("Mis Pedidos", R.drawable.ic_view_compact_black_48dp,new Intent(this, ActEstadoPedido.class)));
        this.addSection(newSection("Favoritos", R.drawable.ic_favorite_black_24dp, new FragmentFavoritos()));
        this.addSection(newSection("Mi Ubicación", R.drawable.ic_pin_drop_black_48dp, new Intent(this, ActMaps.class)));
        this.addSection(newSection("Carrito", R.drawable.ic_local_grocery_store_black_24dp, new FragmentCarrito()));

        // create bottom section
        this.addBottomSection(newSection("Salir",R.drawable.ic_exit_to_app_black_24dp,new Intent(this,Settings.class)));

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, Settings.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        super.onBackPressed();  // optional depending on your needs

    }


}
