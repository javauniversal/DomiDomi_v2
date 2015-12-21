package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.HashMap;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Empresas.getEmpresastatic;

public class ActivitySedes extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sedes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sedes: "+getEmpresastatic().getDescripcion());
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        cargarSedes();
    }

    public void cargarSedes(){
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"listSedes");
        RequestQueue rq = Volley.newRequestQueue(this);
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
                        mSwipyRefreshLayout.setRefreshing(false);
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

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    private boolean parseJSON(String json) {
        boolean indicant = false;
        Gson gson = new Gson();
        if (!json.equals("[]")){
            try {

                ListSede listSedes = gson.fromJson(json, ListSede.class);

                adapter = new AdapterRecyclerSedesEmpresa(this, listSedes);
                recycler.setAdapter(adapter);
                mSwipyRefreshLayout.setRefreshing(false);
                recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        startActivity(new Intent(ActivitySedes.this, ActMenu.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                }));
                indicant = true;

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
                indicant = false;
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("STATE", "EMPTY");
            startActivity(intent);
        }

        mSwipyRefreshLayout.setRefreshing(false);
        return indicant;
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        cargarSedes();
    }
}
