package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.ListComentarios;
import com.appgestor.domidomi.R;


public class AdapterComentario extends BaseAdapter {

    private Activity actx;
    private ListComentarios elements;

    public AdapterComentario(Activity actx, ListComentarios elements){
        this.actx = actx;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
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
            convertView = View.inflate(actx, R.layout.item_comentario, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.message.setText(elements.get(position).getComentario());

        return convertView;

    }

    class ViewHolder {
        public TextView message = null;

        public ViewHolder(View view) {
            message = (TextView) view.findViewById(R.id.txtComentario);
            view.setTag(this);
        }
    }

}
