package com.appgestor.domidomi.Entities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appgestor.domidomi.R;

public class RowViewHolderSede extends RecyclerView.ViewHolder {

    public TextView txtNombreSedexyz;
    public TextView txtCiudad;
    public TextView txtDireccion;
    public RatingBar ratingBar;

    public RowViewHolderSede(View itemView, Context context) {
        super(itemView);

        this.txtNombreSedexyz = (TextView) itemView.findViewById(R.id.txtNombreSedexyz);

        this.txtCiudad = (TextView) itemView.findViewById(R.id.txtCiudad);

        this.txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);

        this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);


    }

}
