package com.appgestor.domidomi.mockedFragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterEstadoPedido;
import com.appgestor.domidomi.Entities.ListPedidoEstado;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class FragmentMenuPedidos extends BaseVolleyFragmentSoport {

    private int operador;
    protected View rootView;
    protected LinearLayout linearLayout;
    private SwipeMenuListView listView;
    protected LinearLayout linearLayoutHis;
    private SwipeMenuListView listViewHis;
    private AlertDialog alertDialog;
    private RequestQueue rq;

    public static FragmentMenuPedidos newInstance(Bundle param1) {
        FragmentMenuPedidos fragment = new FragmentMenuPedidos();
        fragment.setArguments(param1);
        return fragment;
    }

    public FragmentMenuPedidos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operador = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = null;
        switch (operador) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_estado_pedido_ver, container, false);
                listView = (SwipeMenuListView) rootView.findViewById(R.id.listViewSeguimiento);
                alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
                linearLayout = (LinearLayout) rootView.findViewById(R.id.layoutSeguimiento);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_historial, container, false);
                listViewHis = (SwipeMenuListView) rootView.findViewById(R.id.listViewHis);
                linearLayoutHis = (LinearLayout) rootView.findViewById(R.id.layoutHis);
                break;
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (operador) {
            case 0:
                getSeguimiento();
                break;
            case 1:
                getHistorial();
                break;
        }
    }

    private void getSeguimiento() {
        alertDialog.show();
        linearLayout.setVisibility(View.GONE);
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "estadoPedido");
        rq = Volley.newRequestQueue(getActivity());
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
                        alertDialog.dismiss();
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String identifier;
                if ( Build.VERSION.SDK_INT >= 23){
                    identifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    identifier = telephonyManager.getDeviceId();
                }

                params.put("idTelefono", identifier);
                return params;
            }
        };
        rq.add(jsonRequest);
    }

    private boolean parseJSON(String json) {

        if (!json.equals("[]")){
            try {
                Gson gson = new Gson();
                ListPedidoEstado pedidos = gson.fromJson(json, ListPedidoEstado.class);
                AdapterEstadoPedido adapter = new AdapterEstadoPedido(getActivity(), pedidos);
                listView.setAdapter(adapter);
                alertDialog.dismiss();

                return true;
            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            alertDialog.dismiss();
            linearLayout.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private void getHistorial() {
        linearLayoutHis.setVisibility(View.GONE);
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "getEstadoPedidoCancelados");
        rq = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSONHis(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String identifier;
                if ( Build.VERSION.SDK_INT >= 23){
                    identifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    identifier = telephonyManager.getDeviceId();
                }

                params.put("idTelefono", identifier);

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    private void parseJSONHis(String json) {
        if (!json.equals("[]")){
            try {
                Gson gson = new Gson();
                ListPedidoEstado pedidosHis = gson.fromJson(json, ListPedidoEstado.class);
                AdapterEstadoPedido adapterHis = new AdapterEstadoPedido(getActivity(), pedidosHis);
                listViewHis.setAdapter(adapterHis);

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            linearLayoutHis.setVisibility(View.VISIBLE);
        }
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

}

