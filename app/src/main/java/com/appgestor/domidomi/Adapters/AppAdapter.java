package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Adiciones;
import com.appgestor.domidomi.Entities.ProductoEditAdd;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;

public class AppAdapter extends BaseAdapter {

    private Activity actx;
    List<ProductoEditAdd> data;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private DecimalFormat format;

    public AppAdapter(Activity actx, List<ProductoEditAdd> data){
        this.actx = actx;
        this.data = data;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(actx).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        options1 = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        DBHelper mydb = new DBHelper(actx);

        format = new DecimalFormat("#,###.##");

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
    public ProductoEditAdd getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_list_app, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        ProductoEditAdd item = getItem(position);

        String adicion = mostrarAdiciones(item.getAdicionesListselet());

        double totalAdicion = totalAdiciones(item.getAdicionesListselet());

        holder.tv_name.setText(String.format("%1s - (x%2s)", item.getDescripcion(), item.getCantidad()));

        holder.valor_total_productos.setText(String.format("$ %s", format.format(item.getPrecio() * item.getCantidad())));

        if (adicion != null && adicion != ""){
            holder.tv_adiciones.setVisibility(View.VISIBLE);
            holder.total_adiciones.setVisibility(View.VISIBLE);
            holder.tituloadiciones.setVisibility(View.VISIBLE);

            holder.tv_adiciones.setText(adicion);
            holder.total_adiciones.setText(String.format("$ %s", format.format(totalAdicion)));
        }else {
            holder.tv_adiciones.setVisibility(View.GONE);
            holder.total_adiciones.setVisibility(View.GONE);
            holder.tituloadiciones.setVisibility(View.GONE);
        }

        holder.sut_total.setText(String.format("$ %s", format.format(item.getValor_total())));

        CargarImagen(holder,item);
        return convertView;
    }

    private double totalAdiciones(List<Adiciones> adicionesListselet) {
        double totalAdicion = 0.0;
        if(adicionesListselet.size() > 0){
            for (int i = 0; i < adicionesListselet.size(); i++) {
                totalAdicion = totalAdicion + (adicionesListselet.get(i).getCantidadAdicion() * adicionesListselet.get(i).getValor());
            }
        }

        return totalAdicion;
    }

    public String mostrarAdiciones(List<Adiciones> adicionesListselet){

        //List<Adiciones> adicionesList = mydb.getAdiciones(idcarrito, idempresa, idsede);

        String formato = "";

        if(adicionesListselet.size() > 0){
            String concatAdiciones = "";
            for (int i = 0; i < adicionesListselet.size(); i++) {
                if (concatAdiciones.isEmpty()){
                    concatAdiciones = adicionesListselet.get(i).getDescripcion() +" - (x"+ adicionesListselet.get(i).getCantidadAdicion()+")";
                }
                else {
                    concatAdiciones = concatAdiciones +" | "+ adicionesListselet.get(i).getDescripcion() +" - (x"+ adicionesListselet.get(i).getCantidadAdicion()+")";
                }
            }

            formato = String.format("%1s %2s", "Adiciones: ", concatAdiciones);
        }

        return formato;
    }


    private void CargarImagen(ViewHolder holder, ProductoEditAdd item){
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
                //holder.image.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
            }
        };

        imageLoader1.displayImage(item.getFoto(), holder.iv_icon, options1 , listener);
    }

    class ViewHolder {

        ImageView iv_icon;
        TextView tv_name;
        TextView valor_total_productos;
        TextView tv_adiciones;
        TextView total_adiciones;
        TextView sut_total;
        TextView tituloadiciones;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            valor_total_productos = (TextView) view.findViewById(R.id.valor_total_productos);
            tv_adiciones = (TextView) view.findViewById(R.id.tv_adiciones);
            total_adiciones = (TextView) view.findViewById(R.id.total_adiciones);
            sut_total = (TextView) view.findViewById(R.id.sut_total);
            tituloadiciones = (TextView) view.findViewById(R.id.tituloadiciones);

            view.setTag(this);
        }
    }

}
