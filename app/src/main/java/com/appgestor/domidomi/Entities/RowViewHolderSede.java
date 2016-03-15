package com.appgestor.domidomi.Entities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgestor.domidomi.R;

public class RowViewHolderSede extends RecyclerView.ViewHolder {

    public TextView txtNombre;
    public TextView txtPedidoMunimo;
    public TextView txtDireccion;
    public TextView txtHora;
    public ImageView imgSede;

    public RowViewHolderSede(View itemView, Context context) {
        super(itemView);

        this.txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
        this.txtPedidoMunimo = (TextView) itemView.findViewById(R.id.txtPedidoMunimo);
        this.txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);
        this.txtHora = (TextView) itemView.findViewById(R.id.txtHora);
        this.imgSede = (ImageView) itemView.findViewById(R.id.imgSede);

    }

}
