package com.appgestor.domidomi.Entities;


public class Ciudades {

    private int idCiudad;
    private String nombreCiudad;
    private int idPais;

    public Ciudades(int idCiudad, String nombreCiudad, int idPais) {
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.idPais = idPais;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    @Override
    public String toString() {
        return nombreCiudad;
    }
}
