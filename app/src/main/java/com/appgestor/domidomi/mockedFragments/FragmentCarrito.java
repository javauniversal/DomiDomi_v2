package com.appgestor.domidomi.mockedFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appgestor.domidomi.Activities.ActCarritoMenu;
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

        }else {

            linearLayout4.setVisibility(View.VISIBLE);

            if (addProductCars.size() == 1){
                Bundle bundle = new Bundle();
                bundle.putInt("empresa", addProductCars.get(0).getIdcompany());
                bundle.putInt("sede", addProductCars.get(0).getIdsede());
                bundle.putString("sedeNomebre", addProductCars.get(0).getNameSede());

                bundle.putString("horaInicial", addProductCars.get(0).getHoraInicioEmpresa());
                bundle.putString("horaFinal", addProductCars.get(0).getHoraFinalEmpresa());

                bundle.putDouble("cosEnvio", addProductCars.get(0).getCostoEnvio());
                bundle.putDouble("valMinimo", addProductCars.get(0).getValorMinimo());
                bundle.putBoolean("indicador", false);

                startActivity(new Intent(getActivity(), ActCarritoMenu.class).putExtras(bundle));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } else {

                AdapterSedesDialog adapterSedesDialog = new AdapterSedesDialog(getActivity(), addProductCars);
                listView.setAdapter(adapterSedesDialog);

            }

        }

    }

}
