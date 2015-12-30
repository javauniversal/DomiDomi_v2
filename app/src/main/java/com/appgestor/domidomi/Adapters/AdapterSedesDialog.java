package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.R;

import java.util.List;

public class AdapterSedesDialog extends BaseAdapter {

    private Activity actx;
    private List<AddProductCar> elements;

    public AdapterSedesDialog(Activity actx, List<AddProductCar> elements){
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
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_carrito_dialog, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.txtSedeDialog.setText(elements.get(position).getNameSede());

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
