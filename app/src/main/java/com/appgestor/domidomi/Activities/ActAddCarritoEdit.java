package com.appgestor.domidomi.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.Adiciones;
import com.appgestor.domidomi.Entities.ProductoEditAdd;
import com.appgestor.domidomi.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActAddCarritoEdit extends AppCompatActivity {

    private ProductoEditAdd productoEditAdd = new ProductoEditAdd();
    private TextView precio;
    private TextView ingredientes;
    private DecimalFormat format;
    private LinearLayout root;
    private QuantityView cantidad;
    private TextView txt_valor_prodocto;
    private TextView txt_valor_adicioness;
    private TextView txt_valor_total;
    private TextView total_final_oculto;
    private TextView EditComment;
    private Double totalfinal = null;
    private Double totalAdicion = 0.0;
    private DBHelper mydb;
    private String indicador_accion;
    private String pagina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carrito_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mydb = new DBHelper(this);
        format = new DecimalFormat("#,###.##");

        KenBurnsView imagen_producto = (KenBurnsView) findViewById(R.id.displayImagen);
        TextView nombre_producto = (TextView) findViewById(R.id.nombre_producto);
        precio = (TextView) findViewById(R.id.precio);
        ingredientes = (TextView) findViewById(R.id.ingredientes);
        cantidad = (QuantityView) findViewById(R.id.quantityView_default);

        txt_valor_prodocto = (TextView) findViewById(R.id.txt_valor_prodocto);
        txt_valor_adicioness = (TextView) findViewById(R.id.txt_valor_adicioness);
        txt_valor_total = (TextView) findViewById(R.id.txt_valor_total);
        total_final_oculto = (TextView) findViewById(R.id.totalFinalOculto);
        EditComment = (TextView) findViewById(R.id.EditComment);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        root = (LinearLayout) findViewById(R.id.liner_adiciones_dinamic); //or whatever your root control is

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        productoEditAdd = (ProductoEditAdd) bundle.getSerializable("value");

        indicador_accion = bundle.getString("indicador");
        pagina = bundle.getString("pagina");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicGuardar(productoEditAdd);
            }
        });

        //SET datos de la Actividad.
        cargarImagen(imagen_producto, productoEditAdd.getFoto());
        cargarInformacion(nombre_producto, precio, ingredientes, productoEditAdd);
        cargarAdiciones(productoEditAdd);
        cargarValoresTotales(txt_valor_prodocto, txt_valor_adicioness, txt_valor_total, productoEditAdd, total_final_oculto);

        cantidad.setValorTotal(productoEditAdd, txt_valor_total, total_final_oculto, root, txt_valor_prodocto, txt_valor_adicioness);

        if (indicador_accion.equals("nuevo")) {
            toolbar.setTitle("Agregar Producto");
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_shopping_cart_white_24dp));
        } else {
            toolbar.setTitle("Editar Producto");
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_border_color_white_24dp));
            EditComment.setText(productoEditAdd.getComentario());
            cantidad.setQuantity(productoEditAdd.getCantidad());
            cantidad.cargarValorCantidad();

            totalAdicion = loopQuestions(root, totalAdicion);

        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private Double loopQuestions(ViewGroup parent, Double CurrentSum) {
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if(child instanceof CheckBox) {
                //Support for Checkboxes
                CheckBox cb = (CheckBox)child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    for(int f = 0; f < productoEditAdd.getAdicionesList().size(); f++) {
                        if(cb.getId() == productoEditAdd.getAdicionesList().get(f).getIdadicionales()){
                            CurrentSum += (productoEditAdd.getAdicionesList().get(f).getValor()) * productoEditAdd.getAdicionesList().get(f).getCantidadAdicion();
                            break;
                        }
                    }
                }
            }

            if(child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup)child;
                CurrentSum = loopQuestions(group, CurrentSum);
            }
        }

        return CurrentSum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_cart) {
            Bundle bundle = new Bundle();
            bundle.putInt("sede", productoEditAdd.getId_sede());
            bundle.putInt("empresa", productoEditAdd.getId_empresa());

            if (pagina.equals("editar")){
                startActivity(new Intent(ActAddCarritoEdit.this, ActCar.class).putExtras(bundle));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                startActivity(new Intent(ActAddCarritoEdit.this, ActCarritoMenu.class).putExtras(bundle));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void cargarValoresTotales(TextView txt_valor_prodocto, TextView txt_valor_adicioness, TextView txt_valor_total, ProductoEditAdd productoEditAdd, TextView total_final_oculto) {
        txt_valor_prodocto.setText(String.format("$ %s", format.format(productoEditAdd.getPrecio())));
        txt_valor_adicioness.setText(String.format("$ %s", format.format(0)));
        txt_valor_total.setText(String.format("$ %s",format.format(productoEditAdd.getPrecio())));
        total_final_oculto.setText(String.format("%s", productoEditAdd.getPrecio()));
    }

    private void clicGuardar(final ProductoEditAdd productoEditAdd){
        if(cantidad.getQuantity() < 1) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
        }else if(productoEditAdd.getCantidad() == 0) {
            Toast.makeText(this, "El producto se encuentra agotado", Toast.LENGTH_LONG).show();
        }else{

            if (indicador_accion.equals("nuevo")) {
                new MaterialDialog.Builder(this)
                        .title(R.string.titulo_confirmacion_pedido)
                        .content(R.string.contenido_confirmacion_pedido)
                        .positiveText(R.string.realizar_pedido)
                        .negativeText(R.string.add_producto)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                //Realizar pedido
                                if (GuardarPedido()) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("sede", productoEditAdd.getId_sede());
                                    bundle.putInt("empresa", productoEditAdd.getId_empresa());

                                    //bundle.putString("paginacion", "sin_menu");
                                    startActivity(new Intent(ActAddCarritoEdit.this, ActCar.class).putExtras(bundle));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                }
                            }
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                //Agregar mas productos
                                if (GuardarPedido()) {
                                    finish();
                                }
                            }
                        }).show();
            } else {
                if (GuardarPedido()) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("sede", productoEditAdd.getId_sede());
                    bundle.putInt("empresa", productoEditAdd.getId_empresa());

                    if (pagina.equals("editar")){
                        startActivity(new Intent(ActAddCarritoEdit.this, ActCar.class).putExtras(bundle));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        startActivity(new Intent(ActAddCarritoEdit.this, ActCarritoMenu.class).putExtras(bundle));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                }
            }
        }
    }

    private boolean GuardarPedido(){

        ProductoEditAdd addProductCar = new ProductoEditAdd();

        addProductCar.setAuto_incremental(productoEditAdd.getAuto_incremental());
        addProductCar.setCodigo_producto(productoEditAdd.getCodigo_producto());
        addProductCar.setDescripcion(productoEditAdd.getDescripcion());
        addProductCar.setIngredientes(productoEditAdd.getIngredientes());
        Double temp = Double.valueOf(total_final_oculto.getText().toString());
        addProductCar.setPrecio(productoEditAdd.getPrecio());
        addProductCar.setCantidad(cantidad.getQuantity());
        addProductCar.setFoto(productoEditAdd.getFoto());
        addProductCar.setEstado(productoEditAdd.getEstado());
        addProductCar.setIdmenumovil(productoEditAdd.getIdmenumovil());
        addProductCar.setAdicionesList(productoEditAdd.getAdicionesList());
        addProductCar.setValor_unitario(productoEditAdd.getPrecio());
        addProductCar.setValor_total(temp);
        addProductCar.setComentario(EditComment.getText().toString());
        addProductCar.setId_empresa(productoEditAdd.getId_empresa());
        addProductCar.setId_sede(productoEditAdd.getId_sede());
        addProductCar.setNombre_sede(productoEditAdd.getNombre_sede());
        addProductCar.setHora_inicial(productoEditAdd.getHora_inicial());
        addProductCar.setHora_final(productoEditAdd.getHora_final());
        addProductCar.setCosto_envio(productoEditAdd.getCosto_envio());
        addProductCar.setValor_minimo(productoEditAdd.getValor_minimo());

        if(productoEditAdd.getAdicionesList() != null && productoEditAdd.getAdicionesList().size() > 0){
            List<Adiciones> adicionesList = new ArrayList<>();

            addProductCar.setAdicionesListselet(loopQuestions(root, adicionesList));
        }

        if (indicador_accion.equals("nuevo")) {
            if (mydb.insertProductoCarrito(addProductCar)){
                //Guardado
                return true;
            }else{
                //Problemas
                Toast.makeText(ActAddCarritoEdit.this, "Problemas al guardar en el carrito.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {

            if (mydb.updateProducto(addProductCar)){
                return true;
            } else {
                Toast.makeText(ActAddCarritoEdit.this, "Problemas al Actualizar en el carrito.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    private  List<Adiciones> loopQuestions(ViewGroup parent, List<Adiciones> CurrentSum) {
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if(child instanceof CheckBox) {
                //Support for Checkboxes
                CheckBox cb = (CheckBox)child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    for(int f = 0; f < productoEditAdd.getAdicionesList().size(); f++) {
                        if(cb.getId() == productoEditAdd.getAdicionesList().get(f).getIdadicionales()){
                            CurrentSum.add(
                                    new Adiciones(cb.getId(),
                                            productoEditAdd.getAdicionesList().get(f).getDescripcion(),
                                            productoEditAdd.getAdicionesList().get(f).getTipo(),
                                            productoEditAdd.getAdicionesList().get(f).getValor(),
                                            productoEditAdd.getAdicionesList().get(f).getEstado(),
                                            productoEditAdd.getAdicionesList().get(f).getIdproductos(),
                                            productoEditAdd.getId_sede(),
                                            productoEditAdd.getId_empresa(),
                                            productoEditAdd.getAdicionesList().get(f).getCantidadAdicion(),
                                            productoEditAdd.getAdicionesList().get(f).getAutoIncrementAdicion()));
                            break;
                        }
                    }
                }
            }

            if(child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup)child;
                CurrentSum = loopQuestions(group, CurrentSum);
            }
        }

        return CurrentSum;
    }

    private void cargarAdiciones(final ProductoEditAdd productoEditAdd) {

        CardView pll = (CardView) findViewById(R.id.card_view);
        //LinearLayout lm = (LinearLayout) findViewById(R.id.llAdiciones);

        if(productoEditAdd.getAdicionesList() != null){
            pll.setVisibility(View.VISIBLE);

            int adiciones = productoEditAdd.getAdicionesList().size();
            for (int i = 0; i < adiciones; i++) {

                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);

                final CheckBox cb = new CheckBox(this);
                cb.setText(String.format("%1s $ %2s", productoEditAdd.getAdicionesList().get(i).getDescripcion(),format.format(productoEditAdd.getAdicionesList().get(i).getValor())));
                cb.setId(productoEditAdd.getAdicionesList().get(i).getIdadicionales());
                cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                cb.setGravity(Gravity.LEFT | Gravity.CENTER);

                cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                ll.addView(cb);

                final TextView valor = new TextView(this);
                valor.setText("0");
                valor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3.7f));
                valor.setGravity(Gravity.CENTER);
                valor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

                if (indicador_accion.equals("editar") && productoEditAdd.getAdicionesListselet() != null) {
                    for (int r = 0; r < productoEditAdd.getAdicionesListselet().size(); r++) {
                        if (productoEditAdd.getAdicionesList().get(i).getIdadicionales() == productoEditAdd.getAdicionesListselet().get(r).getIdadicionales()){
                            cb.setChecked(true);
                            valor.setText(productoEditAdd.getAdicionesListselet().get(r).getCantidadAdicion()+"");
                            productoEditAdd.getAdicionesList().get(i).setCantidadAdicion(productoEditAdd.getAdicionesListselet().get(r).getCantidadAdicion());
                            break;
                        }
                    }
                }


                ll.addView(valor);

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        final Double totaltemporal = Double.valueOf(total_final_oculto.getText().toString());

                        if (((CheckBox) v).isChecked()) {
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.dialog_edit_produt, null);

                            final EditText numberEdit = (EditText) dialoglayout.findViewById(R.id.editTextNumber);
                            numberEdit.setText(String.format("%s", 1));
                            numberEdit.setSelection(numberEdit.getText().toString().length());
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActAddCarritoEdit.this);
                            builder.setCancelable(false);
                            builder.setTitle("Digite la Cantidad");
                            builder.setView(dialoglayout)
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {

                                            if (!numberEdit.getText().toString().equals("")){
                                                if(Integer.parseInt(numberEdit.getText().toString()) > 0){
                                                    for(int f = 0; f < productoEditAdd.getAdicionesList().size(); f++) {
                                                        if(v.getId() == productoEditAdd.getAdicionesList().get(f).getIdadicionales()){
                                                            totalfinal = (totaltemporal + (productoEditAdd.getAdicionesList().get(f).getValor() * Integer.parseInt(numberEdit.getText().toString())));

                                                            totalAdicion = totalAdicion + productoEditAdd.getAdicionesList().get(f).getValor() * Integer.parseInt(numberEdit.getText().toString());

                                                            productoEditAdd.getAdicionesList().get(f).setCantidadAdicion(Integer.parseInt(numberEdit.getText().toString()));

                                                            break;
                                                        }
                                                    }

                                                    valor.setText(numberEdit.getText().toString());

                                                    txt_valor_total.setText(String.format("$ %s", format.format(totalfinal)));

                                                    total_final_oculto.setText(totalfinal+"");

                                                    txt_valor_adicioness.setText(String.format("$ %s", format.format(totalAdicion)));

                                                } else {
                                                    cb.toggle();
                                                }
                                            } else {
                                                cb.toggle();
                                            }
                                        }
                                    });

                            builder.show();
                        } else {
                            for(int j = 0; j < productoEditAdd.getAdicionesList().size(); j++) {
                                if(v.getId() == productoEditAdd.getAdicionesList().get(j).getIdadicionales()){
                                    totalfinal = (totaltemporal - (productoEditAdd.getAdicionesList().get(j).getValor() * productoEditAdd.getAdicionesList().get(j).getCantidadAdicion()));

                                    totalAdicion = totalAdicion - productoEditAdd.getAdicionesList().get(j).getValor() * productoEditAdd.getAdicionesList().get(j).getCantidadAdicion();

                                    break;
                                }
                            }

                            valor.setText("0");

                            txt_valor_total.setText(String.format("%s", format.format(totalfinal)));

                            total_final_oculto.setText(totalfinal + "");

                            txt_valor_adicioness.setText(String.format("$ %s", format.format(totalAdicion)));
                        }
                    }
                });


                root.addView(ll);
            }
        }
    }

    private void cargarInformacion(TextView nombre_producto, TextView precio, TextView ingredientes, ProductoEditAdd productoEditAdd) {
        nombre_producto.setText(productoEditAdd.getDescripcion());
        precio.setText(String.format("$ %s", format.format(productoEditAdd.getPrecio())));
        ingredientes.setText(productoEditAdd.getIngredientes());
    }

    private void cargarImagen(KenBurnsView imagen_producto, String foto) {
        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);
        //Setup options for ImageLoader so it will handle caching for us.
        DisplayImageOptions options1 = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        ImageLoadingListener listener = new ImageLoadingListener(){
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //progressBar2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                //progressBar2.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                //progressBar2.setVisibility(View.GONE);
            }
        };

        imageLoader1.displayImage(foto, imagen_producto, options1, listener);
    }

}
