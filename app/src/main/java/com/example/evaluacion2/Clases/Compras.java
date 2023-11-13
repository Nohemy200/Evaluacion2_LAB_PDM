package com.example.evaluacion2.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Compras implements Parcelable {
    String idCompra;
    String titulo;
    double presupuesto;
    boolean activa;
    double total;

    public Compras() {
    }

    protected Compras(Parcel in) {
        idCompra = in.readString();
        titulo = in.readString();
        presupuesto = in.readDouble();
        activa = in.readByte() != 0;
        total = in.readDouble();
    }

    public static final Creator<Compras> CREATOR = new Creator<Compras>() {
        @Override
        public Compras createFromParcel(Parcel in) {
            return new Compras(in);
        }

        @Override
        public Compras[] newArray(int size) {
            return new Compras[size];
        }
    };

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idCompra);
        dest.writeString(titulo);
        dest.writeDouble(presupuesto);
        dest.writeByte((byte) (activa ? 1 : 0));
        dest.writeDouble(total);
    }
}
