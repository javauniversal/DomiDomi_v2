package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by servintesas on 25/07/15.
 */
public class PedidoWebCabeza {

    @SerializedName("idCompany")
    private int idCompany;

    @SerializedName("nombreUsuario")
    private String nombreUsuario;

    @SerializedName("celular")
    private String celular;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("direccionReferen")
    private String direccionReferen;

    @SerializedName("longitud")
    private double longitud;

    @SerializedName("latitud")
    private double latitud;

    @SerializedName("estadoPedido")
    private String estadoPedido;

    @SerializedName("productos")
    private List<AddProductCar> producto;

    public int getIdCompany() { return idCompany; }

    public void setIdCompany(int idCompany) { this.idCompany = idCompany; }

    public String getNombreUsuairo() {
        return nombreUsuario;
    }

    public void setNombreUsuairo(String nombreUsuairo) {
        this.nombreUsuario = nombreUsuairo;
    }

    public String getCelularp() {
        return celular;
    }

    public void setCelularp(String celular) {
        this.celular = celular;
    }

    public String getDireccionp() {
        return direccion;
    }

    public void setDireccionp(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionReferen() {
        return direccionReferen;
    }

    public void setDireccionReferen(String direccionReferen) {
        this.direccionReferen = direccionReferen;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) { this.longitud = longitud; }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<AddProductCar> getProducto() {
        return producto;
    }

    public void setProducto(List<AddProductCar> producto) {
        this.producto = producto;
    }
}
