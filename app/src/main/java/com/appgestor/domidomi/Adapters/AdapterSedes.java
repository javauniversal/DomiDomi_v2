package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.MasterItem2;
import com.appgestor.domidomi.R;

import java.util.ArrayList;

public class AdapterSedes extends BaseAdapter {

    private Activity actx;
    private ArrayList<MasterItem2> data;

    public AdapterSedes(Activity actx, ArrayList<MasterItem2> data){
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
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflator = actx.getLayoutInflater();
        view = inflator.inflate(R.layout.items_sedes, null);


        TextView sede = (TextView) view.findViewById(R.id.txtSede);
        TextView ciudad= (TextView) view.findViewById(R.id.txtCiudada);
        TextView direccion = (TextView) view.findViewById(R.id.txtDireccoion);

        sede.setText(data.get(position).getDescripcion());
        ciudad.setText(data.get(position).getCiudad());
        direccion.setText(data.get(position).getDireccion());

        return view;
    }


}
