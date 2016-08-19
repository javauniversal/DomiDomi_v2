package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        holder.fecha.setText(elements.get(position).getFechain());
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(elements.get(position).getCalificacion())));

        if (elements.get(position).getCalificacion() < 3.0){
            holder.imageView.setImageResource(R.drawable.ic_thumb_down_outline_grey600_36dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_thumb_up_outline_grey600_36dp);
        }

        return convertView;

    }

    class ViewHolder {
        public TextView message = null;
        public TextView fecha = null;
        public RatingBar ratingBar;
        public ImageView imageView;

        public ViewHolder(View view) {
            message = (TextView) view.findViewById(R.id.txtComentario);
            fecha = (TextView) view.findViewById(R.id.txtFechaComentario);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar4);
            imageView = (ImageView) view.findViewById(R.id.imageView7);
            view.setTag(this);
        }
    }

}
