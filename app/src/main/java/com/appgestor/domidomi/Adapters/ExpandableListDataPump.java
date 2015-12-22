package com.appgestor.domidomi.Adapters;

import com.appgestor.domidomi.Entities.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {



    public static HashMap<String, List<String>> getData(List<Menu> data) {
        int elements = 0;

        HashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        if(data != null) {
            for (int i = 0; i < data.size(); i++) {

                List<String> technology = new ArrayList<>();
                if (data.get(i).getProductos() != null) {
                    elements = data.get(i).getProductos().size();
                }

                for (int a = 0; a < elements; a++) {
                    if (data.get(i).getProductos() != null) {
                        technology.add(a, data.get(i).getProductos().get(a).getDescripcion());
                    }
                }

                expandableListDetail.put(data.get(i).getDescipcion(), technology);

            }
        }
        return expandableListDetail;
    }
}
