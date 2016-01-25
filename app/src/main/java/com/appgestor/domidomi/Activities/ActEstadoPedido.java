package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.Adapters.AdapterEstadoPedido;
import com.appgestor.domidomi.Entities.ListPedidoEstado;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.Accounts;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActEstadoPedido extends AppCompatActivity {

    private SwipeMenuListView listView;
    private AlertDialog alertDialog;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_estado_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActEstadoPedido.this, Accounts.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        listView = (SwipeMenuListView) findViewById(R.id.listView);
        alertDialog = new SpotsDialog(this, R.style.Custom);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enviarPedido();
    }

    public void enviarPedido() {

        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "estadoPedido");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        startActivity(new Intent(ActEstadoPedido.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                params.put("idTelefono", telephonyManager.getDeviceId());

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (rq != null)
            rq.cancelAll("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rq != null)
            rq.cancelAll("");
    }

    private boolean parseJSON(String json) {

        if (!json.equals("[]")){
            try {
                Gson gson = new Gson();
                ListPedidoEstado pedidos = gson.fromJson(json, ListPedidoEstado.class);
                AdapterEstadoPedido adapter = new AdapterEstadoPedido(this, pedidos);
                listView.setAdapter(adapter);
                alertDialog.dismiss();

                return true;
            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            alertDialog.dismiss();
            startActivity(new Intent(this, DetailsActivity.class).putExtra("STATE", "EMPTYP"));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return false;

    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ActEstadoPedido.this, Accounts.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

}
