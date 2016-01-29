package com.appgestor.domidomi.Entities;

/**
 * Created by servintesas on 16/01/16.
 */
public class UbicacionPreferen {

    public static Double latitudStatic;
    public static Double longitudStatic;
    public static String adressStatic;
    public static String ciudadStatic;
    public static String zonaStatic;

    public static String getCiudadStatic() {
        return ciudadStatic;
    }

    public static void setCiudadStatic(String ciudadStatic) {
        UbicacionPreferen.ciudadStatic = ciudadStatic;
    }

    public static String getZonaStatic() {
        return zonaStatic;
    }

    public static void setZonaStatic(String zonaStatic) {
        UbicacionPreferen.zonaStatic = zonaStatic;
    }

    public static Double getLatitudStatic() {
        return latitudStatic;
    }

    public static void setLatitudStatic(Double latitudStatic) {
        UbicacionPreferen.latitudStatic = latitudStatic;
    }

    public static Double getLongitudStatic() {
        return longitudStatic;
    }

    public static void setLongitudStatic(Double longitudStatic) {
        UbicacionPreferen.longitudStatic = longitudStatic;
    }

    public static String getAdressStatic() {
        return adressStatic;
    }

    public static void setAdressStatic(String adressStatic) {
        UbicacionPreferen.adressStatic = adressStatic;
    }

}
