package com.appgestor.domidomi.mockedFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appgestor.domidomi.Activities.DetailsActivity;
import com.appgestor.domidomi.Adapters.AdapterSedesDialog;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
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

        List<AddProductCar> addProductCars = mydb.getProductCarAll();
        if (addProductCars == null || addProductCars.size() <= 0){

            linerSinArticulos.setVisibility(View.VISIBLE);
            linearLayout4.setVisibility(View.INVISIBLE);

            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra("STATE", "EMPTYC"));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }else {

            linearLayout4.setVisibility(View.VISIBLE);
            AdapterSedesDialog adapterSedesDialog = new AdapterSedesDialog(getActivity(), addProductCars);
            listView.setAdapter(adapterSedesDialog);

        }

    }

}
