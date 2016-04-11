package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.DetallePedido;
import com.appgestor.domidomi.Entities.EstadoPedido;
import com.appgestor.domidomi.R;

import java.text.DecimalFormat;
import java.util.List;

public class ActDetalleCompra extends AppCompatActivity {

    private Bundle bundle;
    private EstadoPedido item = new EstadoPedido();
    private TextView txt_valor_adicion;
    private TextView txt_valor_prodocto;
    private TextView txt_valor_total_prodocto;
    private TextView totales_pedido;
    private TextView txt_valor_domicilio;
    private DecimalFormat format;
    private double total_adiciones = 0.0;
    private double total_producto = 0.0;
    private LinearLayout lm;
    private LinearLayout lm2;
    private CardView card_view_adiciones;
    private RelativeLayout ralative_adiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        format = new DecimalFormat("#,###.##");

        txt_valor_adicion = (TextView) findViewById(R.id.txt_valor_adicion);
        txt_valor_prodocto = (TextView) findViewById(R.id.txt_valor_prodocto);
        txt_valor_total_prodocto = (TextView) findViewById(R.id.txt_valor_total_prodocto);
        totales_pedido = (TextView) findViewById(R.id.totales_pedido);
        txt_valor_domicilio = (TextView) findViewById(R.id.txt_valor_domicilio);
        card_view_adiciones = (CardView) findViewById(R.id.card_view_adiciones);
        ralative_adiciones = (RelativeLayout) findViewById(R.id.ralative_adiciones);

