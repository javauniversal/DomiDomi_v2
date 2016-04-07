package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.appgestor.domidomi.Entities.HorarioEmpresa;
import com.appgestor.domidomi.Entities.ListMedioPago;
import com.appgestor.domidomi.Entities.Sede;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.ActivityMain;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.MedioPago.setMedioPagoListstatic;
import static com.appgestor.domidomi.Entities.Menu.setMenuListStatic;
import static com.appgestor.domidomi.Entities.Sede.setSedeStaticNew;

public class ActCarritoMenu extends AppCompatActivity implements View.OnClickListener{

    private AppAdapter mAdapter;
    private DBHelper mydb;
    private TextView total;
    private Button pedirService;
    private SwipeMenuListView mListView;
    private List<AddProductCar> mAppListPublico;
    private AlertDialog dialog;
    private Bundle bundleset;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private DecimalFormat format;
    int clicK = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_car);
        Intent intent = getIntent();
        bundleset = intent.getExtras();
        mydb = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(bundleset.getString("sedeNomebre"));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        format = new DecimalFormat("#,###.##");

        alertDialog = new SpotsDialog(this, R.style.Custom);

        total = (TextView) findViewById(R.id.totaltexto);
        pedirService = (Button) findViewById(R.id.pedirServices);
        pedirService.setOnClickListener(this);

        TextView domicilioAdd = (TextView) findViewById(R.id.domicilioAdd);
        domicilioAdd.setText(String.format("$ %s", format.format(bundleset.getDouble("cosEnvio"))));
        dialog = new SpotsDialog(this, R.style.Custom);
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
                openItem.setTitle("Editar");
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
                        // Editar
                        editarProducto(position);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void editarProducto(final int position) {
        final List<AddProductCar> mAppList = mydb.getProductCar(bundleset.getInt("empresa"), bundleset.getInt("sede"));
        //mAppList.get(position)

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_edit_produt, null);

        final EditText numberEdit = (EditText) dialoglayout.findViewById(R.id.editTextNumber);
        numberEdit.setText(String.format("%s", mAppList.get(position).getQuantity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Cambiar la Cantidad del Pedido");
        builder.setView(dialoglayout)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Validaciones...
                        if (isValidNumber(numberEdit.getText().toString().trim())) {
                            numberEdit.setError("Campo requerido");
                            numberEdit.requestFocus();
                        } else if (numberEdit.getText().toString().trim().equals("0")){
                            numberEdit.setError("El valor debe ser mayor a 0");
                            numberEdit.requestFocus();
                        } else {
                            if (!mydb.UpdateProduct(Integer.parseInt(numberEdit.getText().toString()), bundleset.getInt("empresa"), bundleset.getInt("sede"), mAppList.get(position))){
                                Toast.makeText(getApplicationContext(), "Problemas al Actualizar el Producto", Toast.LENGTH_SHORT).show();
                            } else {
                                llenarData();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

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

            dValor = dValor + bundleset.getDouble("cosEnvio");

            if (dValor > bundleset.getDouble("valMinimo")) {
                pedirService.setVisibility(View.VISIBLE);
            } else {
                pedirService.setVisibility(View.GONE);
                Toast.makeText(this, "El pedido no supera el valor mínimo.", Toast.LENGTH_LONG).show();
            }

            total.setText(String.format("$ %s", format.format(dValor)));

        }else{
            pedirService.setVisibility(View.GONE);
            total.setText(String.format("$ %s", 0));
        }

    }

    private void createDialog(final int position){
        new MaterialDialog.Builder(ActCarritoMenu.this)
                .title("Eliminar producto")
                .content("Esta seguro de eliminar del carrito")
                .positiveText("Aceptar")
                .negativeText("Cancelar")
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.accion_add) {
            getSedeId();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSedeId() {

        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "listSedesID");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSONID(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        alertDialog.dismiss();
                        startActivity(new Intent(ActCarritoMenu.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idsede", String.valueOf(bundleset.getInt("sede")));

                return params;
            }
        };

        rq.add(jsonRequest);

    }

    private void parseJSONID(String json) {
        if (!json.equals("[]")){
            try {
                Gson gson = new Gson();
                Sede sedeID = gson.fromJson(json, Sede.class);

                setSedeStaticNew(sedeID);
                setMenuListStatic(sedeID.getMenuList());

                startActivity(new Intent(this, ActMenu.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                alertDialog.dismiss();

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            alertDialog.dismiss();
            startActivity(new Intent(this, DetailsActivity.class).putExtra("STATE", "EMPTYP"));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pedirServices:

                if (clicK == 1)
                    return;

                clicK = 1;
                VvalidarDisponivilidad();

                break;
        }
    }

    public void VvalidarDisponivilidad(){
        String url = String.format("%1$s%2$s", getString(R.string.url_base),"ValiDisponivilidad");
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON2(response);
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

                params.put("idEmpresa", String.valueOf(bundleset.getInt("sede")));

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    private void parseJSON2(String json) {
        Gson gson = new Gson();
        if (!json.equals("[]")){
            try {

                final HorarioEmpresa horarioJsEmpresa = gson.fromJson(json, HorarioEmpresa.class);

                Date date = new Date();
                DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

                if (isHourInInterval(hourdateFormat.format(date).toString(), horarioJsEmpresa.getHorainicio(), horarioJsEmpresa.getHorafinal())) {
                    cargarMedioPago();
                } else {
                    Toast.makeText(getApplicationContext(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                    clicK = 0;
                }

            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("STATE", "EMPTY");
            startActivity(intent);
        }
    }

    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
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

    @Override
    public void onStop() {
        super.onStop();
        if (rq != null)
            rq.cancelAll(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rq != null)
            rq.cancelAll(TAG);
    }

    private void parseJSON(String json) {
        Gson gson = new Gson();
        if (!json.equals("[]")){
            try {

                final ListMedioPago listMedioPago = gson.fromJson(json, ListMedioPago.class);

                setMedioPagoListstatic(listMedioPago);

                Bundle bundle = new Bundle();
                bundle.putInt("empresa", bundleset.getInt("empresa"));
                bundle.putInt("sede", bundleset.getInt("sede"));
                bundle.putDouble("cosEnvio", bundleset.getDouble("cosEnvio"));


                startActivity(new Intent(ActCarritoMenu.this, ActFinalizarPedidoMenu.class).putExtras(bundle));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("STATE", "EMPTY");
            startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        clicK = 0;
        llenarData();
    }

    @Override
    public void onBackPressed() {

        if (!bundleset.getBoolean("indicador")) {
            startActivity(new Intent(this, ActivityMain.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        super.onBackPressed();  // optional depending on your needs

    }
}
