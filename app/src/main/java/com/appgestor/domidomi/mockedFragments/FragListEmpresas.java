package com.appgestor.domidomi.mockedFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActMenu;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterRecyclerSedesEmpresa;
import com.appgestor.domidomi.Adapters.RecyclerItemClickListener;
import com.appgestor.domidomi.Entities.LisMenuData;
import com.appgestor.domidomi.Entities.ListSede;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.RadarView.RandomTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.Menu.setMenuListStatic;
import static com.appgestor.domidomi.Entities.Sede.setSedeStaticNew;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.getLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.getLongitudStatic;

public class FragListEmpresas extends BaseVolleyFragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recycler;
    private RandomTextView randomTextView;
    private RelativeLayout relativeLayout_radar;
    private RelativeLayout relativeLayout_sedes;
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

        relativeLayout_radar = (RelativeLayout) myView.findViewById(R.id.relativeLayout_radar);
        relativeLayout_sedes = (RelativeLayout) myView.findViewById(R.id.relativeLayout_sedes);

        randomTextView = (RandomTextView) myView.findViewById(R.id.random_textview);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        /*randomTextView.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
            @Override
            public void onRippleViewClicked(View view) {
                //ActivitySedes.this.startActivity(new Intent(ActivitySedes.this, RefreshProgressActivity.class));
            }
        });*/

        recycler = (RecyclerView) myView.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupGrid();

    }

    private void setupGrid() {
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
                        startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "ERROR"));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("latitud", String.valueOf(getLatitudStatic()));
                params.put("longitud", String.valueOf(getLongitudStatic()));

                return params;
            }
        };
        addToQueue(jsonRequest);
    }




    private boolean parseJSON(String json) {
        if (!json.equals("[]")){
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                final ListSede listSedes = gson.fromJson(json, ListSede.class);

                /*for (int i = 0; i < listSedes.size(); i++) {
                    randomTextView.addKeyWord(listSedes.get(i).getDescripcion());
                }

                randomTextView.show();*/

                Animation out = AnimationUtils.makeOutAnimation(getActivity(), true);
                relativeLayout_radar.startAnimation(out);
                relativeLayout_radar.setVisibility(View.GONE);

                adapter = new AdapterRecyclerSedesEmpresa(getActivity(), listSedes);
                recycler.setAdapter(adapter);
                relativeLayout_sedes.setVisibility(View.VISIBLE);
                /*new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        try {


                        }catch (Exception e){
                            Log.d("SedesEmpresas", e.getMessage());
                        }
                    }
                }, 2 * 900);*/
                recycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Date date = new Date();
                        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

                        if (isHourInInterval(hourdateFormat.format(date).toString(), listSedes.get(position).getHorainicio(), listSedes.get(position).getHorafinal())) {

                            setSedeStaticNew(listSedes.get(position));
                            /**/
                            getDataSedeDem(listSedes.get(position).getIdsedes());

                        } else {
                            Toast.makeText(getActivity(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                        }

                    }
                }));

                return true;

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        } else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return false;
    }

    private void getDataSedeDem(final int idSede) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "dataSedesDem");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        parseJSONSEDE(response);
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
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("idsedes", String.valueOf(idSede));

                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSONSEDE(String json) {
        alertDialog.dismiss();
        if (!json.equals("[]")){
            try {

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                final LisMenuData lismenudata = gson.fromJson(json, LisMenuData.class);
                setMenuListStatic(lismenudata);
                startActivity(new Intent(getActivity(), ActMenu.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTY"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }

}
