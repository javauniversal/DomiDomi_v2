package com.appgestor.domidomi.Adapters;

import com.appgestor.domidomi.Entities.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {



    public static HashMap<String, List<String>> getData(List<Menu> data) {

        HashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        if(data != null) {
            for (int i = 0; i < data.size(); i++) {

                if (data.get(i).getProductos() != null){

                    List<String> technology = new ArrayList<>();

                    for (int a = 0; a < data.get(i).getProductos().size(); a++) {
                        technology.add(a, data.get(i).getProductos().get(a).getDescripcion());
                    }

                    expandableListDetail.put(data.get(i).getDescipcion()+"||"+data.get(i).getIdmenumovil(), technology);
                }
            }
        }
        return expandableListDetail;
    }
}
