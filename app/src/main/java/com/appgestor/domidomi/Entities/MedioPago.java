package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

public class MedioPago {

    @SerializedName("idmediopago")
    private int idmediopago;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("estado")
    private int estado;

    @SerializedName("idsedes")
    private int idsedes;





    //Metodo GET SET
    public int getIdmediopago() {
        return idmediopago;
    }

    public void setIdmediopago(int idmediopago) {
        this.idmediopago = idmediopago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
