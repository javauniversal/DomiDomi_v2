package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductoEditAdd implements Serializable {

    private int auto_incremental;

    @SerializedName("idProduct")
    private int codigo_producto;

    @SerializedName("nameProduct")
    private String descripcion;

    private String ingredientes;

    @SerializedName("valueunitary")
    private double precio;

    @SerializedName("quantity")
    private int cantidad;

    @SerializedName("valueoverall")
    private Double valor_total;

    @SerializedName("comment")
    private String comentario;

    @SerializedName("idcompany")
    private int id_empresa;

    @SerializedName("idsede")
    private int id_sede;

    @SerializedName("adicionesListselet")
    private List<Adiciones> adicionesListselet;

    private double valor_gratis;

    private String nombre_sede;

    private String hora_inicial;

    private String hora_final;

    private double costo_envio;

    private double valor_minimo;

    private String foto;

    private int estado;

    private int idmenumovil;

    private List<Adiciones> adicionesList;

    private Double valor_unitario;

    public int getAuto_incremental() {
        return auto_incremental;
    }

    public void setAuto_incremental(int auto_incremental) {
        this.auto_incremental = auto_incremental;
    }

    public List<Adiciones> getAdicionesListselet() {
        return adicionesListselet;
    }

    public void setAdicionesListselet(List<Adiciones> adicionesListselet) {
        this.adicionesListselet = adicionesListselet;
    }

    public int getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(int codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public Double getValor_unitario() {
        return valor_unitario;
    }

    public void setValor_unitario(Double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

    public Double getValor_total() {
        return valor_total;
    }

    public void setValor_total(Double valor_total) {
        this.valor_total = valor_total;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_sede() {
        return id_sede;
    }

    public void setId_sede(int id_sede) {
        this.id_sede = id_sede;
    }

    public String getNombre_sede() {
        return nombre_sede;
    }

    public void setNombre_sede(String nombre_sede) {
        this.nombre_sede = nombre_sede;
    }

    public String getHora_inicial() {
        return hora_inicial;
    }

    public void setHora_inicial(String hora_inicial) {
        this.hora_inicial = hora_inicial;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public double getCosto_envio() {
        return costo_envio;
    }

    public void setCosto_envio(double costo_envio) {
        this.costo_envio = costo_envio;
    }

    public double getValor_minimo() {
        return valor_minimo;
    }

    public void setValor_minimo(double valor_minimo) {
        this.valor_minimo = valor_minimo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdmenumovil() {
        return idmenumovil;
    }

    public void setIdmenumovil(int idmenumovil) {
        this.idmenumovil = idmenumovil;
    }

    public List<Adiciones> getAdicionesList() {
        return adicionesList;
    }

    public void setAdicionesList(List<Adiciones> adicionesList) {
        this.adicionesList = adicionesList;
    }

    public double getValor_gratis() {
        return valor_gratis;
    }

    public void setValor_gratis(double valor_gratis) {
        this.valor_gratis = valor_gratis;
    }

}
