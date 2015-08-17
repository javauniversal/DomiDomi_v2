package com.appgestor.domidomi.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Poo_Code on 23/06/2015.
 */
public class InformacioCompania {

    @SerializedName("descripcion")
    private String nombre;

    @SerializedName("nit")
    private String nit;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("celular")
    private String celular;

    @SerializedName("children")
    private ArrayList<MasterItem2> hijos;

    public InformacioCompania(String nombre, String nit, String direccion, String telefono, String celular, ArrayList<MasterItem2> hijos) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.hijos = hijos;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }

    public void setNit(String nit) { this.nit = nit; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCelular() { return celular; }

    public void setCelular(String celular) { this.celular = celular; }

    public ArrayList<MasterItem2> getHijos() { return hijos; }

    public void setHijos(ArrayList<MasterItem2> hijos) { this.hijos = hijos; }

    @Override
    public String  toString(){
        return getNombre();
    }
}
