package com.appgestor.domidomi.Entities;


import com.google.gson.annotations.SerializedName;

public class HorarioEmpresa {

    @SerializedName("horainicio")
    private String horainicio;

    @SerializedName("horafinal")
    private String horafinal;

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

}
