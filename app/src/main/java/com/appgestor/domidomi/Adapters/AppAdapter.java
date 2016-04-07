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
    List<AddProductCar> data;
    private ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private DBHelper mydb;
    private DecimalFormat format;

    public AppAdapter(Activity actx, List<AddProductCar> data){
        this.actx = actx;
        this.data = data;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(actx).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        options1 = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        mydb = new DBHelper(actx);

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
    public AddProductCar getItem(int position) {
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

        AddProductCar item = getItem(position);

        String adicion = mostrarAdiciones(item.getIdProduct(), item.getIdcompany(), item.getIdsede());

        double totalAdicion = totalAdiciones(item.getIdProduct(), item.getIdcompany(), item.getIdsede());

        holder.tv_name.setText(String.format("%1s(x%2s)", item.getNameProduct(), item.getQuantity()));
        holder.valorProductoCar.setText(String.format("Valor: $ %s", format.format(item.getValueunitary())));
        holder.totalProductoCar.setText(String.format("Valor Total: $ %s", format.format(item.getValueunitary() * item.getQuantity())));

        holder.tv_preci.setText(String.format("Valor Total: $ %s", format.format(item.getValueoverall())));

        //holder.tv_cantidad.setText(String.format("Total: $ %s", format.format(item.getValueoverall())));

        if (adicion != null && adicion != ""){
            holder.tv_adiciones.setVisibility(View.VISIBLE);
            holder.totalAdicion.setVisibility(View.VISIBLE);
            holder.tv_adiciones.setText(adicion);
            holder.totalAdicion.setText(String.format("Total: $ %s", format.format(totalAdicion)));
        }else {
            holder.tv_adiciones.setVisibility(View.GONE);
            holder.totalAdicion.setVisibility(View.GONE);
        }

        CargarImagen(holder,item);
        return convertView;
    }

    private double totalAdiciones(int idProduct, int idcompany, int idsede) {

        double totalAdicion = 0.0;

        List<Adiciones> adicionesList = mydb.getAdiciones(idProduct, idcompany, idsede);
        if(adicionesList.size() > 0){
            for (int i = 0; i < adicionesList.size(); i++) {
                totalAdicion = totalAdicion + (adicionesList.get(i).getCantidadAdicion() * adicionesList.get(i).getValor());
            }
        }
        return totalAdicion;
    }

    public String mostrarAdiciones(int idcarrito, int idempresa, int idsede){

        List<Adiciones> adicionesList = mydb.getAdiciones(idcarrito, idempresa, idsede);

        String formato = "";

        if(adicionesList.size() > 0){
            String concatAdiciones = "";
            for (int i = 0; i < adicionesList.size(); i++) {
                concatAdiciones = concatAdiciones +" | "+ adicionesList.get(i).getDescripcion() +"(x "+ adicionesList.get(i).getCantidadAdicion()+")";
            }

            formato = String.format(" %1s %2s", "Adiciones:", concatAdiciones);
        }

        return formato;
    }


    private void CargarImagen(ViewHolder holder,AddProductCar item){
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

        imageLoader1.displayImage(item.getUrlimagen(), holder.iv_icon, options1 , listener);
    }

    class ViewHolder {

        ImageView iv_icon;
        TextView tv_name;
        TextView tv_preci;
        TextView totalAdicion;
        TextView tv_adiciones;
        TextView valorProductoCar;
        TextView totalProductoCar;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_preci = (TextView) view.findViewById(R.id.tv_preci);
            totalAdicion = (TextView) view.findViewById(R.id.totalAdicion);
            tv_adiciones = (TextView) view.findViewById(R.id.tv_adiciones);
            valorProductoCar = (TextView) view.findViewById(R.id.valorProductoCar);
            totalProductoCar = (TextView) view.findViewById(R.id.totalProductoCar);
            view.setTag(this);
        }
    }

}
