package com.appgestor.domidomi.dark;

import android.content.Intent;
import android.os.Bundle;

import com.appgestor.domidomi.ActMaps;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.mockedActivity.Settings;
import com.appgestor.domidomi.mockedFragments.FragListCompania;
import com.appgestor.domidomi.mockedFragments.FragmentEstadoPedido;
import com.appgestor.domidomi.mockedFragments.FragmentIndex;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


public class Accounts extends MaterialNavigationDrawer  {

    @Override
    public void init(Bundle savedInstanceState) {
        // create sections
        setDrawerHeaderImage(R.drawable.mat2);
        setUsername("Domi Domi");
        setUserEmail("La manera mas facíl de pedir un domicilio");
        //setFirstAccountPhoto(getResources().getDrawable(R.drawable.photo));

        // create sections
        this.addSection(newSection("Rrestaurante", R.drawable.ic_restaurant_menu_black_24dp, new FragListCompania()));
        this.addSection(newSection("Mi ubicación", R.drawable.ic_pin_drop_black_48dp, new Intent(this, ActMaps.class)));
        this.addSection(newSection("Estado del pedido", R.drawable.ic_view_compact_black_48dp, new FragmentEstadoPedido()));
        this.addSection(newSection("Favoritos", R.drawable.ic_favorite_black_24dp, new FragmentIndex()));

        //this.addSection(newSection("Section 3",R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        //this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        this.addBottomSection(newSection("Botón Salir",R.drawable.ic_exit_to_app_black_24dp,new Intent(this,Settings.class)));

    }


}
