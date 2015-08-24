package com.appgestor.domidomi.mockedFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActProductAdd;
import com.appgestor.domidomi.Adapters.AdapterSedes;
import com.appgestor.domidomi.Adapters.ExpandableListAdapter;
import com.appgestor.domidomi.Adapters.ExpandableListDataPump;
import com.appgestor.domidomi.Entities.CompaniaList;
import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.Entities.InformacioCompania;
import com.appgestor.domidomi.Entities.MasterItem2;
import com.appgestor.domidomi.Entities.MenuList;
import com.appgestor.domidomi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragMenu extends BaseVolleyFragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    protected View rootView;
    private int operador;
    private MenuList menu = null;
    private TextView nombreEmpresa;
    private TextView nit;
    private TextView direccion;
    private TextView telefono;
    private TextView celular;
    private TableLayout tabla;
    private InformacioCompania infor;
    private AdapterSedes adapterSedes;

    public static FragMenu newInstance(Bundle param1) {
        FragMenu fragment = new FragMenu();
        fragment.setArguments(param1);
        return fragment;
    }

    public FragMenu() {
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
                rootView = inflater.inflate(R.layout.fragment_menu, container, false);
                expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_informacion, container, false);
                nombreEmpresa = (TextView) rootView.findViewById(R.id.txtNombreEmpresa);
                nit = (TextView) rootView.findViewById(R.id.txtNit);
                direccion = (TextView) rootView.findViewById(R.id.textDireccion);
                telefono = (TextView) rootView.findViewById(R.id.txtTelefono);
                celular = (TextView) rootView.findViewById(R.id.txtCelulat);
                tabla = (TableLayout) rootView.findViewById(R.id.myTable);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (operador) {
            case 0:

                break;
            case 1:

                break;
            case 2:
                break;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (operador) {
            case 0:
                setExpandableListView();
                break;
            case 1:
                cargaInformacionE();
                break;
            case 2:
                break;
        }
    }

    private void cargaInformacionE(){
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"getInformation");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        // response
                        new AsyncTask<String[], Long, Long>(){
                            @Override
                            protected Long doInBackground(String[]... params) {
                                parseJSON2(response);
                                return null;
                            }
                            protected void onPreExecute() {
                                //multiColumnList.setVisibility(View.GONE);
                            }
                            @Override
                            public void onProgressUpdate(Long... value) {

                            }
                            @Override
                            protected void onPostExecute(Long result){
                                nombreEmpresa.setText(infor.getNombre());
                                nit.setText(infor.getNit());
                                direccion.setText(String.format("Dir: %1$s", infor.getDireccion()));
                                telefono.setText(String.format("Tel: %1$s", infor.getTelefono()));
                                celular.setText(String.format("Cel: %1$s", infor.getCelular()));

                                if(infor.getHijos() != null){

                                    setAdapterSedes(infor.getHijos());

                                    //adapterSedes = new AdapterSedes(getActivity(), infor.getHijos());
                                    //sedes.setAdapter(adapterSedes);
                                }
                            }
                        }.execute();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                params.put("codigo", String.valueOf(Companias.getCodigoS()));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    public void setAdapterSedes(ArrayList<MasterItem2> hijos){

        for (int i = 0; i < hijos.size(); i++){
            TableRow currentRow = new TableRow(getContext());
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TextView currentText = new TextView(getContext());
            currentText.setText(hijos.get(i).getDescripcion());
            currentText.setTextSize(11);
            currentText.setTextColor(Color.BLACK);

            currentRow.setLayoutParams(params);
            currentRow.addView(currentText);

            tabla.addView(currentRow);
        }

    }

    private void setExpandableListView() {
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"listMenuProduc");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        // response
                        new AsyncTask<String[], Long, Long>(){
                            @Override
                            protected Long doInBackground(String[]... params) {
                                parseJSON(response);
                                return null;
                            }
                            protected void onPreExecute() {
                                //multiColumnList.setVisibility(View.GONE);
                            }
                            @Override
                            public void onProgressUpdate(Long... value) {

                            }
                            @Override
                            protected void onPostExecute(Long result){
                                //Clic Titulo.
                                expandableListAdapter = new ExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
                                expandableListView.setAdapter(expandableListAdapter);
                                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                    @Override
                                    public void onGroupExpand(int groupPosition) {
                                        //Toast.makeText(getActivity(),expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Cerrar lista desplegable.
                                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                                    @Override
                                    public void onGroupCollapse(int groupPosition) {
                                        //Toast.makeText(getActivity(), expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Clic en el elemento hijo.
                                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                                        //Toast.makeText(getActivity(), menu.get(groupPosition).getHijos().get(childPosition).getCodigo()+"", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        //setProductDescripStatic(menu.get(groupPosition).getHijos().get(childPosition));
                                        bundle.putInt("codeproduct", menu.get(groupPosition).getHijos().get(childPosition).getCodigo());
                                        bundle.putString("descripcion", menu.get(groupPosition).getHijos().get(childPosition).getDescripcion());
                                        bundle.putString("ingredientes", menu.get(groupPosition).getHijos().get(childPosition).getIngredientes());
                                        bundle.putDouble("precio", menu.get(groupPosition).getHijos().get(childPosition).getPrecio());
                                        bundle.putString("foto", menu.get(groupPosition).getHijos().get(childPosition).getFoto());

                                        startActivity(new Intent(getActivity(), ActProductAdd.class).putExtras(bundle));
                                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                        return false;
                                    }
                                });

                            }
                        }.execute();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                params.put("codigo", String.valueOf(Companias.getCodigoS()));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSON(String json) {
        if (json != null && json.length() > 0) {
            try {
                Gson gson = new Gson();
                menu = gson.fromJson(json, MenuList.class);
                expandableListDetail = ExpandableListDataPump.getData(menu);
                expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void parseJSON2(String json) {

        if (json != null && json.length() > 0) {
            try {
                Gson gson = new Gson();
                CompaniaList comData = gson.fromJson(json, CompaniaList.class);
                for (int i = 0; i < comData.size(); i++) {
                    infor = new InformacioCompania(comData.get(i).getNombre(), comData.get(i).getNit(),
                            comData.get(i).getDireccion(), comData.get(i).getTelefono(), comData.get(i).getCelular(),
                            comData.get(i).getHijos());
                }

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

    }


}
