package com.appgestor.domidomi.Adapters;

import com.appgestor.domidomi.Entities.MenuList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {



    public static HashMap<String, List<String>> getData(MenuList data) {
        int elements = 0;

        HashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        for (int i = 0; i < data.size(); i++) {
            List<String> technology = new ArrayList<>();
            if(data.get(i).getHijos() != null){
                elements = data.get(i).getHijos().size();
            }

            for (int a = 0; a < elements; a++){
                if(data.get(i).getHijos() != null) {
                    technology.add(a, data.get(i).getHijos().get(a).getDescripcion());
                }
            }
            expandableListDetail.put(data.get(i).getDescripcion(), technology);
        }

        return expandableListDetail;
    }
}
