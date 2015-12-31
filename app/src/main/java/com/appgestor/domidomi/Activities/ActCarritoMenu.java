package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.Adapters.AppAdapter;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.ListMedioPago;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.MedioPago.setMedioPagoListstatic;

public class ActCarritoMenu extends AppCompatActivity implements View.OnClickListener{

    private AppAdapter mAdapter;
    private DBHelper mydb;
    private TextView total;
    private Button pedirService;
    private SwipeMenuListView mListView;
    private List<AddProductCar> mAppListPublico;
    private AlertDialog dialog;
    private Bundle bundleset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_car);
        Intent intent = getIntent();
        bundleset = intent.getExtras();
        mydb = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(bundleset.getString("sedeNomebre"));
        toolbar.setNavigationIcon(R.mipmap.ic_action_cartw);
        setSupportActionBar(toolbar);

        total = (TextView) findViewById(R.id.totaltexto);
        pedirService = (Button) findViewById(R.id.pedirServices);
        pedirService.setOnClickListener(this);

        dialog = new SpotsDialog(ActCarritoMenu.this);
        dialog.show();

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        llenarData();

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //AddProductCar item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        createDialog(position);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(position);
                return false;
            }
        });

    }

    private void llenarData() {
        List<AddProductCar> mAppList = mydb.getProductCar(bundleset.getInt("empresa"), bundleset.getInt("sede"));
        mAdapter = new AppAdapter(ActCarritoMenu.this, mAppList);
        mListView.setAdapter(mAdapter);
        sumarValoresFinales(mAppList);
        mAppListPublico = mAppList;
        dialog.dismiss();
    }

    private void sumarValoresFinales(List<AddProductCar> data){

        if(data.size() > 0){
            double dValor = 0;

            for (int i = 0; i < data.size(); i++) {
                dValor = dValor + data.get(i).getValueoverall();
                //dValor = dValor * data.get(i).getQuantity();
            }

            dValor = dValor + 2500;
            if (dValor > 20000) {
                pedirService.setVisibility(View.VISIBLE);
            }else{
                pedirService.setVisibility(View.GONE);
            }
            total.setText( "Total: $"+dValor);
        }else{
            pedirService.setVisibility(View.GONE);
        }

    }

    private void createDialog(final int position){
        new MaterialDialog.Builder(ActCarritoMenu.this)
                .title("Eliminar producto")
                .content("Esta seguro de eliminar del carrito")
                .positiveText("Aceptar")
                .negativeText("Cancelar")
                .backgroundColor(getResources().getColor(R.color.color_gris))
                .positiveColor(getResources().getColor(R.color.color_negro))
                .negativeColor(getResources().getColor(R.color.color_negro))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //Aceptar
                        if (!mydb.DeleteProduct(mAppListPublico.get(position).getIdProduct(), mAppListPublico.get(position).getIdcompany())) {
                            Toast.makeText(getApplicationContext(), "Problemas al eliminar el producto", Toast.LENGTH_SHORT).show();
                        } else {
                            mAppListPublico.remove(position);
                            mAdapter.notifyDataSetChanged();
                            sumarValoresFinales(mAppListPublico);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        //Cancelar
                        dialog.dismiss();
                    }
                }).show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        /*
        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pedirServices:

                cargarMedioPago();

                break;
        }
    }


    public void cargarMedioPago(){
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"getMedioPago");
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
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("STATE", "ERROR");
                        startActivity(intent);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idsedes", String.valueOf(bundleset.getInt("sede")));

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

                final ListMedioPago listMedioPago = gson.fromJson(json, ListMedioPago.class);

                setMedioPagoListstatic(listMedioPago);

                Bundle bundle = new Bundle();
                bundle.putInt("empresa", bundleset.getInt("empresa"));
                bundle.putInt("sede", bundleset.getInt("sede"));
                startActivity(new Intent(ActCarritoMenu.this, ActFinalizarPedidoMenu.class).putExtras(bundle));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
                indicant = false;
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("STATE", "EMPTY");
            startActivity(intent);
        }

        return indicant;

    }
}