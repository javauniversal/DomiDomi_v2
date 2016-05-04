package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Adiciones;
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

import static com.appgestor.domidomi.Entities.Producto.getProductoStatic;
import static com.appgestor.domidomi.Entities.Sede.getSedeStaticNew;

public class ActProductAdd extends AppCompatActivity implements View.OnClickListener {

    private KenBurnsView displayImagen;
    private QuantityView cantidad;
    private TextView totalFinal;
    private TextView totalFinalOculto;
    private DBHelper mydb;
    private EditText myComment;
    private ProgressBar progressBar2;
    private DecimalFormat format;
    private LinearLayout root;
    Double totalfinal = null;
    Double totalAdicion = 0.0;
    private TextView totalAdiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_add);

        mydb = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getProductoStatic().getDescripcion());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        displayImagen = (KenBurnsView) findViewById(R.id.displayImagen);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        CargarImagen();

        ImageView imagenAgotado = (ImageView) findViewById(R.id.inside_imageview);

        if(getProductoStatic().getCantidad() == 0)
            imagenAgotado.setImageResource(R.drawable.agotado);

        TextView descripcion = (TextView) findViewById(R.id.descripcion);
        TextView ingredientes = (TextView) findViewById(R.id.ingredientes);
        TextView precio = (TextView) findViewById(R.id.precio);

        cantidad = (QuantityView) findViewById(R.id.quantityView_default);

        totalAdiciones = (TextView) findViewById(R.id.totalAdiciones);

        TextView totalProducto = (TextView) findViewById(R.id.totalProducto);

        totalFinal = (TextView) findViewById(R.id.totalFinal);

        totalFinalOculto = (TextView) findViewById(R.id.totalFinalOculto);
        myComment = (EditText) findViewById(R.id.EditComment);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        descripcion.setText(getProductoStatic().getDescripcion());
        ingredientes.setText(getProductoStatic().getIngredientes());
        format = new DecimalFormat("#,###.##");

        precio.setText(String.format("Valor Unitario: $ %s", format.format(getProductoStatic().getPrecio())));

        totalProducto.setText(String.format("Total Productos: $ %s", format.format(getProductoStatic().getPrecio())));

        totalAdiciones.setText(String.format("Total Adiciones: $ %s", format.format(0)));

        totalFinal.setText(String.format("%s",format.format(getProductoStatic().getPrecio())));

        totalFinalOculto.setText(String.format("%s", getProductoStatic().getPrecio()));

        root = (LinearLayout) findViewById(R.id.liner_adiciones_dinamic); //or whatever your root control is

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CargarAdiciones();

        //cantidad.setValorTotal(getProductoStatic().getPrecio(), totalFinal, totalFinalOculto, root, totalProducto);

    }

    public void CargarAdiciones(){

        LinearLayout pll = (LinearLayout) findViewById(R.id.paterAdiciones);
        //LinearLayout lm = (LinearLayout) findViewById(R.id.llAdiciones);

        if(getProductoStatic().getAdicionesList() != null){
            pll.setVisibility(View.VISIBLE);

            int adiciones = getProductoStatic().getAdicionesList().size();
            for (int i = 0; i < adiciones; i++) {

                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);

                final CheckBox cb = new CheckBox(this);
                cb.setText(String.format("%1s $ %2s",getProductoStatic().getAdicionesList().get(i).getDescripcion(),format.format(getProductoStatic().getAdicionesList().get(i).getValor())));
                cb.setId(getProductoStatic().getAdicionesList().get(i).getIdadicionales());
                cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                cb.setGravity(Gravity.LEFT | Gravity.CENTER);
                cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                ll.addView(cb);

                final TextView valor = new TextView(this);
                valor.setText("0");
                valor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3.7f));
                valor.setGravity(Gravity.CENTER);
                valor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                ll.addView(valor);

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        final Double totaltemporal = Double.valueOf(totalFinalOculto.getText().toString());

                        if (((CheckBox) v).isChecked()) {
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.dialog_edit_produt, null);

                            final EditText numberEdit = (EditText) dialoglayout.findViewById(R.id.editTextNumber);
                            numberEdit.setText(String.format("%s", 1));
                            numberEdit.setSelection(numberEdit.getText().toString().length());
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActProductAdd.this);
                            builder.setCancelable(false);
                            builder.setTitle("Digite la Cantidad");
                            builder.setView(dialoglayout)
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            if (!numberEdit.getText().toString().equals("")){
                                                if(Integer.parseInt(numberEdit.getText().toString()) > 0){
                                                    for(int f = 0; f < getProductoStatic().getAdicionesList().size(); f++) {
                                                        if(v.getId() == getProductoStatic().getAdicionesList().get(f).getIdadicionales()){
                                                            totalfinal = (totaltemporal + (getProductoStatic().getAdicionesList().get(f).getValor() * Integer.parseInt(numberEdit.getText().toString())));

                                                            totalAdicion = totalAdicion + getProductoStatic().getAdicionesList().get(f).getValor() * Integer.parseInt(numberEdit.getText().toString());

                                                            getProductoStatic().getAdicionesList().get(f).setCantidadAdicion(Integer.parseInt(numberEdit.getText().toString()));

                                                            break;
                                                        }
                                                    }

                                                    valor.setText(numberEdit.getText().toString());

                                                    totalFinal.setText(String.format("%s", format.format(totalfinal)));

                                                    totalFinalOculto.setText(totalfinal+"");

                                                    totalAdiciones.setText(String.format("Total Adiciones: $ %s", format.format(totalAdicion)));

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
                            for(int j = 0; j < getProductoStatic().getAdicionesList().size(); j++) {
                                if(v.getId() == getProductoStatic().getAdicionesList().get(j).getIdadicionales()){
                                    totalfinal = (totaltemporal - (getProductoStatic().getAdicionesList().get(j).getValor() * getProductoStatic().getAdicionesList().get(j).getCantidadAdicion()));

                                    totalAdicion = totalAdicion - getProductoStatic().getAdicionesList().get(j).getValor() * getProductoStatic().getAdicionesList().get(j).getCantidadAdicion();

                                    break;
                                }
                            }

                            valor.setText("0");

                            totalFinal.setText(String.format("%s", format.format(totalfinal)));

                            totalFinalOculto.setText(totalfinal + "");

                            totalAdiciones.setText(String.format("Total Adiciones: $ %s", format.format(totalAdicion)));
                        }
                    }
                });


                root.addView(ll);


            }

        }

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
        }else if(id == R.id.action_cart){
            Bundle bundle = new Bundle();
            bundle.putInt("compania", getSedeStaticNew().getIdempresa());
            startActivity(new Intent(ActProductAdd.this, ActCar.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:

                if(cantidad.getQuantity() < 1) {
                    Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
                }else if(getProductoStatic().getCantidad() == 0) {
                    Toast.makeText(this, "El producto se encuentra agotado", Toast.LENGTH_LONG).show();
                }else if(cantidad.getQuantity() > getProductoStatic().getCantidad()) {
                    Toast.makeText(this, "La cantida que desea pedir supera la disponible", Toast.LENGTH_LONG).show();
                }else{
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
                                        bundle.putInt("compania", getSedeStaticNew().getIdsedes());
                                        startActivity(new Intent(ActProductAdd.this, ActCar.class).putExtras(bundle));
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
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
                }

                break;
        }
    }

    private boolean GuardarPedido(){

        AddProductCar car = new AddProductCar();

        car.setCodeProcut(getProductoStatic().getIdproductos());
        car.setNameProduct(getProductoStatic().getDescripcion());
        car.setQuantity(cantidad.getQuantity());
        car.setValueunitary(getProductoStatic().getPrecio());

        Double temp = Double.valueOf(totalFinalOculto.getText().toString());
        car.setValueoverall(temp);

        car.setComment(myComment.getText().toString());
        car.setUrlimagen(getProductoStatic().getFoto());
        car.setIdsede(getSedeStaticNew().getIdsedes());
        car.setIdcompany(getSedeStaticNew().getIdempresa());
        car.setNameSede(getSedeStaticNew().getDescripcion());

        car.setHoraInicioEmpresa(getSedeStaticNew().getHorainicio());
        car.setHoraFinalEmpresa(getSedeStaticNew().getHorafinal());
        car.setCostoEnvio(getSedeStaticNew().getCosenvio());
        car.setValorMinimo(getSedeStaticNew().getPedidomeinimo());


        if(getProductoStatic().getAdicionesList() != null && getProductoStatic().getAdicionesList().size() > 0){
            List<Adiciones> adicionesList = new ArrayList<>();

            car.setAdicionesList(loopQuestions(root, adicionesList));
        }

        if (mydb.insertProduct(car)){
            //Guardado
            return true;
        }else{
            //Problemas
            Toast.makeText(ActProductAdd.this, "Problemas al guardar en el carrito.", Toast.LENGTH_SHORT).show();
            return false;
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
                    for(int f = 0; f < getProductoStatic().getAdicionesList().size(); f++) {
                        if(cb.getId() == getProductoStatic().getAdicionesList().get(f).getIdadicionales()){

                            CurrentSum.add(
                                    new Adiciones(cb.getId(),
                                            getProductoStatic().getAdicionesList().get(f).getDescripcion(),
                                            getProductoStatic().getAdicionesList().get(f).getTipo(),
                                            getProductoStatic().getAdicionesList().get(f).getValor(),
                                            getProductoStatic().getAdicionesList().get(f).getEstado(),
                                            getProductoStatic().getAdicionesList().get(f).getIdproductos(),
                                            getSedeStaticNew().getIdsedes(),getSedeStaticNew().getIdempresa(),
                                            getProductoStatic().getAdicionesList().get(f).getCantidadAdicion(),
                                            getProductoStatic().getAdicionesList().get(f).getAutoIncrementAdicion()));
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


    private void CargarImagen() {
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
                progressBar2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                progressBar2.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                progressBar2.setVisibility(View.GONE);
            }
        };

        imageLoader1.displayImage(getProductoStatic().getFoto(), displayImagen, options1, listener);
    }

}
