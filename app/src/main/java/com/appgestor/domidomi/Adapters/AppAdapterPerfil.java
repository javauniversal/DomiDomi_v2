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
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;

public class AppAdapterPerfil extends BaseAdapter {

    private Activity actx;
    List<Cliente> data;

    public AppAdapterPerfil(Activity actx, List<Cliente> data){
        this.actx = actx;
        this.data = data;
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
    public Cliente getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_perfil, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        Cliente item = getItem(position);

        if (item.getOficina().equals("Ninguno")){
            holder.txt_direccion.setText(String.format("%1s %2s # %3s - %4s ", item.getCalle_carrera(),
                    item.getDir_1().trim(), item.getDir_2().trim(), item.getDir_3().trim()));
        } else {
            holder.txt_direccion.setText(String.format("%1s %2s # %3s - %4s %5s %6s", item.getCalle_carrera(),
                    item.getDir_1().trim(), item.getDir_2().trim(), item.getDir_3().trim(), item.getOficina(), item.getNum_oficina()));
        }

        /*if (item.getCelular().equals("")){
            holder.txt_cell.setText(String.format("%s", item.getTelefono()));
        } else {
            holder.txt_cell.setText(String.format("%s", item.getCelular()));
        }*/

        holder.txt_nombre.setText(String.format("%s", item.getNombre()));

        return convertView;
    }

    class ViewHolder {

        TextView txt_nombre;
        TextView txt_direccion;
        //TextView txt_cell;


        public ViewHolder(View view) {

            txt_direccion = (TextView) view.findViewById(R.id.txt_direccion);
            //txt_cell = (TextView) view.findViewById(R.id.txt_cell);
            txt_nombre = (TextView) view.findViewById(R.id.txtNombre);

            view.setTag(this);
        }
    }

}
