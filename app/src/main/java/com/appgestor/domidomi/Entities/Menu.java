package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menu {

    @SerializedName("idmenumovil")
    private int idmenumovil;

    @SerializedName("descipcion")
    private String descipcion;

    @SerializedName("estado")
    private int estado;

    @SerializedName("idsedes")
    private int idsedes;

    @SerializedName("productos")
    private List<Producto> productos;

    public static List<Menu> menuListStatic;

    public static List<Menu> getMenuListStatic() {
        return menuListStatic;
    }

    public static void setMenuListStatic(List<Menu> menuListStatic) {
        Menu.menuListStatic = menuListStatic;
    }

    public int getIdmenumovil() {
        return idmenumovil;
    }

    public void setIdmenumovil(int idmenumovil) {
        this.idmenumovil = idmenumovil;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdsedes() {
        return idsedes;
    }

    public void setIdsedes(int idsedes) {
        this.idsedes = idsedes;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
       this.productos = productos;
    }
}
