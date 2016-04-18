package com.appgestor.domidomi.mockedFragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appgestor.domidomi.Activities.ActCar;
import com.appgestor.domidomi.Activities.ActCarritoMenu;
import com.appgestor.domidomi.Adapters.AdapterSedesDialog;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.ProductoEditAdd;
import com.appgestor.domidomi.R;

import java.util.List;


public class FragmentCarrito extends Fragment {

    private DBHelper mydb;
    private ListView listView;
    private LinearLayout linerSinArticulos;
    private LinearLayout linearLayout4;

    public FragmentCarrito() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_carrito, container, false);

        listView = (ListView) view.findViewById(R.id.listSedesCarrito);
        linerSinArticulos = (LinearLayout) view.findViewById(R.id.linerSinArticulos);
        linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);

        mydb = new DBHelper(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getEmpresas();

    }

    public void getEmpresas(){

        List<ProductoEditAdd> addProductCars = mydb.getProductCarAll();
        if (addProductCars == null || addProductCars.size() <= 0){

            linerSinArticulos.setVisibility(View.VISIBLE);
            linearLayout4.setVisibility(View.INVISIBLE);

        }else {

            linearLayout4.setVisibility(View.VISIBLE);

            if (addProductCars.size() == 1){

                Bundle bundle = new Bundle();
                bundle.putInt("sede", addProductCars.get(0).getId_sede());
                bundle.putInt("empresa", addProductCars.get(0).getId_empresa());
                bundle.putString("paginacion", "menu");
                startActivity(new Intent(getActivity(), ActCar.class).putExtras(bundle));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } else {
                AdapterSedesDialog adapterSedesDialog = new AdapterSedesDialog(getActivity(), addProductCars);
                listView.setAdapter(adapterSedesDialog);
            }
        }

    }

}
