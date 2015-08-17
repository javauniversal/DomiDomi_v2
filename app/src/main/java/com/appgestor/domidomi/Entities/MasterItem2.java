package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

public class MasterItem2 {

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("ciudad")
    private String ciudad;

    @SerializedName("direccion")
    private String direccion;


    public int getCodigo() { return codigo; }

    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCiudad() { return ciudad; }

    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        String valor = "Nombre: "+descripcion;
        return String.format(valor);
    }

}
