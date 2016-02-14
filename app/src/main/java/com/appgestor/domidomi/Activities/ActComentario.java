package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.Accounts;

import java.util.HashMap;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Sede.getSedeIdeStatic;

public class ActComentario extends AppCompatActivity implements View.OnClickListener {

    private EditText Comentario;
    private Button enviar;
    private Button cancelar;
    protected RequestQueue rq;
    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_comentario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Comentario = (EditText) findViewById(R.id.EditComment);

        enviar = (Button) findViewById(R.id.btnEnviar);
        enviar.setOnClickListener(this);

        cancelar = (Button) findViewById(R.id.btnCancelcom);
        cancelar.setOnClickListener(this);

    }

    private void setComentarios() {
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"setComentarios");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ActComentario.this, "Comentario enviado", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ActComentario.this, Accounts.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        startActivity(new Intent(ActComentario.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("comentario", Comentario.getText().toString());
                params.put("sede", String.valueOf(getSedeIdeStatic()));

                return params;
            }
        };

        rq.add(jsonRequest);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (rq != null)
            rq.cancelAll(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rq != null)
            rq.cancelAll(TAG);
    }


    private boolean isValidNumber(String number){

        return number == null || number.length() == 0;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnEnviar:

                if (isValidNumber(Comentario.getText().toString())) {
                    Comentario.setError("El comentario es un campo requerido");
                }else{
                    setComentarios();
                }

                break;

            case R.id.btnCancelcom:

                startActivity(new Intent(ActComentario.this, Accounts.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

                break;
        }

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, Accounts.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        super.onBackPressed();  // optional depending on your needs

    }
}
