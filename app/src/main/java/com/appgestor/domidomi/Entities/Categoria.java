package com.appgestor.domidomi.Entities;

/**
 * Created by Poo_Code on 20/06/2015.
 */
public class Categoria {

    private int codigo;
    private String nombre;
    private String foto;

    public Categoria(int codigo, String nombre, String foto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }



}
