package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Empresas {

    @SerializedName("idempresa")
    private int idempresa;

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

    @SerializedName("correo")
    private String correo;

    @SerializedName("encargado")
    private String encargado;

    @SerializedName("foto")
    private String foto;

    @SerializedName("horainicio")
    private String horainicio;

    @SerializedName("horafinal")
    private String horafinal;

    @SerializedName("pemin")
    private double valorMin;

    @SerializedName("estado")
    private int estado;

    private static Empresas empresastatic;

    private static List<Empresas> empresasListStatic;

    public static List<Empresas> getEmpresasListStatic() {
        return empresasListStatic;
    }

    public static void setEmpresasListStatic(List<Empresas> empresasListStatic) {
        Empresas.empresasListStatic = empresasListStatic;
    }

    public static Empresas getEmpresastatic() {
        return empresastatic;
    }

    public static void setEmpresastatic(Empresas empresastatic) {
        Empresas.empresastatic = empresastatic;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(String horafinal) {
        this.horafinal = horafinal;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getValorMin() {
        return valorMin;
    }

    public void setValorMin(double valorMin) {
        this.valorMin = valorMin;
    }

}
