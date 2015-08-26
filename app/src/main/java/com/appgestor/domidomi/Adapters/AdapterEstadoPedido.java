package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        if(item.getEstado() == 1)
           estado = "Pendiente";

        holder.txtEstado.setText(String.format("Estado del pedido: %s", estado));

        return convertView;
    }

    class ViewHolder {
        TextView txtEmpresa;
        TextView txtDireccion;
        TextView txtFecha;
        TextView txtCantidad;
        TextView txtValor;
        TextView txtEstado;

        public ViewHolder(View view) {
            txtEmpresa = (TextView) view.findViewById(R.id.txtEmpresa);
            txtDireccion = (TextView) view.findViewById(R.id.txtDireccion);
            txtFecha = (TextView) view.findViewById(R.id.txtFecha);
            txtCantidad = (TextView) view.findViewById(R.id.txtCantidad);
            txtValor = (TextView) view.findViewById(R.id.txtValor);
            txtEstado = (TextView) view.findViewById(R.id.txtEstado);
            view.setTag(this);
        }
    }

}
