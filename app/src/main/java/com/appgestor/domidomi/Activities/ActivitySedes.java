package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.Adapters.AdapterRecyclerSedesEmpresa;
import com.appgestor.domidomi.Adapters.RecyclerItemClickListener;
import com.appgestor.domidomi.Entities.ListSede;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.RadarView.RandomTextView;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.HashMap;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Empresas.getEmpresastatic;
import static com.appgestor.domidomi.Entities.Sede.setSedeStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.getLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.getLongitudStatic;

public class ActivitySedes extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    //private SwipyRefreshLayout mSwipyRefreshLayout;
    //private AlertDialog alertDialog;
    private RandomTextView randomTextView;
    private TextView txt_title_radar;
    private RelativeLayout relativeLayout_radar;
    private RelativeLayout relativeLayout_sedes;
    private RequestQueue rq;
    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_title_radar = (TextView) findViewById(R.id.txt_title_radar);


        relativeLayout_radar = (RelativeLayout) findViewById(R.id.relativeLayout_radar);
        relativeLayout_sedes = (RelativeLayout) findViewById(R.id.relativeLayout_sedes);

        randomTextView = (RandomTextView) findViewById(R.id.random_textview);
        randomTextView.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
                    @Override
                    public void onRippleViewClicked(View view) {
                        //ActivitySedes.this.startActivity(new Intent(ActivitySedes.this, RefreshProgressActivity.class));
                    }
        });

        cargarSedes();

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);


    }

    public void cargarSedes(){
        //alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"listSedes");
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
                        //alertDialog.dismiss();
                        //mSwipyRefreshLayout.setRefreshing(false);
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("STATE", "ERROR");
                        startActivity(intent);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("empresa", String.valueOf(getEmpresastatic().getIdempresa()));
                params.put("latitud", String.valueOf(getLatitudStatic()));
                params.put("longitud", String.valueOf(getLongitudStatic()));

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
        rq.cancelAll(TAG);
        super.onDestroy();
    }

    private boolean parseJSON(String json) {
        boolean indicant = false;
        Gson gson = new Gson();
        if (!json.equals("[]")){
            try {

                final ListSede listSedes = gson.fromJson(json, ListSede.class);

                for (int i = 0; i < listSedes.size(); i++) {
                    randomTextView.addKeyWord(listSedes.get(i).getDescripcion());
                }

                randomTextView.show();
                txt_title_radar.setText("Estos son los establecimientos encontrados");

                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        Animation out = AnimationUtils.makeOutAnimation(ActivitySedes.this, true);
                        relativeLayout_radar.startAnimation(out);
                        relativeLayout_radar.setVisibility(View.GONE);

                        adapter = new AdapterRecyclerSedesEmpresa(ActivitySedes.this, listSedes);
                        recycler.setAdapter(adapter);

                        relativeLayout_sedes.setVisibility(View.VISIBLE);

                    }

                }, 2 * 1500);

                recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        setSedeStatic(listSedes.get(position));

                        startActivity(new Intent(ActivitySedes.this, ActMenu.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                }));

                indicant = true;

                //alertDialog.dismiss();

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
                //alertDialog.dismiss();
                indicant = false;
            }
        }else {
            //alertDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("STATE", "EMPTY");
            startActivity(intent);
        }

        //mSwipyRefreshLayout.setRefreshing(false);
        return indicant;
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        cargarSedes();
    }
}
