package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

public class Adiciones {

    @SerializedName("idadicionales")
    private int idadicionales;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("valor")
    private double valor;

    @SerializedName("estado")
    private int estado;

    @SerializedName("idproductos")
    private int idproductos;

    public int getIdadicionales() {
        return idadicionales;
    }

    public void setIdadicionales(int idadicionales) {
        this.idadicionales = idadicionales;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdproductos() {
        return idproductos;
    }

    public void setIdproductos(int idproductos) {
        this.idproductos = idproductos;
    }
}
