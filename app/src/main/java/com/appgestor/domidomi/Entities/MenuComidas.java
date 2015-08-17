package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MenuComidas {

    public MenuComidas(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("children")
    private ArrayList<MasterItem> hijos;

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<MasterItem> getHijos() { return hijos; }

    public void setHijos(ArrayList<MasterItem> hijos) {
        this.hijos = hijos;
    }

    @Override
    public String toString(){
        return getDescripcion();
    }

}
