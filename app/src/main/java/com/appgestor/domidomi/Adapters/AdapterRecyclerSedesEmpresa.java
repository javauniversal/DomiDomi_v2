package com.appgestor.domidomi.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appgestor.domidomi.Entities.RowViewHolderSede;
import com.appgestor.domidomi.Entities.Sede;
import com.appgestor.domidomi.R;

import java.util.List;

public class AdapterRecyclerSedesEmpresa extends RecyclerView.Adapter<RowViewHolderSede> {

    private Context context;
    private List<Sede> sedeList;


    public AdapterRecyclerSedesEmpresa(Context context, List<Sede> sedeList) {
        super();
        this.context = context;
        this.sedeList = sedeList;
    }

    @Override
    public RowViewHolderSede onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sedes, parent, false);
        return new RowViewHolderSede(v, context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RowViewHolderSede holder, final int position) {

        Sede items = sedeList.get(position);

        holder.txtNombreSedexyz.setText(items.getDescripcion());
        holder.txtCiudad.setText(items.getCiudad());
        holder.txtDireccion.setText(items.getDireccion());

    }


    @Override
    public int getItemCount() {
        if (sedeList == null) {
            return 0;
        } else {
            return sedeList.size();
        }
    }

}
