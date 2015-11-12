package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

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

    @SerializedName("fechainicial")
    private String fechainicial;

    @SerializedName("fechafinal")
    private String fechafinal;

    private static Companias codigoS;

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

    public static Companias getCodigoS(){
        return codigoS;
    }

    public static void setCodigoS(Companias codigoS){
        Companias.codigoS = codigoS;
    }

    public String getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(String fechainicial) {
        this.fechainicial = fechainicial;
    }

    public String getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(String fechafinal) {
        this.fechafinal = fechafinal;
    }

}
