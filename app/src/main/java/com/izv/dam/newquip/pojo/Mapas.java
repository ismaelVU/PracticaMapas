package com.izv.dam.newquip.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ismael on 07/12/2016.
 */

@DatabaseTable(tableName = "Mapas")
public class Mapas implements Parcelable {

    @DatabaseField(columnName = "idMapa", generatedId = true)
    private int idMapa;
    @DatabaseField( columnName = "latitud")
    private double latitud;
    @DatabaseField(columnName = "longitud")
    private double longitud;
    @DatabaseField(columnName = "idNota")
    private long idNota;

    public  Mapas(){

    }


    public Mapas(int idMapa, double latitud, double longitud, long idNota){
        this.idMapa=idMapa;
        this.latitud=latitud;
        this.longitud=longitud;
        this.idNota=idNota;
    }


    protected Mapas(Parcel in) {
        idMapa = in.readInt();
        latitud = in.readDouble();
        longitud = in.readDouble();
        idNota = in.readLong();
    }

    public static final Creator<Mapas> CREATOR = new Creator<Mapas>() {
        @Override
        public Mapas createFromParcel(Parcel in) {
            return new Mapas(in);
        }

        @Override
        public Mapas[] newArray(int size) {
            return new Mapas[size];
        }
    };

    public String toString(){
        return "Mapas(" +
                "idMapa' =" + idMapa    + '/' +
                "latitud=" + latitud    + '/' +
                "longitud" + longitud   + '/' +
                "idNota" + idNota       + '/' +
                ')';
    }

    public int getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(int idMapa) {
        this.idMapa = idMapa;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idMapa);
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
        dest.writeLong(idNota);
    }
}
