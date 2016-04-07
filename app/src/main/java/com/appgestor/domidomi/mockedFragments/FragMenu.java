package com.appgestor.domidomi.mockedFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActProductAdd;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterComentario;
import com.appgestor.domidomi.Adapters.ExpandableListAdapter;
import com.appgestor.domidomi.Adapters.ExpandableListDataPump;
import com.appgestor.domidomi.Entities.Comentario;
import com.appgestor.domidomi.Entities.InformacioCompania;
import com.appgestor.domidomi.Entities.ListComentarios;
import com.appgestor.domidomi.Entities.Sede;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appgestor.domidomi.Entities.Menu.getMenuListStatic;
import static com.appgestor.domidomi.Entities.Producto.setProductoStatic;
import static com.appgestor.domidomi.Entities.Sede.getSedeStaticNew;

public class FragMenu extends BaseVolleyFragmentSoport {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    protected View rootView;
    private int operador;
    private TextView nombreEmpresa;
    private TextView nit;
    //private TextView direccion;
    private TextView telefono;
    private TextView celular;

    private TextView txtNombreSede;
    //private TextView txtDireccion;
    private TextView txtTiempo;
    private TextView txtCosto;
    //private TextView txtMedioPago;
    private ImageView imgEmpresa;
    protected DecimalFormat format;

    private TableLayout tabla;
    private SwipeMenuListView listView;
    private InformacioCompania comData;
    private ListComentarios comentarios;
    private AdapterComentario adapter;

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

                txtNombreSede = (TextView) rootView.findViewById(R.id.txtNombreSede);
                //txtDireccion = (TextView) rootView.findViewById(R.id.txtDireccion);
                txtTiempo = (TextView) rootView.findViewById(R.id.txtTiempoespera);
                txtCosto = (TextView) rootView.findViewById(R.id.txtCosto);
                //txtMedioPago = (TextView) rootView.findViewById(R.id.txtMediosPago);
                imgEmpresa = (ImageView) rootView.findViewById(R.id.imgEmpresa);

                expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_informacion, container, false);

                nombreEmpresa = (TextView) rootView.findViewById(R.id.txtNombreEmpresa);
                nit = (TextView) rootView.findViewById(R.id.txtNit);
                //direccion = (TextView) rootView.findViewById(R.id.textDireccion);
                telefono = (TextView) rootView.findViewById(R.id.txtTelefono);
                celular = (TextView) rootView.findViewById(R.id.txtCelulat);
                tabla = (TableLayout) rootView.findViewById(R.id.myTable);

                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
                listView = (SwipeMenuListView) rootView.findViewById(R.id.listView);
                break;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (operador) {
            case 0:

                format = new DecimalFormat("#,###.##");
                CargarImagen();
                txtNombreSede.setText(getSedeStaticNew().getDescripcion());
                //txtDireccion.setText(getSedeStaticNew().getDireccion());
                txtTiempo.setText(String.format("Tiempo de Entrega: %s aprox", getSedeStaticNew().getTiempoEnvio()));
                txtCosto.setText(String.format("Costo de Envío: $ %s", format.format(getSedeStaticNew().getCosenvio())));

                String mediosPagos = "";

                /*if (getSedeStaticNew() != null){
                    if (getSedeStaticNew().getMedioPagoList() != null){
                        for(int f = 0; f < getSedeStaticNew().getMedioPagoList().size(); f++) {
                            mediosPagos = mediosPagos +" | "+ getSedeStaticNew().getMedioPagoList().get(f).getDescripcion();
                        }
                    }
                }*/

                //txtMedioPago.setText(String.format("Medios de Pago: %s", mediosPagos));

                setExpandableListView();

                break;
            case 1:
                cargaInformacionE();
                break;
            case 2:
                getComentarios();

                break;
        }
    }

    private void CargarImagen() {
        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);
        //Setup options for ImageLoader so it will handle caching for us.
        DisplayImageOptions options1 = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        ImageLoadingListener listener = new ImageLoadingListener(){
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            }
        };

        imageLoader1.displayImage(getSedeStaticNew().getImgEmpresa(), imgEmpresa, options1, listener);
    }

    private void getComentarios() {
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "getComentarios");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        parseJSONGet(response);
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
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                params.put("sede", String.valueOf(getSedeStaticNew().getIdsedes()));

                return params;
            }
        };
        addToQueue(jsonRequest);
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

                                nombreEmpresa.setText(comData.getDescripcion());
                                nit.setText(comData.getNit());
                                //direccion.setText(String.format("Dir: %1$s", comData.getDireccion()));
                                telefono.setText(String.format("Tel: %1$s", comData.getTelefono()));
                                celular.setText(String.format("Cel: %1$s", comData.getCelular()));

                                if(comData.getSedes() != null || comData.getSedes().size() > 1){
                                    setAdapterSedes(comData.getSedes());
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
                        //onConnectionFailed(error.toString());
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<>();
                params.put("codigo", String.valueOf(getSedeStaticNew().getIdempresa()));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    public void setAdapterSedes(ArrayList<Sede> hijos){

        for (int i = 0; i < hijos.size(); i++){
            TableRow currentRow = new TableRow(getContext());
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TextView currentText = new TextView(getContext());
            currentText.setText(hijos.get(i).getDescripcion());
            currentText.setTextSize(12);
            currentText.setTextColor(Color.BLACK);

            currentRow.setLayoutParams(params);
            currentRow.addView(currentText);

            tabla.addView(currentRow);
        }

    }

    private void setExpandableListView() {
        // response
        new AsyncTask<String[], Long, Long>(){
            @Override
            protected Long doInBackground(String[]... params) {
                expandableListDetail = ExpandableListDataPump.getData(getMenuListStatic());
                expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
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
                expandableListAdapter = new ExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
                if (expandableListTitle.size() > 0)
                    expandableListView.expandGroup(0);

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

                        setProductoStatic(getMenuListStatic().get(groupPosition).getProductos().get(childPosition));

                        startActivity(new Intent(getActivity(), ActProductAdd.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        return false;
                    }
                });

            }
        }.execute();
    }

    private void parseJSON2(String json) {

        if (json != null || json.length() > 0 && !json.equals("[null]")) {
            try {
                Gson gson = new Gson();
                comData = gson.fromJson(json, InformacioCompania.class);

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }else{
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private boolean parseJSONGet(String json) {

        if (json != null && json.length() > 0) {
            try {
                Gson gson = new Gson();
                comentarios = gson.fromJson(json, ListComentarios.class);

                adapter = new AdapterComentario(getActivity(), comentarios);
                listView.setAdapter(adapter);

                return true;
            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }else{
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return false;

    }

}
