package com.appgestor.domidomi.Entities;

import java.io.Serializable;

public class Bean implements Serializable {

    public boolean isChecked;

    public static String pagina_static;

    public static String getPagina_static() {
        return pagina_static;
    }

    public static void setPagina_static(String pagina_static) {
        Bean.pagina_static = pagina_static;
    }
}
