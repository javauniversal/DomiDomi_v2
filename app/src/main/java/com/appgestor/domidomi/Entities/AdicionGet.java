package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdicionGet implements Serializable {

    @SerializedName("idadicionorden")
    private int idadicionorden;

    @SerializedName("idadicion")
    private int idadicion;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("idproducto")
    private int idproducto;

    @SerializedName("idsede")
    private int idsede;

    @SerializedName("valor")
    private double valor;

    @SerializedName("idcarrito")
    private int idcarrito;

    @SerializedName("cantidadorden")
    private int cantidadorden;

    public int getIdadicionorden() {
        return idadicionorden;
    }

    public void setIdadicionorden(int idadicionorden) {
        this.idadicionorden = idadicionorden;
    }

    public int getIdadicion() {
        return idadicion;
    }

    public void setIdadicion(int idadicion) {
        this.idadicion = idadicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getIdsede() {
        return idsede;
    }

    public void setIdsede(int idsede) {
        this.idsede = idsede;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdcarrito() {
        return idcarrito;
    }

    public void setIdcarrito(int idcarrito) {
        this.idcarrito = idcarrito;
    }

    public int getCantidadorden() {
        return cantidadorden;
    }

    public void setCantidadorden(int cantidadorden) {
        this.cantidadorden = cantidadorden;
    }

}
