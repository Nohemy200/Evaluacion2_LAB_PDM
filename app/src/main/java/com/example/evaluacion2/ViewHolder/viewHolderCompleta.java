package com.example.evaluacion2.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion2.R;

public class viewHolderCompleta extends RecyclerView.ViewHolder {
    private TextView tvTitulo, tvPresupuesto;

    public TextView getTvTitulo() {
        return tvTitulo;
    }
    public viewHolderCompleta(@NonNull View itemView) {
        super(itemView);
        this.tvTitulo = itemView.findViewById(R.id.tvTitulo);
    }
}
