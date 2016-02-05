package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.Accounts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.appgestor.domidomi.Entities.UbicacionPreferen.setCiudadStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setZonaStatic;

public class ActDireccionEscrita extends AppCompatActivity implements View.OnClickListener {

    private List<Ciudades> ciudades;
    private String selecte_ciudad;
    private Spinner dir_3;
    private Spinner dir_1;
    private Spinner spinner_zona;
    private String[] dir1_parant;
    private String[] dir1Zona_parant;
    private String Calle_Carrera;
    private String zona_dir;
    private LinearLayout zonaLayout;
    private Button button_buscar_establesimiento;
    private EditText txt_dir_1;
    private EditText txt_dir_2;
    private EditText txt_dir_3;
    List<Address> returnedaddresses;
    Geocoder geocoder;
    private Double latitud;
    private Double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_direccion_escrita);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dir_3 = (Spinner) findViewById(R.id.spinner_ciudades);
        dir_1 = (Spinner) findViewById(R.id.spinner_dir);
        spinner_zona = (Spinner) findViewById(R.id.spinner_zona);

        button_buscar_establesimiento = (Button) findViewById(R.id.button_buscar_establesimiento);
        button_buscar_establesimiento.setOnClickListener(this);

        zonaLayout = (LinearLayout) findViewById(R.id.zonaLayout);

        txt_dir_1 = (EditText) findViewById(R.id.txt_dir_1);
        txt_dir_2 = (EditText) findViewById(R.id.txt_dir_2);
        txt_dir_3 = (EditText) findViewById(R.id.txt_dir_3);

        loadCiudades();
        loadAdress();

        geocoder = new Geocoder(this, Locale.ENGLISH);
    }

    private void loadCiudades() {
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {

                ciudades = new ArrayList<>();
                ciudades.add(new Ciudades(1, "Medellín", 1));
                ciudades.add(new Ciudades(2, "Bogotá", 1));

                return null;
            }

            protected void onPreExecute() {}

            @Override
            public void onProgressUpdate(Long... value) {}

            @Override
            protected void onPostExecute(Long result){

                ArrayAdapter<Ciudades> prec3 = new ArrayAdapter<Ciudades>(ActDireccionEscrita.this, android.R.layout.simple_spinner_dropdown_item, ciudades);
                dir_3.setAdapter(prec3);
                dir_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selecte_ciudad = parent.getItemAtPosition(position).toString();

                        if (selecte_ciudad.equals("Medellín")){
                            zonaLayout.setVisibility(View.VISIBLE);
                            loadLlenarZona();
                        }else {
                            zonaLayout.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

            }
        }.execute();
    }

    private void loadLlenarZona() {

        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                dir1Zona_parant = new String[]{"Envigado", "Sabaneta", "Itaguí", "La Estrella", "Medellín", "Bello"};

                return null;
            }

            protected void onPreExecute() { }

            @Override
            public void onProgressUpdate(Long... value) { }

            @Override
            protected void onPostExecute(Long result){
                ArrayAdapter<String> prec1 = new ArrayAdapter<String>(ActDireccionEscrita.this, R.layout.textview_spinner, dir1Zona_parant);
                spinner_zona.setAdapter(prec1);
                spinner_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        zona_dir = parent.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }

        }.execute();

    }

    private void loadAdress() {
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                        "Diagonal", "Manzana", "Transversal", "Vía"};

                return null;
            }

            protected void onPreExecute() { }

            @Override
            public void onProgressUpdate(Long... value) { }

            @Override
            protected void onPostExecute(Long result){
                ArrayAdapter<String> prec1 = new ArrayAdapter<String>(ActDireccionEscrita.this, R.layout.textview_spinner, dir1_parant);
                dir_1.setAdapter(prec1);
                dir_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Calle_Carrera = parent.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }

        }.execute();
    }

    private boolean isValidNumber(String number){
        return number == null || number.length() == 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_buscar_establesimiento:
                if (isValidNumber(txt_dir_1.getText().toString())){
                    txt_dir_1.setError("Requerido");
                    txt_dir_1.requestFocus();
                    return;
                } else if (isValidNumber(txt_dir_2.getText().toString())){
                    txt_dir_2.setError("Requerido");
                    txt_dir_2.requestFocus();
                    return;
                } else if (isValidNumber(txt_dir_3.getText().toString())) {
                    txt_dir_3.setError("Requerido");
                    txt_dir_3.requestFocus();
                    return;
                } else {

                    String adressObten;

                    if (zonaLayout.getVisibility() == View.VISIBLE){
                        adressObten = Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim() +" "+zona_dir+" "+selecte_ciudad;
                    } else {
                        adressObten = Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim() +" "+selecte_ciudad;
                    }

                    loadAdressLatLon(adressObten);

                }

                break;
        }
    }

    public void loadAdressLatLon(final String adressObten){
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                try {

                    returnedaddresses = geocoder.getFromLocationName(adressObten, 1);

                    latitud = returnedaddresses.get(0).getLatitude();
                    longitud = returnedaddresses.get(0).getLongitude();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPreExecute() {
            }

            @Override
            public void onProgressUpdate(Long... value) {

            }

            @Override
            protected void onPostExecute(Long result){

                if(returnedaddresses != null){

                    setLatitudStatic(latitud);
                    setLongitudStatic(longitud);

                    setCiudadStatic(selecte_ciudad);

                    if (zonaLayout.getVisibility() == View.VISIBLE ){
                        setZonaStatic(zona_dir);
                    }

                    startActivity(new Intent(ActDireccionEscrita.this, Accounts.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }

            }

        }.execute();
    }

}
