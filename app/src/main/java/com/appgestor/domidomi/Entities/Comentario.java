package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

public class Comentario {

    @SerializedName("comentarios")
    private String comentario;

    @SerializedName("estado")
    private int estado;

    @SerializedName("idcompania")
    private int idCompania;

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

    public int getIdCompania() {
        return idCompania;
    }

    public void setIdCompania(int idCompania) {
        this.idCompania = idCompania;
    }
}
