package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.Entities.ListCompanias;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.List;

public class AdapterCompania extends BaseAdapter {

    private Activity actx;
    private ListCompanias elements;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;

    public AdapterCompania(Activity actx, ListCompanias elements){
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
        ViewHolder holder = (ViewHolder) convertView.getTag();

        ImageLoadingListener listener = new ImageLoadingListener(){
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
            }
        };

        imageLoader1.displayImage(elements.get(position).getFoto(),holder.image,options1, listener);

        holder.name.setText(elements.get(position).getDescripcion());
        holder.categoria.setText(elements.get(position).getCategoria());

        return convertView;
    }

    class ViewHolder {
        public TextView name = null;
        public TextView categoria = null;
        public ImageView image = null;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.txtNombreCompania);
            categoria = (TextView) view.findViewById(R.id.txtCategoria);
            image = (ImageView) view.findViewById(R.id.listicon);
            view.setTag(this);
        }
    }
}
