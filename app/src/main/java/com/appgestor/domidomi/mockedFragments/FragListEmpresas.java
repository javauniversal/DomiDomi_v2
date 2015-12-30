package com.appgestor.domidomi.mockedFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActivitySedes;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterRecyclerEmpresas;
import com.appgestor.domidomi.Adapters.RecyclerItemClickListener;
import com.appgestor.domidomi.Entities.ListEmpresas;
import com.appgestor.domidomi.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.Empresas.setEmpresastatic;

public class FragListEmpresas extends BaseVolleyFragment implements SwipyRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private AlertDialog alertDialog;

    public FragListEmpresas() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_empresas, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mSwipyRefreshLayout = (SwipyRefreshLayout) myView.findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGrid();

    }

    private void setupGrid() {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "listEmpresas");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                        mSwipyRefreshLayout.setRefreshing(false);
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                return new HashMap<>();
            }
        };
        addToQueue(jsonRequest);
    }

    private boolean parseJSON(String json) {
        if (!json.equals("[]")){

            try {

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                final ListEmpresas listEmpresas = gson.fromJson(json, ListEmpresas.class);

                //setEmpresasListStatic(listEmpresas);

                mLayoutManager = new GridLayoutManager(getActivity(), 1);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new AdapterRecyclerEmpresas(getActivity(), listEmpresas);
                mRecyclerView.setAdapter(mAdapter);
                mSwipyRefreshLayout.setRefreshing(false);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        setEmpresastatic(listEmpresas.get(position));
                        startActivity(new Intent(getActivity(), ActivitySedes.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }));

                alertDialog.dismiss();

                return true;


            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        }else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            mSwipyRefreshLayout.setRefreshing(false);
            alertDialog.dismiss();
        }

        mSwipyRefreshLayout.setRefreshing(false);
        return false;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        setupGrid();
    }
}
