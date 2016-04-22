package com.appgestor.domidomi.Activities;

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
import com.appgestor.domidomi.Adapters.AppAdapter;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.ProductoEditAdd;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.dark.ActivityMain;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.appgestor.domidomi.Entities.Bean.getPagina_static;

public class ActCarritoMenu extends AppCompatActivity implements View.OnClickListener{

    private AppAdapter mAdapter;
    private DBHelper mydb;
    private TextView valor_domicilio;
    private TextView total_pago;
    private SwipeMenuListView mListView;
    private DecimalFormat format;
    int clicK = 0;
    Toolbar toolbar;
    public List<ProductoEditAdd> mAppList;
    private Button pedirService;
    private String indicadorcar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_car_list);
        setContentView(R.layout.layout_car_list);
        mydb = new DBHelper(this);
        format = new DecimalFormat("#,###.##");
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Bundle reicieveParams = getIntent().getExtras();

        indicadorcar = reicieveParams.getString("paginacion");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getPagina_static().equals("menu_muchos")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fragmento", "carrito");
                    startActivity(new Intent(ActCarritoMenu.this, ActivityMain.class).putExtras(bundle));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    startActivity(new Intent(ActCarritoMenu.this, ActivityMain.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }
        });


        valor_domicilio = (TextView) findViewById(R.id.valor_domicilio);
        total_pago = (TextView) findViewById(R.id.total_pago);
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        pedirService = (Button) findViewById(R.id.pedirServices);
        pedirService.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (reicieveParams != null){
            llenarData(reicieveParams);
        }

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
                        // Edit
                        editarProducto(mAppList.get(position));
                        break;
                    case 1:
                        // delete
                        deletePrduct(position);
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

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletePrduct(position);
                return false;
            }
        });

    }

    private void editarProducto(ProductoEditAdd productoEditAdd) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(ActCarritoMenu.this, ActAddCarritoEdit.class);
        bundle.putSerializable("value", productoEditAdd);
        bundle.putString("indicador", "editar");
        bundle.putString("pagina", "editar_menu");
        intent.putExtras(bundle);
        startActivity(intent);
        //finish();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }

    private void deletePrduct(final int position){
        new MaterialDialog.Builder(ActCarritoMenu.this)
                .title("Eliminar producto")
                .content("¿Está seguro de eliminar del carrito?")
                .positiveText("Aceptar")
                .negativeText("Cancelar")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //Aceptar
                        if (!mydb.DeleteProduct(mAppList.get(position).getId_sede(), mAppList.get(position).getId_empresa(), mAppList.get(position).getAuto_incremental())) {
                            Toast.makeText(getApplicationContext(), "Problemas al eliminar el producto", Toast.LENGTH_SHORT).show();
                        } else {
                            mAppList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            sumarValoresFinales(mAppList);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        //Cancelar
                        dialog.dismiss();
                    }
                }).show();
    }

    private void llenarData(Bundle reicieveParams) {
        mAppList = mydb.selectProductoCarrito(reicieveParams.getInt("empresa"), reicieveParams.getInt("sede"));
        mAdapter = new AppAdapter(ActCarritoMenu.this, mAppList);
        mListView.setAdapter(mAdapter);
        sumarValoresFinales(mAppList);
    }

    private void sumarValoresFinales(List<ProductoEditAdd> data){

        if(data.size() > 0){
            double dValor = 0;

            for (int i = 0; i < data.size(); i++) {
                dValor = dValor + data.get(i).getValor_total();
            }

            dValor = dValor + data.get(0).getCosto_envio();

            valor_domicilio.setText(String.format("$ %s", format.format(data.get(0).getCosto_envio())));

            if (dValor > data.get(0).getValor_minimo()) {
                pedirService.setVisibility(View.VISIBLE);
            }else{
                pedirService.setVisibility(View.GONE);
                Toast.makeText(this, "El pedido no supera el valor mínimo.", Toast.LENGTH_LONG).show();
            }

            total_pago.setText(String.format("$ %s", format.format(dValor)));

        }else{
            //pedirService.setVisibility(View.GONE);
            total_pago.setText(String.format("$ %s", 0));
        }

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
        if (id == R.id.accion_add) {
            startActivity(new Intent(ActCarritoMenu.this, ActivityMain.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        clicK = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pedirServices:
                Date date = new Date();
                DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

                if (isHourInInterval(hourdateFormat.format(date).toString(), mAppList.get(0).getHora_inicial(), mAppList.get(0).getHora_final())){
                    //Abierto
                    Bundle bundle = new Bundle();
                    bundle.putInt("sede", mAppList.get(0).getId_sede());
                    bundle.putInt("empresa", mAppList.get(0).getId_empresa());
                    startActivity(new Intent(ActCarritoMenu.this, ActFinalizarPedido.class).putExtras(bundle));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else {
                    //Cerrado
                    Toast.makeText(getApplicationContext(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (getPagina_static().equals("menu_muchos")) {
            Bundle bundle = new Bundle();
            bundle.putString("fragmento", "carrito");
            startActivity(new Intent(ActCarritoMenu.this, ActivityMain.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(ActCarritoMenu.this, ActivityMain.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
}
