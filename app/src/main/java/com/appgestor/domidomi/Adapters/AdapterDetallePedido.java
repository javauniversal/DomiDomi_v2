package com.appgestor.domidomi.Adapters;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.DetallePedido;
import com.appgestor.domidomi.R;

import java.util.List;

public class AdapterDetallePedido extends BaseAdapter {

    private Activity actx;
    private List<DetallePedido> data;

    public AdapterDetallePedido(Activity actx, List<DetallePedido> data){
        this.actx = actx;
        this.data = data;
    }

    @Override
    public int getCount() {

        if (data == null) {
            return 0;
        } else {
            return data.size();
        }

    }

    @Override
    public DetallePedido getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_det_pedido_dialog, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        DetallePedido item = getItem(position);


        holder.txtProducto.setText(item.getNombreproducto());
        holder.txtCantidad.setText(item.getCantidad()+"");
        holder.txtPrecio.setText(item.getValor()+"");

        return convertView;

    }

    class ViewHolder {
        TextView txtProducto;
        TextView txtCantidad;
        TextView txtPrecio;

        public ViewHolder(View view) {
            txtProducto = (TextView) view.findViewById(R.id.txtProducto);
            txtCantidad = (TextView) view.findViewById(R.id.txtCantidad);
            txtPrecio = (TextView) view.findViewById(R.id.txtPrecio);
            view.setTag(this);
        }
    }
}
