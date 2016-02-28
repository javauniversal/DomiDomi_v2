package com.appgestor.domidomi.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appgestor.domidomi.Entities.RowViewHolderSede;
import com.appgestor.domidomi.Entities.Sede;
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

public class AdapterRecyclerSedesEmpresa extends RecyclerView.Adapter<RowViewHolderSede> {

    private Context context;
    private List<Sede> sedeList;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private DecimalFormat format;
    private Date date;
    private DateFormat hourdateFormat;

    public AdapterRecyclerSedesEmpresa(Context context, List<Sede> sedeList) {
        super();
        this.context = context;
        this.sedeList = sedeList;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        format = new DecimalFormat("#,###.##");
        date = new Date();
        hourdateFormat = new SimpleDateFormat("HH:mm:ss");

    }

    @Override
    public RowViewHolderSede onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sedes, parent, false);

        RowViewHolderSede rowViewHolderSede = new  RowViewHolderSede(v, context);

        return rowViewHolderSede;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RowViewHolderSede holder, final int position) {

        Sede items = sedeList.get(position);

        loadeImagenView(items, holder.imgSede, holder);
        holder.txtNombre.setText(items.getDescripcion());
        holder.txtPedidoMunimo.setText(String.format("Pedido Mínimo $ %s", format.format(items.getPedidomeinimo())));
        holder.txtDireccion.setText(items.getDireccion());

        int color;
        if(items.getHorario().equals("Cerrado")){
            color = Color.parseColor("#FF0000");
        } else {
            color = Color.parseColor("#0000FF");

        }

        holder.txtHora.setText(items.getHorario());
        holder.txtHora.setTextColor(color);

    }

    public void loadeImagenView(Sede data, ImageView img, final RowViewHolderSede holder){

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

        imageLoader1.displayImage(data.getImgEmpresa(), img, options1, listener);
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
