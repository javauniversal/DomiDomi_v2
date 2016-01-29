package com.appgestor.domidomi.Activities;

import android.content.Intent;
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
import android.widget.Switch;
import android.widget.Toast;

import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.Accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActMyPerfil extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner_dir;
    private String Calle_Carrera;
    private String[] dir1Zona_parant;
    private String zona_dir;
    private Spinner spinner_zona;
    private LinearLayout zonaLayout;
    private Spinner spinner_ciudades;
    private String[] dir1_parant;
    private List<Ciudades> ciudades;
    private String selecte_ciudad;
    private EditText editNombreCliente;
    private EditText editCelular;
    private EditText telefono;
    private EditText txt_dir_1;
    private EditText txt_dir_2;
    private EditText txt_dir_3;
    private Button btnGuardar;
    private DBHelper mydb;
    private Switch switch1;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudades);
        zonaLayout = (LinearLayout) findViewById(R.id.zonaLayout);
        spinner_zona = (Spinner) findViewById(R.id.spinner_zona);
        spinner_dir = (Spinner) findViewById(R.id.spinner_dir);

        btnGuardar = (Button) findViewById(R.id.buttonG);
        btnGuardar.setOnClickListener(this);

        editNombreCliente = (EditText) findViewById(R.id.editNombreCliente);
        editCelular = (EditText) findViewById(R.id.editCelular);
        telefono = (EditText) findViewById(R.id.editTelefono);
        txt_dir_1 = (EditText) findViewById(R.id.txt_dir_1);
        txt_dir_2 = (EditText) findViewById(R.id.txt_dir_2);
        txt_dir_3 = (EditText) findViewById(R.id.txt_dir_3);

        switch1 = (Switch) findViewById(R.id.switch1);

        loadAdress();

        loadCiudades();

        setDataBase();

    }

    private void setDataBase() {

        cliente = mydb.getUsuarioAll();
        boolean indiswi = false;
        if(cliente != null){
            editNombreCliente.setText(cliente.getNombre());
            editCelular.setText(cliente.getCelular());
            telefono.setText(cliente.getTelefono());
            txt_dir_1.setText(cliente.getDir_1());
            txt_dir_2.setText(cliente.getDir_2());
            txt_dir_3.setText(cliente.getDir_3());

            List<String> strListAd = new ArrayList<>(Arrays.asList(dir1_parant));
            spinner_dir.setSelection(strListAd.indexOf(cliente.getCalle_carrera()));

            selectValue(ciudades, cliente.getCiudad(), spinner_ciudades);

            if(cliente.getIncluir() == 1)
                indiswi = true;

            switch1.setChecked(indiswi);

        }
    }

    private void selectValue(List<Ciudades> ciudad, String value, Spinner spinner) {
        for (int i = 0; i < ciudad.size(); i++) {
            if (ciudad.get(i).getNombreCiudad().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }

    }

    private void loadAdress() {

        dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                "Diagonal", "Manzana", "Transversal", "Via"};

        ArrayAdapter<String> prec1 = new ArrayAdapter<String>(ActMyPerfil.this, R.layout.textview_spinner, dir1_parant);
        spinner_dir.setAdapter(prec1);
        spinner_dir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calle_Carrera = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadCiudades() {

        ciudades = new ArrayList<>();
        ciudades.add(new Ciudades(1, "Medellin", 1));
        ciudades.add(new Ciudades(2, "Bogota", 1));

        ArrayAdapter<Ciudades> prec3 = new ArrayAdapter<Ciudades>(ActMyPerfil.this, android.R.layout.simple_spinner_dropdown_item, ciudades);
        spinner_ciudades.setAdapter(prec3);
        spinner_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecte_ciudad = parent.getItemAtPosition(position).toString();

                if (selecte_ciudad.equals("Medellin")){
                    zonaLayout.setVisibility(View.VISIBLE);
                    loadLlenarZona();
                }else {
                    zonaLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });
    }

    private void loadLlenarZona() {

        dir1Zona_parant = new String[]{"Envigado", "Sabaneta", "Itagui", "La estrella", "Medellin", "Bello"};
        ArrayAdapter<String> prec1 = new ArrayAdapter<>(ActMyPerfil.this, R.layout.textview_spinner, dir1Zona_parant);
        spinner_zona.setAdapter(prec1);
        List<String> strListZona = new ArrayList<>(Arrays.asList(dir1Zona_parant));
        spinner_zona.setSelection(strListZona.indexOf(cliente.getZona()));
        spinner_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zona_dir = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonG:
                if (isValidNumber(editNombreCliente.getText().toString())) {
                    editNombreCliente.setError("Requerido");
                    editNombreCliente.requestFocus();
                } else if (isValidNumber(txt_dir_1.getText().toString())){
                    txt_dir_1.setError("Requerido");
                    txt_dir_1.requestFocus();
                } else if (isValidNumber(txt_dir_2.getText().toString())){
                    txt_dir_2.setError("Requerido");
                    txt_dir_2.requestFocus();
                } else if (isValidNumber(txt_dir_3.getText().toString())){
                    txt_dir_3.setError("Requerido");
                    txt_dir_3.requestFocus();
                } else {
                    if (isValidNumber(editCelular.getText().toString())){
                        if(isValidNumber(telefono.getText().toString())){
                            editCelular.setError("Requerido");
                            editCelular.requestFocus();
                        } else {
                            //Guardar
                            guardarPerfil();
                        }
                    } else {
                        //Guardar
                        guardarPerfil();
                    }
                }

                break;

        }
    }

    private void guardarPerfil(){
        Cliente cl = new Cliente();

        int swit;
        if(switch1.isChecked())
            swit = 1;
        else
            swit = 0;

        cl.setNombre(editNombreCliente.getText().toString());
        cl.setCelular(editCelular.getText().toString());
        cl.setTelefono(telefono.getText().toString());
        cl.setCalle_carrera(Calle_Carrera);
        cl.setDir_1(txt_dir_1.getText().toString());
        cl.setDir_2(txt_dir_2.getText().toString());
        cl.setDir_3(txt_dir_3.getText().toString());
        cl.setCiudad(selecte_ciudad);
        cl.setZona(zona_dir);
        cl.setIncluir(swit);

        mydb.deleteUsuario();

        if (mydb.insertUsuario(cl)){
            startActivity(new Intent(this, Accounts.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else {
            Toast.makeText(this, "Problemas al Guardar el perfil.", Toast.LENGTH_LONG).show();
        }
    }
}
