package com.appgestor.domidomi.Entities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appgestor.domidomi.R;

public class RowViewHolderEmpresas extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView txtNombreEmpresa;
    public TextView txtDireccion;
    public RatingBar ratingBar;

    public RowViewHolderEmpresas(View itemView, Context context) {
        super(itemView);

        this.txtNombreEmpresa = (TextView) itemView.findViewById(R.id.txtNombreEmpresa);

        this.txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);

        this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        this.imageView = (ImageView) itemView.findViewById(R.id.imgEmpresa);

    }

}
