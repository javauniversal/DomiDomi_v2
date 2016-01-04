package com.appgestor.domidomi.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appgestor.domidomi.Entities.Empresas;
import com.appgestor.domidomi.Entities.RowViewHolderEmpresas;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterRecyclerEmpresas extends RecyclerView.Adapter<RowViewHolderEmpresas> {

    private Context context;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private List<Empresas> empresasList;
    private DecimalFormat format;

    public AdapterRecyclerEmpresas(Context context, List<Empresas> empresasList) {
        super();
        this.context = context;
        this.empresasList = empresasList;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        format = new DecimalFormat("#,###.##");
    }

    @Override
    public RowViewHolderEmpresas onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresa, parent, false);
        return new RowViewHolderEmpresas(v, context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RowViewHolderEmpresas holder, final int position) {

        Empresas items = empresasList.get(position);

        loadeImagenView(items, holder.imageView, holder);

        holder.txtNombreEmpresa.setText(items.getDescripcion());
        holder.txtDireccion.setText(items.getDescripcion());
        holder.txtValormin.setText((String.format("Pedido Mínimo $ %s", format.format(items.getValorMin()))));

        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");

        int color;
        String estado;

        if (isHourInInterval(hourdateFormat.format(date).toString(), items.getHorainicio(), items.getHorafinal())){
            //Abierto
            estado = "Abierto";
            color = Color.parseColor("#0000FF");
        }else{
            //Cerrado
            estado = "Cerrado";
            color = Color.parseColor("#FF0000");
        }

        holder.horaAtencion.setText(estado);
        holder.horaAtencion.setTextColor(color);


    }

    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }


    @Override
    public int getItemCount() {
        if (empresasList == null) {
            return 0;
        } else {
            return empresasList.size();
        }
    }

    public void loadeImagenView(Empresas data, ImageView img, final RowViewHolderEmpresas holder){

        ImageLoadingListener listener = new ImageLoadingListener(){
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //Inicia metodo
                //holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
                //Cancelar
                //holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                //Completado
                //holder.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
                //Error al cargar la imagen.
                //holder.progressBar.setVisibility(View.GONE);
            }
        };

        imageLoader1.displayImage(data.getFoto(), img, options1, listener);
    }
}
