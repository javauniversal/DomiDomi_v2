package com.appgestor.domidomi.Activities;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.appgestor.domidomi.Entities.MedioPago;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;
import com.appgestor.domidomi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Sede.getSedeStatic;


public class ActFinalizarPedido extends AppCompatActivity implements View.OnClickListener {

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
    private ImageView imageView4;
    private MedioPago masterItem;
    private EditText editEfectivo;
    private TextView txtEfectivo;
    private PedidoWebCabeza objeto = new PedidoWebCabeza();
    List<Address> returnedaddresses;
    Geocoder geocoder;
    private Double latitud;
    private Double longitud;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finalizar_pedido);
        mydb = new DBHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        geocoder = new Geocoder(this, Locale.ENGLISH);

        Button myAceptar = (Button)findViewById(R.id.button_accept);
        myAceptar.setOnClickListener(this);

        Button myCancelar = (Button) findViewById(R.id.button_cancel);
        myCancelar.setOnClickListener(this);

        editNombreCliente = (EditText) findViewById(R.id.editNombreCliente);
        editCelularCliente = (EditText) findViewById(R.id.editCelularCliente);
        spinner_dir = (Spinner) findViewById(R.id.spinner_dir);
        txt_dir_1 = (EditText) findViewById(R.id.txt_dir_1);
        txt_dir_2 = (EditText) findViewById(R.id.txt_dir_2);
        txt_dir_3 = (EditText) findViewById(R.id.txt_dir_3);
        editBarrioCliente = (EditText) findViewById(R.id.editBarrioCliente);
        editDirReferencia = (EditText) findViewById(R.id.editDirReferencia);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        Spinner spTarjetas = (Spinner) findViewById(R.id.spinnerTarjetas);
        loaandSpinner(spTarjetas);

        txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        editEfectivo = (EditText) findViewById(R.id.editEfectivo);

        loadAdress();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                } else if(editEfectivo.getVisibility() == View.VISIBLE && isValidNumber(editEfectivo.getText().toString())) {
                    editEfectivo.setError("Por favor digite la cantidad con la que va apagar");
                    editEfectivo.requestFocus();
                } else {
                    if(isValidNumber(editCelularCliente.getText().toString())){ editCelularCliente.setText("0"); }
                    loadAdressLatLon(Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim());

                }

                break;

            case R.id.button_cancel:
                finish();
                break;
        }
    }

    public void loaandSpinner(Spinner spinner){

        String[] dataTarjetas = new String[getSedeStatic().getMedioPagoList().size()];

        for (int i = 0; i < getSedeStatic().getMedioPagoList().size(); i++) {
            dataTarjetas[i] = getSedeStatic().getMedioPagoList().get(i).getDescripcion();
        }

        ArrayAdapter<String> spTarjetas = new ArrayAdapter<>(this, R.layout.textview_spinner, dataTarjetas);
        spinner.setAdapter(spTarjetas);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {

                masterItem = getSedeStatic().getMedioPagoList().get(pos);

                if(masterItem.getDescripcion().equals("Efectivo")){
                    editEfectivo.setVisibility(View.VISIBLE);
                    txtEfectivo.setVisibility(View.VISIBLE);
                    imageView4.setImageResource(R.drawable.ic_local_atm_black_24dp);
                }else{
                    editEfectivo.setVisibility(View.GONE);
                    txtEfectivo.setVisibility(View.GONE);
                    editEfectivo.setText("0");
                    imageView4.setImageResource(R.drawable.ic_credit_card_black_24dp);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });// Fin del evento del spin

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
                ArrayAdapter<String> prec1 = new ArrayAdapter<String>(ActFinalizarPedido.this, R.layout.textview_spinner, dir1_parant);
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

        }.execute();
    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

    public void enviarPedido(){

        String url = String.format("%1$s%2$s", getString(R.string.url_base),"pruebaJson");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(ActFinalizarPedido.this, response, Toast.LENGTH_LONG).show();

                        if(mydb.DeleteProductAll(getSedeStatic().getIdempresa(), getSedeStatic().getIdsedes())){

                            Toast.makeText(ActFinalizarPedido.this, response, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(ActFinalizarPedido.this, ActEstadoPedido.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        }else{
                            Toast.makeText(ActFinalizarPedido.this, "Problemas con el pedido.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        startActivity(new Intent(ActFinalizarPedido.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
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
                objeto.setDireccionp(Calle_Carrera +" "+ txt_dir_1.getText().toString().trim() +" # "+ txt_dir_2.getText().toString().trim() +" - "+txt_dir_3.getText().toString().trim());
                objeto.setDireccionReferen(editDirReferencia.getText().toString());
                objeto.setMedioPago(masterItem.getIdmediopago());

                objeto.setValorPago(Double.valueOf(editEfectivo.getText().toString()));
                objeto.setLatitud(Double.valueOf(latitud));
                objeto.setLongitud(Double.valueOf(longitud));
                objeto.setIdEmpresaP(getSedeStatic().getIdempresa());
                objeto.setIdSedeP(getSedeStatic().getIdsedes());

                List<AddProductCar> mAppList = mydb.getProductCar(getSedeStatic().getIdempresa(), getSedeStatic().getIdsedes());

                for (int i = 0; i < mAppList.size(); i++) {
                    mAppList.get(i).setAdicionesList(mydb.getAdiciones(mAppList.get(i).getIdProduct(),mAppList.get(i).getIdcompany(), mAppList.get(i).getIdsede()));
                }

                objeto.setProducto(mAppList);
                String parJSON = new Gson().toJson(objeto, PedidoWebCabeza.class);

                params.put("pedido", parJSON);

                return params;
            }
        };
        rq.add(jsonRequest);
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
                    enviarPedido();
                }

            }

        }.execute();
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
        rq.cancelAll(TAG);
        super.onDestroy();
    }

}
