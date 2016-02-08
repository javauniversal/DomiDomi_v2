package com.appgestor.domidomi.mockedFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.domidomi.Activities.ActMenu;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Activities.SmoothCheckBox;
import com.appgestor.domidomi.Adapters.AdapterRecyclerSedesEmpresa;
import com.appgestor.domidomi.Adapters.RecyclerItemClickListener;
import com.appgestor.domidomi.Entities.Categoria;
import com.appgestor.domidomi.Entities.LisMenuData;
import com.appgestor.domidomi.Entities.ListSede;
import com.appgestor.domidomi.Entities.Sede;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.RadarView.RandomTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private SearchView search;
    private ListSede listSedes;
    private TextView imgFilter;
    //List<Sede> filteredList;
    List<Sede> filterList;
    private ArrayList<Categoria> mList;
    private List<Categoria> CategoriesSelecteds = new ArrayList<>();

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
        imgFilter = (TextView) myView.findViewById(R.id.imgFilter);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        search = (SearchView) myView.findViewById( R.id.search_sedes);
        search.setOnQueryTextListener(listener);

        recycler = (RecyclerView) myView.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);



        return myView;
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            filterList = getNewListFromFilter(query);

            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new AdapterRecyclerSedesEmpresa(getActivity(), filterList);
            recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            /*
            if(listSedes != null){
                filteredList = new ArrayList<>();

                for (int i = 0; i < listSedes.size(); i++) {
                    final String text = listSedes.get(i).getDescripcion().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(listSedes.get(i));
                    }
                }

                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                adapter = new AdapterRecyclerSedesEmpresa(getActivity(), filteredList);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            */

            return true;

        }
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mList == null)
                    return;

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.categoria_dialog_filter, null);
                ListView listview = (ListView) dialoglayout.findViewById(R.id.lv);

                listview.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        if (mList == null) {
                            return 0;
                        } else {
                            return mList.size();
                        }
                    }

                    @Override
                    public Object getItem(int position) {
                        return mList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        ViewHolder holder;
                        if (convertView == null) {
                            holder = new ViewHolder();
                            convertView = View.inflate(getActivity(), R.layout.item_categoria, null);
                            holder.tv = (TextView) convertView.findViewById(R.id.tv);
                            holder.cb = (SmoothCheckBox) convertView.findViewById(R.id.scb);
                            convertView.setTag(holder);
                        } else {
                            holder = (ViewHolder) convertView.getTag();
                        }

                        final Categoria categoria = mList.get(position);
                        holder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                                categoria.isChecked = isChecked;
                            }
                        });
                        holder.tv.setText(categoria.getDescipcionCategoria());
                        holder.cb.setChecked(categoria.isChecked);


                        return convertView;
                    }

                    class ViewHolder {
                        SmoothCheckBox cb;
                        TextView tv;
                    }
                });

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Categoria categoria = (Categoria) parent.getAdapter().getItem(position);
                        categoria.isChecked = !categoria.isChecked;
                        SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb);
                        checkBox.setChecked(categoria.isChecked, true);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Filtrar por Categorías");
                builder.setView(dialoglayout).setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        CategoriesSelecteds = new ArrayList<Categoria>();
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).isChecked) {
                                CategoriesSelecteds.add(mList.get(i));
                            }
                        }

                        filterList = getNewListFromFilter(search.getQuery());

                        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new AdapterRecyclerSedesEmpresa(getActivity(), filterList);
                        recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }

        });

        setupGrid();
    }

    private List<Sede> getNewListFromFilter(CharSequence query) {

        query = query.toString().toLowerCase();

        List<Sede> filteredListCategoria = new ArrayList<>();

        if (CategoriesSelecteds == null) {
            CategoriesSelecteds = new ArrayList<Categoria>();
        }

        if (listSedes == null){
            listSedes = new ListSede();
        }

        if (CategoriesSelecteds.size() > 0 && !query.toString().equals("")) {
            for (int i = 0; i < listSedes.size(); i++) {
                for (int g = 0; g < CategoriesSelecteds.size(); g++) {
                    if (listSedes.get(i).getIdcategoria() == CategoriesSelecteds.get(g).getIdcategoria()
                            || listSedes.get(i).getDescripcion().toLowerCase().contains(query)) {
                        filteredListCategoria.add(listSedes.get(i));
                    }
                }
            }
        }
        else if (CategoriesSelecteds.size() < 1 && !query.toString().equals("")) {
            for (int i = 0; i < listSedes.size(); i++) {
                if (listSedes.get(i).getDescripcion().toLowerCase().contains(query)) {
                    filteredListCategoria.add(listSedes.get(i));
                }
            }
        }
        else if (CategoriesSelecteds.size() > 0 && query.toString().equals("")) {
            for (int i = 0; i < listSedes.size(); i++) {
                for (int g = 0; g < CategoriesSelecteds.size(); g++) {
                    if (listSedes.get(i).getIdcategoria() == CategoriesSelecteds.get(g).getIdcategoria()) {
                        filteredListCategoria.add(listSedes.get(i));
                    }
                }
            }
        }
        else {
            filteredListCategoria = listSedes;
        }

        return filteredListCategoria;
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
                listSedes = gson.fromJson(json, ListSede.class);

                mList = new ArrayList<>();
                boolean Find;

                for (int i = 0; i < listSedes.size(); i++) {
                    Find = false;
                    if (mList != null){
                        for (int j = 0; j < mList.size(); j++) {
                            if (mList.get(j).getIdcategoria() == listSedes.get(i).getIdcategoria()){
                                Find = true;
                                break;
                            }
                        }
                    }
                    if (!Find) {
                        mList.add(new Categoria(listSedes.get(i).getIdcategoria(), listSedes.get(i).getDescipcionCategoria()));
                    }
                }

                if (mList != null && mList.size() > 0) {
                    Collections.sort(mList, new SortBasedOnCatDescription());
                }

                Animation out = AnimationUtils.makeOutAnimation(getActivity(), true);
                relativeLayout_radar.startAnimation(out);
                relativeLayout_radar.setVisibility(View.GONE);

                adapter = new AdapterRecyclerSedesEmpresa(getActivity(), listSedes);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                relativeLayout_sedes.setVisibility(View.VISIBLE);

                recycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Date date = new Date();
                        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

                        if (filterList != null && filterList.size() > 0){
                            if (isHourInInterval(hourdateFormat.format(date).toString(), filterList.get(position).getHorainicio(), filterList.get(position).getHorafinal())) {
                                setSedeStaticNew(filterList.get(position));
                                getDataSedeDem(filterList.get(position).getIdsedes());
                            } else {
                                Toast.makeText(getActivity(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (isHourInInterval(hourdateFormat.format(date).toString(), listSedes.get(position).getHorainicio(), listSedes.get(position).getHorafinal())) {
                                setSedeStaticNew(listSedes.get(position));
                                getDataSedeDem(listSedes.get(position).getIdsedes());
                            } else {
                                Toast.makeText(getActivity(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));

                return true;

            }catch (IllegalStateException ex) {
                //ex.printStackTrace();
            }

        } else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTYI"));
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

            } catch (IllegalStateException ex) {
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

    public class SortBasedOnCatDescription implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            Categoria dd1 = (Categoria) o1;// where FBFriends_Obj is your object class
            Categoria dd2 = (Categoria) o2;
            return dd1.getDescipcionCategoria().compareToIgnoreCase(dd2.getDescipcionCategoria());//where uname is field name
        }

    }

}
