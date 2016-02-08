package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

public class Categoria {

    @SerializedName("idcategoria")
    private int idcategoria;

    @SerializedName("descipcionCategoria")
    private String descipcionCategoria;

    public boolean isChecked;

    public Categoria(int idcategoria, String descipcionCategoria) {
        this.idcategoria = idcategoria;
        this.descipcionCategoria = descipcionCategoria;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getDescipcionCategoria() {
        return descipcionCategoria;
    }

    public void setDescipcionCategoria(String descipcionCategoria) {
        this.descipcionCategoria = descipcionCategoria;
    }
}
