package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

public class Comentario {

    @SerializedName("idcomentarios")
    private int idcomentarios;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("estado")
    private int estado;

    @SerializedName("idsedes")
    private int idsedes;

    @SerializedName("fechain")
    private String fechain;

    @SerializedName("calificacion")
    private double calificacion;

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getFechain() {
        return fechain;
    }

    public void setFechain(String fechain) {
        this.fechain = fechain;
    }

    public int getIdcomentarios() {
        return idcomentarios;
    }

    public void setIdcomentarios(int idcomentarios) {
        this.idcomentarios = idcomentarios;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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
}
