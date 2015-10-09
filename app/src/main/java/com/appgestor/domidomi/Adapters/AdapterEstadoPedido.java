package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.Entities.EstadoPedido;
import com.appgestor.domidomi.R;

import java.util.List;


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
            case 1:
                estado = "Pendiente";
                break;
            case 2:
                estado = "Enviado";
                holder.cancelPedido.setVisibility(View.GONE);
                break;
            case 3:
                estado = "Cancelado";
                holder.cancelPedido.setVisibility(View.GONE);
                break;
            case 4:
                estado = "Finalizado";
                holder.cancelPedido.setVisibility(View.GONE);
                break;

        }

        holder.txtEstado.setText(String.format("Estado del pedido: %s", estado));

        holder.cancelPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPedido(data.get(position).getIdUnicop());
            }
        });

        return convertView;
    }

    public void cancelPedido(final int pedido){
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
                        enviarCancelar(pedido);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void enviarCancelar(int pedido){

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
