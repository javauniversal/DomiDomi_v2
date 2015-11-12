package com.appgestor.domidomi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.dark.Accounts;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActFinalizarPedido extends AppCompatActivity implements View.OnClickListener {

    private EditText nombre;
    private EditText edtCelular;
    private EditText editdir;
    private EditText editdirreferen;
    private PedidoWebCabeza objeto = new PedidoWebCabeza();
    private EditText textLongitud;
    private EditText textLatitud;
    private DBHelper mydb;

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

        nombre = (EditText)findViewById(R.id.edtNombre);
        edtCelular = (EditText)findViewById(R.id.edtCelular);
        editdir = (EditText)findViewById(R.id.editdir);
        editdirreferen = (EditText)findViewById(R.id.editdirreferen);
        textLongitud = (EditText)findViewById(R.id.textLongitud);
        textLatitud = (EditText)findViewById(R.id.textLatitud);
        textLongitud.setHintTextColor(getResources().getColor(R.color.color_negro));
        textLongitud.setTextColor(getResources().getColor(R.color.color_negro));
        textLatitud.setHintTextColor(getResources().getColor(R.color.color_negro));
        textLatitud.setTextColor(getResources().getColor(R.color.color_negro));

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
                services.setView(findViewById(R.id.textLongitud), findViewById(R.id.textLatitud));
                break;

            case R.id.button_accept:

                if (isValidNumber(nombre.getText().toString())) {
                    nombre.setError("El nombre es un campo requerido");
                } else if (isValidNumber(edtCelular.getText().toString())) {
                    edtCelular.setError("El telefono es un campo requerido");
                } else if (isValidNumber(editdir.getText().toString())){
                    editdir.setError("la direccion es un campo requerido");
                }else{
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

                        if(mydb.DeleteProductAll(Companias.getCodigoS().getCodigo())){

                            Toast.makeText(ActFinalizarPedido.this, response, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(ActFinalizarPedido.this, Accounts.class));
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

                objeto.setNombreUsuairo(nombre.getText().toString());
                objeto.setIdCompany(Companias.getCodigoS().getCodigo());
                objeto.setDireccionp(editdir.getText().toString());
                objeto.setCelularp(edtCelular.getText().toString());
                objeto.setDireccionReferen(editdirreferen.getText().toString());
                objeto.setLatitud(Double.valueOf(textLatitud.getText().toString()));
                objeto.setLongitud(Double.valueOf(textLongitud.getText().toString()));
                objeto.setImeiPhone(telephonyManager.getDeviceId());

                List<AddProductCar> mAppList = mydb.getProductCar(Companias.getCodigoS().getCodigo());

                objeto.setProducto(mAppList);

                params.put("pedido", new Gson().toJson(objeto, PedidoWebCabeza.class));

                //Implementacion de guardar en la base de datos local.
                mydb.insertHistoryhead(objeto);
                int id_auto_detalle = mydb.ultimoRegistro();
                for (int i = 0; i < mAppList.size(); i++) {
                    mAppList.get(i).setIdAutoIncrement(id_auto_detalle);
                    mydb.insertHistorydetail(mAppList.get(i));
                }

                return params;
            }
        };
        rq.add(jsonRequest);
    }

}
