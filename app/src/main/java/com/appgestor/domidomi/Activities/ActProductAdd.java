package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DecimalFormat;

public class ActProductAdd extends AppCompatActivity implements View.OnClickListener{

    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private ImageView displayImagen;
    private Bundle bundle;
    private QuantityView cantidad;
    private TextView preciodec;
    private TextView totalFinal;
    private DecimalFormat format;
    private DBHelper mydb;
    private EditText myComment;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_add);
        mydb = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        displayImagen = (ImageView) findViewById(R.id.displayImagen);
        TextView descripcion = (TextView) findViewById(R.id.descripcion);
        TextView ingredientes = (TextView) findViewById(R.id.ingredientes);
        TextView precio = (TextView) findViewById(R.id.precio);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        cantidad = (QuantityView) findViewById(R.id.quantityView_default);

        preciodec = (TextView) findViewById(R.id.preciodesc);
        totalFinal = (TextView) findViewById(R.id.totalFinal);
        myComment = (EditText) findViewById(R.id.EditComment);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            //Setup the ImageLoader, we'll use this to display our images
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            imageLoader1 = ImageLoader.getInstance();
            imageLoader1.init(config);

            //Setup options for ImageLoader so it will handle caching for us.
            options1 = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

            CargarImagen();

            descripcion.setText(bundle.getString("descripcion"));
            ingredientes.setText(bundle.getString("ingredientes"));
            format = new DecimalFormat("#,###.##");
            precio.setText(String.format("Precio: %s", format.format(bundle.getDouble("precio"))));
            preciodec.setText(String.format("Precio: %s", format.format(bundle.getDouble("precio"))));

            totalFinal.setText(bundle.getDouble("precio")+"");

            cantidad.setValorTotal(bundle.getDouble("precio"),totalFinal);

            toolbar.setTitle(bundle.getString("descripcion"));
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void CargarImagen() {
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

        imageLoader1.displayImage(bundle.getString("foto"), displayImagen, options1, listener);
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
            bundle.putInt("compania", Companias.getCodigoS().getCodigo());
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

                if(cantidad.getQuantity() < 1){
                    Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
                }else {
                    new MaterialDialog.Builder(this)
                            .title(R.string.titulo_confirmacion_pedido)
                            .content(R.string.contenido_confirmacion_pedido)
                            .positiveText(R.string.realizar_pedido)
                            .negativeText(R.string.add_producto)
                            .backgroundColor(getResources().getColor(R.color.color_gris))
                            .positiveColor(getResources().getColor(R.color.color_negro))
                            .negativeColor(getResources().getColor(R.color.color_negro))
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    //Realizar pedido
                                    if (GuardarPedido()) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("compania", Companias.getCodigoS().getCodigo());
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
        car.setCodeProcut(bundle.getInt("codeproduct"));
        car.setNameProduct(bundle.getString("descripcion"));
        car.setQuantity(cantidad.getQuantity());
        car.setValueunitary(bundle.getDouble("precio"));
        Double temp = Double.valueOf(totalFinal.getText().toString());

        car.setValueoverall(temp);
        car.setIdcompany(Companias.getCodigoS().getCodigo());
        car.setComment(myComment.getText().toString());
        car.setUrlimagen(bundle.getString("foto"));
        if (mydb.insertProduct(car)){
            //Guardado
            return true;
        }else{
            //Problemas
            Toast.makeText(ActProductAdd.this, "Problemas al guardar en el carrito.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
