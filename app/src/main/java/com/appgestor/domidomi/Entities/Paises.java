package com.appgestor.domidomi.Entities;


public class Paises {

    public Paises(int idPais, String nombrePais) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    private int idPais;

    private String nombrePais;

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    @Override
    public String toString() {
        return nombrePais;
    }
}