        lm = (LinearLayout) findViewById(R.id.liner_productos_dinamic);
        lm2 = (LinearLayout) findViewById(R.id.liner_adiciones_dinamic);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            item = (EstadoPedido)bundle.getSerializable("value");
            cargarDatosPedido(item);
        }

        totales_pedido.setTypeface(totales_pedido.getTypeface(), Typeface.BOLD);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

    }

    private void cargarDatosPedido(EstadoPedido item) {
        cargarAdicion(item);
        cargarProducto(item);
        cargarDomicilio(item);
        cargarSumaTotal(item);
    }

    private void cargarDomicilio(EstadoPedido item) {
        txt_valor_domicilio.setText(String.format("$ %1s", format.format(item.getCosenvio())));
    }

    private void cargarSumaTotal(EstadoPedido item) {
        txt_valor_total_prodocto.setText(String.format("$ %1s", format.format(total_producto+total_adiciones+item.getCosenvio())));
    }

    private void cargarProducto(EstadoPedido item) {

        LinearLayout ll1 = new LinearLayout(this);
        ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll1.setOrientation(LinearLayout.HORIZONTAL);

        TextView productTitulo = new TextView(this);
        productTitulo.setText("Producto");
        productTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        productTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        productTitulo.setGravity(Gravity.LEFT);
        productTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        productTitulo.setTypeface(productTitulo.getTypeface(), Typeface.BOLD);
        ll1.addView(productTitulo);

        TextView cantidadTitulo = new TextView(this);
        cantidadTitulo.setText("Cantidad");
        cantidadTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        cantidadTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        cantidadTitulo.setTypeface(cantidadTitulo.getTypeface(), Typeface.BOLD);
        cantidadTitulo.setGravity(Gravity.CENTER);
        cantidadTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(cantidadTitulo);

        TextView valorTitulo = new TextView(this);
        valorTitulo.setText("Valor");
        valorTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        valorTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        valorTitulo.setTypeface(valorTitulo.getTypeface(), Typeface.BOLD);
        valorTitulo.setGravity(Gravity.RIGHT);
        valorTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(valorTitulo);

        TextView valorTotalTitulo = new TextView(this);
        valorTotalTitulo.setText("Total");
        valorTotalTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        valorTotalTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        valorTotalTitulo.setTypeface(valorTotalTitulo.getTypeface(), Typeface.BOLD);
        valorTotalTitulo.setGravity(Gravity.RIGHT);
        valorTotalTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(valorTotalTitulo);

        lm.addView(ll1);

        List<DetallePedido> detallePedidoList = item.getDetallePedidoList();

        for(int j = 0; j < detallePedidoList.size(); j++) {

            // Create LinearLayout
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);

            // Create TextView
            TextView product = new TextView(this);
            product.setText(detallePedidoList.get(j).getNombreproducto());
            product.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            product.setGravity(Gravity.LEFT);
            product.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            ll.addView(product);

            // Create TextView
            TextView cantidad = new TextView(this);
            cantidad.setText(detallePedidoList.get(j).getCantidad()+"");
            cantidad.setGravity(Gravity.CENTER | Gravity.FILL_VERTICAL);
            cantidad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            cantidad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            ll.addView(cantidad);

            // Create TextView
            TextView valor = new TextView(this);
            valor.setText(String.format("$ %1s", format.format(detallePedidoList.get(j).getValor())));
            valor.setGravity(Gravity.RIGHT | Gravity.FILL_VERTICAL);
            valor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            valor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            ll.addView(valor);

            // Create TextView
            TextView valorTotal = new TextView(this);
            valorTotal.setText(String.format("$ %1s", format.format(detallePedidoList.get(j).getValor()*detallePedidoList.get(j).getCantidad())));
            valorTotal.setGravity(Gravity.RIGHT | Gravity.FILL_VERTICAL);
            valorTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            valorTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            ll.addView(valorTotal);

            // Create Button
            lm.addView(ll);

        }

        sumarProductos(item);
    }

    private void sumarProductos(EstadoPedido item) {
        if (item.getDetallePedidoList() != null) {
            for (int i = 0; i < item.getDetallePedidoList().size(); i++) {
                total_producto += item.getDetallePedidoList().get(i).getValor() * item.getDetallePedidoList().get(i).getCantidad();
            }
            txt_valor_prodocto.setText(String.format("$ %1s", format.format(total_producto)));
        }
    }

    private void cargarAdicion(EstadoPedido item) {

        LinearLayout ll1 = new LinearLayout(this);
        ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll1.setOrientation(LinearLayout.HORIZONTAL);

        TextView productTitulo = new TextView(this);
        productTitulo.setText("Adición");
        productTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        productTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        productTitulo.setGravity(Gravity.LEFT);
        productTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        productTitulo.setTypeface(productTitulo.getTypeface(), Typeface.BOLD);
        ll1.addView(productTitulo);

        TextView cantidadTitulo = new TextView(this);
        cantidadTitulo.setText("Cantidad");
        cantidadTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        cantidadTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        cantidadTitulo.setTypeface(cantidadTitulo.getTypeface(), Typeface.BOLD);
        cantidadTitulo.setGravity(Gravity.CENTER);
        cantidadTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(cantidadTitulo);

        TextView valorTitulo = new TextView(this);
        valorTitulo.setText("Valor");
        valorTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        valorTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        valorTitulo.setTypeface(valorTitulo.getTypeface(), Typeface.BOLD);
        valorTitulo.setGravity(Gravity.RIGHT);
        valorTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(valorTitulo);

        TextView valorTotalTitulo = new TextView(this);
        valorTotalTitulo.setText("Total");
        valorTotalTitulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        valorTotalTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
        valorTotalTitulo.setTypeface(valorTotalTitulo.getTypeface(), Typeface.BOLD);
        valorTotalTitulo.setGravity(Gravity.RIGHT);
        valorTotalTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        ll1.addView(valorTotalTitulo);

        lm2.addView(ll1);

        List<DetallePedido> detallePedidoList = item.getDetallePedidoList();

        int contador = 0;

        for(int m = 0; m < detallePedidoList.size(); m++) {

            if (detallePedidoList.get(m).getAdicionGetList() != null) {

                for (int j = 0; j < detallePedidoList.get(m).getAdicionGetList().size(); j++) {

                    contador++;

                    // Create LinearLayout
                    LinearLayout ll4 = new LinearLayout(this);
                    ll4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ll4.setOrientation(LinearLayout.HORIZONTAL);

                    // Create TextView
                    TextView product = new TextView(this);
                    product.setText(detallePedidoList.get(m).getAdicionGetList().get(j).getNombre());
                    product.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                    product.setGravity(Gravity.LEFT);
                    product.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    ll4.addView(product);

                    // Create TextView
                    TextView cantidad = new TextView(this);
                    cantidad.setText(detallePedidoList.get(m).getAdicionGetList().get(j).getCantidadorden() + "");
                    cantidad.setGravity(Gravity.CENTER | Gravity.FILL_VERTICAL);
                    cantidad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    cantidad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    ll4.addView(cantidad);

                    // Create TextView
                    TextView valor = new TextView(this);
                    valor.setText(String.format("$ %1s", format.format(detallePedidoList.get(m).getAdicionGetList().get(j).getValor())));
                    valor.setGravity(Gravity.RIGHT | Gravity.FILL_VERTICAL);
                    valor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    valor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    ll4.addView(valor);

                    // Create TextView
                    TextView valorTotal = new TextView(this);
                    valorTotal.setText(String.format("$ %1s", format.format(detallePedidoList.get(m).getAdicionGetList().get(j).getValor() * detallePedidoList.get(m).getAdicionGetList().get(j).getCantidadorden())));
                    valorTotal.setGravity(Gravity.RIGHT | Gravity.FILL_VERTICAL);
                    valorTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    valorTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    ll4.addView(valorTotal);

                    lm2.addView(ll4);
                }
            }
        }

        if (contador <= 0) {
            card_view_adiciones.setVisibility(View.GONE);
            ralative_adiciones.setVisibility(View.GONE);
        }

        sumarAdicion(item);
    }

    private void sumarAdicion(EstadoPedido item) {

        if (item.getDetallePedidoList() != null) {
            for (int i = 0; i < item.getDetallePedidoList().size(); i++) {
                if (item.getDetallePedidoList().get(i).getAdicionGetList() != null) {
                    for (int f = 0; f < item.getDetallePedidoList().get(i).getAdicionGetList().size(); f++) {
                        total_adiciones += item.getDetallePedidoList().get(i).getAdicionGetList().get(f).getValor() * item.getDetallePedidoList().get(i).getAdicionGetList().get(f).getCantidadorden();
                    }
                }
            }

            txt_valor_adicion.setText(String.format("$ %1s", format.format(total_adiciones)));
        }
    }

}
