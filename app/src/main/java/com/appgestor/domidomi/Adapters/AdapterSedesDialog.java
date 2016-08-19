package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appgestor.domidomi.Activities.ActCarritoMenu;
import com.appgestor.domidomi.Entities.ProductoEditAdd;
import com.appgestor.domidomi.R;

import java.util.List;

import static com.appgestor.domidomi.Entities.Bean.setPagina_static;

public class AdapterSedesDialog extends BaseAdapter {

    private Activity actx;
    private List<ProductoEditAdd> elements;

    public AdapterSedesDialog(Activity actx, List<ProductoEditAdd> elements){
        this.actx = actx;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        if (elements == null) {
            return 0;
        } else {
            return elements.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_carrito_dialog, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.txtSedeDialog.setText(elements.get(position).getNombre_sede());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("sede", elements.get(position).getId_sede());
                bundle.putInt("empresa", elements.get(position).getId_empresa());
                setPagina_static("menu_muchos");
                actx.startActivity(new Intent(actx, ActCarritoMenu.class).putExtras(bundle));
                actx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });

        return convertView;

    }



    class ViewHolder {
        public TextView txtSedeDialog = null;

        public ViewHolder(View view) {
            txtSedeDialog = (TextView) view.findViewById(R.id.txtSedeDialog);
            view.setTag(this);
        }
    }
}
