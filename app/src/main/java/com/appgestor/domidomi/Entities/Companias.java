package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Companias {

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("nit")
    private String nit;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("celular")
    private String celular;

    @SerializedName("email")
    private String email;

    @SerializedName("encargado")
    private String encargado;

    @SerializedName("foto2")
    private String foto;

    @SerializedName("categoria")
    private String categoria;

    private static int codigoS;

    static ArrayList<Companias> companiaS;

    public int getCodigo() { return codigo; }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCelular() {
        return celular;
    }

    public String getEmail() {
        return email;
    }

    public String getEncargado() {
        return encargado;
    }

    public String getCategoria() { return categoria; }

    public String getFoto() { return foto; }

    public static void setCompaniasS(ArrayList<Companias> companiaRT){
        Companias.companiaS = companiaRT;
    }

    public static ArrayList<Companias> getCompaniasS(){
        return companiaS;
    }

    public static int getCodigoS(){
        return codigoS;
    }

    public static void setCodigoS(int codigoS){
        Companias.codigoS = codigoS;
    }

}
