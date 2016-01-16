package com.appgestor.domidomi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.appgestor.domidomi.Services.MyService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Sede.getSedeStatic;


public class ActFinalizarPedido extends AppCompatActivity implements View.OnClickListener {

    private EditText nombre;
    private EditText edtCelular;
    private EditText editdir;
    private EditText editdirreferen;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText editEfectivo;
    private TextView txtEfectivo;
    private PedidoWebCabeza objeto = new PedidoWebCabeza();
    private DBHelper mydb;
    private ImageView imageView4;
    private MedioPago masterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finalizar_pedido);
        mydb = new DBHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ImageButton location = (ImageButton) findViewById(R.id.imgButtonLocation);
        location.setOnClickListener(this);

        Button myAceptar = (Button)findViewById(R.id.button_accept);
        myAceptar.setOnClickListener(this);

        Button myCancelar = (Button) findViewById(R.id.button_cancel);
        myCancelar.setOnClickListener(this);

        Spinner spTarjetas = (Spinner) findViewById(R.id.spinnerTarjetas);
        loaandSpinner(spTarjetas);

        txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        txtLongitud = (EditText) findViewById(R.id.txtLongitud);

        txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        editEfectivo = (EditText) findViewById(R.id.editEfectivo);


        imageView4 = (ImageView) findViewById(R.id.imageView4);

        nombre = (EditText)findViewById(R.id.edtNombre);
        edtCelular = (EditText)findViewById(R.id.edtCelular);
        editdir = (EditText)findViewById(R.id.editdir);
        editdirreferen = (EditText)findViewById(R.id.editdirreferen);

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

            case R.id.imgButtonLocation:
                MyService services = new MyService(this);
                //services.setView(findViewById(R.id.txtLatitud), findViewById(R.id.txtLongitud));
                break;

            case R.id.button_accept:

                if (isValidNumber(nombre.getText().toString())) {
                    nombre.setError("El nombre es un campo requerido");
                } else if (isValidNumber(edtCelular.getText().toString())) {
                    edtCelular.setError("El telefono es un campo requerido");
                } else if (isValidNumber(editdir.getText().toString())){
                    editdir.setError("la direccion es un campo requerido");
                }else if(editEfectivo.getVisibility() == View.VISIBLE && isValidNumber(editEfectivo.getText().toString())) {
                    editEfectivo.setError("Por favor digite la cantidad con la con la que va apagar");
                }else{
                    if(isValidNumber(txtLatitud.getText().toString())){ txtLatitud.setText("0"); }
                    if(isValidNumber(txtLongitud.getText().toString())) { txtLongitud.setText("0"); }
                    enviarPedido();
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private boolean isValidNumber(String number){

        return number == null || number.length() == 0;

    }

    public void enviarPedido(){

        String url = String.format("%1$s%2$s", getString(R.string.url_base),"pruebaJson");
        RequestQueue rq = Volley.newRequestQueue(this);

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
                        //onConnectionFailed(error.toString());
                        //Toast.makeText(ActFinalizarPedido.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ActFinalizarPedido.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                objeto.setImeiPhone(telephonyManager.getDeviceId());
                objeto.setNombreUsuairo(nombre.getText().toString());
                objeto.setCelularp(edtCelular.getText().toString());
                objeto.setDireccionp(editdir.getText().toString());
                objeto.setDireccionReferen(editdirreferen.getText().toString());
                objeto.setMedioPago(masterItem.getIdmediopago());


                objeto.setValorPago(Double.valueOf(editEfectivo.getText().toString()));
                objeto.setLatitud(Double.valueOf(txtLatitud.getText().toString()));
                objeto.setLongitud(Double.valueOf(txtLongitud.getText().toString()));
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

    public void loaandSpinner(Spinner spinner){

        String[] dataTarjetas = new String[getSedeStatic().getMedioPagoList().size()];

        for (int i = 0; i < getSedeStatic().getMedioPagoList().size(); i++) {
            dataTarjetas[i] = getSedeStatic().getMedioPagoList().get(i).getDescripcion();
        }

        ArrayAdapter<String> spTarjetas = new ArrayAdapter<String>(this, R.layout.textview_spinner, dataTarjetas);
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

}
