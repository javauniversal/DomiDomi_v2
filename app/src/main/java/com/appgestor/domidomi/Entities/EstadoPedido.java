package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

public class EstadoPedido {

    @SerializedName("idUnicop")
    private int idUnicop;

    @SerializedName("empresa")
    private String empresa;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("valor")
    private double valor;

    @SerializedName("estado")
    private int estado;

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public int getIdUnicop() {
        return idUnicop;
    }

    public void setIdUnicop(int idUnicop) {
        this.idUnicop = idUnicop;
    }
}
