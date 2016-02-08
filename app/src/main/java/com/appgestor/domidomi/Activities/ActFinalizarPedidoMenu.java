package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.Entities.MedioPago;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;
import com.appgestor.domidomi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.MedioPago.getMedioPagoListstatic;


public class ActFinalizarPedidoMenu extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MyTag";
    private EditText editNombreCliente;
    private EditText editCelularCliente;
    private Spinner spinner_dir;
    private String[] dir1_parant;
    private String Calle_Carrera;
    private EditText txt_dir_1;
    private EditText txt_dir_2;
    private EditText txt_dir_3;
    private EditText editBarrioCliente;
    private EditText editDirReferencia;
    private DBHelper mydb;
    private EditText editEfectivo;
    private PedidoWebCabeza objeto = new PedidoWebCabeza();
    List<Address> returnedaddresses;
    Geocoder geocoder;
    private Double latitud = 0.0;
    private Double longitud = 0.0;
    private RequestQueue rq;
    private List<Ciudades> ciudades;
    private Spinner dir_3;
    private String selecte_ciudad;
    private LinearLayout zonaLayout;
    private String[] dir1Zona_parant;
    private String zona_dir;
    private Spinner spinner_zona;
    private Bundle bundle;
    private String adressString;
    private LinearLayout input_layout_Efectivo_liner;
    private Cliente cliente;
    private LinearLayout root;
    private EditText editTelefonoCliente;
    private AlertDialog alertDialog;
    ArrayAdapter<String> prec1;
    ArrayAdapter<Ciudades> prec3;
    ArrayAdapter<String> prec123;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finalizar_pedido);
        mydb = new DBHelper(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        alertDialog = new SpotsDialog(this, R.style.Custom);

        geocoder = new Geocoder(this, Locale.ENGLISH);

        Button myAceptar = (Button)findViewById(R.id.button_accept);
        myAceptar.setOnClickListener(this);

        Button myCancelar = (Button) findViewById(R.id.button_cancel);
        myCancelar.setOnClickListener(this);

        ll = (LinearLayout) findViewById(R.id.llMediosPagos);

        editNombreCliente = (EditText) findViewById(R.id.editNombreCliente);
        editCelularCliente = (EditText) findViewById(R.id.editCelularCliente);
        spinner_dir = (Spinner) findViewById(R.id.spinner_dir);
        txt_dir_1 = (EditText) findViewById(R.id.txt_dir_1);
        txt_dir_2 = (EditText) findViewById(R.id.txt_dir_2);
        txt_dir_3 = (EditText) findViewById(R.id.txt_dir_3);
        editBarrioCliente = (EditText) findViewById(R.id.editBarrioCliente);
        editDirReferencia = (EditText) findViewById(R.id.editDirReferencia);
        editTelefonoCliente = (EditText) findViewById(R.id.editTelefonoCliente);
        input_layout_Efectivo_liner = (LinearLayout) findViewById(R.id.input_layout_Efectivo_liner);

        root = (LinearLayout) findViewById(R.id.llMediosPagos);

        //loaandSpinner(spTarjetas);

        editEfectivo = (EditText) findViewById(R.id.editEfectivo);

        dir_3 = (Spinner) findViewById(R.id.spinner_ciudades);

        zonaLayout = (LinearLayout) findViewById(R.id.zonaLayout);

        spinner_zona = (Spinner) findViewById(R.id.spinner_zona);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {

                loadAdress();
                loadCiudades();
                setLlenarZona();
                CargarMedioPago();
                CargarDataCliente();

                return null;
            }

            protected void onPreExecute() { }

            @Override
            public void onProgressUpdate(Long... value) { }

            @Override
            protected void onPostExecute(Long result){
                alertDialog.dismiss();
                setAdress();
                setCiudades();
                setZonas();
                setCargarCliente();
            }

        }.execute();

    }

    private void CargarDataCliente() {
        cliente = mydb.getUsuarioAll();
    }

    private void setCargarCliente() {
        if(cliente != null){
            if(cliente.getIncluir() == 1){
                editNombreCliente.setText(cliente.getNombre());
                editCelularCliente.setText(cliente.getCelular());
                editTelefonoCliente.setText(cliente.getTelefono());
                txt_dir_1.setText(cliente.getDir_1());
                txt_dir_2.setText(cliente.getDir_2());
                txt_dir_3.setText(cliente.getDir_3());
                editBarrioCliente.setText(cliente.getBarrio());
                editDirReferencia.setText(cliente.getDirReferencia());

                List<String> strListAd = new ArrayList<>(Arrays.asList(dir1_parant));
                spinner_dir.setSelection(strListAd.indexOf(cliente.getCalle_carrera()));

                selectValue(ciudades, cliente.getCiudad(), dir_3);

            }
        }
    }

    private void loadLlenarZona() {
        dir1Zona_parant = new String[]{"Bello", "Caldas", "Envigado",  "Itaguí", "La Estrella", "Medellín", "Sabaneta"};
    }

    private void setLlenarZona(){
        loadLlenarZona();
        prec123 = new ArrayAdapter<>(ActFinalizarPedidoMenu.this, R.layout.textview_spinner, dir1Zona_parant);
    }

    private void setZonas(){
        spinner_zona.setAdapter(prec123);
        loadLlenarZona();
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

    private void setAdress() {

        spinner_dir.setAdapter(prec1);
        spinner_dir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calle_Carrera = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });
    }


    private void loadAdress() {
        dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                "Diagonal", "Manzana", "Transversal", "Vía"};

        prec1 = new ArrayAdapter<>(ActFinalizarPedidoMenu.this, R.layout.textview_spinner, dir1_parant);

    }

    private void selectValue(List<Ciudades> ciudad, String value, Spinner spinner) {
        for (int i = 0; i < ciudad.size(); i++) {
            if (ciudad.get(i).getNombreCiudad().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }

    }

    private void CargarMedioPago() {

        if (getMedioPagoListstatic() != null){

            int mediosPAgos = getMedioPagoListstatic().size();
            for (int i = 0; i < mediosPAgos; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 70, 0, 0);
                CheckBox cb = new CheckBox(this);
                cb.setText(getMedioPagoListstatic().get(i).getDescripcion());
                cb.setId(getMedioPagoListstatic().get(i).getIdmediopago());
                if (ll != null)
                    ll.addView(cb);
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {

                            if (((CheckBox) v).getText().equals("Efectivo") || ((CheckBox) v).getText().equals("efectivo")) {
                                input_layout_Efectivo_liner.setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (((CheckBox) v).getText().equals("Efectivo") || ((CheckBox) v).getText().equals("efectivo")) {
                                input_layout_Efectivo_liner.setVisibility(View.GONE);
                                editEfectivo.setText("0");
                            }

                        }
                    }
                });
            }

        }
    }

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
        prec3 = new ArrayAdapter<>(ActFinalizarPedidoMenu.this, R.layout.textview_spinner, ciudades);
    }

    private void setCiudades(){

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_accept:

                if (isValidNumber(editNombreCliente.getText().toString())) {
                    editNombreCliente.setError("Campo requerido");
                    editNombreCliente.requestFocus();
                } else if (isValidNumber(txt_dir_1.getText().toString())){
                    txt_dir_1.setError("Campo requerido");
                    txt_dir_1.requestFocus();
                } else if (isValidNumber(txt_dir_2.getText().toString())){
                    txt_dir_2.setError("Campo requerido");
                    txt_dir_2.requestFocus();
                } else if (isValidNumber(txt_dir_3.getText().toString())){
                    txt_dir_3.setError("Campo requerido");
                    txt_dir_3.requestFocus();
                } else if (isValidNumber(editBarrioCliente.getText().toString())) {
                    editBarrioCliente.setError("Campo requerido");
                    editBarrioCliente.requestFocus();
                } else if (isValidNumber(editDirReferencia.getText().toString())) {
                    editDirReferencia.setError("Campo requerido");
                    editDirReferencia.requestFocus();
                } else if(validateCheckBox()){
                    Toast.makeText(this, "Marque un medio de pago", Toast.LENGTH_LONG).show();
                } else if(input_layout_Efectivo_liner.getVisibility() == View.VISIBLE && isValidNumber(editEfectivo.getText().toString())) {
                    editEfectivo.setError("Por favor digite la cantidad con la que va apagar");
                    editEfectivo.requestFocus();
                } else if(input_layout_Efectivo_liner.getVisibility() == View.VISIBLE && validateCantidadValor()) {
                    editEfectivo.setError("La cantidad con la que paga no supera el monto total del pedido.  ");
                    editEfectivo.requestFocus();
                }else {

                    if (isValidNumber(editCelularCliente.getText().toString())){
                        if(isValidNumber(editTelefonoCliente.getText().toString())){
                            editCelularCliente.setError("Requerido");
                            editCelularCliente.requestFocus();
                        } else {
                            //Guardar
                            resulFinalValidate();
                        }
                    } else {
                        //Guardar
                        resulFinalValidate();
                    }

                }

                break;

            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private boolean validateCantidadValor(){

        List<AddProductCar> mAppList = mydb.getProductCar(bundle.getInt("empresa"), bundle.getInt("sede"));
        double valorFinalPedido = 0;
        for (int i = 0; i < mAppList.size(); i++) {
            valorFinalPedido = valorFinalPedido + mAppList.get(i).getValueoverall();
        }

        valorFinalPedido = valorFinalPedido + bundle.getDouble("cosEnvio");
        return Double.parseDouble(editEfectivo.getText().toString()) < valorFinalPedido;

    }

    private void resulFinalValidate() {

        if(isValidNumber(editEfectivo.getText().toString()))
            editEfectivo.setText("0");

        if (zonaLayout.getVisibility() == View.VISIBLE){
            adressString = Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim() +" "+zona_dir +" "+selecte_ciudad;
        } else {
            adressString = Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim() +" "+selecte_ciudad;
        }
        alertDialog.show();
        loadAdressLatLon(adressString);
    }

    private boolean validateCheckBox(){

        boolean resulCheckBox = true;

        for(int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    resulCheckBox = false;
                    break;
                }
            }
        }

        return resulCheckBox;
    }

    public void loadAdressLatLon(final String adressObten){
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                try {

                    returnedaddresses = geocoder.getFromLocationName(adressObten, 1);
                    if (returnedaddresses.size() > 0) {
                        latitud = returnedaddresses.get(0).getLatitude();
                        longitud = returnedaddresses.get(0).getLongitude();
                    }

                } catch (IOException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPreExecute() { }

            @Override
            public void onProgressUpdate(Long... value) { }

            @Override
            protected void onPostExecute(Long result){
                if(returnedaddresses != null){
                    enviarPedido();
                }
            }

        }.execute();
    }

    public void enviarPedido(){

        String url = String.format("%1$s%2$s", getString(R.string.url_base),"pruebaJson");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response

                        if(mydb.DeleteProductAll(bundle.getInt("empresa"), bundle.getInt("sede"))){
                            alertDialog.dismiss();
                            Toast.makeText(ActFinalizarPedidoMenu.this, response, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(ActFinalizarPedidoMenu.this, ActEstadoPedido.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        }else{
                            Toast.makeText(ActFinalizarPedidoMenu.this, "Problemas con el pedido.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        alertDialog.dismiss();
                        startActivity(new Intent(ActFinalizarPedidoMenu.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                objeto.setImeiPhone(telephonyManager.getDeviceId());
                objeto.setNombreUsuairo(editNombreCliente.getText().toString());
                objeto.setCelularp(editCelularCliente.getText().toString());
                objeto.setDireccionp(adressString);
                objeto.setDireccionReferen("Barrio: "+editBarrioCliente.getText().toString()+", "+editDirReferencia.getText().toString());
                //objeto.setMedioPago(masterItem.getIdmediopago());

                objeto.setValorPago(Double.valueOf(editEfectivo.getText().toString()));
                objeto.setLatitud(latitud);
                objeto.setLongitud(longitud);
                objeto.setIdEmpresaP(bundle.getInt("empresa"));
                objeto.setIdSedeP(bundle.getInt("sede"));

                List<AddProductCar> mAppList = mydb.getProductCar(bundle.getInt("empresa"), bundle.getInt("sede"));

                for (int i = 0; i < mAppList.size(); i++) {
                    mAppList.get(i).setAdicionesList(mydb.getAdiciones(mAppList.get(i).getIdProduct(),mAppList.get(i).getIdcompany(), mAppList.get(i).getIdsede()));
                }

                ArrayList<MedioPago> medioPagoList = new ArrayList<>();

                for(int i = 0; i < root.getChildCount(); i++) {
                    View child = root.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox cb = (CheckBox) child;
                        int answer = cb.isChecked() ? 1 : 0;
                        if (answer == 1){
                            MedioPago medioPago = new MedioPago();
                            medioPago.setDescripcion(cb.getText().toString());
                            medioPago.setIdmediopago(cb.getId());

                            medioPagoList.add(medioPago);
                        }
                    }
                }

                objeto.setMediosPagosList(medioPagoList);

                objeto.setProducto(mAppList);
                String parJSON = new Gson().toJson(objeto, PedidoWebCabeza.class);

                params.put("pedido", parJSON);

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    private boolean isValidNumber(String number){
        return number == null || number.length() == 0;
    }

}
