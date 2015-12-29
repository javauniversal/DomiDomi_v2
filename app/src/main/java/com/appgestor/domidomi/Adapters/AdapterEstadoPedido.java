package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.Activities.ActEstadoPedido;
import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Entities.EstadoPedido;
import com.appgestor.domidomi.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterEstadoPedido extends BaseAdapter {

    private Activity actx;
    private List<EstadoPedido> data;

    public AdapterEstadoPedido(Activity actx, List<EstadoPedido> data){
        this.actx = actx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public EstadoPedido getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_estado_pedido, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        EstadoPedido item = getItem(position);

        holder.txtEmpresa.setText(String.format("Empresa: %s",item.getEmpresa()));
        holder.txtDireccion.setText(String.format("Dirección: %s", item.getDireccion()));
        holder.txtFecha.setText(String.format("Fecha predido: %s", item.getFecha()));
        holder.txtCantidad.setText(String.format("Cantidad: %s", item.getCantidad()));
        holder.txtValor.setText(String.format("Valor total: %s", item.getValor()));

        String estado = null;

        switch (item.getEstado()){
            case "Iniciado":
                estado = "Iniciado";
                holder.txtEstado.setTextColor(Color.parseColor("#088A08"));
                break;
            case "En proceso":
                estado = "En proceso";
                holder.cancelPedido.setVisibility(View.GONE);
                holder.txtEstado.setTextColor(Color.parseColor("#AEB404"));
                break;
            case "Alistado":
                estado = "Alistado";
                holder.cancelPedido.setVisibility(View.GONE);
                holder.txtEstado.setTextColor(Color.parseColor("#0000FF"));
                break;
            case "Enviado":
                estado = "Enviado";
                holder.cancelPedido.setVisibility(View.GONE);
                holder.txtEstado.setTextColor(Color.parseColor("#FF8000"));
                break;
            case "Finalizado":
                estado = "Finalizado";
                holder.cancelPedido.setVisibility(View.GONE);
                holder.txtEstado.setTextColor(Color.parseColor("#F20505"));
                break;

        }

        holder.txtEstado.setText(String.format("Estado del pedido: %s", estado));

        holder.cancelPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPedido(data.get(position).getEmeicel(), data.get(position).getIdordencompra(),data.get(position).getIdsede());
            }
        });

        return convertView;
    }

    public void cancelPedido(final String pedido, final int idcompra, final int idSede){
        new MaterialDialog.Builder(actx)
                .title("Alerta!")
                .content("Quiere cancelar el pedido?")
                .positiveText("Aceptar")
                .backgroundColor(actx.getResources().getColor(R.color.color_gris))
                .positiveColor(actx.getResources().getColor(R.color.color_negro))
                .negativeColor(actx.getResources().getColor(R.color.color_negro))
                .negativeText("Cancelar")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //Acción de cancelado del pedido.
                        enviarCancelar(pedido, idcompra, idSede);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void enviarCancelar(final String idpedidocel, final int idcompra, final int idSede){

        String url = String.format("%1$s%2$s", actx.getString(R.string.url_base), "CancelarPedido");
        RequestQueue rq = Volley.newRequestQueue(actx);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        actx.startActivity(new Intent(actx, ActEstadoPedido.class));
                        actx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        actx.finish();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        actx.startActivity(new Intent(actx, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        actx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idTelefono", idpedidocel);
                params.put("idcompra", String.valueOf(idcompra));
                params.put("idSede", String.valueOf(idSede));

                return params;
            }
        };

        rq.add(jsonRequest);

    }

    class ViewHolder {
        TextView txtEmpresa;
        TextView txtDireccion;
        TextView txtFecha;
        TextView txtCantidad;
        TextView txtValor;
        TextView txtEstado;
        Button cancelPedido;

        public ViewHolder(View view) {
            txtEmpresa = (TextView) view.findViewById(R.id.txtEmpresa);
            txtDireccion = (TextView) view.findViewById(R.id.txtDireccion);
            txtFecha = (TextView) view.findViewById(R.id.txtFecha);
            txtCantidad = (TextView) view.findViewById(R.id.txtCantidad);
            txtValor = (TextView) view.findViewById(R.id.txtValor);
            txtEstado = (TextView) view.findViewById(R.id.txtEstado);
            cancelPedido = (Button) view.findViewById(R.id.btnCancel);
            view.setTag(this);
        }
    }

}
