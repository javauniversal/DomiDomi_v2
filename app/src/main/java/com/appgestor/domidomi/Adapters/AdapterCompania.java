package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.ListEmpresas;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdapterCompania extends BaseAdapter {

    private Activity actx;
    private ListEmpresas elements;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;

    public AdapterCompania(Activity actx, ListEmpresas elements){
        this.actx = actx;
        this.elements = elements;

        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(actx).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
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
            convertView = View.inflate(actx, R.layout.list_giditem_compania, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        ImageLoadingListener listener = new ImageLoadingListener(){
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //Inicia metodo
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //Cancelar
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                //Completado
                holder.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
                //Error al cargar la imagen.
                holder.progressBar.setVisibility(View.GONE);
            }
        };

        imageLoader1.displayImage(elements.get(position).getFoto(),holder.image,options1, listener);

        holder.name.setText(elements.get(position).getDescripcion());

        //holder.categoria.setText(elements.get(position).getCategoria());
        //holder.horario.setText(String.format("Horario: %1$s a %2$s", elements.get(position).getFechainicial(), elements.get(position).getFechafinal()));

        /*if(beween(elements.get(position).getFechainicial(), elements.get(position).getFechafinal())){
            holder.estadoRes.setText("Abierto");
            holder.estadoRes.setTextColor(actx.getResources().getColor(R.color.colorErrorConexion));
        }else {
            holder.estadoRes.setText("Cerrado");
            holder.estadoRes.setTextColor(actx.getResources().getColor(R.color.bckg));
        }*/

        return convertView;
    }

    public boolean beween(String hora1, String hora2) {
        boolean indicBool = false;
        try {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            Calendar calendario = Calendar.getInstance();

            int hora, minutos, segundos;

            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);

            String horaNueva = hora + ":" + minutos + ":" + segundos;

            Date date1, date2, dateNueva;
            date1 = dateFormat.parse(hora1);
            date2 = dateFormat.parse(hora2);
            dateNueva = dateFormat.parse(horaNueva);

            if ((date1.compareTo(dateNueva) <= 0) && (date2.compareTo(dateNueva) >= 0)){
                indicBool = true;
            } else {
                indicBool = false;
            }
        } catch (ParseException parseException){
            parseException.printStackTrace();
        }

        return indicBool;
    }

    class ViewHolder {
        public TextView name = null;
        public TextView categoria = null;
        public TextView estadoRes = null;
        public TextView horario = null;
        public ImageView image = null;
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.txtNombreCompania);
            categoria = (TextView) view.findViewById(R.id.txtCategoria);
            estadoRes = (TextView) view.findViewById(R.id.txtEstado);
            horario = (TextView) view.findViewById(R.id.txtHorario);
            image = (ImageView) view.findViewById(R.id.listicon);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setTag(this);
        }
    }
}
