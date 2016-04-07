package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.R;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActCreatePerfil extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //private Button fecha_cumple;
    //private EditText edit_cumple;
    private EditText edit_nombres;
    private EditText edit_cell;
    private EditText edit_phone;
    private EditText edit_a;
    private EditText edit_b;
    private EditText edit_c;
    private EditText editBarrioCliente;
    private EditText editDirReferencia;

    private EditText edit_ofi;
    private String fecha;
    private Spinner spinner_dir;
    private Spinner spinner_oficina;
    private Spinner spinner_ciudades;
    private Spinner spinner_zona;
    private String[] dir1_parant;
    private String[] ofic_parant;
    private String[] dir1Zona_parant;
    private String Calle_Carrera;
    private String oficina_spinn;
    private String selecte_ciudad;
    private List<Ciudades> ciudades;
    private RelativeLayout zonaLayout;
    private String zona_dir;
    private FloatingActionButton fabGuardar;
    private DBHelper mydb;
    private LinearLayout numeroLayout;
    private Cliente thumbs = new Cliente();
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);

        zonaLayout = (RelativeLayout) findViewById(R.id.relativeLayout_zona);
        numeroLayout = (LinearLayout) findViewById(R.id.numeroLayout);

        //fecha_cumple = (Button) findViewById(R.id.button_cumple);
        //edit_cumple = (EditText) findViewById(R.id.edit_cumple);
        edit_ofi = (EditText) findViewById(R.id.edit_ofi);

        spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudad);
        spinner_dir = (Spinner) findViewById(R.id.spinner_dir);
        spinner_oficina = (Spinner) findViewById(R.id.spinner_ofi);
        spinner_zona = (Spinner) findViewById(R.id.spinner_zona);

        edit_nombres = (EditText) findViewById(R.id.edit_nombres);
        edit_nombres.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edit_cell = (EditText) findViewById(R.id.edit_cell);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_a = (EditText) findViewById(R.id.edit_a);
        edit_b = (EditText) findViewById(R.id.edit_b);
        edit_c = (EditText) findViewById(R.id.edit_c);
        editBarrioCliente = (EditText) findViewById(R.id.editBarrioCliente);
        editDirReferencia = (EditText) findViewById(R.id.editDirReferencia);

        fabGuardar = (FloatingActionButton) findViewById(R.id.fab_guardar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*fecha_cumple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        ActCreatePerfil.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });*/

        loadAdress();

        loadOficina();

        loadCiudades();

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            thumbs = (Cliente)bundle.getSerializable("value");
            loadLlenarEditar(thumbs);
        }

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCampos()){
                    guardarPerfil();
                }
            }
        });
    }

    private void loadLlenarEditar(Cliente thumbs) {
        edit_nombres.setText(thumbs.getNombre());
        edit_cell.setText(thumbs.getCelular());
        edit_phone.setText(thumbs.getTelefono());

        List<String> strListAd = new ArrayList<>(Arrays.asList(dir1_parant));
        spinner_dir.setSelection(strListAd.indexOf(thumbs.getCalle_carrera()));

        edit_a.setText(thumbs.getDir_1());
        edit_b.setText(thumbs.getDir_2());
        edit_c.setText(thumbs.getDir_3());

        List<String> strListAdofic = new ArrayList<>(Arrays.asList(ofic_parant));
        spinner_oficina.setSelection(strListAdofic.indexOf(thumbs.getOficina()));

        edit_ofi.setText(thumbs.getNum_oficina() + "");

        selectValue(ciudades, thumbs.getCiudad(), spinner_ciudades);

        editBarrioCliente.setText(thumbs.getBarrio());
        editDirReferencia.setText(thumbs.getDirReferencia());

    }

    private void selectValue(List<Ciudades> ciudad, String value, Spinner spinner) {
        for (int i = 0; i < ciudad.size(); i++) {
            if (ciudad.get(i).getNombreCiudad().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public boolean validarCampos() {

        boolean indicadorValidate = false;

        if (isValidNumber(edit_nombres.getText().toString().trim())){
            edit_nombres.setFocusable(true);
            edit_nombres.setFocusableInTouchMode(true);
            edit_nombres.requestFocus();
            edit_nombres.setText("");
            edit_nombres.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if(isValidNumberCell(edit_cell.getText().toString().trim(), edit_phone.getText().toString().trim())) {
            edit_cell.setFocusable(true);
            edit_cell.setFocusableInTouchMode(true);
            edit_cell.requestFocus();
            edit_cell.setText("");
            edit_cell.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_a.getText().toString().trim())) {
            edit_a.setFocusable(true);
            edit_a.setFocusableInTouchMode(true);
            edit_a.requestFocus();
            edit_a.setText("");
            edit_a.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_b.getText().toString().trim())) {
            edit_b.setFocusable(true);
            edit_b.setFocusableInTouchMode(true);
            edit_b.requestFocus();
            edit_b.setText("");
            edit_b.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_c.getText().toString().trim())) {
            edit_c.setFocusable(true);
            edit_c.setFocusableInTouchMode(true);
            edit_c.requestFocus();
            edit_c.setText("");
            edit_c.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumberOficina(edit_ofi.getText().toString().trim())) {
            edit_ofi.setFocusable(true);
            edit_ofi.setFocusableInTouchMode(true);
            edit_ofi.requestFocus();
            edit_ofi.setText("");
            edit_ofi.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(editBarrioCliente.getText().toString().trim())){
            editBarrioCliente.setFocusable(true);
            editBarrioCliente.setFocusableInTouchMode(true);
            editBarrioCliente.requestFocus();
            editBarrioCliente.setText("");
            editBarrioCliente.setError("Este campo es obligatorio");
            indicadorValidate = true;
        }

        return indicadorValidate;
    }

    private boolean isValidNumberOficina(String trim) {

        if (numeroLayout.getVisibility() == View.VISIBLE){
            return isValidNumber(trim);
        }
        return false;

    }

    private boolean isValidNumberCell(String dataCell, String dataPhone){
        if (dataCell == null || dataCell.length() == 0){
            if (dataPhone == null || dataPhone.length() == 0){
                return true;
            }
        }

        return false;
    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

    private void loadCiudades() {
        ciudades = new ArrayList<>();
        ciudades.add(new Ciudades(1, "Barranquilla", 1));
        ciudades.add(new Ciudades(2, "Bogotá", 1));
        ciudades.add(new Ciudades(3, "Bucaramanga", 1));
        ciudades.add(new Ciudades(4, "Cali", 1));
        ciudades.add(new Ciudades(5, "Cartagena", 1));
        ciudades.add(new Ciudades(6, "Cúcuta", 1));
        ciudades.add(new Ciudades(7, "Manizales", 1));
        ciudades.add(new Ciudades(8, "Medellín", 1));
        ciudades.add(new Ciudades(9, "Montería", 1));
        ciudades.add(new Ciudades(10, "Neiva", 1));
        ciudades.add(new Ciudades(11, "Pasto", 1));
        ciudades.add(new Ciudades(12, "Pereira", 1));
        ciudades.add(new Ciudades(13, "Santa Marta", 1));
        ciudades.add(new Ciudades(14, "Valledupar", 1));

        ArrayAdapter<Ciudades> prec3 = new ArrayAdapter<>(this, R.layout.textview_spinner, ciudades);
        spinner_ciudades.setAdapter(prec3);
        spinner_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecte_ciudad = parent.getItemAtPosition(position).toString();

                if (selecte_ciudad.equals("Medellín")) {
                    zonaLayout.setVisibility(View.VISIBLE);
                    loadLlenarZona();
                } else {
                    zonaLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    private void loadLlenarZona() {

        dir1Zona_parant = new String[]{"Bello", "Caldas", "Envigado",  "Itaguí", "La Estrella", "Medellín", "Sabaneta"};
        ArrayAdapter<String> prec1 = new ArrayAdapter<>(this, R.layout.textview_spinner, dir1Zona_parant);
        spinner_zona.setAdapter(prec1);
        List<String> strListZona = new ArrayList<>(Arrays.asList(dir1Zona_parant));
        spinner_zona.setSelection(strListZona.indexOf(thumbs.getZona()));
        spinner_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zona_dir = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void loadOficina() {
        ofic_parant = new String[]{"Apartamento", "Bodega", "Casa", "Local", "Ninguno", "Oficina"};

        ArrayAdapter<String> oficina = new ArrayAdapter<>(this, R.layout.textview_spinner, ofic_parant);
        spinner_oficina.setAdapter(oficina);
        spinner_oficina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oficina_spinn = parent.getItemAtPosition(position).toString();

                if (oficina_spinn.equals("Ninguno")) {
                    numeroLayout.setVisibility(View.GONE);
                    edit_ofi.setText("");
                    edit_ofi.setError(null);
                } else {
                    edit_ofi.setError(null);
                    numeroLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        monthOfYear = monthOfYear+1;
        fecha = year+"/"+monthOfYear+"/"+dayOfMonth;
        //edit_cumple.setText(fecha);
    }

    private void loadAdress() {

        dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                "Diagonal", "Manzana", "Transversal", "Vía"};

        ArrayAdapter<String> prec1 = new ArrayAdapter<>(this, R.layout.textview_spinner, dir1_parant);
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

    private void guardarPerfil(){

        Cliente cl = new Cliente();

        cl.setNombre(edit_nombres.getText().toString());
        cl.setCelular(edit_cell.getText().toString());
        cl.setTelefono(edit_phone.getText().toString());
        cl.setCalle_carrera(Calle_Carrera);
        cl.setDir_1(edit_a.getText().toString());
        cl.setDir_2(edit_b.getText().toString());
        cl.setDir_3(edit_c.getText().toString());
        cl.setCiudad(selecte_ciudad);
        cl.setZona(zona_dir);
        cl.setOficina(oficina_spinn);

        if (numeroLayout.getVisibility() == View.VISIBLE)
            cl.setNum_oficina(Integer.parseInt(edit_ofi.getText().toString()));
        else
            cl.setNum_oficina(0);

        cl.setBarrio(editBarrioCliente.getText().toString());
        cl.setDirReferencia(editDirReferencia.getText().toString());

        if (bundle != null){
            if (mydb.UpdatePerfil(thumbs.getCodigo(), cl)){
                Toast.makeText(this, "Datos Actualizados Exitosamente", Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {
                Toast.makeText(this, "Problemas al Actualizar el perfil.", Toast.LENGTH_LONG).show();
            }
        } else {
            if (mydb.insertUsuario(cl)) {
                Toast.makeText(this, "Datos Guardados Exitosamente", Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {
                Toast.makeText(this, "Problemas al Guardar el perfil.", Toast.LENGTH_LONG).show();
            }
        }

    }


}
