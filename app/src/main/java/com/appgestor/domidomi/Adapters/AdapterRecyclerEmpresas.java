package com.appgestor.domidomi.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.List;

public class AdapterRecyclerEmpresas extends RecyclerView.Adapter<RowViewHolderEmpresas> {

    private Context context;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private List<Empresas> empresasList;


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
