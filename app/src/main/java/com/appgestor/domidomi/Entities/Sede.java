package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sede {

    @SerializedName("idsedes")
    private int idsedes;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("ciudad")
    private String ciudad;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("estado")
    private int estado;

    @SerializedName("latitud")
    private String latitud;

    @SerializedName("longitud")
    private String longitud;

    @SerializedName("idempresa")
    private int idempresa;

    @SerializedName("tiempoenv")
    private String tiempoEnvio;

    @SerializedName("cosenvio")
    private double cosenvio;

    @SerializedName("menu")
    private List<Menu> menus;

    @SerializedName("mpm")
    private List<MedioPago> medioPagoList;

    public static Sede sedeStatic;

    public static int sedeIdeStatic;



    // MEtodos GET SET
    public static Sede getSedeStatic() {
        return sedeStatic;
    }

    public static void setSedeStatic(Sede sedeStatic) {
        Sede.sedeStatic = sedeStatic;
    }

    public int getIdsedes() {
        return idsedes;
    }

    public void setIdsedes(int idsedes) {
        this.idsedes = idsedes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<MedioPago> getMedioPagoList() {
        return medioPagoList;
    }

    public void setMedioPagoList(List<MedioPago> medioPagoList) {
        this.medioPagoList = medioPagoList;
    }

    public String getTiempoEnvio() {
        return tiempoEnvio;
    }

    public void setTiempoEnvio(String tiempoEnvio) {
        this.tiempoEnvio = tiempoEnvio;
    }

    public double getCosenvio() {
        return cosenvio;
    }

    public void setCosenvio(double cosenvio) {
        this.cosenvio = cosenvio;
    }

    public static int getSedeIdeStatic() {
        return sedeIdeStatic;
    }

    public static void setSedeIdeStatic(int sedeIdeStatic) {
        Sede.sedeIdeStatic = sedeIdeStatic;
    }

}
