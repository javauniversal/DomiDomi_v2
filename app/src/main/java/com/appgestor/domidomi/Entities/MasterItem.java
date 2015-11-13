package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

public class MasterItem {

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("ingredientes")
    private String ingredientes;

    @SerializedName("precio")
    private double precio;

    @SerializedName("foto")
    private String foto;

    @SerializedName("cantidad")
    private int existencia;

    public static MasterItem getMasterItemStatic() {
        return masterItemStatic;
    }

    public static void setMasterItemStatic(MasterItem masterItemStatic) {
        MasterItem.masterItemStatic = masterItemStatic;
    }

    private static MasterItem masterItemStatic;

    public int getCodigo() { return codigo; }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() { return foto; }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public static MasterItem getProductDescripStatic(){
        return masterItemStatic;
    }

    public static void setProductDescripStatic(MasterItem masterItemStatic){
        MasterItem.masterItemStatic = masterItemStatic;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

}
