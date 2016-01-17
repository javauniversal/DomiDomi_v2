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
import android.widget.Spinner;

import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Paises;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.dark.Accounts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;

public class ActUbicacion extends AppCompatActivity implements View.OnClickListener {

    private Spinner dir_1;
    private Spinner dir_2;
    private Spinner dir_3;
    private List<Paises> paises;
    private List<Ciudades> ciudades;
    private Paises paisesEvent;
    private List<Ciudades> ciudadesList;
    private String[] dir1_parant;
    private Button myUbication;
    private Button myAdress;
    private EditText txt_dir_1;
    private EditText txt_dir_2;
    private EditText txt_dir_3;
    private String Calle_Carrera;
    private String selecte_ciudad;
    private Double latitud;
    private Double longitud;

    Geocoder geocoder;
    List<Address> returnedaddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ubicacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dir_1 = (Spinner) findViewById(R.id.spinner_dir);
        dir_2 = (Spinner) findViewById(R.id.spinner_pais);
        dir_3 = (Spinner) findViewById(R.id.spinner_ciudad);

        txt_dir_1 = (EditText) findViewById(R.id.txt_dir_1);
        txt_dir_2 = (EditText) findViewById(R.id.txt_dir_2);
        txt_dir_3 = (EditText) findViewById(R.id.txt_dir_3);


        myUbication = (Button) findViewById(R.id.button_ubi_actual);
        myUbication.setOnClickListener(this);
        myAdress = (Button) findViewById(R.id.button_buscar_establesimiento);
        myAdress.setOnClickListener(this);

        loadPaises();

        loadAdress();

        geocoder = new Geocoder(this, Locale.ENGLISH);

    }

    private void loadAdress() {
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                        "Diagonal", "Manzana", "Transversal", "Via"};

                return null;
            }

            protected void onPreExecute() {
            }

            @Override
            public void onProgressUpdate(Long... value) {

            }

            @Override
            protected void onPostExecute(Long result){
                ArrayAdapter<String> prec1 = new ArrayAdapter<String>(ActUbicacion.this, R.layout.textview_spinner, dir1_parant);
                dir_1.setAdapter(prec1);
                dir_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Calle_Carrera = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
            }

        }.execute();
    }

    private void loadCiudades() {
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {

                ciudadesList = new ArrayList<>();

                for (int i = 0; i < ciudades.size(); i++) {
                    if (ciudades.get(i).getIdPais() == paisesEvent.getIdPais()){
                        ciudadesList.add(new Ciudades(i, ciudades.get(i).getNombreCiudad(), paisesEvent.getIdPais()));
                    }

                }

                return null;
            }

            protected void onPreExecute() {
                //multiColumnList.setVisibility(View.GONE);
            }

            @Override
            public void onProgressUpdate(Long... value) {

            }

            @Override
            protected void onPostExecute(Long result){

                ArrayAdapter<Ciudades> prec3 = new ArrayAdapter<Ciudades>(ActUbicacion.this, android.R.layout.simple_spinner_dropdown_item, ciudadesList);
                dir_3.setAdapter(prec3);
                dir_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selecte_ciudad = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

            }

        }.execute();
    }

    private void loadPaises() {
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {

                paises = new ArrayList<>();
                paises.add(new Paises(1, "Colombia"));
                paises.add(new Paises(2, "Venezuela"));

                ciudades = new ArrayList<>();
                ciudades.add(new Ciudades(1, "Medellin", 1));
                ciudades.add(new Ciudades(2, "Bogota", 1));
                ciudades.add(new Ciudades(3, "Carracas", 2));
                ciudades.add(new Ciudades(4, "Itagui", 1));
                ciudades.add(new Ciudades(5, "Envigado", 1));
                ciudades.add(new Ciudades(6, "Sabaneta", 1));

                return null;
            }

            protected void onPreExecute() {}

            @Override
            public void onProgressUpdate(Long... value) {}

            @Override
            protected void onPostExecute(Long result){

                ArrayAdapter<Paises> prec2 = new ArrayAdapter<Paises>(ActUbicacion.this, android.R.layout.simple_spinner_dropdown_item, paises);
                dir_2.setAdapter(prec2);
                dir_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        paisesEvent = paises.get(position);

                        loadCiudades();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

            }
        }.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_ubi_actual:

                MyService gps = new MyService(this);

                if (gps.canGetLocation()){

                    setLatitudStatic(gps.getLatitude());
                    setLongitudStatic(gps.getLongitude());

                    startActivity(new Intent(this, Accounts.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }else {
                    gps.showSettingsAlert();
                }

                break;

            case R.id.button_buscar_establesimiento:

                if (isValidNumber(txt_dir_1.getText().toString())){
                    txt_dir_1.setError("Campo requerido");
                    txt_dir_1.requestFocus();
                    return;
                } else if (isValidNumber(txt_dir_2.getText().toString())){
                    txt_dir_2.setError("Campo requerido");
                    txt_dir_2.requestFocus();
                    return;
                } else if (isValidNumber(txt_dir_3.getText().toString())) {
                    txt_dir_3.setError("Campo requerido");
                    txt_dir_3.requestFocus();
                    return;
                }else {

                    String adressObten = Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim() +" "+selecte_ciudad;
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

                    startActivity(new Intent(ActUbicacion.this, Accounts.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                    //Toast.makeText(ActUbicacion.this, String.valueOf(latitud)+" , "+String.valueOf(longitud), Toast.LENGTH_LONG).show();
                }

            }

        }.execute();
    }


    private boolean isValidNumber(String number){
        return number == null || number.length() == 0;
    }
}
