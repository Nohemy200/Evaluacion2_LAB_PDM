package com.example.evaluacion2.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion2.R;

public class viewHolderCompras extends RecyclerView.ViewHolder {
    private TextView tvNombre, tvPrecio, tvCantidad;

    public TextView getTvNombre() {
        return tvNombre;
    }
    public TextView getTvPrecio() {
        return tvPrecio;
    }
    public TextView getTvCantidad() {
        return tvCantidad;
    }
    public viewHolderCompras(@NonNull View itemView) {
        super(itemView);
        this.tvNombre = itemView.findViewById(R.id.tvNombre);
        this.tvPrecio = itemView.findViewById(R.id.tvPrecio);
        this.tvCantidad = itemView.findViewById(R.id.tvCantidad);
    }
}
