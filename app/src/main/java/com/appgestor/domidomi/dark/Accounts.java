package com.appgestor.domidomi.dark;

import android.content.Intent;
import android.os.Bundle;


import com.appgestor.domidomi.R;
import com.appgestor.domidomi.mockedActivity.Settings;
import com.appgestor.domidomi.mockedFragments.FragListCompania;
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
        this.addSection(newSection("Lista de restaurante", new FragListCompania()));
        this.addSection(newSection("Perfil", new FragmentIndex()));
        this.addSection(newSection("Mi ubicación", new FragmentIndex()));
        this.addSection(newSection("Estado de los pedidos", new FragmentIndex()));

        //this.addSection(newSection("Section 3",R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        //this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        this.addBottomSection(newSection("Botón Salir",R.mipmap.ic_settings_black_24dp,new Intent(this,Settings.class)));
    }


}
