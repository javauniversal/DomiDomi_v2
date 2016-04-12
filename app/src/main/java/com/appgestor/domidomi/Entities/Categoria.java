package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

    public class Categoria {

    @SerializedName("idcategoria")
    private int idcategoria;

    @SerializedName("descripcion")
    private String descripcion;

    public boolean isChecked;

    public Categoria(int idcategoria, String descripcion) {
        this.idcategoria = idcategoria;
        this.descripcion = descripcion;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public void setDescipcion(String descipcionCategoria) {
        this.descripcion = descipcionCategoria;
    }

    public String getDescipcion() {
        return descripcion;
    }
}
