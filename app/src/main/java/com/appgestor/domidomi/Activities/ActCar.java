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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.Adapters.AppAdapter;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.appgestor.domidomi.Entities.Empresas.getEmpresastatic;
import static com.appgestor.domidomi.Entities.Sede.getSedeStatic;

public class ActCar extends AppCompatActivity implements View.OnClickListener {

    private AppAdapter mAdapter;
    private DBHelper mydb;
    private TextView total;
    private Button pedirService;
    private SwipeMenuListView mListView;
    private List<AddProductCar> mAppListPublico;
    private DecimalFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_car);

        mydb = new DBHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getEmpresastatic().getDescripcion());
        toolbar.setNavigationIcon(R.mipmap.ic_action_cartw);
        setSupportActionBar(toolbar);

        format = new DecimalFormat("#,###.##");

        total = (TextView) findViewById(R.id.totaltexto);
        TextView domicilioAdd = (TextView) findViewById(R.id.domicilioAdd);
        pedirService = (Button) findViewById(R.id.pedirServices);
        pedirService.setOnClickListener(this);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        domicilioAdd.setText(String.format("Domicilio: $%s", format.format(getSedeStatic().getCosenvio())));

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
                        // Edit
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

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(position);
                return false;
            }
        });

    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

    private void editarProducto(final int position) {
        final List<AddProductCar> mAppList = mydb.getProductCar(getSedeStatic().getIdempresa(), getSedeStatic().getIdsedes());
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
                            if (!mydb.UpdateProduct(Integer.parseInt(numberEdit.getText().toString()), getSedeStatic().getIdempresa(), getSedeStatic().getIdsedes(), mAppList.get(position))){
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

    private void llenarData() {
        List<AddProductCar> mAppList = mydb.getProductCar(getSedeStatic().getIdempresa(), getSedeStatic().getIdsedes());
        mAdapter = new AppAdapter(ActCar.this, mAppList);
        mListView.setAdapter(mAdapter);
        sumarValoresFinales(mAppList);
        mAppListPublico = mAppList;
    }

    private void sumarValoresFinales(List<AddProductCar> data){

        if(data.size() > 0){
            double dValor = 0;

            for (int i = 0; i < data.size(); i++) {
                dValor = dValor + data.get(i).getValueoverall();
                //dValor = dValor * data.get(i).getQuantity();
            }

            dValor = dValor + getSedeStatic().getCosenvio();

            if (dValor > getEmpresastatic().getValorMin()) {
                pedirService.setVisibility(View.VISIBLE);
            }else{
                pedirService.setVisibility(View.GONE);
            }

            total.setText(String.format("Total: $%s", format.format(dValor)));

        }else{
            pedirService.setVisibility(View.GONE);
        }

    }

    private void createDialog(final int position){
        new MaterialDialog.Builder(ActCar.this)
                .title("Eliminar producto")
                .content("Esta seguro de eliminar del carrito?")
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

    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pedirServices:
                Date date = new Date();
                DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

                if (isHourInInterval(hourdateFormat.format(date).toString(), getEmpresastatic().getHorainicio(), getEmpresastatic().getHorafinal())){
                    //Abierto
                    startActivity(new Intent(ActCar.this, ActFinalizarPedido.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else {
                    //Cerrado
                    Toast.makeText(getApplicationContext(), "El establecimiento se encuentra Cerrado", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

}
