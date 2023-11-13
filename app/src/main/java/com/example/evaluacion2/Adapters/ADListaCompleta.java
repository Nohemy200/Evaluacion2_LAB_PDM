package com.example.evaluacion2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion2.R;
import com.example.evaluacion2.ViewHolder.viewHolderCompleta;
import com.example.evaluacion2.Clases.Compras;

import java.util.List;

public class ADListaCompleta extends RecyclerView.Adapter<viewHolderCompleta> {
    private List<Compras> datos;

    public ADListaCompleta(List<Compras> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public viewHolderCompleta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent,false);
        return new viewHolderCompleta(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolderCompleta holder, int position) {
        holder.getTvTitulo().setText(datos.get(position).getTitulo() + "");
    }

    @Override
    public int getItemCount() {return datos.size();}
}