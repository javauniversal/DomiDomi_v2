package com.appgestor.domidomi.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Paises;
import com.appgestor.domidomi.R;

import java.util.ArrayList;
import java.util.List;

public class ActUbicacion extends AppCompatActivity {

    private Spinner dir_1;
    private Spinner dir_2;
    private Spinner dir_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ubicacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final List<Paises> paises = new ArrayList<>();

        paises.add(new Paises(1, "Colombia"));
        paises.add(new Paises(2, "Venezuela"));


        final List<Ciudades> ciudades = new ArrayList<>();
        ciudades.add(new Ciudades(1, "Medellin", 1));
        ciudades.add(new Ciudades(2, "Bogota", 1));
        ciudades.add(new Ciudades(3, "Carracas", 2));


        dir_1 = (Spinner) findViewById(R.id.spinner_dir);
        dir_2 = (Spinner) findViewById(R.id.spinner_pais);
        dir_3 = (Spinner) findViewById(R.id.spinner_ciudad);


        ArrayAdapter<Paises> prec2 = new ArrayAdapter<Paises>(this, R.layout.textview_spinner, paises);
        dir_2.setAdapter(prec2);
        dir_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Paises paises1 = paises.get(position);

                //List<Ciudades> contacts = stream(ciudades).where(c -> c.getNombreCiudad().equals("")).toList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ArrayAdapter<Paises> prec3 = new ArrayAdapter<Paises>(this, R.layout.textview_spinner, paises);
        dir_2.setAdapter(prec2);
        dir_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Paises paises1 = paises.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });




        String[] dir1_parant = { "Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                                "Diagonal", "Manzana", "Transversal", "Via"};

        ArrayAdapter<String> prec1 = new ArrayAdapter<String>(this, R.layout.textview_spinner, dir1_parant);

        dir_1.setAdapter(prec1);

    }

}
