package com.appgestor.domidomi.mockedFragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Adapters.AdapterEstadoPedido;
import com.appgestor.domidomi.Entities.ListPedidoEstado;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class FragmentEstadoPedido extends BaseVolleyFragment {

    private SwipeMenuListView listView;
    private ListPedidoEstado pedidos;

    public FragmentEstadoPedido() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estado_pedido_ver, container, false);
        listView = (SwipeMenuListView) view.findViewById(R.id.listView);
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getEstadoPedido();
    }

    public void getEstadoPedido(){

        final AlertDialog alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base),"estadoPedido");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        parseJSON(response);

                        AdapterEstadoPedido adapter = new AdapterEstadoPedido(getActivity(), pedidos);
                        listView.setAdapter(adapter);
                        alertDialog.dismiss();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();

                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                params.put("imei", telephonyManager.getDeviceId());

                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private boolean parseJSON(String json) {

        try {
            Gson gson = new Gson();
            pedidos = gson.fromJson(json, ListPedidoEstado.class);

            return true;
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return false;

    }

}
