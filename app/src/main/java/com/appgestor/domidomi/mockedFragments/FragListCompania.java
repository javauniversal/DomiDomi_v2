package com.appgestor.domidomi.mockedFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActMenu;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterCompania;
import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.Entities.ListCompanias;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class FragListCompania extends BaseVolleyFragment {

    private SwipeMenuListView multiColumnList = null;
    private Activity activity;
    private ListCompanias companias;
    private Companias companiass;

    public FragListCompania() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_compania, container, false);
        multiColumnList = (SwipeMenuListView) myView.findViewById(R.id.listView);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGrid();
    }

    private void setupGrid() {
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"listCompania");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        parseJSON(response);
                        AdapterCompania adapter = new AdapterCompania(getActivity(), companias);
                        multiColumnList.setAdapter(adapter);
                        multiColumnList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                //Toast.makeText(getActivity(), "Ya tiene una Ruta en Ejecución", Toast.LENGTH_LONG).show();
                                companiass.setCodigoS(companias.get(position).getCodigo());
                                startActivity(new Intent(getActivity(), ActMenu.class));
                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //onConnectionFailed(error.toString());
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                    //params.put("operador", "listCompania");
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private boolean parseJSON(String json) {
        if (!json.equals("[]")){

            try {
                Gson gson = new Gson();
                companias = gson.fromJson(json, ListCompanias.class);
                Companias.setCompaniasS(companias);
                return true;
            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        }else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return false;

    }

}
